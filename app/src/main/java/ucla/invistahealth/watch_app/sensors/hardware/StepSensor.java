package ucla.invistahealth.watch_app.sensors.hardware;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;

import ucla.invistahealth.watch_app.sensors.config.StepSensorConfig;
import ucla.invistahealth.watch_app.sensors.data.StepData;
import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.utils.constant.Intents;

/**
 * Created by arjun on 3/23/17.
 */

public class StepSensor extends HardwareSensor<StepData, StepSensorConfig> {


    private static final String TAG = StepSensor.class.getSimpleName();

    /**
     * Create a StepSensor object with sampling-delay : 10 seconds & reporting-delay : 60 seconds
     */
    public StepSensor(Context context) {
        super(new StepSensorConfig(context), context);
    }

    @Override
    protected StepData setWriteToEmptyFileData() {
        return new StepData(0L, 0);
    }

    @Override
    protected boolean onStopScan() {
        flushSensor();
        return unregisterSensor();
    }

    @Override
    protected boolean onStartScan() {
        return registerSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        timeSortedFIFO.add(new StepData(event.timestamp, event.values[0]));
    }

    @Override
    public void pollSensor() {
        flushSensor();
    }

    @Override
    protected void completeFlushNow() {

        // every-time new steps are available, send broadcast
        final StepData lastStepData = timeSortedFIFO.peekLast();

        if (lastStepData != null) {
            updateStepCount(lastStepData.getStepCount());
        }

        // flush the data to disk
        flushToDisk();
    }

    private void updateStepCount(final int stepCount) {
        FileUtil.writeToLog(TAG, "Update step count");
        final Intent bIntent = new Intent(Intents.STEPCOUNT_UPDATED);
        bIntent.putExtra(Intents.STEPCOUNT_INT_EXTRA, stepCount);
        mContext.sendBroadcast(bIntent);
    }

}