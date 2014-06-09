package com.fd.alcw;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.dexmaker.stock.ProxyBuilder;

public class ActivityLifeCycleCallbackCompat
{
	public interface ActivityLifeCycleCallbackCompatListener
	{
		public void onActivityCreated(Activity aActivity, Bundle savedInstanceState);

		public void onActivityDestroyed(Activity aActivity);

		public void onActivityPaused(Activity aActivity);

		public void onActivitySaveInstanceState(Activity activity, Bundle outState);

		public void onActivityResumed(Activity aActivity);

		public void onActivityStarted(Activity aActivity);

		public void onActivityStopped(Activity aActivity);
	}

	private Set<ActivityLifeCycleCallbackCompatListener> mListeners = new HashSet<ActivityLifeCycleCallbackCompatListener>();

	public class InstrumentationProxy implements java.lang.reflect.InvocationHandler
	{
		private Object mConcrete;

		public InstrumentationProxy(Object aActivityManager)
		{
			mConcrete = aActivityManager;
		}

		@Override
		public Object invoke(Object target, Method method, Object[] arguments) throws Throwable
		{
			Log.d("ALCW", "Method: " + method.getName() + " args: " + arguments);
			if (method.getName().contentEquals("callActivityOnCreate"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivityCreated((Activity) arguments[0], (Bundle) arguments[1]);
				}
			}
			else if (method.getName().contentEquals("callActivityOnPause"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivityPaused((Activity) arguments[0]);
				}
			}
			else if (method.getName().contentEquals("callActivityOnSaveInstanceState"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivitySaveInstanceState((Activity) arguments[0], (Bundle) arguments[1]);
				}
			}
			else if (method.getName().contentEquals("callActivityOnStart"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivityStarted((Activity) arguments[0]);
				}
			}
			else if (method.getName().contentEquals("callActivityOnStop"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivityStopped((Activity) arguments[0]);
				}
			}
			else if (method.getName().contentEquals("callActivityOnResume"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivityResumed((Activity) arguments[0]);
				}
			}
			else if (method.getName().contentEquals("callActivityOnDestroy"))
			{
				for (ActivityLifeCycleCallbackCompatListener listener : mListeners)
				{
					listener.onActivityDestroyed((Activity) arguments[0]);
				}
			}
			return method.invoke(mConcrete, arguments);
		}
	}

	public ActivityLifeCycleCallbackCompat(Context aContext)
	{
		bindProxy();

		System.setProperty("dexmaker.dexcache", aContext.getCacheDir().getAbsolutePath());
	}

	private void bindProxy()
	{
		try
		{
			ClassLoader classLoader = getClass().getClassLoader();

			Class<?> activityThreadClass = classLoader.loadClass("android.app.ActivityThread");
			Class<?> instrumentationClass = getClass().getClassLoader().loadClass("android.app.Instrumentation");

			Method getCurrentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread");
			Object activityThreadInstance = getCurrentActivityThread.invoke(activityThreadClass);

			Field instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
			instrumentationField.setAccessible(true);
			Object instrumentationInstance = instrumentationField.get(activityThreadInstance);

			ProxyBuilder<?> proxyClass = ProxyBuilder.forClass(instrumentationClass);

			proxyClass.handler(new InstrumentationProxy(instrumentationInstance));

			Object newInstrumentationProxy = proxyClass.build();
			instrumentationField.set(activityThreadInstance, newInstrumentationProxy);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	public void registerActivityLifeCycleCallbacks(ActivityLifeCycleCallbackCompatListener activityLifeCycleCallbackCompatListener)
	{
		mListeners.add(activityLifeCycleCallbackCompatListener);
	}

	public void unregisterActivityLifeCycleCallbacks(ActivityLifeCycleCallbackCompatListener activityLifeCycleCallbackCompatListener)
	{
		mListeners.remove(activityLifeCycleCallbackCompatListener);
	}
}
