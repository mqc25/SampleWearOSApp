package ucla.invistahealth.watch_app.sensors.config;

import android.content.Context;
import android.hardware.Sensor;

public interface HardwareSensorConfig extends SensorConfig {

    /**
     * @return Instance of Android {@link Sensor}
     */
    Sensor getUnderlyingSensor();

    /**
     * Sets the sampling delay of the {@link Sensor} in microseconds.
     *
     * @return microsecond interval between successive timestamps
     */
    int getSamplingDelayUs();

    /**
     * Sets the reporting delay the {@link Sensor} in microseconds. Typically calculated by
     * multiplying {@link Sensor#getMaxDelay()} with {@link Sensor#getFifoMaxEventCount()}
     *
     * @return microsecond interval before all sensor values are dumped to {@link HardwareSensor#flushSensor()}
     */
    int getReportingDelayUs();
}
