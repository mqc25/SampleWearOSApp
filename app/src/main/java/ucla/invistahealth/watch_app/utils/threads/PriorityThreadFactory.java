package ucla.invistahealth.watch_app.utils.threads;

import android.os.Process;

import java.util.concurrent.ThreadFactory;

import ucla.invistahealth.watch_app.utils.FileUtil;

public class PriorityThreadFactory implements ThreadFactory {
    public static final String TAG = PriorityThreadFactory.class.getName();

    private final int mThreadPriority;

    public PriorityThreadFactory(int threadPriority) {
        mThreadPriority = threadPriority;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                } catch (Throwable t) {
                    FileUtil.writeToLog(TAG, "Fail to set thread priority");
                }
                runnable.run();
            }
        };
        return new Thread(wrapperRunnable);
    }

}
