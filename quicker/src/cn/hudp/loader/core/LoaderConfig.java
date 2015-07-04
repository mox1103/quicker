package cn.hudp.loader.core;

import cn.hudp.loader.cache.CacheModel;

public class LoaderConfig {
	public static final String LOG_TAG = "DPLoader";
	/**
	 * 获取失败显示图片的ResId
	 */
	public static int failResId;
	/**
	 * 内存缓存占用内存
	 */
	public static int useMemory;
	/**
	 * 缓存模式
	 */
	public static CacheModel eCacheModel;
	/**
	 * 磁盘缓存路径
	 */
	public static String diskCacheFilePath;
}
