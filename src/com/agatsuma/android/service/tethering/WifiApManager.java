package com.agatsuma.android.service.tethering;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class WifiApManager {
    private static final boolean D = true;
    private static final String TAG = "WifiApManager";
    
	private WifiManager mWM;
	private WifiConfiguration config;
	private Handler handler;
	private ServerSocketWatcher server;
	
	public WifiApManager(Context context, Handler handler) {
		config = new WifiConfiguration();
		this.handler = handler;
		config.SSID = "TetherTest";
		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		mWM = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public boolean tetheringStart() {
		setWifiApEnabled(true);
		server = new ServerSocketWatcher(handler);
		return true;
	}
	
	public boolean tetheringStop() {
		setWifiApEnabled(false);
		server.closeServerSocket();
		server = null;
		return true;
	}
	
	private boolean setWifiApEnabled(final boolean enabled) {
		try {
			if (enabled) {
				mWM.setWifiEnabled(false);
			}
			final Method setWifiApEnabled = mWM.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, boolean.class);
			setWifiApEnabled.invoke(mWM, config, enabled);
		} catch(Exception e) {
			Log.e(TAG, "Cannot set WiFi AP state", e);
			return false;
		}
		return true;
	}
}
