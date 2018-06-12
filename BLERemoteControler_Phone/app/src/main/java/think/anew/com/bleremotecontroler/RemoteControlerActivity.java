package think.anew.com.bleremotecontroler;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.lang.reflect.Type;
import java.util.List;

import think.anew.com.bleremotecontroler.adapter.BleAdapter;
import think.anew.com.bleremotecontroler.util.Command;
import think.anew.com.bleremotecontroler.util.PreferenceUtil;
import think.anew.com.bleremotecontroler.util.XLog;
import think.anew.com.bleremotecontroler.util.XToast;

/**
 * @author Administrator
 */
public class RemoteControlerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AutoConnectHelper.DeviceStateListener, View.OnClickListener, View.OnLongClickListener {

    private ProgressBar mProgressBar;
    private ViewGroup mMainContent;
    private AutoConnectHelper mAutoConnectHelper;
    //    private WifiHelper mWifiHelper;
    private ImageButton mBtUp, mBtDown, mBtLeft, mBtRight, mBtCenter, mBtBack;
    private EditText mInput;

    private ImageButton mBtFunctionPowerOn, mBtFunctionSyncWifi, mBtFunctionShareWifi, mBtFunctionWifiDirect,
            mBtFunctionHDMI, mBtFunctionClear, mBtFunctionBluetoothPair;
    private ImageButton mBtMouse, mBtHome, mBtUninstall, mBtTShape, mBtMore;
    private Switch mSwitchTShape;
    private SeekBar mSeekBarTShape;
    private ViewGroup mCover;
    private BleAdapter mBleAdapter;
    private MaterialDialog mBleListDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_controller);
        initViews();
        initFunctionButtons();

        mBleAdapter = new BleAdapter(this, mAutoConnectHelper);
        mBleListDialog = new MaterialDialog.Builder(this)
                .title(R.string.ble_list_title)
                .adapter(mBleAdapter, new LinearLayoutManager(this))
                .dividerColor(Color.RED).build();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAutoConnectHelper = new AutoConnectHelper(this);
