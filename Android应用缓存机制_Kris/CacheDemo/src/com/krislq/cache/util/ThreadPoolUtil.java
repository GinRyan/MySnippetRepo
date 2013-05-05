/**
 * 
 */
package com.krislq.cache.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 20, 2012
 * @version 1.0.0
 *
 */
public class ThreadPoolUtil {
	private static final ExecutorService pool;
	
	static {
		pool = Executors.newCachedThreadPool();
//		pool = Executors.newFixedThreadPool(4);
//		pool = Executors.newSingleThreadExecutor();
//		pool = Executors.newScheduledThreadPool(4);
	}
	
	/**
	 * 
	 * @param command
	 */
	public static void execute(Runnable command) {
		pool.execute(command);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param task
	 * @return
	 */
	public static <T> Future<T> submit(Callable<T> task) {
		return pool.submit(task);
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	public static Future<?> submit(Runnable task) {
		return pool.submit(task);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param task
	 * @param result
	 * @return
	 */
	public static <T> Future<T> submit(Runnable task, T result) {
		return pool.submit(task, result);
	}
}
