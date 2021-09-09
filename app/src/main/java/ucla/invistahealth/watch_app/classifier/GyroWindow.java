package ucla.invistahealth.watch_app.classifier;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

import ucla.invistahealth.watch_app.sensors.data.MotionSensorData;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Feature extraction for gyroscope
 * {@link ucla.invistahealth.watch_app.sensors.data.MotionSensorData} data using 32 features.
 * Uses Apache {@link FastMath} and {@link DescriptiveStatistics} libraries for statistical feature
 * generation. Currently not in use since gyroscope is not used for active/inactive classification.
 *
 * @author Arjun
 * @since 4/14/17
 */
public final class GyroWindow implements FeatureWindow {

    private static final String TAG = GyroWindow.class.getSimpleName();
    /**
     * {@link DescriptiveStatistics} for sensor x-axis
     */
    private final DescriptiveStatistics mStats_X = new DescriptiveStatistics();
    /**
     * {@link DescriptiveStatistics} for sensor y-axis
     */
    private final DescriptiveStatistics mStats_Y = new DescriptiveStatistics();
    /**
     * {@link DescriptiveStatistics} for sensor z-axis
     */
    private final DescriptiveStatistics mStats_Z = new DescriptiveStatistics();
    /**
     * {@link DescriptiveStatistics} for sensor norm sqrt[x^2 + y^2 + z^2]
     */
    private final DescriptiveStatistics mStats_A = new DescriptiveStatistics();

    /**
     * Construct a {@link FeatureWindow} with the given buffer of
     * {@link ucla.invistahealth.watch_app.sensors.data.MotionSensorData} objects.
     *
     * @param sensorData Sensor data buffer
     */
    public GyroWindow(final ArrayDeque<MotionSensorData> sensorData) {
        for (final MotionSensorData sensor : sensorData) {
            final double x = FastMath.abs(sensor.getX());
            final double y = FastMath.abs(sensor.getY());
            final double z = FastMath.abs(sensor.getZ());
            mStats_X.addValue(x);
            mStats_Y.addValue(y);
            mStats_Z.addValue(z);
            mStats_A.addValue(FastMath.hypot(x, FastMath.hypot(y, z)));
        }
    }

    @Override
    public int resetWindowSize(final int windowSize) {

        mStats_A.clear();
        mStats_X.clear();
        mStats_Y.clear();
        mStats_Z.clear();

        mStats_A.setWindowSize(windowSize);
        mStats_X.setWindowSize(windowSize);
        mStats_Y.setWindowSize(windowSize);
        mStats_Z.setWindowSize(windowSize);

        return FastMath.min(mStats_A.getWindowSize(), FastMath.min(mStats_X.getWindowSize(), mStats_Z.getWindowSize()));
    }

    @Override
    public int getWindowSizeMs() {
        return -1;
    }

    @Override
    public Instance toInstance() {

        final ArrayList<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute("max_A"));
        attributeList.add(new Attribute("max_X"));
        attributeList.add(new Attribute("max_Y"));
        attributeList.add(new Attribute("max_Z"));

        attributeList.add(new Attribute("min_A"));
        attributeList.add(new Attribute("min_X"));
        attributeList.add(new Attribute("min_Y"));
        attributeList.add(new Attribute("min_Z"));

        attributeList.add(new Attribute("mean_A"));
        attributeList.add(new Attribute("mean_X"));
        attributeList.add(new Attribute("mean_Y"));
        attributeList.add(new Attribute("mean_Z"));

        attributeList.add(new Attribute("geo_mean_A"));
        attributeList.add(new Attribute("geo_mean_X"));
        attributeList.add(new Attribute("geo_mean_Y"));
        attributeList.add(new Attribute("geo_mean_Z"));

        attributeList.add(new Attribute("std_dev_A"));
        attributeList.add(new Attribute("std_dev_X"));
        attributeList.add(new Attribute("std_dev_Y"));
        attributeList.add(new Attribute("std_dev_Z"));

        attributeList.add(new Attribute("skew_A"));
        attributeList.add(new Attribute("skew_X"));
        attributeList.add(new Attribute("skew_Y"));
        attributeList.add(new Attribute("skew_Z"));

        attributeList.add(new Attribute("median_A"));
        attributeList.add(new Attribute("median_X"));
        attributeList.add(new Attribute("median_Y"));
        attributeList.add(new Attribute("median_Z"));

        attributeList.add(new Attribute("quad_mean_A"));
        attributeList.add(new Attribute("quad_mean_X"));
        attributeList.add(new Attribute("quad_mean_Y"));
        attributeList.add(new Attribute("quad_mean_Z"));

        final Attribute classAttribute = new Attribute("class", Arrays.asList(new String[]{"stationary", "walking"}));
        attributeList.add(classAttribute);

        Instances dataInstance = new Instances(TAG, attributeList, 1);
        dataInstance.setClass(classAttribute);

        Instance newInstance = new DenseInstance(32 + 1);
        newInstance.setDataset(dataInstance);
        newInstance.setClassMissing();

        newInstance.setValue(0, mStats_A.getMax());
        newInstance.setValue(1, mStats_X.getMax());
        newInstance.setValue(2, mStats_Y.getMax());
        newInstance.setValue(3, mStats_Z.getMax());

        newInstance.setValue(4, mStats_A.getMin());
        newInstance.setValue(5, mStats_X.getMin());
        newInstance.setValue(6, mStats_Y.getMin());
        newInstance.setValue(7, mStats_Z.getMin());

        newInstance.setValue(8, mStats_A.getMean());
        newInstance.setValue(9, mStats_X.getMean());
        newInstance.setValue(10, mStats_Y.getMean());
        newInstance.setValue(11, mStats_Z.getMean());

        newInstance.setValue(12, mStats_A.getGeometricMean());
        newInstance.setValue(13, mStats_X.getGeometricMean());
        newInstance.setValue(14, mStats_Y.getGeometricMean());
        newInstance.setValue(15, mStats_Z.getGeometricMean());

        newInstance.setValue(16, mStats_A.getStandardDeviation());
        newInstance.setValue(17, mStats_X.getStandardDeviation());
        newInstance.setValue(18, mStats_Y.getStandardDeviation());
        newInstance.setValue(19, mStats_Z.getStandardDeviation());

        newInstance.setValue(20, mStats_A.getSkewness());
        newInstance.setValue(21, mStats_X.getSkewness());
        newInstance.setValue(22, mStats_Y.getSkewness());
        newInstance.setValue(23, mStats_Z.getSkewness());

        newInstance.setValue(24, mStats_A.getPercentile(50));
        newInstance.setValue(25, mStats_X.getPercentile(50));
        newInstance.setValue(26, mStats_Y.getPercentile(50));
        newInstance.setValue(27, mStats_Z.getPercentile(50));

        newInstance.setValue(28, mStats_A.getQuadraticMean());
        newInstance.setValue(29, mStats_X.getQuadraticMean());
        newInstance.setValue(30, mStats_Y.getQuadraticMean());
        newInstance.setValue(31, mStats_Z.getQuadraticMean());

        return newInstance;
    }
}