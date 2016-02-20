package com.lge.stark.smp.session;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.Lists;
import com.lge.stark.IdGenerator;
import com.lge.stark.Jsonizable;
import com.lge.stark.Literals;
import com.lge.stark.Settings;
import com.lge.stark.smp.smpframe.Smpframe;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@JsonPropertyOrder({ "id", "deviceId", "createTime", "lastIncomingTime", "networkType", "clientAddress",
		"isChannelActive" })
public class Session extends Jsonizable implements Comparable<Session> {

	private static final Logger logger = LoggerFactory.getLogger(Session.class);

	private static final int DEF_WEBSERVER_RESPONSE_TIMEOUT_IN_SECS = 240;
	private static final int MS_UNITS = 1000;

	private static IdGenerator idGenerator = new IdGenerator(0);

	private final String id;
	private final String deviceId;
	private final Date createTime;

	private final IdGenerator smpframeIdGenerator;
	private final ChannelHandlerContext channelContext;
	private Date lastIncomingTime;
	private final List<ResponseArrivedListener> responseArrivedListeners;
	private NetworkType networkType;

	public Session(String deviceId, ChannelHandlerContext channelContext) {
		this.id = String.format("%010d", idGenerator.nextInteger());
		this.deviceId = deviceId;
		this.createTime = new Date();

		this.smpframeIdGenerator = new IdGenerator(0);
		this.responseArrivedListeners = Lists.newArrayList();
		this.channelContext = channelContext;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Literals.DATE_DEFAULT_FORMAT, timezone = Literals.DATE_DEFAULT_TIMEZONE)
	@JsonProperty("Last Incoming Time")
	public Date lastIncomingTime() {
		return lastIncomingTime;
	}

	public void lastIncomingTime(Date lastIncomingTime) {
		this.lastIncomingTime = lastIncomingTime;
	}

	@JsonProperty("Network Type")
	public NetworkType networkType() {
		return networkType;
	}

	public void networkType(NetworkType networkType) {
		this.networkType = networkType;
	}

	@JsonProperty("id")
	public String id() {
		return id;
	}

	@JsonProperty("device ID")
	public String deviceId() {
		return deviceId;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Literals.DATE_DEFAULT_FORMAT, timezone = Literals.DATE_DEFAULT_TIMEZONE)
	@JsonProperty("Create Time")
	public Date createTime() {
		return createTime;
	}

	@JsonProperty("Client Address")
	public InetSocketAddress clientAddress() {
		return (InetSocketAddress) channelContext.channel().remoteAddress();
	}

	@JsonProperty("isChannelActive")
	public boolean isChannelActive() {
		if (channelHandlerContext().channel() == null) { return false; }

		return channelHandlerContext().channel().isActive();
	}

	@Override
	public int compareTo(Session session) {
		return id.compareTo(session.id());
	}

	public ChannelHandlerContext channelHandlerContext() {
		return channelContext;
	}

	public void dispose() {
		flushExpiredResponseArrivedListeners(null);

		if (channelHandlerContext().channel().isActive() == false
				&& channelHandlerContext().channel().isOpen() == false) { return; }

		channelHandlerContext().close();
	}

	@Override
	public String toString() {
		return this.toJsonString();
	}

	// For removing Sonar warning. Sonar says "Critical".
	@Override
	public int hashCode() {
		int code = 0;
		for (int i = 0, n = id().length(); i < n; i++) {
			code += id().charAt(i);
		}
		return code;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Session == false) { return false; }

		Session target = (Session) obj;
		return id().equals(target.id());
	}

	public Integer nextPushframeId() {
		return smpframeIdGenerator.nextInteger();
	}

	public void addListener(ResponseArrivedListener responseArrivedListener) {
		if (responseArrivedListener == null) { return; }

		synchronized (responseArrivedListeners) {
			responseArrivedListeners.add(responseArrivedListener);
		}
	}

	public void removeListener(ResponseArrivedListener responseArrivedListener) {
		if (responseArrivedListener == null
				|| responseArrivedListeners.contains(responseArrivedListener) == false) { return; }

		synchronized (responseArrivedListeners) {
			responseArrivedListeners.remove(responseArrivedListener);
		}
	}

	public void send(final Smpframe message) {
		send(message, null, null);
	}

	public void send(final Smpframe message, ResponseArrivedListener responseArrivedListener,
			final ChannelFutureListener operationCompleteListener) {
		try {
			logger.debug("Session.send() started. sessionID|smpframe - {}|{}", this.id(), message.toString());

			if (responseArrivedListener != null) {
				addListener(responseArrivedListener);
			}

			ChannelFuture cf = channelHandlerContext().writeAndFlush(new TextWebSocketFrame(message.toJsonString()));
			if (operationCompleteListener != null) {
				cf.addListener(operationCompleteListener);
			}

			logger.debug("Session.send() finished. sessionID|smpframe - {}|{}", this.id(), message.toString());
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);

			if (responseArrivedListener != null) {
				responseArrivedListener.responseArrived(null);
			}
		}
	}

	public void flushExpiredResponseArrivedListeners(Date expiration) {

		int timeoutInSecond = Settings.SELF.getInt("webserver.responseTimeout", DEF_WEBSERVER_RESPONSE_TIMEOUT_IN_SECS);
		List<Integer> ids = Lists.newArrayList();

		for (ResponseArrivedListener item : responseArrivedListeners) {
			if (expiration == null
					|| ((expiration.getTime() - item.incomingTime().getTime()) > timeoutInSecond * MS_UNITS)) {
				ids.add(item.requestSmpframe().id());
			}
		}

		for (Integer item : ids) {
			this.fireResponseArrived(item, null);
		}
	}

	public void responseArrived(Smpframe frame) {
		fireResponseArrived(frame.id() - 1, frame);
	}

	private void fireResponseArrived(Integer requestSmpFrameId, Smpframe response) {
		List<ResponseArrivedListener> removeTargets = Lists.newArrayList();

		synchronized (responseArrivedListeners) {
			for (ResponseArrivedListener item : responseArrivedListeners) {
				if (item.requestSmpframe().id() != requestSmpFrameId) {
					continue;
				}

				item.responseArrived(response);
				removeTargets.add(item);
			}

			for (ResponseArrivedListener item : removeTargets) {
				responseArrivedListeners.remove(item);
			}
		}

		logger.debug("responseArrived fired. requestSmpframeId|response - {}|{}", requestSmpFrameId,
				response.toString());
	}
}