package ucla.invistahealth.watch_app.sensors.data;

import android.os.SystemClock;

import java.nio.ByteBuffer;

public abstract class SensorData {

    protected static final int offset_v_0_ = (1 * (Long.SIZE / 8)) + (0 * Float.SIZE / 8);
    protected static final int offset_v_1_ = (1 * (Long.SIZE / 8)) + (1 * Float.SIZE / 8);
    protected static final int offset_v_2_ = (1 * (Long.SIZE / 8)) + (2 * Float.SIZE / 8);
    protected static final int offset_mi_0 = (1 * (Long.SIZE / 8)) + (0 * Byte.SIZE / 8);
    protected static final int offset_mi_1 = (1 * (Long.SIZE / 8)) + (1 * Byte.SIZE / 8);
    protected static final int offset_rssi = (1 * (Long.SIZE / 8)) + (2 * Byte.SIZE / 8);
    protected static final int offset_txPr = (1 * (Long.SIZE / 8)) + (3 * Byte.SIZE / 8);
    protected static final int offset_step = (1 * (Long.SIZE / 8)) + (0 * Integer.SIZE / 8);
    protected static final int offset_walk_duration = (1 * (Long.SIZE / 8)) + (0 * Long.SIZE / 8);
    protected static final int offset_version_id = (1 * (Long.SIZE / 8)) + (0 * Short.SIZE / 8);
    protected static final int offset_question_id = (1 * (Long.SIZE / 8)) + (1 * Short.SIZE / 8);
    protected static final int offset_response_id = (1 * (Long.SIZE / 8)) + (2 * Short.SIZE / 8);
    protected static final int offset_heart_rate = (1 * (Long.SIZE / 8)) + (0 * Short.SIZE / 8);
    protected static final int offset_heart_rate_acc = (1 * (Long.SIZE / 8)) + (1 * Short.SIZE / 8);
    private static final int offset_time = (0 * (Long.SIZE / 8)) + (0 * Float.SIZE / 8);
    public ByteBuffer bytes;


    public SensorData(final int byteCount, final long elapsedNanos) {
        bytes = ByteBuffer.allocate((Long.SIZE / 8) + byteCount);
        bytes.putLong(offset_time, getEpochTimeFromElapsedTimeNanos(elapsedNanos));
    }

    public SensorData(final int byteCount) {
        bytes = ByteBuffer.allocate((Long.SIZE / 8) + byteCount);
        final long elapsedNanos = SystemClock.elapsedRealtimeNanos();
        bytes.putLong(offset_time, getEpochTimeFromElapsedTimeNanos(elapsedNanos));
    }

    private static long getEpochTimeFromElapsedTimeNanos(final long elapsedTimeNanos) {
        final long timeAtBoot = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        return elapsedTimeNanos / 1000000L + timeAtBoot;
    }

    public long getTimestamp() {
        return bytes.getLong(offset_time);
    }
}
