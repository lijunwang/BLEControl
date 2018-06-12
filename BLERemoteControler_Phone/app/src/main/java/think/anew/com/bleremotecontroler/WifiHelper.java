package think.anew.com.bleremotecontroler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import think.anew.com.bleremotecontroler.util.XLog;

/**
 * @author Administrator
 * @date 2018/5/15
 */

public class WifiHelper {

    private Context mContext;
    private WifiManager mWifiManager;
    private WifiAdapter mWifiAdapter;
    private MaterialDialog.Builder mWifiPWDDialogBuilder;

    public WifiHelper(Context context) {
        mContext = context;
        //监听wifi是开关变化的状态
        mWifiStateFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //监听wifi连接状态广播,是否连接了一个有效路由
        mWifiStateFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mWifiStateFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiAdapter = new WifiAdapter();

        mWifiPWDDialogBuilder = new MaterialDialog.Builder(mContext)
                .title(R.string.title_wifi_pwd)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void onResume() {
//        mContext.registerReceiver(mWifiStateReceiver, mWifiStateFilter);
    }

    public void onDestroy() {
//        mContext.unregisterReceiver(mWifiStateReceiver);
    }

    public void listAllWifiDialog() {
        new MaterialDialog.Builder(mContext)
                .title(R.string.title_wifi_list)
                .adapter(mWifiAdapter, new LinearLayoutManager(mContext))
                .dividerColor(Color.RED).show();
    }

    private void showWifiPWDInputDialog() {
        mWifiPWDDialogBuilder.show();
    }

    private void showWifiPWDInputDialog(final String content) {
        mWifiPWDDialogBuilder.content(content).input(R.string.hint_pwd, 0, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(MaterialDialog dialog, CharSequence input) {
                Log.d("Main", "test onInput ... " + input);
                if (mContext instanceof RemoteControlerActivity) {
                    ((RemoteControlerActivity) mContext).onSyncWifiTo(content, input.toString().trim());
                }
            }
        }).show();
    }

    private IntentFilter mWifiStateFilter = new IntentFilter();
    private WifiBroadcastReceiver mWifiStateReceiver = new WifiBroadcastReceiver();

    private class WifiAdapter extends RecyclerView.Adapter {

        private List<ScanResult> mData;

        public WifiAdapter() {
//            mData = mWifiManager.getScanResults();
            mData = sortWifiList(mWifiManager.getScanResults());
        }

        public void notifyWifiChanged() {
            mData.clear();
            XLog.d("notifyWifiChanged ...");
//            mData = mWifiManager.getScanResults();
            mData = sortWifiList(mWifiManager.getScanResults());
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WifiItemHolder(LayoutInflater.from(mContext).inflate(R.layout.wifi_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            WifiItemHolder wifiItemHolder = (WifiItemHolder) holder;
            ((WifiItemHolder) holder).mWifiName.setText(mData.get(position).SSID);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class WifiItemHolder extends RecyclerView.ViewHolder {

        TextView mWifiName;
        ImageView mWifiSign;

        public WifiItemHolder(View itemView) {
            super(itemView);
            mWifiName = itemView.findViewById(R.id.wifi_name);
            mWifiSign = itemView.findViewById(R.id.wifi_sign);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWifiPWDInputDialog(mWifiName.getText().toString());
                }
            });
        }
    }

    //监听wifi状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    case WifiManager.WIFI_STATE_DISABLED: {
                        XLog.d("已经关闭");
                        Toast.makeText(mContext, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        XLog.d("正在关闭 ...");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        XLog.d("已经打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        XLog.d("正在打开 ... ");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        XLog.d("未知状态");
                        break;
                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    XLog.d("wifi未连接上");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    XLog.d("wifi连接上了");
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    XLog.d("wifi 正在连接");
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                XLog.d("网络列表变化了");
            }
        }
    }

    private List<ScanResult> sortWifiList(List<ScanResult> in) {
        for(ScanResult result : in){
            XLog.d("in  ... " + result.SSID + "," + result.BSSID + "," + result.level);
        }
        List<ScanResult> newSr = new ArrayList<ScanResult>();
        for (ScanResult result : in) {
            if (!TextUtils.isEmpty(result.SSID) && !containName(newSr, result.SSID)) {
                newSr.add(result);
            }
        }
        Collections.sort(newSr, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult o1, ScanResult o2) {
                return o1.SSID.compareTo(o2.SSID);
            }
        });
        for(ScanResult result : newSr){
            XLog.d("out return 22 ... " + result.SSID + "," + result.BSSID + "," + result.level);
        }
        return newSr;
    }

    public static boolean containName(List<ScanResult> sr, String name) {
        for (ScanResult result : sr) {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
