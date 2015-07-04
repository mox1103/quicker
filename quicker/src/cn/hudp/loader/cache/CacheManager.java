package cn.hudp.loader.cache;

import java.util.List;

import android.graphics.Bitmap;
import cn.hudp.loader.core.LoaderConfig;

/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public class CacheManager {
	public static void putCache(String key, Bitmap mT) {
		CacheModel eCacheModel = LoaderConfig.eCacheModel;
		if (eCacheModel == CacheModel.MEMORY) {
			MemoryCache.getInstance().put(key, mT);
		} else if (eCacheModel == CacheModel.MEMORY_AND_DISK) {
			MemoryCache.getInstance().put(key, mT);
			DiskCache.getInstance().put(key, mT);
		}
	}

	public static Bitmap getCache(String key) {
		Bitmap bitmap = null;
		CacheModel eCacheModel = LoaderConfig.eCacheModel;
		if (eCacheModel == CacheModel.MEMORY) {
			bitmap = MemoryCache.getInstance().get(key);
		} else if (eCacheModel == CacheModel.MEMORY_AND_DISK) {
			bitmap = MemoryCache.getInstance().get(key);
			if (bitmap == null) {
				bitmap = DiskCache.getInstance().get(key);
			}
		}
		return bitmap;
	}

	public static void clearAllMemory(List<String> keys) {
		MemoryCache.getInstance().clearAll(keys);
	}
}
