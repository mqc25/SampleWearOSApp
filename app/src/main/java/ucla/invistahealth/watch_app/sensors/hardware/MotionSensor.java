package ucla.invistahealth.watch_app.sensors.hardware;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Build;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.classifier.AccelWindow;
import ucla.invistahealth.watch_app.classifier.WatchClassifier;
import ucla.invistahealth.watch_app.sensors.config.MotionSensorConfig;
import ucla.invistahealth.watch_app.sensors.data.MotionSensorData;
import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.utils.constant.Intents;

/**
 * Created by arjun on 3/21/17.
 */


public class MotionSensor extends HardwareSensor<MotionSensorData, MotionSensorConfig> {

    private static final String TAG = MotionSensor.class.getSimpleName();

    public MotionSensor(Context context) {
        super(new MotionSensorConfig(context), context);
    }

    @Override
    protected boolean onStopScan() {
        setSensorDumpAlarm(false);
        return unregisterSensor();
    }

    @Override
    protected boolean onStartScan() {
        setSensorDumpAlarm(true);
        return registerSensor();
    }

    /**
     * wait for flush if device is not in interactive mode, otherwise dump sensors now !
     **/

    @Override
    public void pollSensor() {

        setWakelock(true);

        if (isInteractive()) {
            completeFlushNow();
        } else {
            flushSensor();
        }
    }

    @Override
    protected void completeFlushNow() {
        setSensorDumpAlarm(true);
        runAnalysis();
        flushToDisk();
        setWakelock(false);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        timeSortedFIFO.add(new MotionSensorData(event.timestamp, event.values));
    }

    @Override
    protected void runAnalysis() {

        final int minSamplesPerWindow = 30;
        final int totalSamples = timeSortedFIFO.size();

        // Log if no sensor values were received
        if (totalSamples == 0) {
            FileUtil.writeToLog(TAG, "Error: No sensor values received");
            return;
        }

        // Do nothing if num samples is less than 30
        if (totalSamples < minSamplesPerWindow) {
            return;
        }

        final AccelWindow window = (AccelWindow) mSensorConfig.getFeatureWindow();
        final WatchClassifier classifier = mSensorConfig.getClassifier();
        final long windowDurationMs = window.getWindowSizeMs();

        /* dynamic programming solution --> */
        long startTimestampMs = timeSortedFIFO.peekFirst().getTimestamp();

        for (final MotionSensorData currentData : timeSortedFIFO) {
            final long currentTimestampMs = currentData.getTimestamp();
            final long windowLength = currentTimestampMs - startTimestampMs;

            // window is still being built, add data and move on.
            if (windowLength < windowDurationMs) {
                AccelWindow.add(currentData);
                continue;
            }

            final int numSamples = window.getCount();
            // window is of appropriate length, but the sample density is too low
            // this window is useless, reset and move on to next window;
            if (numSamples < minSamplesPerWindow) {
                startTimestampMs = currentTimestampMs;
                window.clear();
                continue;
            }

            // window is of appropriate length and sample density is high enough,
            // compute features and make prediction
            final boolean isActive = classifier.classify(window);

            // prediction says we are not active: clear this window,
            // reset start-time & build the next one
            if (!isActive) {
                startTimestampMs = currentTimestampMs;
                window.clear();
                continue;
            }

            // prediction says we are active: run beacon scan and exit out
            window.clear();
            requestAction(Intents.RUN_BEACON_SCAN);
            return;
        }
    }

    private void setSensorDumpAlarm(final boolean state) {
        final Intent p_intent = new Intent(mContext, BackgroundService.class).setAction(Intents.DUMP_SENSORS);
        final PendingIntent pendingAlarmIntent = PendingIntent.getService(mContext, 0, p_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (state) {
            final long now = System.currentTimeMillis();
            final long reportingDelayMs = mSensorConfig.getReportingDelayUs() / 1000L;
            final long alarmTime = now + reportingDelayMs;
            final AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(alarmTime, pendingAlarmIntent);
            alarmManager.setAlarmClock(alarmClockInfo, pendingAlarmIntent);
        } else {
            alarmManager.cancel(pendingAlarmIntent);
        }
    }
}