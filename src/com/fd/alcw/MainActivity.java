package com.fd.alcw;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity
{
	private final static String TAG = "MainActivity";
	
	private String mTestString = null;
	
	
	public void setString(String aValue)
	{
		mTestString = aValue;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate "+mTestString);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(TAG, "onStart");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.d(TAG, "onStop");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.d(TAG, "onRestart");
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		Log.d(TAG, "onPostCreate");
	}
	
	@Override
	protected void onPostResume()
	{
		super.onPostResume();
		Log.d(TAG, "onPostResume");
	}
}
