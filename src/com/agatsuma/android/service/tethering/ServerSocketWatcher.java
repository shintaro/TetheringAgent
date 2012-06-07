package com.agatsuma.android.service.tethering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerSocketWatcher implements Runnable {
	private static final String TAG = "TetheringService";

	private ServerSocket mServer;
    private Socket mSocket;
    private int port = 6001;
    private volatile Thread runner = null;
    private Handler handler;
	
    public ServerSocketWatcher(Handler handler) {
    	this.handler = handler;
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }
    
    public void closeServerSocket() {
    	try {
    		if (mSocket != null)
    			mSocket.close();
        	if (mServer != null)
        		mServer.close();    		
    	} catch (IOException e) {
    		Log.e(TAG, e.getMessage());    		
    	}
    	mSocket = null;
    	mServer = null;
    }
    
	@Override
	public void run() {
		try {
            mServer = new ServerSocket(port);
            mSocket = mServer.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            String message;
            final StringBuilder messageBuilder = new StringBuilder();
            while ((message = in.readLine()) != null){
                messageBuilder.append(message);
            }
    		Log.d(TAG, messageBuilder.toString());
            
            new Thread(new Runnable() {
            	@Override
            	public void run() {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    int i = Integer.parseInt(messageBuilder.toString());
                    if (i > 30 && i < 6000) {
                    	data.putInt("number", Integer.parseInt(messageBuilder.toString()));
                    	msg.setData(data);
                    	handler.sendMessage(msg);
                    }
            	}
            }).start();
            
        } catch (IOException e) {
    		Log.e(TAG, e.getMessage());
        }
	}
}
