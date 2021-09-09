package ucla.invistahealth.watch_app.sensors.hardware;

import android.content.Context;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;

import ucla.invistahealth.watch_app.sensors.Sensor;
import ucla.invistahealth.watch_app.sensors.config.HardwareSensorConfig;
import ucla.invistahealth.watch_app.sensors.data.SensorData;
import ucla.invistahealth.watch_app.utils.FileUtil;

public abstract class HardwareSensor<T extends SensorData, S extends HardwareSensorConfig> extends Sensor<T, S> implements SensorEventListener2 {

    private static final String TAG = HardwareSensor.class.getSimpleName();

    private boolean isRegistered;

    public HardwareSensor(final S sensorConfig, Context context) {
        super(sensorConfig, context);
        this.isRegistered = false;
    }

    /**
     * Register the current listener for this HardwareSensor, parameters are fetched from sensor-
     * config. The actual listener callback has to be implemented by the underlying Concrete
     * implementation of a HardwareSensor class. Examples include StepSensor & MotionSensor
     *
     * @return
     */
    protected boolean registerSensor() {
        final android.hardware.Sensor sensor = mSensorConfig.getUnderlyingSensor();
        final int reportingDelay = mSensorConfig.getReportingDelayUs();
        final int samplingDelay = mSensorConfig.getSamplingDelayUs();
        final SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        isRegistered = sensorManager.registerListener(this, sensor, samplingDelay, reportingDelay);
        return isRegistered;
    }

    /**
     * Unregister the current listener for this HardwareSensor
     *
     * @return
     */
    protected boolean unregisterSensor() {
        if (isRegistered) {
            final SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
            isRegistered = false;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        FileUtil.writeToLog(TAG, "onAccuracyChange : " + sensor.getStringType() + " : " + accuracy);
    }

    protected void flushSensor() {
        final SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.flush(this);
    }

    @Override
    public void onFlushCompleted(android.hardware.Sensor sensor) {
        FileUtil.writeToLog(TAG, "onFlushCompleted for sensor: " + sensor.getStringType());
        completeFlushNow();
    }

    protected abstract void completeFlushNow();

    protected abstract void pollSensor();
}
