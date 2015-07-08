package cn.hudp.loader.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

import cn.hudp.tools.DeviceUtils;

/**
 * 
 * 
 * @author HuDP
 * @Email:mox113@foxmail.com
 * @data 2015-7-4
 */
public class Loader {
	public static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		};
	};
	/**
	 * 缓存调度线程池
	 */
	public static ExecutorService cacheThreadPool = Executors.newSingleThreadExecutor();
	/**
	 * 网络调度线程池
	 */
	public static ExecutorService netThreadPool;

	static {
		int core = DeviceUtils.getNumCores();
		if (core <= 2) {
			netThreadPool = Executors.newFixedThreadPool(1);
		} else if (core <= 4) {
			netThreadPool = Executors.newFixedThreadPool(3);
		} else if (core <= 8) {
			netThreadPool = Executors.newFixedThreadPool(5);
		}
	}
}
