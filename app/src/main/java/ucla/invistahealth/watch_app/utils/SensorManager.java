package ucla.invistahealth.watch_app.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import ucla.invistahealth.watch_app.R;
import ucla.invistahealth.watch_app.sensors.hardware.MotionSensor;
import ucla.invistahealth.watch_app.sensors.hardware.StepSensor;
import ucla.invistahealth.watch_app.utils.constant.Intents;
import ucla.invistahealth.watch_app.utils.status.DeviceStatus;

public class SensorManager {
    public static final String TAG = SensorManager.class.getSimpleName();

    public static final String TUGTEST = "TUGTEST";
    public static final String STANDBY = "STANDBY"; // service is running, sensors are batched
    public static final String STOPPED = "NOT WEARING"; // service has been stopped
    public static final String SENSING = "SENSING"; // classifying data to predict movement for scanning
    public static final String SCAN_ON = "SCAN ON"; // bluetooth scan is being performed
    public static final String SUSPEND = "SUSPEND"; // service is running but all sensors are stopped

    private DeviceStatus deviceStatus;

    public boolean onBody = false;
    private String mCurrentState;
    private Context mContext;

//    private MotionSensor motionSensor;
    private StepSensor stepSensor;

//    private static HeartRateSensor heartRateSensor;
//    private static BeaconStripSensor stripSensor;
//    private static iBeaconSensor beaconSensor;
//    private static BodySensor bodySensor;
//    private static LightSensor lightSensor;
//    private static boolean lightSensorOn = false;

