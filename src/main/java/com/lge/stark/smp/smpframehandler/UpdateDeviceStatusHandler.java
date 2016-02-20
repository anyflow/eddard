package com.lge.stark.smp.smpframehandler;

import com.lge.stark.FaultException;
import com.lge.stark.controller.DeviceController;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.ErrorStarkService;
import com.lge.stark.smp.smpframe.ReturnOk;
import com.lge.stark.smp.smpframe.UpdateDeviceStatus;

import io.netty.channel.ChannelHandlerContext;

public class UpdateDeviceStatusHandler extends SmpframeHandler<UpdateDeviceStatus> {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateDeviceStatusHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, UpdateDeviceStatus smpframe, Session session)
			throws Exception {
		try {
			DeviceController.SELF.updateStatus(smpframe.deviceId(), smpframe.isActive());

			session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
		}
		catch (FaultException e) {
			logger.error(e.getMessage(), e);

			session.send(new ErrorStarkService(session.id(), smpframe.responseSmpframeId(), e.fault()));
		}
	}
}