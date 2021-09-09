package ucla.invistahealth.watch_app.sensors.data;

public final class StepData extends SensorData {

    private static final int byteCount = Integer.SIZE / 8;

    public StepData(final long nanosTimestamp, final float stepCount) {
        super(byteCount, nanosTimestamp);
        bytes.putInt(offset_step, Math.round(stepCount));
    }

    public int getStepCount() {
        return bytes.getInt(offset_step);
    }

    @Override
    public String toString() {
        return getTimestamp() + "," + getStepCount();
    }

}
