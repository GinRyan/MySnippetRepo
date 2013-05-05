package com.hitfm.improve;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

public class MyMediaPlaybackActivity extends Activity {
	private AudioManager mAudioManager;
	private ComponentName mRemoteControlResponder;
	private static Method mRegisterMediaButtonEventReceiver;
	private static Method mUnregisterMediaButtonEventReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mRemoteControlResponder = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
	}

	@SuppressWarnings("unused")
	private static void initializeRemoteControlRegistrationMethods() {
		try {
			if (mRegisterMediaButtonEventReceiver == null) {
				mRegisterMediaButtonEventReceiver = AudioManager.class.getMethod("registerMediaButtonEventReceiver",
						new Class[] { ComponentName.class });
			}
			if (mUnregisterMediaButtonEventReceiver == null) {
				mUnregisterMediaButtonEventReceiver = AudioManager.class.getMethod("unregisterMediaButtonEventReceiver",
						new Class[] { ComponentName.class });
			}
			/* success, this device will take advantage of better remote */
			/* control event handling */
		} catch (NoSuchMethodException nsme) {
			/* failure, still using the legacy behavior, but this app */
			/* is future-proof! */
		}
	}

	@SuppressWarnings("unused")
	private void registerRemoteControl() {
		try {
			if (mRegisterMediaButtonEventReceiver == null) {
				return;
			}
			mRegisterMediaButtonEventReceiver.invoke(mAudioManager, mRemoteControlResponder);
		} catch (InvocationTargetException ite) {
			/* unpack original exception when possible */
			Throwable cause = ite.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else if (cause instanceof Error) {
				throw (Error) cause;
			} else {
				/* unexpected checked exception; wrap and re-throw */
				throw new RuntimeException(ite);
			}
		} catch (IllegalAccessException ie) {
			Log.e("MyApp", "unexpected " + ie);
		}
	}

	@SuppressWarnings("unused")
	private void unregisterRemoteControl() {
		try {
			if (mUnregisterMediaButtonEventReceiver == null) {
				return;
			}
			mUnregisterMediaButtonEventReceiver.invoke(mAudioManager, mRemoteControlResponder);
		} catch (InvocationTargetException ite) {
			/* unpack original exception when possible */
			Throwable cause = ite.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else if (cause instanceof Error) {
				throw (Error) cause;
			} else {
				/* unexpected checked exception; wrap and re-throw */
				throw new RuntimeException(ite);
			}
		} catch (IllegalAccessException ie) {
			System.err.println("unexpected " + ie);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mAudioManager.registerMediaButtonEventReceiver(mRemoteControlResponder);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAudioManager.unregisterMediaButtonEventReceiver(mRemoteControlResponder);
	}
}