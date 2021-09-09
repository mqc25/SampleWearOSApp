package ucla.invistahealth.watch_app.classifier;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

import ucla.invistahealth.watch_app.sensors.data.MotionSensorData;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Feature extraction for accelerometer
 * {@link ucla.invistahealth.watch_app.sensors.data.MotionSensorData} data using 21 features.
 * Uses Apache {@link FastMath} and {@link DescriptiveStatistics} libraries for statistical feature
 * generation.
 *
 * @author Arjun
 * @since 4/14/17
 */
public final class AccelWindow implements FeatureWindow {

    /**
     * Feature window duration size in milliseconds
     */
    private static final int mWindowDurationMs = 2850;

    /**
     * {@link DescriptiveStatistics} for sensor x-axis
     */
    private static final DescriptiveStatistics mStats_X = new DescriptiveStatistics();

    /**
     * {@link DescriptiveStatistics} for sensor z-axis
     */
    private static final DescriptiveStatistics mStats_Z = new DescriptiveStatistics();

    /**
     * {@link DescriptiveStatistics} for sensor norm sqrt[x^2 + y^2 + z^2]
     */
    private static final DescriptiveStatistics mStats_A = new DescriptiveStatistics();

    private static final String TAG = AccelWindow.class.getSimpleName();

    private static Instance mInstance = initInstance();

    public AccelWindow() {

    }

    public AccelWindow(final int windowSize) {
        resetWindowSize(windowSize);
    }

    /**
     * Adds the provided sensor value to the feature window, but performs {@link FastMath#abs(double)}
     * on the axes first.
     *
     * @param sensorData Value to be added to the window
     */
    public static void add(final MotionSensorData sensorData) {
        final float x = FastMath.abs(sensorData.getX());
        final float y = sensorData.getY();
        final float z = FastMath.abs(sensorData.getZ());
        final double a = FastMath.tanh(FastMath.hypot(x, FastMath.hypot(y, z)) - 9.789);
        mStats_X.addValue(x);
        mStats_Z.addValue(z);
        mStats_A.addValue(a);
    }

    /**
     * Initializes a vector for features: max, min, mean, geo_mean, quad_mean, std_dev, median for
     * A, X, Z axes. Class labels are {active, inactive}
     *
     * @return Weka {@link Instance} of 21 feature vector, currently empty.
     */
    private static Instance initInstance() {

        final ArrayList<Attribute> attributeList = new ArrayList<>(22);

        attributeList.add(new Attribute("max_A"));
        attributeList.add(new Attribute("max_X"));
        attributeList.add(new Attribute("max_Z"));

        attributeList.add(new Attribute("min_A"));
        attributeList.add(new Attribute("min_X"));
        attributeList.add(new Attribute("min_Z"));

        attributeList.add(new Attribute("mean_A"));
        attributeList.add(new Attribute("mean_X"));
        attributeList.add(new Attribute("mean_Z"));

        attributeList.add(new Attribute("geo_mean_A"));
        attributeList.add(new Attribute("geo_mean_X"));
        attributeList.add(new Attribute("geo_mean_Z"));

        attributeList.add(new Attribute("std_dev_A"));
        attributeList.add(new Attribute("std_dev_X"));
        attributeList.add(new Attribute("std_dev_Z"));

        attributeList.add(new Attribute("median_A"));
        attributeList.add(new Attribute("median_X"));
        attributeList.add(new Attribute("median_Z"));

        attributeList.add(new Attribute("quad_mean_A"));
        attributeList.add(new Attribute("quad_mean_X"));
        attributeList.add(new Attribute("quad_mean_Z"));

        final Attribute classAttribute = new Attribute("class", Arrays.asList(new String[]{"inactive", "active"}));
        attributeList.add(classAttribute);

        Instances dataInstance = new Instances(TAG, attributeList, 1);
        dataInstance.setClass(classAttribute);

        Instance newInstance = new DenseInstance(21 + 1);
        newInstance.setDataset(dataInstance);
        newInstance.setClassMissing();

        return newInstance;
    }

    /**
     * Computes statistical features listed in {@link AccelWindow#toInstance()} for the current
     * values in the internal arrays.
     *
     * @return Weka {@link Instance} of 21 feature vector, recently recomputed
     */
    private static Instance compute() {

        mInstance.setValue(0, mStats_A.getMax());
        mInstance.setValue(1, mStats_X.getMax());
        mInstance.setValue(2, mStats_Z.getMax());

        mInstance.setValue(3, mStats_A.getMin());
        mInstance.setValue(4, mStats_X.getMin());
        mInstance.setValue(5, mStats_Z.getMin());

        mInstance.setValue(6, mStats_A.getMean());
        mInstance.setValue(7, mStats_X.getMean());
        mInstance.setValue(8, mStats_Z.getMean());

        mInstance.setValue(9, mStats_A.getGeometricMean());
        mInstance.setValue(10, mStats_X.getGeometricMean());
        mInstance.setValue(11, mStats_Z.getGeometricMean());

        mInstance.setValue(12, mStats_A.getStandardDeviation());
        mInstance.setValue(13, mStats_X.getStandardDeviation());
        mInstance.setValue(14, mStats_Z.getStandardDeviation());

        mInstance.setValue(15, mStats_A.getPercentile(50));
        mInstance.setValue(16, mStats_X.getPercentile(50));
        mInstance.setValue(17, mStats_Z.getPercentile(50));

        mInstance.setValue(18, mStats_A.getQuadraticMean());
        mInstance.setValue(19, mStats_X.getQuadraticMean());
        mInstance.setValue(20, mStats_Z.getQuadraticMean());

        return mInstance;
    }

    /**
     * Clears the three internal {@link DescriptiveStatistics} arrays
     */
    public void clear() {
        mStats_A.clear();
        mStats_X.clear();
        mStats_Z.clear();
    }

    /**
     * {@link AccelWindow#clear()} then set the internal window sizes to the provided length
     *
     * @param windowSize the length of internal arrays
     * @return actual value after setting the windowSize
     */
    @Override
    public int resetWindowSize(final int windowSize) {

        clear();
        mStats_A.setWindowSize(windowSize);
        mStats_X.setWindowSize(windowSize);
        mStats_Z.setWindowSize(windowSize);

        return FastMath.min(mStats_A.getWindowSize(), FastMath.min(mStats_X.getWindowSize(), mStats_Z.getWindowSize()));
    }

    /**
     * @return The feature window duration in milliseconds
     */
    @Override
    public int getWindowSizeMs() {
        return mWindowDurationMs;
    }

    /**
     * @return Total number of values in the internal arrays
     */
    public int getCount() {
        return mStats_A.getValues().length;
    }

    /**
     * Wrapper for {@link AccelWindow#toInstance()}
     *
     * @return {@link AccelWindow#toInstance()}
     */
    @Override
    public Instance toInstance() {
        return compute();
    }
}