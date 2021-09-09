package ucla.invistahealth.watch_app.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.utils.threads.DefaultExecutorSupplier;

public class FileUtil {
    public static final String TAG = "FileUtil";
    public static final String DIRECTORY = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Gimbal";
    public static final String LOG_FILE = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Gimbal/Log";
    private static File logFile;
    private static DefaultExecutorSupplier defaultExecutorSupplier;


    public static void initLogging(DefaultExecutorSupplier logThread) {
        defaultExecutorSupplier = logThread;
        File file = new File(LOG_FILE);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }


        logFile = new File(LOG_FILE + "/Log_" + getCurrentDate() + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public static void check_delete_logFile(File file) {
        String currentDate = getCurrentDate();
        if (file.getName().contains("txt") && !file.getName().contains(currentDate)) {
            file.delete();
            writeToLog(TAG, "Delete file: " + file.getName());
        }
    }

    private static String getCurrentTimeInCorrectLocale() {
        String formatString = "MMM d, yyyy hh:mm:ss aaa";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString, Locale.US);
        simpleDateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        return simpleDateFormat.format(new Date());
    }

    private static String getCurrentDate() {
        String formatString = "MM_dd_yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString, Locale.US);
        simpleDateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        return simpleDateFormat.format(new Date());
    }

    public static void writeToLog(String tag, String message) {
        if (defaultExecutorSupplier == null) {
            Log.d(tag, message);
            return;
        }

        defaultExecutorSupplier.forLightWeightBackgroundTasks().execute(() -> {
            String output = String.format("[%s]: %s: %s", getCurrentTimeInCorrectLocale(), tag, message);
            if (message != null) Log.d(tag, message);
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.write(output);
                buf.newLine();
                buf.flush();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static String getFileName(String type) {
        String serialNumber = BackgroundService.getSerialNumber();
        Date currentTime = Calendar.getInstance().getTime();
        int year = 1900 + currentTime.getYear();
        int month = 1 + currentTime.getMonth();
        int day = currentTime.getDate();
        int hour = currentTime.getHours();
        String milis = String.valueOf(currentTime.getTime());
        String deviceType = "";
        switch (type) {
            default:
                deviceType = type;
                break;
        }
        String fileName = String.format("%s_%04d-%02d-%02d-%02d_%s.%s.json", serialNumber, year, month, day, hour, milis, deviceType);
        return fileName;
    }


}
