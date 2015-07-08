package cn.hudp.loader.core;

import java.io.InputStream;

import cn.hudp.loader.download.DownLoad;
import cn.hudp.loader.listener.LoadListener;
import cn.hudp.loader.tools.Streamprocessor;

/**
 * 网络请求 获取字符串
 * 
 * @author HuDP
 * @param <T>
 * @Email:mox113@foxmail.com
 * @data 2015-7-4
 */
public class LoaderString {
	public LoadListener<String> listener;
	public void getStringFromUrl(final String url,
			LoadListener<String> mListener) {
		this.listener = mListener;
		Loader.netThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				InputStream is = DownLoad.getInputStreamFromUrl(url);
				if (is != null) {
					final String str = Streamprocessor.getString(is);
					if (listener != null)
						Loader.mHandler.post(new Runnable() {
							@Override
							public void run() {
								listener.onLoadComplete(url, str);
							}
						});
				}
			}
		});
	}
}
