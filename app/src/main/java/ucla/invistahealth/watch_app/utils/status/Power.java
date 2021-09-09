package ucla.invistahealth.watch_app.utils.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.utils.FileUtil;

public class Power {
    private static final String TAG = Power.class.getSimpleName();
    private Context mContext;
    private DeviceStatus deviceStatus;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null) return;

            switch (action){
                case Intent.ACTION_BATTERY_CHANGED:
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int battery_level = (int) (100.0f * (level / (float) scale));
                    deviceStatus.setBatteryLevel(battery_level);
                    FileUtil.writeToLog(TAG, "Battery level changed: " + battery_level);
                    break;

                case Intent.ACTION_POWER_CONNECTED:
                    deviceStatus.setChargingState(true);
                    FileUtil.writeToLog(TAG, "Charging state: true");
                    break;

                case Intent.ACTION_POWER_DISCONNECTED:
                    deviceStatus.setChargingState(false);
                    FileUtil.writeToLog(TAG, "Charging state: false");
                    break;

            }

        }
    };

    public Power(Context context, DeviceStatus deviceStatus){
        mContext = context;
        this.deviceStatus = deviceStatus;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        Intent batteryStatus = mContext.registerReceiver(broadcastReceiver, intentFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        boolean status = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) > 0;

        deviceStatus.setBatteryLevel((int) (100.0f * (level / (float) scale)));
        deviceStatus.setChargingState(status);
    }

    public static int getBatteryLevel(Context context) {
        final Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (batteryStatus == null)
            return -1;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return (int) (100.0f * (level / (float) scale));
    }

    public static boolean statePlugged(Context context) {
        final Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return (batteryStatus == null || batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) > 0);
    }



    public static void notifySensorService(final Context wakefulContext, final String action) {
        final Intent intent = new Intent(wakefulContext, BackgroundService.class);
        intent.setAction(action);
        wakefulContext.sendBroadcast(intent);
    }

}
