package cn.hudp.loader.cache;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import cn.hudp.loader.core.LoaderConfig;
import cn.hudp.loader.core.LoaderImage;
import cn.hudp.loader.tools.BitmapProcessor;

/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public class DiskCache implements Cache<Bitmap> {
	private static final String LOG_TAG = "DiskCache";
	private static DiskCache mDiskCache;

	private DiskCache() {
	}

	public static DiskCache getInstance() {
		if (mDiskCache == null) {
			synchronized (LoaderImage.class) {
				if (mDiskCache == null) {
					mDiskCache = new DiskCache();
				}
			}
		}
		return mDiskCache;
	}

	@Override
	public void put(String key, Bitmap bitmap) {
		if (key == null || bitmap == null)
			return;
		String picName = String.valueOf(key.hashCode());
		// 保存在sd卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = null;
			try {
				file = new File(LoaderConfig.diskCacheFilePath);
				if (file.isDirectory()) {
					// 可以检查新鲜度 就是缓存是否过期
					return;
				} else {
					file.mkdirs();
				}
				file = new File(file, picName);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Bitmap get(String key) {
		if (key == null)
			return null;
		String picName = String.valueOf(key.hashCode());
		File file = new File(LoaderConfig.diskCacheFilePath, picName);
		if (!file.exists()) {
			return null;
		}
		return BitmapProcessor.getBitmap(file.getPath());
	}

	@Override
	public void clear(String key) {
		if (key == null)
			return;
		String picName = String.valueOf(key.hashCode());
		File file = new File(LoaderConfig.diskCacheFilePath, picName);
		if (file.exists()) {
			file.delete();
		}
	}
}
