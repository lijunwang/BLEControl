package think.anew.com.bleremotecontroler.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Administrator
 * @date 2018/5/9
 */

public class XToast {
    private static Toast mToast;

    public static void show(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showLong(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void show(Context context, int msgId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, context.getResources().getString(msgId), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msgId);
        }
        mToast.show();
    }

    public static void showLong(Context context, int msgId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, context.getResources().getString(msgId), Toast.LENGTH_LONG);
        } else {
            mToast.setText(msgId);
        }
        mToast.show();
    }
}
