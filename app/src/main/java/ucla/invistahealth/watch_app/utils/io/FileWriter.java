package ucla.invistahealth.watch_app.utils.io;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.sensors.Sensor;
import ucla.invistahealth.watch_app.sensors.data.SensorData;
import ucla.invistahealth.watch_app.utils.FileUtil;

public abstract class FileWriter {

    private static final String TAG = FileWriter.class.getSimpleName();
    protected String mNameExtension;

    public FileWriter(final Sensor sensor, final String extension) {
        mNameExtension = sensor.getName() + ".raw." + extension;
    }

    /**
     * Constructor for {@link FileWriter}.
     *
     * @param sensor
     */
    public FileWriter(final Sensor sensor) {
        mNameExtension = sensor.getName() + ".raw";
    }

    /**
     * Filename formatting utility, formats filenames based on the naming convention declared and
     * used by SARP servers (legacy as well as current).
     *
     * @param suffix    filename suffix (e.g. beacon, steps)
     * @param extension file extension (e.g. csv, raw)
     * @return {@link String} formatted filename
     */
    public static String formatFilename(final String suffix, final String extension) {
        return (BackgroundService.getSerialNumber() + "_" + dateFormat(new Date()) + "." + suffix + "." + extension);
    }

    /**
     * String formatting utility, formats String objects based on the "yyyy-MM-dd-HH" naming
     * convention. This format is used by SARP servers (legacy as well as current).
     *
     * @param date {@link Date} object specifying the requested date
     * @return {@link String} formatted String
     */
    public static String dateFormat(final Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(BackgroundService.locale));
        return simpleDateFormat.format(date);
    }

    public final synchronized void write(final ArrayDeque<SensorData> data) {
        //TODO ::
        // 1. ensure that the file-outputstream is current, set it up before calling onWrite
        //
        try {
            onWrite(data);
        } catch (Exception e) {
            FileUtil.writeToLog(TAG, e.getMessage());
        }
    }

    protected abstract void onWrite(final ArrayDeque<SensorData> data);
}