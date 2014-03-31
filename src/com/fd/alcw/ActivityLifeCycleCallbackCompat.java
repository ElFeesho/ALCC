package com.fd.alcw;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;

import com.google.dexmaker.stock.ProxyBuilder;

public class ActivityLifeCycleCallbackCompat
{
	public interface ActivityLifeCycleCallbackCompatListener
	{
		public void activityLifeCycleCallbackOnCreateWillBeCalled(Activity aActivity);
		public void activityLifeCycleCallbackOnResumeWillBeCalled(Activity aActivity);
		public void activityLifeCycleCallbackOnPauseWillBeCalled(Activity aActivity);
		public void activityLifeCycleCallbackOnStartWillBeCalled(Activity aActivity);
		public void activityLifeCycleCallbackOnStopWillBeCalled(Activity aActivity);
		public void activityLifeCycleCallbackOnDestroyWillBeCalled(Activity aActivity);
	}
	
	private ActivityLifeCycleCallbackCompatListener mListener = null;
	
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
			if(method.getName().contentEquals("callActivityOnCreate"))
			{
				mListener.activityLifeCycleCallbackOnCreateWillBeCalled((Activity) arguments[0]);
			}
			else if(method.getName().contentEquals("callActivityOnPause"))
			{
				mListener.activityLifeCycleCallbackOnPauseWillBeCalled((Activity) arguments[0]);
			}
			else if(method.getName().contentEquals("callActivityOnStart"))
			{
				mListener.activityLifeCycleCallbackOnStartWillBeCalled((Activity) arguments[0]);
			}
			else if(method.getName().contentEquals("callActivityOnStop"))
			{
				mListener.activityLifeCycleCallbackOnStopWillBeCalled((Activity) arguments[0]);
			}
			else if(method.getName().contentEquals("callActivityOnResume"))
			{
				mListener.activityLifeCycleCallbackOnResumeWillBeCalled((Activity) arguments[0]);
			}
			else if(method.getName().contentEquals("callActivityOnDestroy"))
			{
				mListener.activityLifeCycleCallbackOnDestroyWillBeCalled((Activity) arguments[0]);
			}
			return method.invoke(mConcrete, arguments);
		}
	}
	
	public ActivityLifeCycleCallbackCompat(ActivityLifeCycleCallbackCompatListener aListener)
	{
		mListener = aListener;
		bindProxy();
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
}
