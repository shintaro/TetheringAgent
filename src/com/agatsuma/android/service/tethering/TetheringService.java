package com.agatsuma.android.service.tethering;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class TetheringService extends Service {

	private static final String TAG = "TetheringService";
	private WifiApManager wapm;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
		wapm = new WifiApManager(getBaseContext(), tetherStopHandler);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		Toast.makeText(this, "Service Start", Toast.LENGTH_SHORT).show();
		wapm.tetheringStart();
	    return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    Log.d(TAG, "onDestroy");
	    wapm.tetheringStop();
	    Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
	}
	
	final Handler tetherStopHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			int i = data.getInt("number");
			if (i < 30) {
				wapm.tetheringStart();
			    Toast.makeText(getApplicationContext(), "Tethering Start", Toast.LENGTH_SHORT).show();
			}
			else {
				data.putInt("number", 0);
				msg.setData(data);
				tetherWakeHandler.sendEmptyMessageDelayed(0, (i-30)*1000);
				wapm.tetheringStop();
			    Toast.makeText(getApplicationContext(), String.format("Tethering Stops, and restart %ds later", i), Toast.LENGTH_SHORT).show();
			}
		}
	};

	final Handler tetherWakeHandler = new Handler() {
		public void handleMessage(Message msg) {
			wapm.tetheringStart();
		}
	};
}
