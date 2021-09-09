package ucla.invistahealth.watch_app.utils.status;

import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.utils.constant.Intents;

public class Connectivity {
    private static final String TAG = Connectivity.class.getSimpleName();
    private Context mContext;
    private ConnectivityManager connManager;
    private DeviceStatus deviceStatus;
    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            FileUtil.writeToLog(TAG, "onAvailable[NETWORK]: " + network.toString());
            deviceStatus.setInternet(true);
            notifyUploadService(new Intent(Intents.INTERNET_AVAILABLE));
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            FileUtil.writeToLog(TAG, "onLost[NETWORK]: " + network.toString());
            deviceStatus.setInternet(false);
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            FileUtil.writeToLog(TAG, action);

            switch (action){
                case Intents.INTERNET_AVAILABLE:


                    break;

                case Intents.INTERNET_LOST:
                    deviceStatus.setInternet(false);
                    break;
            }
        }
    };

    public Connectivity(Context context, DeviceStatus deviceStatus){
        mContext = context;
        this.deviceStatus = deviceStatus;
        connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connManager.registerNetworkCallback(networkRequest, networkCallback);
        deviceStatus.setInternet(connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isConnected());
        FileUtil.writeToLog(TAG, "Registered Network Callback Listener");
    }

    private void notifyUploadService(Intent intent) {
        final Intent serviceIntent = new Intent(mContext, BackgroundService.class);
        serviceIntent.setAction(intent.getAction());
        serviceIntent.putExtras(intent);
        mContext.startService(intent);
    }


}
