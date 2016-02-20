package com.lge.stark.smp.smpframehandler;

import com.lge.stark.FaultException;
import com.lge.stark.controller.DeviceController;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.DeleteDevice;
import com.lge.stark.smp.smpframe.ErrorStarkService;
import com.lge.stark.smp.smpframe.ReturnOk;

import io.netty.channel.ChannelHandlerContext;

public class DeleteDeviceHandler extends SmpframeHandler<DeleteDevice> {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteDeviceHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, DeleteDevice smpframe, Session session)
			throws Exception {
		try {
			DeviceController.SELF.delete(smpframe.deviceId());

			session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
		}
		catch (FaultException e) {
			logger.error(e.getMessage(), e);

			session.send(new ErrorStarkService(session.id(), smpframe.responseSmpframeId(), e.fault()));
		}
	}
}