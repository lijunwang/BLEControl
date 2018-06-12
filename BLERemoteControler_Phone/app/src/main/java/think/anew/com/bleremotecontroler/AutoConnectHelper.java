package think.anew.com.bleremotecontroler;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

import think.anew.com.bleremotecontroler.util.Command;
import think.anew.com.bleremotecontroler.util.PreferenceUtil;
import think.anew.com.bleremotecontroler.util.XLog;
import think.anew.com.bleremotecontroler.util.XToast;

/**
 * @author Administrator
 * @date 2018/5/4
 */

public class AutoConnectHelper {
    private BleManager mBleManager;
    private BleScanRuleConfig mScanConfig;
    private BleScanRuleConfig.Builder mBuild;
    private DeviceStateListener mStateListener;
    //    private static final String MAC = "98:7B:F3:5B:1F:C2";
//    private static final String DEVICES_NAME = "ZHANGYANG";
//    private static final String MAC = "58:7A:62:11:12:DC";
//    private static final String DEVICES_NAME = "BKY-E101";
    private static final String MAC = "C8:FD:19:40:76:06";
    private static final String DEVICES_NAME = "JDY-08";

    public BluetoothGatt connectBle(BleDevice device) {
        XLog.d(getClass().getSimpleName() + " connectBle " + device.getName() + "," + device.getMac());
        return mBleManager.connect(device, mGattCallback);
    }

    private BleScanCallback mScanCallBack = new BleScanCallback() {
        @Override
        public void onScanStarted(boolean success) {
            XLog.d(getClass().getSimpleName() + " onScanStarted ");
            if (mStateListener != null) {
                mStateListener.onStartScan();
            }
        }

        @Override
        public void onScanning(BleDevice result) {
            XLog.d(getClass().getSimpleName() + " onScanning 22ww " + result.getMac() + "," + result.getName());
            if (mStateListener != null) {
                mStateListener.onScanning(result);
                /*if (filterDevice(result)) {
                    XLog.d(getClass().getSimpleName() + " goto connect" + result.getMac() + "," + result.getName());
                    mBleManager.cancelScan();
                    mBleManager.connect(result, mGattCallback);
                }*/

                if (PreferenceUtil.isConnected(mActivity) && filterDevice2(result)) {
                    mBleManager.cancelScan();
                    mBleManager.connect(result, mGattCallback);
                }
            }
        }

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            XLog.d(getClass().getSimpleName() + " onScanFinished " + scanResultList.size());
            if (mStateListener != null) {
                mStateListener.onScanFinished(scanResultList);
            }
        }
    };

    private BleGattCallback mGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            XLog.d(getClass().getSimpleName() + " onStartConnect ");
            if (mStateListener != null) {
                mStateListener.onStartConnect();
            }
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            XLog.d(getClass().getSimpleName() + " onConnectFail " + bleDevice.getName() + "," + bleDevice.getMac() + "," + exception.getDescription());
            if (mStateListener != null) {
                mStateListener.onConnectFail(bleDevice, exception);
            }
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            XLog.d(getClass().getSimpleName() + " onConnectSuccess 22 " + bleDevice.getName() + "," + bleDevice.getMac() + "," + gatt + "," + status);
            mConnectedDevice = bleDevice;
            if (mStateListener != null) {
                mStateListener.onConnectedSuccess(bleDevice, gatt, status);
            }
            List<BluetoothGattService> services = gatt.getServices();
            XLog.d("onConnectSuccess ... " + services.size());
            for (BluetoothGattService service : services) {
                UUID uuid = service.getUuid();
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                XLog.d("onConnectSuccess service:" + uuid);
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    XLog.d("onConnectSuccess characteristic:" + characteristic.getValue() + "," + characteristic.getUuid());
                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                }
            }
            mStateConnected = true;
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            XLog.d(getClass().getSimpleName() + " onDisConnected " + isActiveDisConnected + "," + device.getMac() + "," + gatt + "," + status);
            if (mStateListener != null) {
                mStateListener.onDisConnected(isActiveDisConnected, device, gatt, status);
                mStateConnected = false;
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            XLog.d(getClass().getSimpleName() + " onCharacteristicChanged " + "," + new String(characteristic.getValue()));

        }
    };

    private BleWriteCallback mWriteCallBack = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(int current, int total, byte[] justWrite) {
            XLog.d("onWriteSuccess aa ... ");
            if (mStateListener != null) {
                mStateListener.onWriteSuccess(current, total, justWrite);
            }
        }

        @Override
        public void onWriteFailure(BleException exception) {
            XLog.d("onWriteFailure aa ... ");
            if (mStateListener != null) {
                mStateListener.onWriteFail(exception);
            }
        }
    };

    private boolean filterDevice(BleDevice result) {
        if (!TextUtils.isEmpty(result.getName()) && result.getName().trim().equals(DEVICES_NAME)) {
            return true;
        } else if (!TextUtils.isEmpty(result.getMac()) && result.getMac().trim().equals(MAC)) {
            return true;
        }
        return false;
    }

    private boolean filterDevice2(BleDevice result) {
        String savedName = PreferenceUtil.getConnectedBleName(mActivity);
        String savedMac = PreferenceUtil.getConnectedBleMac(mActivity);
        XLog.d("filterDevice2 ... " + savedName + "," + savedMac);
        if (!TextUtils.isEmpty(result.getName()) && result.getName().trim().equals(savedName) &&
                !TextUtils.isEmpty(result.getMac()) && result.getMac().trim().equals(savedMac)) {
            return true;
        }
        return false;
    }

    private BleDevice mConnectedDevice;
    //OLD
