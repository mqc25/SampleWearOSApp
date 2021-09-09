package ucla.invistahealth.watch_app.sensors;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.sensors.config.SensorConfig;
import ucla.invistahealth.watch_app.sensors.data.SensorData;
import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.utils.io.FileWriter;

public abstract class Sensor<T extends SensorData, S extends SensorConfig> {

    private static final String TAG = Sensor.class.getSimpleName();
    protected final ArrayDeque<T> timeSortedFIFO;
    protected PowerManager.WakeLock mPowerWakeLock;
    protected boolean mSensorValid;
    protected S mSensorConfig;
    private File mOutputParentDirectory;
    private boolean mStarted = false;
    private T mWriteToEmptyFileData;
    protected Context mContext;

    public Sensor(final S sensorConfig, Context context) {
        mSensorConfig = sensorConfig;
        mSensorValid = mSensorConfig.doesSensorExist();
        mWriteToEmptyFileData = setWriteToEmptyFileData();
        mOutputParentDirectory = mSensorConfig.setOutputFileDirectory();
        timeSortedFIFO = new ArrayDeque<>(mSensorConfig.getBufferCapacity());
        mContext = context;
    }

    protected boolean isInteractive() {
        final PowerManager pwrMgr = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        return pwrMgr.isInteractive();
    }

    /**
     * Clears the current result buffer, sets the output file name, and runs onStartScan()
     */
    public final void startScan() {
        if (mSensorValid && !mStarted) {
            // flush any pending sensor values to disk
            flushToDisk();

            // start the scan and set the scan state
            mStarted = onStartScan();

            // if started, send the successful start broadcast
            if (mStarted) {
                final Intent successfulStartBroadcast = onSuccessfulStartBroadcast();
                if (successfulStartBroadcast != null) {
                    mContext.sendBroadcast(successfulStartBroadcast);
                }
            }
            //mStarted = true; // TODO :: check if commenting this out adds issues
        }
    }

    /**
     * When concrete sensor stopScan is called, ensure that the sensor performs it's stop-scan function,
     * then flush the current @timeSortedFIFO to disk. This will be done in the function below :
     */
    public final void stopScan() {
        if (mSensorValid && mStarted) {
            // set scan state based on actual state value
            mStarted = onStopScan();

            // save everything to disk
            flushToDisk();

            // if started, send the successful start broadcast
            if (!mStarted) {
                final Intent successfulStopBroadcast = onSuccessfulStopBroadcast();
                if (successfulStopBroadcast != null) {
                    mContext.sendBroadcast(successfulStopBroadcast);
                }
            }
            //mStarted = false; TODO :: check if commenting this out adds issues
        }
    }

    /**
     * forces all sensors to implement broadcast parameters for successful start and stop states
     **/
    protected Intent onSuccessfulStartBroadcast() {
        return null;
    }

    protected Intent onSuccessfulStopBroadcast() {
        return null;
    }

    protected void runAnalysis() {
    }

    protected abstract boolean onStopScan();

    protected abstract boolean onStartScan();

    protected T setWriteToEmptyFileData() {
        return null;
    }

    private BufferedOutputStream getCurrentFileChannel() throws IOException {

        final String sensorName = mSensorConfig.getSensorName();
        final String filename = FileWriter.formatFilename(sensorName, "raw");

        if (!mOutputParentDirectory.exists() || !mOutputParentDirectory.isDirectory()) {
            mOutputParentDirectory.mkdir();
        }
        final File outputFile = new File(mOutputParentDirectory, filename);

        final boolean newFileCreated = outputFile.createNewFile();

        if (mWriteToEmptyFileData != null && newFileCreated) {
            timeSortedFIFO.add(mWriteToEmptyFileData);
        }

        final int numEvents = timeSortedFIFO.size();
        final int bufferSize = numEvents * timeSortedFIFO.peek().bytes.limit();
        FileUtil.writeToLog(sensorName, "Number of bytes to write: " + bufferSize);

        return (new BufferedOutputStream(new FileOutputStream(outputFile, true), bufferSize));
    }

    protected boolean setWakelock(boolean state) {
        if (mPowerWakeLock == null) {
            final PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mPowerWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, mSensorConfig.getSensorName());
        }

        if (state && !mPowerWakeLock.isHeld()) mPowerWakeLock.acquire();
        if (!state && mPowerWakeLock.isHeld()) mPowerWakeLock.release();
        return false;
    }

    protected final boolean flushToDisk() {
        return timeSortedFIFO.isEmpty() || performFlushToFile();
    }

    private boolean performFlushToFile() {
        try {
            writeDataToFile(getCurrentFileChannel(), timeSortedFIFO);
            return timeSortedFIFO.isEmpty();
        } catch (Exception e) {
            FileUtil.writeToLog(TAG, e.getMessage());
            mOutputParentDirectory.mkdirs();
            return false;
        } finally {
            timeSortedFIFO.clear();
        }
    }


    private void writeDataToFile(final OutputStream outputFileChannel, final ArrayDeque<T> data) throws IOException {
        for (final T single : data) {
            outputFileChannel.write(single.bytes.array());
        }
        outputFileChannel.close();
    }

    public String getName() {
        return mSensorConfig.getSensorName();
    }

    protected void requestAction(String action){
        final Intent bIntent = new Intent(mContext, BackgroundService.class);
        bIntent.setAction(action);
        mContext.startService(bIntent);
    }
}
