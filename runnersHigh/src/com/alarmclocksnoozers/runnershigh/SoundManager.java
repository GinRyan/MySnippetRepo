package com.alarmclocksnoozers.runnershigh;

import android.content.Context;

import java.util.HashMap;

import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	 
	static private SoundManager _instance;
	private static SoundPool mSoundPool;
	private static HashMap<Integer, Integer> mSoundPoolMap;
	private static AudioManager  mAudioManager;
	private static Context mContext;
 
	private SoundManager()
	{
	}
 
	/**
	 * Requests the instance of the Sound Manager and creates it
	 * if it does not exist.
	 *
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance()
	{
	    if (_instance == null)
	      _instance = new SoundManager();
	    return _instance;
	 }
 
	/**
	 * Initialises the storage for the sounds
	 *
	 * @param theContext The Application context
	 */
	public static  void initSounds(Context theContext)
	{
		 mContext = theContext;
	     mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	     mSoundPoolMap = new HashMap<Integer, Integer>();
	     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	} 
 
	/**
	 * Add a new Sound to the SoundPool
	 *
	 * @param Index - The Sound Index for Retrieval
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index,int SoundID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}
 
	/**
	 * Loads the various sound assets
	 * Currently hardcoded but could easily be changed to be flexible.
	 */
	public static void loadSounds()
	{
		// loading WMA causes problems on HTC Sensation
//		mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.reset, 1));
//		mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.threek, 1));
		mSoundPoolMap.put(3, mSoundPool.load(mContext, R.raw.jump, 1));
		mSoundPoolMap.put(4, mSoundPool.load(mContext, R.raw.save, 1));
		mSoundPoolMap.put(5, mSoundPool.load(mContext, R.raw.slow , 1));
		mSoundPoolMap.put(6, mSoundPool.load(mContext, R.raw.trampoline, 1));
		mSoundPoolMap.put(7, mSoundPool.load(mContext, R.raw.petenicesplash , 1));
		mSoundPoolMap.put(8, mSoundPool.load(mContext, R.raw.bonus, 1));
		mSoundPoolMap.put(9, mSoundPool.load(mContext, R.raw.ninek, 1));
		
	}
 
	
	public static void playSound(int index,float speed, float volumeL, float volumeR, int loopMode)
	{
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	     if(mSoundPoolMap.get(index)!=null)
	    	 mSoundPool.play(mSoundPoolMap.get(index), streamVolume*volumeL, streamVolume*volumeR, 1, loopMode, speed);		
	}
	/**
	 * Plays a Sound
	 *
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for compatibility
	 */
	public static void playSound(int index,float speed)
	{
		playSound(index, speed, 1.0f, 1.0f, 0);
	}
 
	/**
	 * Stop a Sound
	 * @param index - index of the sound to be stopped
	 */
	public static void stopSound(int index)
	{
		mSoundPool.stop(mSoundPoolMap.get(index));
	}
 
	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	public static void cleanup()
	{
		if (mSoundPool != null) mSoundPool.release();
		mSoundPool = null;
		if (mSoundPoolMap != null) mSoundPoolMap.clear();
		if (mAudioManager != null) mAudioManager.unloadSoundEffects();
	    _instance = null;
 
	}
 
}