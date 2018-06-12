package util;

import android.util.Log;
/*
样本
5a5a 1503 00d0 02 9e 

5a5a 1503 00d2 02 a0               

5a5a 1503 00d1 02 9f

5a5a 1503 00d1 02 9f

5a5a 1503 00d1 02 9f

5a5a 1503 00ea 02 b8

格式：
	一、前4个字节5a5a1503为固定值
	二、第五、六个字节表示距离为，分高八位和低八位，计算公式为高八位<<8 | 低八位
	三、第七个直接表示精度模式，02表示高精度
	四、第八个直接为校验位。 
 */
public class Wt06Util {
	private static final String TAG = "Wt06Util";
	public static int getDistance(byte[] buffer) {
		String raw = ByteUtil.byteArrToHexString(buffer).substring(0, 16);
		String high8Bit = raw.substring(8, 10);
		String low8Bit = raw.substring(10, 12);
		Log.d(TAG, "getDistance in buffer ... " + raw + "," + high8Bit + "," + low8Bit);
		int distance = Integer.parseInt(high8Bit,16) << 8 | Integer.parseInt(low8Bit,16);
		Log.d(TAG, "getDistance ... " + distance);
		return distance;
	}

	public static boolean isDataCorrect(byte[] buffer) {
		return false;
	}
	
	public static int getAccuracyMode(byte[] buffer){
		String raw = ByteUtil.byteArrToHexString(buffer).substring(0, 16);
		String mode = raw.substring(12, 14);
		Log.d(TAG, "getAccuracyMode in buffer ... " + raw + "," + mode);
		return Integer.parseInt(mode);
	}
}
