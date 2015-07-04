package cn.hudp.loader.download;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import cn.hudp.loader.cache.CacheManager;
import cn.hudp.loader.core.LoaderImage;
import cn.hudp.loader.tools.BitmapProcessor;
import cn.hudp.loader.tools.L;

/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public class ImageNetAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private static final String LOG_TAG = "NetAsyncTask";
	private ImageView imageView;
	private String url;

	public ImageNetAsyncTask(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		url = params[0];
		if (url == null) {
			return null;
		}
		InputStream inputStream = DownLoad.getInputStreamFromUrl(url);
		Bitmap bitmap = BitmapProcessor.getBitmap(inputStream, imageView);
		if (bitmap != null) {
			CacheManager.putCache(url, bitmap);
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (bitmap != null && imageView != null) {
			if (imageView.getTag() == null || imageView.getTag().equals(url))
				imageView.setImageBitmap(bitmap);
		} else {
			if (bitmap == null)
				L.e(LOG_TAG, "bitmap == null");
			if (imageView == null)
				L.e(LOG_TAG, "imageView == null");
		}
		LoaderImage.mTasks.remove(this);
	}

}