package util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.KeyEvent;

public class CommandHelper {
	private static final String KEY_ACTION = "com.newhaisiwei.keycode";
	private static final String EXTRA_KEY_VALUE = "KeyCode";

	private static void sendKeyIntent(Context conetxt, int key) {
		Intent intent = new Intent(KEY_ACTION);
		intent.putExtra(EXTRA_KEY_VALUE, key);
		conetxt.sendBroadcast(intent);
	}

	public static void hanlderCommand(Context context, String command) {
		Log.d("CommandHelper", "hanlderCommand 22ww... " + command);
		if(command.startsWith("^")){
			//replace
			Log.d("CommandHelper", "sendTextToEditText replace ");
			sendTextToEditText(context, command.substring(1),false);
		}else if(command.startsWith("|")){
			//append
			Log.d("CommandHelper", "sendTextToEditText append ");
			sendTextToEditText(context, command.substring(1),true);
		}else if (Command.COMMAND_CODE_UP.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_DPAD_UP);
		} else if (Command.COMMAND_CODE_DOWN.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_DPAD_DOWN);
		} else if (Command.COMMAND_CODE_LEFT.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_DPAD_LEFT);
		} else if (Command.COMMAND_CODE_RIGHT.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_DPAD_RIGHT);
		} else if (Command.COMMAND_CODE_OK.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_DPAD_CENTER);
		} else if (Command.COMMAND_CODE_BACK.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_BACK);
		} else if (Command.COMMAND_CODE_MENU.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_MENU);
		} else if (Command.COMMAND_CODE_HOME.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_HOME);
		} else if (Command.COMMAND_CODE_POWER_ON.equals(command)) {
			//TODO
		} else if (Command.COMMAND_CODE_HDMI.equals(command)) {
			//see android.view.KeyEvent.java
			sendKeyIntent(context, 268);
		}else if (Command.COMMAND_CODE_WIFI_DIRECT.equals(command)) {
			//see android.view.KeyEvent.java
			Intent displayInent = new Intent();
			displayInent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$WifiDisplaySettingsActivity"));
			displayInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(displayInent);
		} else if (Command.COMMAND_CODE_CLEAR.equals(command)) {
			sendKeyIntent(context, 269);
		} else if (Command.COMMAND_CODE_SHARE_WIFI.equals(command)) {
			Intent wifiHotInent = new Intent();
			wifiHotInent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$TetherWifiSettingsActivity"));
			wifiHotInent.putExtra("AutoWifiHot", true);
			wifiHotInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(wifiHotInent);
		} else if (Command.COMMAND_CODE_T_SHAPE_ON.equals(command)) {

		} else if (Command.COMMAND_CODE_T_SHAPE_OFF.equals(command)) {

		} else if(Command.COMMAND_CODE_T_SHAPE.equals(command)){
			Intent tShapeInent = new Intent();
			tShapeInent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DisplaySettingsActivity"));
			tShapeInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			tShapeInent.putExtra("AutoGoTShape", true);
			context.startActivity(tShapeInent);
		}else if (Command.COMMAND_CODE_BLUETOOTH_PAIR.equals(command)) {
			Log.d("CommandHelper", "hanlderCommand bt ... " + command);
			Intent btIntent = new Intent();
			btIntent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$BluetoothSettingsActivity"));
			btIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(btIntent);
		}else if(Command.COMMAND_CODE_SYNC_WIFI.equals(command)){
			Log.d("CommandHelper", "hanlderCommand sync wifi ... " + command);
			Intent wifiIntent = new Intent();
			wifiIntent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity"));
			wifiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(wifiIntent);
		}else if (Command.COMMAND_CODE_UNINSTALL.equals(command)) {
			sendKeyIntent(context, KeyEvent.KEYCODE_DEL);
		}
	}
	private static final String ACTION = "com.wlj.send.text";
    private static final Intent mTextIntent = new Intent(ACTION);
    private static final int MSG_TEST = 123;
    private static final String EXTRA_TAG = "text";
    private static final String EXTRA_APPEND = "append";
	private static void sendTextToEditText(Context context, String text, boolean append){
		Log.d("CommandHelper", "sendTextToEditText ww ... " + text + "," + append);
		mTextIntent.putExtra(EXTRA_TAG, text);
		mTextIntent.putExtra(EXTRA_APPEND, append);
		context.sendBroadcast(mTextIntent);
	}
}