//    private String mServiceUUID = "0000fff0-0000-1000-8000-00805f9b34fb";
//    private String mCharacterUUID = "0000fff1-0000-1000-8000-00805f9b34fb";
    //JDY-08
    private String mServiceUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private String mCharacterUUID_POER_ON = "0000ffe2-0000-1000-8000-00805f9b34fb";
    private final String HIGH = "E7F101";
    private final String LOW = "E7F100";
    private String mCharacterUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";

    public String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public boolean writeCharacteristic(BleDevice device, String msg) {
        mBleManager.write(device, mServiceUUID, mCharacterUUID, HexUtil.hexStringToBytes(msg), mWriteCallBack);
        return false;
    }

    public boolean writeCharacteristic(String msg) {
        XLog.d("writeCharacteristic ww22... " + msg + "," + mConnectedDevice);
        if (mConnectedDevice != null) {
            mBleManager.write(mConnectedDevice, mServiceUUID, mCharacterUUID, msg.getBytes(), mWriteCallBack);
        }
        return false;
    }

    private Handler mHandler;

    public void powerOn() {
        if (mConnectedDevice != null) {
            XLog.d(getClass().getSimpleName() + " highPower ... " + HIGH + "," + HexUtil.hexStringToBytes(HIGH));
            mBleManager.write(mConnectedDevice, mServiceUUID, mCharacterUUID_POER_ON, HexUtil.hexStringToBytes(HIGH), mWriteCallBack);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    XLog.d(getClass().getSimpleName() + " lowPower ... " + LOW + "," + HexUtil.hexStringToBytes(LOW));
                    mBleManager.write(mConnectedDevice, mServiceUUID, mCharacterUUID_POER_ON, HexUtil.hexStringToBytes(LOW), mWriteCallBack);
                }
            }, 5000);
        }
    }

    public void powerOff() {
        if (mConnectedDevice != null) {
            XLog.d(getClass().getSimpleName() + " highPower ... " + HIGH + "," + HexUtil.hexStringToBytes(HIGH));
            mBleManager.write(mConnectedDevice, mServiceUUID, mCharacterUUID_POER_ON, HexUtil.hexStringToBytes(HIGH), mWriteCallBack);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    XLog.d(getClass().getSimpleName() + " lowPower ... " + LOW + "," + HexUtil.hexStringToBytes(LOW));
                    mBleManager.write(mConnectedDevice, mServiceUUID, mCharacterUUID_POER_ON, HexUtil.hexStringToBytes(LOW), mWriteCallBack);
                }
            }, 1000);
        }
    }

    private boolean mStateConnected = false;

    public boolean isConnected() {
        return mStateConnected;
    }

    private Activity mActivity;

    public AutoConnectHelper(Activity activity) {
        mHandler = new Handler(activity.getMainLooper());
        mActivity = activity;
        mBleManager = BleManager.getInstance();
        mBleManager.init(activity.getApplication());
        mBleManager.enableLog(true).setReConnectCount(1, 5000).setOperateTimeout(5000);
        if (!mBleManager.isBlueEnable()) {
            XToast.showLong(activity, R.string.toast_enable_bluetooth);
            mBleManager.enableBluetooth();
        }

        if (!mBleManager.isSupportBle()) {
            XToast.show(activity, R.string.toast_no_ble);
            activity.finish();
        }

        String macSaved = PreferenceUtil.getConnectedBleMac(activity);
        String nameSaved = PreferenceUtil.getConnectedBleName(activity);

        XLog.d(getClass().getSimpleName() + " ww macName: " + macSaved + ", " + nameSaved);
        /*mScanConfig = new BleScanRuleConfig.Builder().
                setDeviceMac(MAC).
                setDeviceName(false,DEVICES_NAME).
                setAutoConnect(true).
                build();*/
        mBuild = new BleScanRuleConfig.Builder();
        if (!TextUtils.isEmpty(macSaved)) {
            mBuild.setDeviceMac(macSaved);
        }
        if (!TextUtils.isEmpty(nameSaved)) {
            mBuild.setDeviceName(false, nameSaved);
        }
        mScanConfig = mBuild.build();
//        mBleManager.initScanRule(mScanConfig);
        mBleManager.scan(mScanCallBack);
    }


    public void refresh() {
        String macSaved = PreferenceUtil.getConnectedBleMac(mActivity);
        String nameSaved = PreferenceUtil.getConnectedBleName(mActivity);
        XLog.d(getClass().getSimpleName() + " refresh macName: " + macSaved + ", " + nameSaved);
        if (!TextUtils.isEmpty(macSaved)) {
            mBuild.setDeviceMac(macSaved);
        }
        if (!TextUtils.isEmpty(nameSaved)) {
            mBuild.setDeviceName(false, nameSaved);
        }
        mScanConfig = mBuild.build();
        mBleManager.initScanRule(mScanConfig);
        mBleManager.scan(mScanCallBack);
    }

    public void destroy() {
        mBleManager.disconnectAllDevice();
        mBleManager.destroy();
    }

    public void setDeviceStateListener(DeviceStateListener listener) {
        mStateListener = listener;
    }

    public interface DeviceStateListener {
        public void onStartScan();

        public void onScanning(BleDevice device);

        public void onScanFinished(List<BleDevice> scanResultList);

        public void onStartConnect();

        public void onConnectedSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status);

        public void onConnectFail(BleDevice bleDevice, BleException exception);

        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status);

        public void onWriteSuccess(int current, int total, byte[] justWrite);

        public void onWriteFail(BleException exception);
    }

    public void executeCommand(String command) {
        writeCharacteristic(command);
    }
}
