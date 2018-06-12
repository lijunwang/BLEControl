package android_serialport_api.sample;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.InvalidParameterException;
import util.ByteUtil;
import util.Command;
import util.CommandHelper;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android_serialport_api.SerialPort;

public class DataReceiverService extends Service {

	private static final String TAG = "DataReceiverService";
	private Application mApplication;
	private SerialPort mSerialPort;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private static final String KEY_ACTION = "com.newhaisiwei.keycode";
	private static final String EXTRA_KEY_VALUE = "KeyCode";
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort();
			mInputStream = mSerialPort.getInputStream();
			mReadThread = new ReadThread();
			mReadThread.start();
		} catch (InvalidParameterException | SecurityException | IOException e) {
			e.printStackTrace();
			Log.d(TAG, "onCreate exception ... " + e.getMessage());
		}
//		importance
		startForeground(1, new Notification());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand ...." + intent);
		return super.onStartCommand(intent, Service.START_STICKY, startId);
	}

	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			byte[] buffer = new byte[256];
			int size;
			StringBuffer sb = new StringBuffer(128);
			boolean isWifiInfo = false;
			int count = 3;
			while (/*!isInterrupted()*/true) {
				try {
					if (mInputStream == null){
						Log.d(TAG, "mInputStream == null");
						return;
					}
					size = mInputStream.read(buffer);
					if (size > 0) {
						String command = new String(buffer, 0, size);
						Log.d(TAG, "in data bb : " + command);
						if(command.contains(Command.COMMAND_CODE_SYNC_WIFI)){
							isWifiInfo = true;
						}
						if(isWifiInfo && count > 0){
							sb.append(command);
							count --;
						}else{
							isWifiInfo = false;
							count =3;
						}
						Log.d(TAG, "fuck you wifi command: " + command );
						Log.d(TAG, "fuck you wifi sb: " + sb.toString());
						CommandHelper.hanlderCommand(DataReceiverService.this, command);
					}
				} catch (IOException e) {
					e.printStackTrace();
					Log.d(TAG, "run IOException:" + e.getMessage());
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy ....");
		startService(new Intent(this, DataReceiverService.class));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
