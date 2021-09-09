package ucla.invistahealth.watch_app.classifier;

import weka.core.Instance;

/**
 * Feature window class accepted by {@link WatchClassifier#classify(FeatureWindow)} method for
 * classification. Provides clean interface for future use with non-accelerometer feature data such
 * as beacon or gyroscope data.
 *
 * @author Arjun
 * @since 4/14/17
 */
public interface FeatureWindow {

    /**
     * All {@link FeatureWindow} instances implement this interface to allow classification with
     * {@link WatchClassifier#classify(FeatureWindow)} method.
     *
     * @return Weka {@link Instance}
     */
    public abstract Instance toInstance();

    /**
     * Helper function for clear the window data and resetting the window size. Refer to concrete
     * implementations for details.
     *
     * @param windowSize requested window size
     * @return newly set window size
     */
    public abstract int resetWindowSize(final int windowSize);

    /**
     * Typically all classes implementing the {@link FeatureWindow} interface have a physical window
     * duration attribute. Helper function to quick get window length without knowing window-type
     *
     * @return Classifier specific window duration in milliseconds
     */
    public abstract int getWindowSizeMs();
}