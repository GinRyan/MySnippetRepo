package com.mindtherobot.samples.asyncmvc.controller;

import android.os.Message;

public interface ControllerState {
	boolean handleMessage(Message msg);
}
