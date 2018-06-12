package think.anew.com.bleremotecontroler.util;

import android.content.Context;
import android.text.TextUtils;

import com.clj.fastble.data.BleDevice;

import org.w3c.dom.Text;

/**
 * @author Administrator
 * @date 2018/6/5
 */

public class PreferenceUtil {
    public static final String CONNECTED_BLE_NAME = "name";
    public static final String CONNECTED_BLE_MAC = "address";

    public static void isSystemReady(Context context) {
        context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getBoolean("", false);
    }

    public static boolean saveConnectedDeviceInfo(Context context, BleDevice device) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).
                edit().
                putString(CONNECTED_BLE_NAME, device.getName()).
                putString(CONNECTED_BLE_MAC, device.getMac()).
                commit();
    }


    public static boolean isConnected(Context context){
        String name = getConnectedBleName(context);
        String mac = getConnectedBleMac(context);
        return !TextUtils.isEmpty(name) || !TextUtils.isEmpty(mac);
    }

    public static String getConnectedBleName(Context context){
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString(CONNECTED_BLE_NAME, "");
    }

    public static String getConnectedBleMac(Context context){
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString(CONNECTED_BLE_MAC, "");
    }

    public static boolean clear(Context context){
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).
                edit().
                putString(CONNECTED_BLE_NAME, "").
                putString(CONNECTED_BLE_MAC, "").
                commit();
    }
}