    private BroadcastReceiver sensorUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action == null) return;

            FileUtil.writeToLog(TAG, action);

            switch (action){
//                case Intents.MINUTE_ALARM:
//                    if (mBroadcastHandler != null) mBroadcastHandler.onTimeTick();
//                    final boolean notificationState = SurveyManager.evaluateSurveyNotificationState(context.getApplicationContext());
//                    if (mBroadcastHandler != null)
//                        mBroadcastHandler.onSurveyResponseStatusChanged(notificationState);
//                    break;
//
//                case Intents.UPLOAD_STATUS:
//                    final boolean uploadStatus = intent.getBooleanExtra(Intents.UPLOAD_STATE_BOOLEAN_EXTRA, false);
//                    persistState(Intents.UPLOAD_STATUS, uploadStatus);
//                    if (mBroadcastHandler != null)
//                        mBroadcastHandler.onUploadStatusChanged(uploadStatus);
//                    break;
//
//                case Intents.LOCATION_UPDATED:
//                    final String location = intent.getStringExtra(Intents.LOCATION_STRING_EXTRA);
//                    persistState(Intents.LOCATION_UPDATED, location);
//                    if (mBroadcastHandler != null) mBroadcastHandler.onLocationChanged(location);
//                    break;
//
                case Intents.STEPCOUNT_UPDATED:
                    int stepCount = intent.getIntExtra(Intents.STEPCOUNT_INT_EXTRA, 0);
                    deviceStatus.setStepCount(stepCount);
                    break;
//
//                case Intents.SENSOR_STATE_REFRESH:
//                    final String scanStatus = intent.getStringExtra(Intents.SCAN_STATE);
//                    persistState(Intents.SENSOR_STATE_REFRESH, scanStatus);
//                    if (mBroadcastHandler != null) mBroadcastHandler.onScanStateChanged(scanStatus);
//                    break;
//
//                case Intents.HEARTRATE_SCAN_STARTED:
//                    persistState(Intents.HEARTRATE_INT_EXTRA, HeartRateSensor.HEARTRATESCAN_STARTED);
//                    persistState(Intents.HEARTRATE_ACC_INT_EXTRA, HeartRateSensor.HEARTRATE_ACC_UNKNOWN);
//                    if (mBroadcastHandler != null) mBroadcastHandler.onHeartRateScanStarted();
//                    break;
//
//                case Intents.HEARTRATE_SCAN_STOPPED:
//                    persistState(Intents.HEARTRATE_INT_EXTRA, HeartRateSensor.HEARTRATESCAN_STOPPED);
//                    persistState(Intents.HEARTRATE_ACC_INT_EXTRA, HeartRateSensor.HEARTRATE_ACC_UNKNOWN);
//                    if (mBroadcastHandler != null) mBroadcastHandler.onHeartRateScanStopped();
//                    break;
//
//                case Intents.HEARTRATE_UPDATED:
//                    final int heartRate = intent.getIntExtra(Intents.HEARTRATE_INT_EXTRA, HeartRateSensor.HEARTRATE_DATA_UNKNOWN);
//                    final int accuracy = intent.getIntExtra(Intents.HEARTRATE_ACC_INT_EXTRA, HeartRateSensor.HEARTRATE_ACC_UNKNOWN);
//                    persistState(Intents.HEARTRATE_INT_EXTRA, heartRate);
//                    persistState(Intents.HEARTRATE_ACC_INT_EXTRA, accuracy);
//                    if (mBroadcastHandler != null)
//                        mBroadcastHandler.onHeartRateUpdate(heartRate, accuracy);
//                    break;
//
//
//                case Intent.ACTION_POWER_CONNECTED:
//                case Intent.ACTION_POWER_DISCONNECTED:
//                    if (mBroadcastHandler != null) mBroadcastHandler.onPowerStateChanged();
//                    break;
//
//
//                case Intent.ACTION_DATE_CHANGED:
//                    SharedPreferences.Editor editor =
//                            context.getSharedPreferences(context.getPackageName(),
//                                    Context.MODE_PRIVATE).edit();
//                    editor.putString(context.getApplicationContext().getString(R.string.step_count_offset),
//                            PersistentSystemStateReceiver.getCachedStepCount());
//                    editor.commit();
//
//                    editor = MainActivity.getSharedPreferences(context).edit();
//                    editor.putString(context.getApplicationContext().getString(R.string.step_count_offset),
//                            PersistentSystemStateReceiver.getCachedStepCount());
//                    editor.commit();
//
//                    MainActivity.writeToFile(TAG, "Put step count offset " + PersistentSystemStateReceiver.getCachedStepCount(), INFO);
//
//                case Intents.HOURLY_ALARM:
//                case Intent.ACTION_TIME_CHANGED:
//                case Intent.ACTION_TIMEZONE_CHANGED:
//
////                final boolean notificationState = SurveyManager.evaluateSurveyNotificationState(context.getApplicationContext());
////                if (mBroadcastHandler != null)
////                    mBroadcastHandler.onSurveyResponseStatusChanged(notificationState);
//                    break;
//
//                case Intents.SURVEY_COMPLETE:
//                    if (mBroadcastHandler != null)
//                        mBroadcastHandler.onSurveyResponseStatusChanged(false);
//                    break;
            }

        }
    };


    public SensorManager(Context context, DeviceStatus deviceStatus){
        mContext = context;
//        motionSensor = new MotionSensor(context);
        stepSensor = new StepSensor(context);

        this.deviceStatus = deviceStatus;

//        heartRateSensor = new HeartRateSensor();
//        stripSensor = new BeaconStripSensor();
//        beaconSensor = new iBeaconSensor();
//        bodySensor = new BodySensor();
//        lightSensor = new LightSensor();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intents.STEPCOUNT_UPDATED);
        intentFilter.addAction(Intents.SENSOR_STATE_REFRESH);

        mContext.registerReceiver(sensorUpdateReceiver, intentFilter);

        setState(STANDBY);
    }

    public String getState() {
        return mCurrentState;
    }

    public void setState(String state){
        deviceStatus.setStatus(state);

        if (mCurrentState == null || !mCurrentState.equals(state)) {
            mCurrentState = state;
            onScanStateUpdated();
        }

        switch (state) {

            case TUGTEST:
//                stripSensor.startScan();
                break;

            case SENSING:
//                motionSensor.pollSensor();
                break;

            case SCAN_ON:
//                beaconSensor.startScan();
                break;

            // start the motion sensor & step sensor, and register for minute level updates
            case STANDBY:
//                motionSensor.startScan();
                stepSensor.startScan();
                FileUtil.writeToLog(TAG, "Start step sensor");
                // minuteAlarmReceiver.register();
                break;

            // unregister the minute level receiver, and stop the scan on all the sensors
            case SUSPEND:
            case STOPPED:
                //minuteAlarmReceiver.unregister();
//                heartRateSensor.stopScan();
//                motionSensor.stopScan();
//                beaconSensor.stopScan();
//                stripSensor.stopScan();
//                stepSensor.stopScan();
                break;

            default:
                break;
        }
    }

    public void pollSensors(){
        stepSensor.pollSensor();
    }


    public void receivedAction(String action){
        switch (action) {
            case Intents.NIGHT_TIME:
//                int dim = Integer.valueOf(RemoteConfig.getConfig(RemoteConfig.DIM_BRIGHTNESS));
//                WatchFaceFragment.setScreenBrightness(dim);
                break;

            case Intents.DAY_TIME:
//                WatchFaceFragment.setScreenBrightness(100);
                break;

            case Intents.START_SENSORS:
                //startForeground(372, new Notification());
                setState(STANDBY);
                break;

            case Intents.DUMP_SENSORS:
                setState(SENSING);
                break;

            case Intents.RUN_BEACON_SCAN:
                setState(SCAN_ON);
                break;

            case Intents.RUN_HEARTRATE_SCAN:
//                if (Power.determineCurrentState() == Power.STATE_FUNCTIONAL) {
//                    heartRateSensor.startScan();
//                }
                break;

            case Intents.RUN_TUG_TEST:
//                if (Power.determineCurrentState() == Power.STATE_FUNCTIONAL) {
//                    stripSensor.startScan();
//                }
                break;

            case Intents.STOP_SENSORS:
                //stopForeground(true);
                setState(STOPPED);
                break;

            case Intents.MINUTE_ALARM:
//                FileUtil.writeToLog("On body: " + onBody + " Battery level", "" + Power.getBatteryLevel());
//                LocationSensor.isAway = false;
//                FileUtil.writeToLog(TAG, "Send status check to admin");



//                if (!onBody && !mCurrentState.equals(SCAN_ON)) {
//                    setState(STOPPED);
//                    break;
//                } else if (mCurrentState.equals(STOPPED) && !Power.statePlugged()) {
//                    motionSensor.startScan();
//                    stepSensor.startScan();
//                    setState(SENSING);
//                }
//
//                // every minute, poll the step counter
//                stepSensor.pollSensor();
//                WatchFaceFragment.setScreenBrightness(WatchFaceFragment.screen_brightness, true);
                break;

            case Intents.BODY_SENSOR:
//                if (!onBody) {
//                    setState(STOPPED);
//                    break;
//                } else if (mCurrentState.equals(STOPPED) && !Power.statePlugged()) {
//                    motionSensor.startScan();
//                    stepSensor.startScan();
//                    setState(SENSING);
//                }
                break;

            default:
                break;
        }
    }



    public void onScanStateUpdated() {
        // TODO :: save current scan state to disk
        // broadcast current scan state to registered listeners
        final Intent sIntent = new Intent(Intents.SENSOR_STATE_REFRESH);
        sIntent.putExtra(Intents.SCAN_STATE, mCurrentState);
        mContext.sendBroadcast(sIntent);
    }

}
