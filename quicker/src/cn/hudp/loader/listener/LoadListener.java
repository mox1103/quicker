package cn.hudp.loader.listener;


/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public interface LoadListener<T> {
	void onLoadComplete(String key, T mT);

	void onLoadFailed(String key, T mT);
}
