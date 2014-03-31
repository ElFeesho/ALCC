package com.fd.alcw;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.fd.alcw.ActivityLifeCycleCallbackCompat.ActivityLifeCycleCallbackCompatListener;

public class ALCWApplication extends Application
{
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		// Time to do this shiz
		
		new ActivityLifeCycleCallbackCompat(new ActivityLifeCycleCallbackCompatListener()
		{
			
			@Override
			public void activityLifeCycleCallbackOnStopWillBeCalled(Activity aActivity)
			{
				Log.d("ALCWApplication", "onStop "+aActivity.getClass().getSimpleName());
			}
			
			@Override
			public void activityLifeCycleCallbackOnStartWillBeCalled(Activity aActivity)
			{
				Log.d("ALCWApplication", "onStart "+aActivity.getClass().getSimpleName());
			}
			
			@Override
			public void activityLifeCycleCallbackOnResumeWillBeCalled(Activity aActivity)
			{
				Log.d("ALCWApplication", "onResume "+aActivity.getClass().getSimpleName());
			}
			
			@Override
			public void activityLifeCycleCallbackOnPauseWillBeCalled(Activity aActivity)
			{
				Log.d("ALCWApplication", "onPause "+aActivity.getClass().getSimpleName());
			}
			
			@Override
			public void activityLifeCycleCallbackOnDestroyWillBeCalled(Activity aActivity)
			{
				Log.d("ALCWApplication", "onDestroy "+aActivity.getClass().getSimpleName());
			}
			
			@Override
			public void activityLifeCycleCallbackOnCreateWillBeCalled(Activity aActivity)
			{
				Log.d("ALCWApplication", "onCreate "+aActivity.getClass().getSimpleName());
				((MainActivity)aActivity).setString("ON CREATE YO");
			}
		});
		
	}
}
