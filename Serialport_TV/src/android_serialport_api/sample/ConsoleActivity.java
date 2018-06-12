/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.xhsw.smotor.mysmotor.smotorjni;

import util.ByteUtil;
import util.Wt06Util;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ConsoleActivity extends SerialPortActivity implements OnClickListener{
	private static final String TAG = "ConsoleActivity";
	EditText mReception;
	private EditText mETSend;
	private Button mBtSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console);
		Log.d(TAG, "onCreate ww22... ");
//		setTitle("Loopback test");
		mReception = (EditText) findViewById(R.id.EditTextReception);
		mETSend = (EditText) findViewById(R.id.EditTextEmission);
		mBtSend = (Button) findViewById(R.id.send);
		mBtSend.setOnClickListener(this);
		EditText Emission = (EditText) findViewById(R.id.EditTextEmission);
		Emission.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int i;
				CharSequence t = v.getText();
				char[] text = new char[t.length()];
				for (i=0; i<t.length(); i++) {
					text[i] = t.charAt(i);
				}
				Log.d(TAG, "onEditorAction ... " + t.toString());
				try {
					mOutputStream.write(new String(text).getBytes());
					mOutputStream.write('\n');
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (mReception != null) {
					String command = new String(buffer, 0, size);
					Log.d(TAG, "onDataReceived tt... " + command);
//					Wt06Util.getDistance(buffer);
//					Wt06Util.getAccuracyMode(buffer);
//					mReception.append(new String(buffer, 0, buffer.length));
					mReception.append(ByteUtil.byteArrToHexString(buffer));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			Log.d(TAG, "send ttww ... " + mETSend.getText().toString());
			sendData(mReception.getText().toString());
			break;
		default:
			break;
		}
	}
	
	private void sendData(String text){
		try {
			Log.d(TAG, "send tt ww... " + text);
			mOutputStream.write(new String(text).getBytes());
			mOutputStream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "send tt IOException... " + e.getMessage());
		}
	}
}
