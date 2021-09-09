package ucla.invistahealth.watch_app;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.TimeZone;

import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.utils.SensorManager;
import ucla.invistahealth.watch_app.utils.status.DeviceStatus;
import ucla.invistahealth.watch_app.utils.threads.DefaultExecutorSupplier;
import ucla.invistahealth.watch_app.viewmodels.HomeViewModel;
import weka.Run;

public class BackgroundService extends Service {
    public static final String TAG = BackgroundService.class.getSimpleName();
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private IBinder mBinder = new BackgroundBinder();
    private static String SN = "UNKNOWN";
    private DefaultExecutorSupplier defaultExecutorSupplier;
    public static String locale = "America/Los_Angeles";
    private DeviceStatus deviceStatus;
    private HomeViewModel homeViewModel;
    private Handler handler;
    private SensorManager sensorManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(defaultExecutorSupplier == null) defaultExecutorSupplier = new DefaultExecutorSupplier();
        FileUtil.initLogging(defaultExecutorSupplier);

        SN = Build.getSerial();
        FileUtil.writeToLog(TAG, "fetch SN " + SN);

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ucla)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        FileUtil.writeToLog(TAG, "start foreground service");
        deviceStatus = new DeviceStatus(getApplicationContext());
        sensorManager = new SensorManager(getApplicationContext(), deviceStatus);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(this, 10000);
            }
        };

        handler = new Handler();
        handler.post(runnable);

        return START_STICKY;
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    public class BackgroundBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    public static String getSerialNumber(){
        return SN;
    }

    public void setHomeViewModel(HomeViewModel homeViewModel){
        this.homeViewModel = homeViewModel;
        homeViewModel.serialNumber.postValue(SN);
        homeViewModel.app_version.postValue(BuildConfig.VERSION_NAME);

    }

    public void updateUI(){
        if(homeViewModel != null){
            defaultExecutorSupplier.forMainThreadTasks().execute(() ->{
                FileUtil.writeToLog(TAG, "update UI");
                updateTime(homeViewModel);
                updateBatteryLevel(homeViewModel);
                updateStepCount(homeViewModel);
                updateStatus(homeViewModel);
                sensorManager.pollSensors();
            });
        }
    }

    private void updateBatteryLevel(HomeViewModel homeViewModel){
        final int formattedString = deviceStatus.getChargingState() ? R.string.charging_level : R.string.battery_level;
        final String text = getApplicationContext().getResources().getString(formattedString, deviceStatus.getBatteryLevel());
        homeViewModel.battery_lvl.postValue(text);
    }

    private void updateTime(HomeViewModel homeViewModel) {
        //final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);/
        //Military format
        final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("hh:mm", Locale.US);
        final SimpleDateFormat AM_PM = new SimpleDateFormat("a", Locale.US);
        //Normal am/pm format
        final TimeZone timeZone = TimeZone.getTimeZone(locale);
        AMBIENT_DATE_FORMAT.setTimeZone(timeZone);
        AM_PM.setTimeZone(timeZone);


        ZonedDateTime watchTime = ZonedDateTime.now(ZoneId.of(locale));
        String time = AMBIENT_DATE_FORMAT.format((watchTime.toInstant().toEpochMilli()));
        String am_pm = AM_PM.format((watchTime.toInstant().toEpochMilli()));
        homeViewModel.time.postValue(time);
        homeViewModel.am_pm.postValue(am_pm);

        FileUtil.writeToLog("Time", time + " " + am_pm);
    }

    private void updateStepCount(HomeViewModel homeViewModel){
        String stepCount = String.valueOf(deviceStatus.getStepCount());
        homeViewModel.steps.postValue(stepCount);
        FileUtil.writeToLog("Stepcount", stepCount);
    }

    private void updateStatus(HomeViewModel homeViewModel){
        String status = String.valueOf(deviceStatus.getStatus());
        homeViewModel.status.postValue(status);
        FileUtil.writeToLog("Status", status);
    }

    public static void broadcast(String intent_action){

    }
}
