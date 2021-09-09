package ucla.invistahealth.watch_app.sensors.config;

import android.content.Context;
import android.hardware.SensorManager;

import java.io.File;

import ucla.invistahealth.watch_app.utils.io.Files;

/**
 * Created by work on 7/14/17.
 */

public class StepSensorConfig implements HardwareSensorConfig {

    /**
     * Attempts to get the desired sensor type, preferring a wake-up sensor. If it doesn't exist,
     * returns a non-wakeup sensor instead. Caution: Can return a NULL sensor object
     * as declard by android.hardware.Sensor
     *
     * @return Sensor
     */

    private Context mContext;

    public StepSensorConfig(Context context){
        mContext = context;
    }

    @Override
    public android.hardware.Sensor getUnderlyingSensor() {
        final SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        android.hardware.Sensor newSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_STEP_COUNTER, true);
        if (newSensor == null) {
            newSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_STEP_COUNTER, false);
        }
        return newSensor;
    }

    @Override
    public boolean doesSensorExist() {
        return (getUnderlyingSensor() != null);
    }

    @Override
    public int getSamplingDelayUs() {

        return 10000 * 1000;
    }

    @Override
    public int getReportingDelayUs() {

        return getSamplingDelayUs() * 6;
    }

    @Override
    public int getBufferCapacity() {
        return 64;
    }

    @Override
    public String getSensorName() {
        return "steps";
    }

    @Override
    public File setOutputFileDirectory() {
        return Files.stepsDirectory;
    }
}
