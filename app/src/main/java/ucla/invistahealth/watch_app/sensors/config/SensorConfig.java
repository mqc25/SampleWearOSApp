package ucla.invistahealth.watch_app.sensors.config;

import java.io.File;

public interface SensorConfig {

    File setOutputFileDirectory();

    boolean doesSensorExist();

    int getBufferCapacity();

    String getSensorName();
}

