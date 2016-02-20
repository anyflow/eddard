package com.lge.stark.smp.session;

import java.util.Date;

import com.lge.stark.smp.smpframe.Smpframe;

public interface ResponseArrivedListener {

	Smpframe requestSmpframe();

	void responseArrived(Smpframe frame);

	Date incomingTime();
}