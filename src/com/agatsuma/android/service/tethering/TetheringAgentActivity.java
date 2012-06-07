package com.agatsuma.android.service.tethering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TetheringAgentActivity extends Activity {
	private Button startButton, stopButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startButton = (Button)findViewById(R.id.startButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        
        startButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                startService(new Intent(TetheringAgentActivity.this, TetheringService.class));
            }
        });

        stopButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                stopService(new Intent(TetheringAgentActivity.this, TetheringService.class));
            }
        });	
    }
}