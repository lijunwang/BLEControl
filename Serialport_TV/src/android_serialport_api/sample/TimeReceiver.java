package android_serialport_api.sample;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimeReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("BootReceiver", "onReceive ... " + intent.getAction());
		if(Intent.ACTION_TIME_TICK.equals(intent.getAction())){
			Intent service = new Intent();
			service.setComponent(new ComponentName("android_serialport_api.sample", "android_serialport_api.sample.DataReceiverService"));
			service.putExtra("flag", "timeTick");
			context.startService(service);
		}
	}
}
