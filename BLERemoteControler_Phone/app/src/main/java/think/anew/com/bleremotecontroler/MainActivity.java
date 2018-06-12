package think.anew.com.bleremotecontroler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import think.anew.com.bleremotecontroler.util.XLog;
import think.anew.com.bleremotecontroler.util.XToast;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button mBt1, mBt2, mBt3;
    private WifiManager mWifiManager;
    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;
    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            for (String permission : NEEDED_PERMISSIONS) {
                if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            this.requestPermissions(NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        XLog.d("onRequestPermissionsResult ..." + requestCode + "," + Arrays.toString(permissions) + "," + Arrays.toString(grantResults));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        XLog.d("onCreate ... " + Build.VERSION.SDK_INT);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!checkPermission()) {
            requestPermission();
        }
    }
    private boolean isZh(){
        return getResources().getConfiguration().locale.getCountry().equals("CN");
    }
    private void initViews() {
        mBt1 = findViewById(R.id.test1);
        mBt2 = findViewById(R.id.test2);
        mBt3 = findViewById(R.id.test3);

        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, isZh() ? "中文" : "English", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test1:
                getWifiInfo();
                break;
            case R.id.test2:
                startActivity(new Intent("com.android.settings.MIRA_VISION"));
                break;
            case R.id.test3:
                startActivity(new Intent(this, RemoteControlerActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    private void listWifiDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getListView().setAdapter(new MyWifiAdapter());
    }

    private class MyWifiAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    private void getWifiInfo() {
        WifiInfo info = mWifiManager.getConnectionInfo();
        int state = mWifiManager.getWifiState();
        XLog.d("getWifiInfo : " + " state: " + state + "," + info);
        if (!mWifiManager.isWifiEnabled()) {
            XToast.show(this, R.string.toast_open_wifi);
            mWifiManager.setWifiEnabled(true);
            return;
        }
        List<ScanResult> resultList = mWifiManager.getScanResults();
        XLog.d("result list " + resultList.size());
        for (ScanResult result : resultList) {
            XLog.d("result ... " + result.SSID + "," + result.BSSID + ",,," + result);
        }

        List<WifiConfiguration> configurationList = mWifiManager.getConfiguredNetworks();
        XLog.d("configurationList ... " + configurationList.size());
        for (WifiConfiguration configuration : configurationList) {
            XLog.d("configuration ... " + configuration.SSID + "," + configuration.BSSID);
        }
    }
}
