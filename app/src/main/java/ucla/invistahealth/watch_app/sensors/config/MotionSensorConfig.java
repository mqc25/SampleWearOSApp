package ucla.invistahealth.watch_app.sensors.config;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import java.io.File;

import ucla.invistahealth.watch_app.R;
import ucla.invistahealth.watch_app.classifier.AccelWindow;
import ucla.invistahealth.watch_app.classifier.FeatureWindow;
import ucla.invistahealth.watch_app.classifier.WatchClassifier;
import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.utils.io.Files;

public final class MotionSensorConfig implements HardwareSensorConfig {

    private static final String TAG = MotionSensorConfig.class.getSimpleName();
    private static WatchClassifier mClassifier;
    private static AccelWindow mFeatureWindow = new AccelWindow();
    private int mBufferCapacity;
    private int mFifoEventCount;
    private int mSamplingDelayUs;
    private int mReportingDelayUs;
    protected Context mContext;

    public MotionSensorConfig(Context context) {
        mContext = context;
        try {
            mClassifier = new WatchClassifier(R.raw.accel_classifier, context);
        } catch (Exception e) {
            FileUtil.writeToLog(TAG, e.getMessage());
        }

        switch (Build.MODEL) {

            case ("LG Watch Sport"):
                mFifoEventCount = 3000;
                mBufferCapacity = mFifoEventCount;
                mSamplingDelayUs = 20000;
                mReportingDelayUs = mFifoEventCount * mSamplingDelayUs;
                break;

            case ("Moto 360"):
                mBufferCapacity = 600;
                mFifoEventCount = 300;
                mSamplingDelayUs = 20000;
                mReportingDelayUs = mFifoEventCount * mSamplingDelayUs;
                break;

            case ("Gear Live"):
                mBufferCapacity = 600;
                mFifoEventCount = 150;
                mSamplingDelayUs = 76667;
                mReportingDelayUs = mFifoEventCount * mSamplingDelayUs;
                break;

            case ("G Watch"):
                mBufferCapacity = 560;
                mFifoEventCount = 124;
                mSamplingDelayUs = 92742;
                mReportingDelayUs = mFifoEventCount * mSamplingDelayUs;
                break;

            case ("LEO-BX9"):
                mBufferCapacity = 800;
                mFifoEventCount = 240;
                mSamplingDelayUs = 62500;
                mReportingDelayUs = mFifoEventCount * mSamplingDelayUs;
                break;

            default:
            case ("SmartWatch 3"):
                mBufferCapacity = 800;
                mFifoEventCount = 184;
                mSamplingDelayUs = 62500;
                mReportingDelayUs = mFifoEventCount * mSamplingDelayUs;
                break;
        }
    }


    //    /** Attempts to get the desired sensor type, preferring a wake-up sensor. If it doesn't exist,
//     * returns a non-wakeup sensor instead. Caution: Can return a NULL sensor object
//     * @param  Sensor type, as declard by android.hardware.Sensor
//     * @return Sensor
//     */
    @Override
    public android.hardware.Sensor getUnderlyingSensor() {
        final SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        android.hardware.Sensor newSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER, true);
        if (newSensor == null) {
            newSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER, false);
        }


        return newSensor;
    }

    @Override
    public int getSamplingDelayUs() {
        return mSamplingDelayUs;
    }

    @Override
    public int getReportingDelayUs() {
        return mReportingDelayUs;
    }

    @Override
    public int getBufferCapacity() {
        return mBufferCapacity;
    }

    @Override
    public String getSensorName() {
        return "accel";
    }

    public WatchClassifier getClassifier() {
        return mClassifier;
    }

    public FeatureWindow getFeatureWindow() {
        return mFeatureWindow;
    }

    @Override
    public File setOutputFileDirectory() {
        return Files.gyroDirectory;
    }

    @Override
    public boolean doesSensorExist() {
        return (getUnderlyingSensor() != null);
    }
}
