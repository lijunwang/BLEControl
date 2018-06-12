package think.anew.com.bleremotecontroler.util;

import android.util.Log;

/**
 *
 * @author Administrator
 * @date 2018/5/4
 */

public class XLog {
    private static final String DEFAULT_TAG = "RemoteControl";
    private static final boolean DEBUG = true;
    public static void d(String tag, String msg){
        if(DEBUG){
            Log.d(tag, msg);
        }
    }

    public static void d(String msg){
        d(DEFAULT_TAG, msg);
    }
}
