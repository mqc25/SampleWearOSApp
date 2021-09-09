package ucla.invistahealth.watch_app.utils.status;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;


public class DeviceStatus {
    public static final String TAG = DeviceStatus.class.getSimpleName();
    public static final int STATE_LOW_BATTERY = 0;
    public static final int STATE_FUNCTIONAL = 1;
    public static final int STATE_CHARGING = 2;

    private static final int LOW_BATT_THRESHOLD = 1;         // Stop scanning @ 2% battery
    private static int CURRENT_STATE = -1;
    private SharedPreferences watchDetails;
    private Context mContext;
    private Power power;


    public static final String BATTERY_LEVEL = "battery_level";
    public static final String CHARGING_STATE = "charging_state";
    public static final String STEP_COUNT = "step_count";


    private Integer batteryLevel;
    private Boolean chargingState;
    private Boolean internet;
    private Integer stepCount;
    private String status;

    public DeviceStatus(Context context){
        mContext = context;
        watchDetails = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);

        batteryLevel = watchDetails.getInt(BATTERY_LEVEL, -1);
        chargingState = watchDetails.getBoolean(CHARGING_STATE, false);
        stepCount = watchDetails.getInt(STEP_COUNT, 0);

        power = new Power(mContext, this);
    }


    public void setBatteryLevel(Integer batteryLevel){
        this.batteryLevel = batteryLevel;
        watchDetails.edit().putInt(BATTERY_LEVEL, batteryLevel).apply();
    }

    public void setChargingState(Boolean chargingState){
        this.chargingState = chargingState;
        watchDetails.edit().putBoolean(CHARGING_STATE, chargingState).apply();
    }

    public Integer getBatteryLevel(){
        return batteryLevel;
    }


    public Boolean getChargingState(){
        return chargingState;
    }

    public int determineCurrentState(Context context) {

        if (getChargingState()) {
            return STATE_CHARGING;
        }

        if (getBatteryLevel() > LOW_BATT_THRESHOLD) {
            return STATE_FUNCTIONAL;
        }

        return STATE_LOW_BATTERY;
    }

    public Boolean getInternet() {
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Integer getStepCount() {
        return stepCount;
    }

    public void setStepCount(Integer step_count) {
        this.stepCount = step_count;
        watchDetails.edit().putInt(STEP_COUNT, step_count).apply();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
