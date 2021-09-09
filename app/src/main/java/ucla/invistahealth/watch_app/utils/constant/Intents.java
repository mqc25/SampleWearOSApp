package ucla.invistahealth.watch_app.utils.constant;

public class Intents {
    public static final String

            WIFI_SCAN_TIMEOUT = "ucla.invistahealth.watch_app.action.WIFI_SCAN_TIMEOUT";
    public static final String SENSOR_STATE_REFRESH = "ucla.invistahealth.watch_app.action.SENSOR_STATE_REFRESH";
    public static final String SCAN_STATE = "SENSOR_SERVICE_STATE";
    public static final String UPLOAD_STATUS = "ucla.invistahealth.watch_app.action.UPLOAD_STATUS";
    public static final String UPLOAD_STATE_BOOLEAN_EXTRA = "ucla.invistahealth.watch_app.string_extra.UPLOAD_STATE_BOOLEAN_EXTRA";
    public static final String RUN_BEACON_SCAN = "ucla.invistahealth.watch_app.action.RUN_BEACON_SCAN";

    /**
     * TUG Test
     **/
    public static final String RUN_TUG_TEST = "ucla.invistahealth.watch_app.action.RUN_TUG_TEST";
    public static final String TUGTEST_STARTED = "ucla.invistahealth.watch_app.action.TUGTEST_STARTED";
    public static final String TUGTEST_STOPPED = "ucla.invistahealth.watch_app.action.TUGTEST_STOPPED";
    public static final String TUGTEST_STARTED_TIMEOUT_EXTRA = "ucla.invistahealth.watch_app.string_extra.TUGTEST_TIMEOUT_MS";

    public static final String START_SENSORS = "ucla.invistahealth.watch_app.action.START_SENSORS";
    public static final String DUMP_SENSORS = "ucla.invistahealth.watch_app.action.DUMP_SENSORS";
    public static final String STOP_SENSORS = "ucla.invistahealth.watch_app.action.STOP_SENSORS";
    public static final String STOP_SELF = "ucla.invistahealth.watch_app.action.STOP_SELF";
    public static final String BODY_SENSOR = "ucla.invistahealth.watch_app.action.BODY_SENSOR";


    public static final String LAUNCH_APP = "ucla.invistahealth.watch_app.action.LAUNCH_APP";
    public static final String MINUTE_ALARM = "ucla.invistahealth.watch_app.action.MINUTE_ALARM";
    public static final String HOURLY_ALARM = "ucla.invistahealth.watch_app.action.HOURLY_ALARM";
    public static final String SURVEY_COMPLETE = "ucla.invistahealth.watch_app.action.SURVEY_COMPLETE";
    public static final String INTERNET_AVAILABLE = "ucla.invistahealth.watch_app.action.INTERNET_AVAILABLE";
    public static final String INTERNET_LOST = "ucla.invistahealth.watch_app.action.INTERNET_LOST";

    /**
     * Location
     **/
    public static final String LOCATION_UPDATED = "ucla.invistahealth.watch_app.action.LOCATION_UPDATED";
    public static final String LOCATION_STRING_EXTRA = "ucla.invistahealth.watch_app.string_extra.location";

    /**
     * Step Count
     **/
    public static final String STEPCOUNT_UPDATED = "ucla.invistahealth.watch_app.action.STEPCOUNT_UPDATED";
    public static final String STEPCOUNT_INT_EXTRA = "ucla.invistahealth.watch_app.int_extra.stepcount";

    /**
     * Heart Rate
     **/
    public static final String RUN_HEARTRATE_SCAN = "ucla.invistahealth.watch_app.action.RUN_HEARTRATE_SCAN";
    public static final String HEARTRATE_SCAN_STARTED = "ucla.invistahealth.watch_app.action.HEARTRATE_SCAN_STARTED";
    public static final String HEARTRATE_SCAN_STOPPED = "ucla.invistahealth.watch_app.action.HEARTRATE_SCAN_STOPPED";
    public static final String HEARTRATE_UPDATED = "ucla.invistahealth.watch_app.action.HEARTRATE_UPDATED";
    public static final String HEARTRATE_ACC_INT_EXTRA = "ucla.invistahealth.watch_app.int_extra.heartrate_acc";
    public static final String HEARTRATE_INT_EXTRA = "ucla.invistahealth.watch_app.int_extra.heartrate";

    /**
     * Light Sensor
     **/

    public static final String NIGHT_TIME = "ucla.invistahealth.watch_app.NIGHT_TIME";
    public static final String DAY_TIME = "ucla.invistahealth.watch_app.DAY_TIME";

    /**
     * Finsh send data
     * begin sync config
     */
    public static final String SEND_DATA_COMPLETE = "ucla.invistahealth.watch_app.action.SEND_DATA_COMPLETE";
    public static final String SYNC_LOCAL_CONFIG_COMPLETE = "ucla.invistahealth.watch_app.action.SYNC_LOCAL_CONFIG_COMPLETE";
    public static final String RETRY_SEND_DATA = "ucla.invistahealth.watch_app.action.RETRY_SEND_DATA";
    public static final String CLOSE_AZURE_CLIENT = "ucla.invistahealth.watch_app.action.CLOSE_AZURE_CLIENT";
    public static final String STOP_UPLOAD = "ucla.invistahealth.watch_app.action.STOP_UPLOAD";
    public static final String STOP_APP = "ucla.invistahealth.watch_app.action.STOP_APP";
    public static final String FORCE_WIFI_CONNECTION = "ucla.invistahealth.watch_app.action.FORCE_WIFI_CONNECTION";


    /**
     * Admin signal
     */
    public static final String UPDATE_FINISH = "ucla.invistahealth.adminapp.UPDATE_FINISH";
    public static final String SARP_LAUNCH = "ucla.invistahealth.adminapp.SARP_LAUNCH";
    public static final String RESTART_SARP = "ucla.invistahealth.adminapp.RESTART_SARP";
    public static final String TURN_OFF_WIFI = "ucla.invistahealth.adminapp.TURN_OFF_WIFI";
    public static final String TURN_ON_WIFI = "ucla.invistahealth.adminapp.TURN_ON_WIFI";
    public static final String DIM_SCREEN = "ucla.invistahealth.adminapp.DIM_SCREEN";
    public static final String LIT_SCREEN = "ucla.invistahealth.adminapp.LIT_SCREEN";
    public static final String REBOOT = "ucla.invistahealth.adminapp.REBOOT";
    public static final String STATUS_CHECK = "ucla.invistahealth.adminapp.STATUS_CHECK";
}
