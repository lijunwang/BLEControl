package think.anew.com.bleremotecontroler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;

import think.anew.com.bleremotecontroler.AutoConnectHelper;
import think.anew.com.bleremotecontroler.R;
import think.anew.com.bleremotecontroler.util.XLog;

/**
 *
 * @author Administrator
 * @date 2018/6/11
 */

public class BleAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private ArrayList<BleDevice> mDeviceList;
    private View.OnClickListener mListener;
    private AutoConnectHelper mHelper;
    private final String NAME_FILER = "JDY-08";
    public BleAdapter(Context context, AutoConnectHelper helper){
        mContext = context;
        mDeviceList = new ArrayList<>();
        mHelper = helper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.ble_item, null));
    }

    public void clear(){
        mDeviceList.clear();
    }
    public void addData(BleDevice device){
//        XLog.d("BleAdapter addData 11... " + device.getName() + "," + device.getMac());
        if(NAME_FILER.equals(device.getName())){
//            XLog.d("BleAdapter addData 22... ");
            if(mDeviceList.isEmpty()){
//                XLog.d("BleAdapter addData 33... " + device.getName() + "," + device.getMac());
                mDeviceList.add(device);
            }else{
                for(BleDevice d : mDeviceList){
                    if(device.getName().equals(d.getName()) && d.getMac().equals(device.getMac())){
//                        XLog.d("BleAdapter addData 44... " + d.getName() + "," + d.getMac());
                        return;
                    }else{
//                        XLog.d("BleAdapter addData 55... " + device.getName() + "," + device.getMac());
                        mDeviceList.add(device);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).mName.setText(mDeviceList.get(position).getName());
        ((ItemHolder) holder).mAddress.setText(mDeviceList.get(position).getMac());
        ((ItemHolder) holder).setItemOnClick(mDeviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder{
//        ImageView mIcon;
        TextView mName;
        TextView mAddress;
        private View mItemView;
        public ItemHolder(View itemView) {
            super(itemView);
//            mIcon = itemView.findViewById(R.id.ble_icon);
            mName = itemView.findViewById(R.id.ble_name);
            mAddress = itemView.findViewById(R.id.ble_mac);
            mItemView = itemView;
        }

        public void setItemOnClick(final BleDevice device){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHelper.connectBle(device);
                }
            });
        }
    }
}
