package com.xhsw.smotor.mysmotor;

import android.util.Log;

/**
 * Created by gg on 2018/3/23.
 */

public class smotorjni {
    static {
        try{
            Log.i("012","try to load smotorjni.so");
            System.loadLibrary("smotorjni");
        }catch (UnsatisfiedLinkError ule){
            Log.e("012","WARNING: Could not load smotorjni.so");
        }
    }
    public native static String myJniTest(int level);
    public native static boolean smotorDeivceOpen();
    public native static boolean smotorDeivceClose();
    public native static int setSmotorSpeed(int speed);
    public native static int setSmotorStep(int step);
    public native static int setSmotorMoveDir(int direction);
}
