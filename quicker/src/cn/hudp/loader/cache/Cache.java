package cn.hudp.loader.cache;

/**
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015/6/26.
 */
public interface Cache<T> {

	void put(String key, T mT);

	T get(String key);

	void clear(String key);
}
