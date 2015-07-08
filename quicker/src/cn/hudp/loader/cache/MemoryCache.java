package cn.hudp.loader.cache;

import java.util.List;

import android.graphics.Bitmap;
import android.util.LruCache;
import cn.hudp.loader.core.LoaderConfig;
import cn.hudp.loader.tools.LogLoader;

/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public class MemoryCache implements Cache<Bitmap> {
	public static LruCache<String, Bitmap> mLruCache;
	private static MemoryCache mMemoryCache;

	private MemoryCache() {
		int maxMemory = LoaderConfig.useMemory;
		if (maxMemory <= 0)
			maxMemory = (int) (Runtime.getRuntime().maxMemory() / 6);
		mLruCache = new LruCache<String, Bitmap>(maxMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 每次存入缓存的时候调用
				return value.getByteCount();
			}
		};
	}

	public static MemoryCache getInstance() {
		if (mMemoryCache == null) {
			synchronized (MemoryCache.class) {
				if (mMemoryCache == null) {
					mMemoryCache = new MemoryCache();
				}
			}
		}
		return mMemoryCache;
	}

	@Override
	public void put(String key, Bitmap mT) {
		if (key == null || mT == null || mT.isRecycled()) {
			LogLoader.e(LoaderConfig.LOG_TAG, "参数为null");
			return;
		} else {
			mLruCache.put(key, mT);
		}
	}

	@Override
	public Bitmap get(String key) {
		if (key == null) {
			LogLoader.e(LoaderConfig.LOG_TAG, "参数为null");
			return null;
		}
		return mLruCache.get(key);
	}

	@Override
	public synchronized void clear(String key) {
		if (mLruCache != null && key != null) {
			Bitmap bitmap = mLruCache.remove(key);
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	/**
	 * 清空LruCache
	 */
	public void clearAll(List<String> keys) {
		if (mLruCache != null && keys != null) {
			for (int i = 0, len = keys.size(); i < len; i++) {
				clear(keys.get(i));
			}
			if (mLruCache.size() > 0) {
				mLruCache.evictAll();
			}
		}
	}
}
