package ucla.invistahealth.watch_app.sensors.data;

/**
 * Created by work on 3/27/17.
 */

public final class MotionSensorData extends SensorData {

    private static final int byteCount = (3 * Float.SIZE / 8);

    public MotionSensorData(final long nanoTimestamp, final float[] v) {
        super(byteCount, nanoTimestamp);
        bytes.putFloat(offset_v_0_, v[0]);
        bytes.putFloat(offset_v_1_, v[1]);
        bytes.putFloat(offset_v_2_, v[2]);
    }

    public float getX() {
        return bytes.getFloat(offset_v_0_);
    }

    public float getY() {
        return bytes.getFloat(offset_v_1_);
    }

    public float getZ() {
        return bytes.getFloat(offset_v_2_);
    }

    @Override
    public String toString() {
        return getTimestamp() + "," + getX() + "," + getY() + "," + getZ();
    }
}
