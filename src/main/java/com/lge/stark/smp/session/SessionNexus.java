package com.lge.stark.smp.session;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lge.stark.Settings;

import io.netty.channel.Channel;

public final class SessionNexus {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SessionNexus.class);
	public static final SessionNexus SELF;

	static {
		SELF = new SessionNexus();
	}

	private final Object locker;

	private final Map<String, Session> sessionMap;
	private final Map<String, Session> deviceMap;
	private final Map<Channel, Session> channelMap;

	private final ScheduledFuture<?> responseListenerCheckerFuture;

	private static final int DEFAULT_EXECUTE_INTERVAL_IN_SECS = 1800;

	private SessionNexus() {
		sessionMap = Maps.newHashMap();
		deviceMap = Maps.newHashMap();
		channelMap = Maps.newHashMap();

		locker = new Object();

		int interval = Settings.SELF.getInt("webserver.ResponseListenerFlusher.executeInterval",
				DEFAULT_EXECUTE_INTERVAL_IN_SECS);
		responseListenerCheckerFuture = Executors.newSingleThreadScheduledExecutor()
				.scheduleWithFixedDelay(new Runnable() {
					@Override
					public void run() {
						List<Session> sessions = list();
						for (Session item : sessions) {
							item.flushExpiredResponseArrivedListeners(new Date());
						}
					}
				}, interval, interval, TimeUnit.SECONDS);
	}

	public ImmutableMap<String, Session> sessions() {
		return ImmutableMap.copyOf(sessionMap);
	}

	public int sessionCount() {
		return sessionMap.size();
	}

	public Session get(String sessionId) {
		return sessionMap.get(sessionId);
	}

	public Session getByDeviceId(String deviceId) {
		return deviceMap.get(deviceId);
	}

	public void put(Session session) {
		synchronized (locker) {
			sessionMap.put(session.id(), session);
			deviceMap.put(session.deviceId(), session);
			channelMap.put(session.channelHandlerContext().channel(), session);
		}
	}

	public void dispose(Session session) {
		if (sessionMap.containsKey(session.id()) == false) { return; }

		synchronized (locker) {
			session.dispose();

			deviceMap.remove(session.deviceId());
			sessionMap.remove(session.id());

			if (session.channelHandlerContext() != null) {
				channelMap.remove(session.channelHandlerContext().channel());
			}
			else {
				channelMap.values().remove(session);
			}
		}
	}

	public Session getByChannel(Channel channel) {
		return channelMap.get(channel);
	}

	public List<Session> list() {
		return Lists.newArrayList(sessionMap.values());
	}

	public void close() {
		responseListenerCheckerFuture.cancel(true);

		List<Session> list = list();

		for (Session item : list) {
			dispose(item);
		}
	}
}