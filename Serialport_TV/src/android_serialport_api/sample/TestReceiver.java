package android_serialport_api.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard.Key;

public class TestReceiver extends BroadcastReceiver{
	private static final String KEY_ACTION = "com.newhaisiwei.keycode";
	private static final String EXTRA_KEY_VALUE = "KeyCode";
	@Override
	public void onReceive(Context context, Intent intent) {
		if(KEY_ACTION.equals(intent.getAction())){
			int key = intent.getIntExtra(EXTRA_KEY_VALUE, -1);
		}
	}

}
