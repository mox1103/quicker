package cn.hudp.annotation.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.view.View;
import cn.hudp.annotation.event.DynamicHandler;
import cn.hudp.annotation.event.EventBase;
import cn.hudp.annotation.view.ContentView;
import cn.hudp.annotation.view.ViewInject;

public class ViewInjectUtils {
	private static final String METHOD_SET_CONTENTVIEW = "setContentView";
	private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

	public static void inject(Activity activity) {
		injectContentView(activity);
		injectViews(activity);
		injectEvents(activity);
	}

	/**
	 * 注入ContentView
	 * 
	 * @param activity
	 */
	private static void injectContentView(Activity activity) {
		Class<? extends Activity> mClass = activity.getClass();
		// 查询传进来的Activity是否存在ContentView注解
		ContentView contentView = mClass.getAnnotation(ContentView.class);
		if (contentView != null) {
			int contentViewLayoutId = contentView.id();
			try {
				Method method = mClass.getMethod(METHOD_SET_CONTENTVIEW, int.class);
				method.setAccessible(true);
				method.invoke(activity, contentViewLayoutId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 注入View
	 */
	private static void injectViews(Activity activity) {
		Class<? extends Activity> mClass = activity.getClass();
		Field[] fields = mClass.getDeclaredFields();
		for (Field field : fields) {
			ViewInject viewInject = field.getAnnotation(ViewInject.class);
			if (viewInject != null) {
				int viewId = viewInject.value(); // 从注解中获取的参数
				if (viewId != -1) {
					try {
						Method method = mClass.getMethod(METHOD_FIND_VIEW_BY_ID, int.class); // 调用方法的方法名和参数类型
						Object resView = method.invoke(activity, viewId);
						field.setAccessible(true); // 设置可见
						field.set(activity, resView);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void injectEvents(Activity activity) {
		Class<? extends Activity> mClass = activity.getClass();
		Method[] methods = mClass.getMethods();
		// 遍历出所有方法
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			// 拿到方法里面的所有注解
			for (Annotation annotation : annotations) {
				Class<? extends Annotation> annotationType = annotation.annotationType();
				EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
				// 如果设置了EventBase
				if (eventBaseAnnotation != null) {
					// 取出监听器的类型 监听器的名称 监听器的方法
					Class<?> listenerType = eventBaseAnnotation.listenerType();
					String listenerSetter = eventBaseAnnotation.listenerSetter();
					String methodName = eventBaseAnnotation.methodName();

					try {
						// 拿到注解里面的value方法
						Method aMethod = annotationType.getDeclaredMethod("value");
						// 取出所有viewId
						int[] viewIds = (int[]) aMethod.invoke(annotation, null);

						// 通过InvocationHandler设置代理
						DynamicHandler handler = new DynamicHandler(activity);
						handler.addMethod(methodName, method);
						Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
								new Class<?>[] { listenerType }, handler);
						for (int viewId : viewIds) {
							View view = activity.findViewById(viewId);
							Method setMethod = view.getClass().getMethod(listenerSetter, listenerType);
							setMethod.invoke(view, listener);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
