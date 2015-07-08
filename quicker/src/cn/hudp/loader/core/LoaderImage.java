package cn.hudp.loader.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import cn.hudp.loader.cache.CacheManager;
import cn.hudp.loader.cache.CacheModel;
import cn.hudp.loader.download.ImageNetAsyncTask;
import cn.hudp.loader.tools.LogLoader;

/**
 * 简易图片加载类 ... 支持内存缓存以及磁盘缓存
 * 
 * @author HuDP
 * @email:mox113@foxmail.com
 * @date 2015/6/6
 */
public class LoaderImage {
	private static final String LOG_TAG = "ImageLoader";
	private static final int MSG_TAG_SHOW_IMAGE = 0x001;
	private static LoaderImage mImageLoader;
	public static Set<ImageNetAsyncTask> mTasks;
	private ListView mListView;

	private LoaderImage() {
		mTasks = new HashSet<ImageNetAsyncTask>();
	}

	/**
	 * @return ImageLoader 实例
	 */
	public static LoaderImage getInstance() {
		if (mImageLoader == null) {
			synchronized (LoaderImage.class) {
				if (mImageLoader == null) {
					mImageLoader = new LoaderImage();
				}
			}
		}
		return mImageLoader;
	}

	/**
	 * 配置ImageLoader信息
	 * 
	 * @param mContext
	 *            上下文
	 * @param mIsDiskCache
	 *            是否开启磁盘缓存
	 * @param mRatio
	 *            使用多少内存做为缓存 mRatio/可用内存
	 * @return
	 */
	public synchronized LoaderImage init(Context mContext, CacheModel eCacheModel, int usableMemoryRatio) {
		LoaderConfig.eCacheModel = eCacheModel;
		if (usableMemoryRatio <= 0) {
			LoaderConfig.useMemory = (int) (Runtime.getRuntime().maxMemory() / 8);
		} else {
			LoaderConfig.useMemory = (int) (Runtime.getRuntime().maxMemory() / usableMemoryRatio);
		}
		LoaderConfig.diskCacheFilePath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/"
				+ mContext.getPackageName();
		return mImageLoader;
	}

	/**
	 * 默认配置
	 * 
	 * @return
	 */
	public LoaderImage init(Context mContext) {
		return init(mContext, CacheModel.MEMORY, 8);
	}

	// --------------------------------------------------

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_TAG_SHOW_IMAGE:
				break;
			}
		}
	};

	/**
	 * 根据Url来获取图片(缓存获取失败,则网络请求)
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param url
	 *            图片地址
	 */
	public void displayImage(ImageView imageView, String url) {
		displayImage(imageView, url, true);
	}

	/**
	 * 根据Url来获取图片
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param url
	 *            图片地址
	 * @param isLoadNet
	 *            缓存未命中 是否网络请求图片(正常清空推荐为True,如若配合displayImagesToListView方法
	 *            推荐为false)
	 */
	public void displayImage(final ImageView imageView, final String url, final boolean isLoadNet) {
		if (imageView == null || url == null) {
			LogLoader.e(LOG_TAG, "传入参数为空");
			return;
		}
		Loader.cacheThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				final Bitmap bitmap = CacheManager.getCache(url);
				// 命中缓存
				if (bitmap != null) {
					LogLoader.e(LOG_TAG, "loadImageFromUrl 命中缓存");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (imageView.getTag() == null || imageView.getTag().equals(url)) {
								imageView.setImageBitmap(bitmap);
							}
						}
					});
				} else {// 未命中缓存
					if (isLoadNet) {
						LogLoader.e(LOG_TAG, "loadImageFromUrl 网络请求图片");
						ImageNetAsyncTask task = new ImageNetAsyncTask(imageView);
						task.execute(url);
						mTasks.add(task);
					} else {
						if (imageView.getTag() == null || imageView.getTag().equals(url)) {
							imageView.setImageResource(LoaderConfig.failResId);
						}
					}
				}
			}
		});
	}

	/**
	 * 加载当前显示 ListView的所有图片
	 * 
	 * @param start
	 *            ListView当前显示的第一行
	 * @param end
	 *            ListView当前显示的最后一行
	 * @param listView
	 *            当前ListView
	 */
	public void displayImagesToListView(ListView listView, List<String> urls, int start, int end) {
		mListView = listView;
		for (String url : urls) {
			ImageView iv = (ImageView) mListView.findViewWithTag(url);
			displayImage(iv, url, true);
		}
	}

	/**
	 * 取消所有加载图片的异步操作
	 */
	public void cancelAllTesks() {
		if (mTasks != null) {
			for (ImageNetAsyncTask tesk : mTasks) {
				tesk.cancel(false);
			}
		}
	}

	/**
	 * 设置请求失败后的图片
	 * 
	 * @param failResId
	 */
	public void setFailResId(int failResId) {
		if (failResId < 0) {
			LoaderConfig.failResId = android.R.drawable.alert_dark_frame;
		} else {
			LoaderConfig.failResId = failResId;
		}
	}

	/**
	 * 是否开启Log
	 * 
	 * @param isDebug
	 */
	public void setIsDebug(boolean isDebug) {
		LogLoader.setIsDebug(isDebug);
	}
}
