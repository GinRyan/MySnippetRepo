package edu.union;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

/**
 * The ViewAnimator is used to animate any view by calling its invalidate method.
 * 
 * @author bburns
 */
public class ViewAnimator extends Handler {
	boolean running;
	View view;
	long nextTime;
	int diff;
	
	public static final int NEXT = 0;
	
	/**
	 * Constructor, defaults to 20 frames/sec.
	 * @param view The view to animate.
	 */
	public ViewAnimator(View view) {
		this(view, -1);
	}
	
	/**
	 * Constructor
	 * @param view The view to animate
	 * @param fps Frames/sec. for the animation
	 */
	public ViewAnimator(View view, int fps) {
		running = false;
		this.view = view;
		this.diff = 1000/fps;
	}
	
	/**
	 * Starts this animation.
	 */
	public void start() {
		if (!running) {
			running = true;
			Message msg = obtainMessage(NEXT);
			sendMessageAtTime(msg, SystemClock.uptimeMillis());
		}
	}
	
	/**
	 * Stops this animation
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * {@inheritDoc}
	 **/
	public void handleMessage(Message msg) {
		if (running && msg.what == NEXT) {
			view.invalidate();
			msg = obtainMessage(NEXT);
			long current = SystemClock.uptimeMillis();
			if (nextTime < current) {
				nextTime = current + diff;
			}
			sendMessageAtTime(msg, nextTime);
			nextTime += diff;
		}
	}
}
