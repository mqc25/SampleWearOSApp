package ucla.invistahealth.watch_app.utils.io;

import static androidx.viewbinding.BuildConfig.DEBUG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ucla.invistahealth.watch_app.utils.FileUtil;

public final class Files extends BroadcastReceiver {

    /* Directory Structure */
    public static final String FS_rootDirectory = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String rootDirectory = FS_rootDirectory + "/Gimbal";
    public static final File publicDirectory = new File(rootDirectory);
    public static final File stepsDirectory = new File(rootDirectory + "/Steps");
    public static final File beaconDirectory = new File(rootDirectory + "/Beacon");
    public static final File surveyDirectory = new File(rootDirectory + "/Survey");
    public static final File locationDirectory = new File(rootDirectory + "/Locations");
    public static final File gyroDirectory = new File(rootDirectory + "/Gyroscope");
    public static final File heartRateDirectory = new File(rootDirectory + "/HeartRate");

    private static final String TAG = Files.class.getSimpleName();

    static {
        File file = new File(rootDirectory);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
    }

    /**
     * Returns a {@link List <File>} object by recursively searching through
     * the app-private directory. If the directory is empty, then an empty
     * {@link ArrayList} is returned.
     *
     * @return list of files within the app-private directory
     * @see List<File>
     */
    public static List<File> getFilesInPrivateDirectory(Context context) {
        return getFilesInDirectory(context.getFilesDir());
    }

    /**
     * Returns a {@link List<File>} object by recursively searching through
     * the app-defined public directory. If the directory is empty, then an empty
     * {@link ArrayList} is returned.
     *
     * @return list of files within the app-defined public directory
     * @see List<File>
     */
    public static List<File> getFilesInPublicDirectory() {
        return getFilesInDirectory(publicDirectory);
    }

    /**
     * Returns a {@link List<File>} object by recursively searching through the app-defined public
     * directory, except skipping files from the current hour. Uses {@link #getFilesInDirectory(File, String)}
     * where exclude string if the current hour, formatted using {@link FileWriter#dateFormat(Date)}
     *
     * @return {@link ArrayList} list of files within the app-defined public directory, skipping current hour
     * @see List<File>
     */
    public static List<File> getFilesInPublicDirectorySkipCurrent() {
        final String filenameCurrentHour = FileWriter.dateFormat(new Date());
        return getFilesInDirectory(publicDirectory, filenameCurrentHour);
    }

    /**
     * Returns a {@link List<File>} object by recursively searching through
     * the provided {@link File} object. If the provided {@link File} object
     * is not a directory or if the directory is empty, then an empty
     * {@link ArrayList} is returned.
     *
     * @param parentDir {@link File} object which will be searched for files
     * @return {@link ArrayList} list of files within the provided directory
     * @see List<File>
     */
    private static List<File> getFilesInDirectory(final File parentDir) {
        final List<File> outputFiles = new ArrayList<>();

        final File[] fileList = parentDir.listFiles();
        if (fileList == null || fileList.length <= 0)
            return outputFiles;

        for (final File thisFile : fileList) {
            if (thisFile.isFile()) {
                outputFiles.add(thisFile);
            } else if (thisFile.isDirectory()) {
                outputFiles.addAll(getFilesInDirectory(thisFile));
            }
        }
        return outputFiles;
    }

    /**
     * Returns a {@link List<File>} object by recursively searching through
     * the provided {@link File} object. Same as as {@link #getFilesInDirectory(File)}, except it
     * excludes files with the given {@link String} extension. If the provided {@link File} object
     * is not a directory or if the directory is empty, then an empty {@link ArrayList} is returned.
     *
     * @param parentDir {@link File} object which will be searched for files
     * @param exclude   {@link String} Files with extension to exclude
     * @return list of files within the provided directory
     * @see List<File>
     */
    private static List<File> getFilesInDirectory(final File parentDir, final String exclude) {
        final List<File> outputFiles = new ArrayList<>();

        final File[] fileList = parentDir.listFiles();
        if (fileList == null || fileList.length <= 0)
            return outputFiles;

        for (final File thisFile : fileList) {
            if (thisFile.isFile() && !thisFile.getName().contains(exclude)) {
                outputFiles.add(thisFile);
            } else if (thisFile.isDirectory()) {
                outputFiles.addAll(getFilesInDirectory(thisFile, exclude));
            }
        }
        return outputFiles;
    }

    /**
     * Deletes the supplied file if it's not the current hour file. Return true if deleted.
     *
     * @param fileToDelete {@link File} the filbe to delete
     * @return {@link Boolean} true if the file is deleted successfully
     */
    public static boolean deleteFileSkipCurrent(final File fileToDelete) {
        final String filenameCurrentHour = FileWriter.dateFormat(new Date());
        return !fileToDelete.getName().contains(filenameCurrentHour) && fileToDelete.delete();
    }

    /**
     * If registered, receives an {@link Intent#ACTION_DEVICE_STORAGE_LOW} event. Currently does not
     * perform any action.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        FileUtil.writeToLog(TAG, action);


        switch (action) {
            case (Intent.ACTION_DEVICE_STORAGE_LOW):
                FileUtil.writeToLog(TAG, "Running low on space, attempting to compress files");
                //TODO:: Do something if device storage is low.
                break;
        }
    }

}
