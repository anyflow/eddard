package com.lge.stark.eddard.smp.session;

import java.util.Date;

import com.lge.stark.eddard.smp.message.Smpframe;

public interface ResponseArrivedListener {

	Smpframe requestSmpframe();

	void responseArrived(Smpframe frame);

	Date incomingTime();
}