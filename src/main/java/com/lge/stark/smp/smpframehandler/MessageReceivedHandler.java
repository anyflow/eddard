package com.lge.stark.smp.smpframehandler;

import java.util.Date;

import org.elasticsearch.client.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.FaultException;
import com.lge.stark.controller.ChannelController;
import com.lge.stark.controller.MessageController;
import com.lge.stark.controller.MessageStatusController;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.model.Message;
import com.lge.stark.model.MessageStatus;
import com.lge.stark.model.MessageStatus.Status;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.MessageReceived;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

import io.netty.channel.ChannelHandlerContext;

public class MessageReceivedHandler extends SmpframeHandler<MessageReceived> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageReceivedHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, MessageReceived smpframe, Session session)
			throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		MessageStatus ms = MessageStatusController.SELF.get(client, smpframe.messageId(), session.deviceId());

		ms.setStatus(Status.READ);
		ms.setReadDate(new Date());

		MessageStatusController.SELF.update(client, ms);

		final Message message = MessageController.SELF.get(client, smpframe.messageId());

		message.setUnreadCount(message.getUnreadCount() - 1);

		MessageController.SELF.update(client, message);

		ChannelController.SELF.broadcast(message.getChannelId(), new Smpframe(OpCode.UNREAD_COUNT_CHANGED) {
			@JsonProperty
			private String channelId = message.getChannelId();

			@JsonProperty
			private String messageId = message.getId();
		});
	}
}