//        mWifiHelper = new WifiHelper(this);
        mAutoConnectHelper.setDeviceStateListener(this);

        mBtUp = findViewById(R.id.control_up);
        mBtDown = findViewById(R.id.control_down);
        mBtLeft = findViewById(R.id.control_left);
        mBtRight = findViewById(R.id.control_right);
        mBtCenter = findViewById(R.id.control_center);
        mBtBack = findViewById(R.id.control_back);
        mMainContent = findViewById(R.id.remote_control_main_layout);
        mProgressBar = findViewById(R.id.progressBar);
        mCover = findViewById(R.id.cover);

        mBtUp.setOnClickListener(this);
        mBtDown.setOnClickListener(this);
        mBtLeft.setOnClickListener(this);
        mBtRight.setOnClickListener(this);
        mBtBack.setOnClickListener(this);
        mBtCenter.setOnClickListener(this);
    }

    private void initFunctionButtons() {
        mBtFunctionPowerOn = findViewById(R.id.function_power_on);
//        mBtFunctionPowerOn.setOnClickListener(this);
        mBtFunctionPowerOn.setOnLongClickListener(this);
        mBtFunctionSyncWifi = findViewById(R.id.function_sync_wifi);
        mBtFunctionSyncWifi.setOnClickListener(this);
        mBtFunctionShareWifi = findViewById(R.id.function_share_wifi);
        mBtFunctionShareWifi.setOnClickListener(this);
        mBtFunctionWifiDirect = findViewById(R.id.function_wifi_direct);
        mBtFunctionWifiDirect.setOnClickListener(this);
        mBtFunctionHDMI = findViewById(R.id.function_HDMI);
        mBtFunctionHDMI.setOnClickListener(this);
        mBtFunctionClear = findViewById(R.id.function_clear);
        mBtFunctionClear.setOnClickListener(this);
        mBtFunctionBluetoothPair = findViewById(R.id.function_bluetooth_pair);
        mBtFunctionBluetoothPair.setOnClickListener(this);
        mBtMouse = findViewById(R.id.function_mouse);
        mBtMouse.setOnClickListener(this);
        mBtHome = findViewById(R.id.function_home);
        mBtHome.setOnClickListener(this);
        mBtUninstall = findViewById(R.id.btn_uninstall);
        mBtUninstall.setOnClickListener(this);
        mBtTShape = findViewById(R.id.function_tshape);
        mBtTShape.setOnClickListener(this);
        mBtMore = findViewById(R.id.function_more);
        mBtMore.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if(mCover.getVisibility() == View.VISIBLE){
            finish();
        }
        //go Home to speed connect
        XLog.d(getClass().getSimpleName() + "onBackPressed go home");
        Intent intentHome = new Intent(Intent.ACTION_MAIN);
        intentHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(intentHome);

        //double quick click to exit
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remote_controler, menu);
        XLog.d("onCreateOptionsMenu ... " + mAutoConnectHelper.isConnected());
        return !mAutoConnectHelper.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        XLog.d("menu onOptionsItemSelected ... " + mAutoConnectHelper.isConnected());
        switch (item.getItemId()) {
            case R.id.refresh:
                mAutoConnectHelper.refresh();
                mProgressBar.setVisibility(View.VISIBLE);
                mCover.setVisibility(View.GONE);
                mMainContent.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
//            XToast.show(this, "showWifiList");
//            mWifiHelper.listAllWifiDialog();
            PreferenceUtil.clear(this);
            mAutoConnectHelper.destroy();
            mCover.setVisibility(View.VISIBLE);
            mAutoConnectHelper.refresh();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStartScan() {
        XLog.d(getClass().getSimpleName() + " onStartScan 22 ");
        /*new MaterialDialog.Builder(this)
                .title(R.string.title_wifi_list)
                .adapter(mBleAdapter, new LinearLayoutManager(this))
                .dividerColor(Color.RED).show();*/
        if(!PreferenceUtil.isConnected(this)){
            mBleListDialog.show();
        }
        mBleAdapter.clear();
    }

    @Override
    public void onScanning(BleDevice device) {
        setState(R.string.state_scanning);
        XLog.d(getClass().getSimpleName() + " onScanning " + device.getMac() + "," + device.getName());
        mBleAdapter.addData(device);
    }

    @Override
    public void onScanFinished(List<BleDevice> scanResultList) {
        for (BleDevice device : scanResultList) {
            XLog.d(getClass().getSimpleName() + " onScanFinished " + device.getName() + "," + device.getMac());
        }
    }

    @Override
    public void onStartConnect() {
        setTitle(R.string.state_connecting);
    }

    @Override
    public void onConnectedSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
        XToast.show(this, R.string.toast_connect_success);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        mMainContent.setVisibility(View.VISIBLE);
        XLog.d(getClass().getSimpleName() + " onConnectedSuccess " + mProgressBar + "," + mMainContent);
        setState(R.string.state_connected);
        invalidateOptionsMenu();
        hideCover();
        PreferenceUtil.saveConnectedDeviceInfo(this, bleDevice);
        mBleListDialog.cancel();
    }

    @Override
    public void onConnectFail(BleDevice bleDevice, BleException exception) {
        XToast.show(this, R.string.toast_connect_fail);
        setState(R.string.state_connect_fail);
        invalidateOptionsMenu();
    }

    @Override
    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
        XToast.show(this, R.string.toast_disconnect);
        setState(R.string.state_disconnected);
        invalidateOptionsMenu();
        showCover();
    }

    private void showCover() {
        mCover.setVisibility(View.VISIBLE);
    }

    private void hideCover() {
        mCover.setVisibility(View.GONE);
    }

    @Override
    public void onWriteSuccess(int current, int total, byte[] justWrite) {
        XLog.d("onWriteSuccess ...");
//        XToast.show(this, R.string.write_success);
    }

    @Override
    public void onWriteFail(BleException exception) {
        XLog.d("onWriteFail ..." + exception.getDescription());
        XToast.show(this, R.string.write_fail);
    }

    private void handlerPowerKey() {
        new MaterialDialog.Builder(this).dividerColor(Color.RED).itemsGravity(GravityEnum.START).items(getResources().getString(R.string.function_power_on), getResources().getString(R.string.function_power_off), getResources().getString(R.string.text_cancel)).
                itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        XLog.d(this.getClass().getSimpleName() + " onSelection " + text + "," + position);
                        switch (position) {
                            case 0:
                                mAutoConnectHelper.powerOn();
                                break;
                            case 1:
                                mAutoConnectHelper.powerOff();
                                break;
                            case 2:
                                dialog.cancel();
                                break;
                            default:
                                break;
                        }
                    }
                }).build().show();
    }

    @Override
    public void onClick(View v) {
        XLog.d("onClick ... ");
        switch (v.getId()) {
            case R.id.control_up:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_UP);
                break;
            case R.id.control_down:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_DOWN);
                break;
            case R.id.control_left:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_LEFT);
                break;
            case R.id.control_right:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_RIGHT);
                break;
            case R.id.control_back:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_BACK);
                break;
            case R.id.control_center:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_OK);
                break;
            /*case R.id.function_power_on:
                handlerPowerKey();
                break;*/
            case R.id.function_bluetooth_pair:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_BLUETOOTH_PAIR);
                break;
            case R.id.function_sync_wifi:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_SYNC_WIFI);
                break;
            case R.id.function_share_wifi:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_SHARE_WIFI);
                break;
            case R.id.function_wifi_direct:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_WIFI_DIRECT);
                break;
            case R.id.function_HDMI:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_HDMI);
                break;
            case R.id.function_clear:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_CLEAR);
                break;
            case R.id.function_mouse:
                startActivity(new Intent(this, MouseActivity.class));
                break;
            case R.id.function_home:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_HOME);
                break;
            case R.id.btn_uninstall:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_UNINSTALL);
                break;
            case R.id.function_tshape:
                mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_T_SHAPE);
                break;
            case R.id.function_more:
                XToast.show(this, R.string.more_function);
                new MaterialDialog.Builder(this).title(R.string.text_send_title).inputType(InputType.TYPE_CLASS_TEXT).input(R.string.text_send_hint, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        XLog.d("onPositive onInput ... " + input);
                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XLog.d("onPositive onClick ... " + which + "," + dialog.getInputEditText().getText());
                        String text = dialog.getInputEditText().getText().toString().trim();
                        if (!TextUtils.isEmpty(text)) {
                            //append
                            mAutoConnectHelper.executeCommand("|" + text);
                        }
                    }
                }).onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XLog.d("onNeutral onClick ... " + which);
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XLog.d("onNegative onClick ... " + which + "," + dialog.getInputEditText().getText());
                        String text = dialog.getInputEditText().getText().toString().trim();
                        if (!TextUtils.isEmpty(text)) {
                            //replace
                            mAutoConnectHelper.executeCommand("^" + text);
                        }
                    }
                }).negativeText(R.string.text_replace).positiveText(R.string.text_append).neutralText(R.string.text_cancel).inputRange(0, 16).show();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        mWifiHelper.onResume();
    }

    public void onSyncWifiTo(String wifiName, String pwd) {
        XLog.d("onSyncWifiTo " + wifiName + "," + pwd);
        mAutoConnectHelper.executeCommand(Command.COMMAND_CODE_SYNC_WIFI + "[" + wifiName + "," + pwd + "]");
//        mAutoConnectHelper.executeCommand(wifiName);
//        mAutoConnectHelper.executeCommand(pwd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAutoConnectHelper.destroy();
//        mWifiHelper.onDestroy();
        XLog.d(getClass().getSimpleName() + " onDestroy ");
    }

    private void setState(int msgId) {
        getSupportActionBar().setTitle(msgId);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.function_power_on:
                handlerPowerKey();
                return true;
            default:
                break;
        }
        return false;
    }
}
