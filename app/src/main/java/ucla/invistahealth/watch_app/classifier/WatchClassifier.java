package ucla.invistahealth.watch_app.classifier;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import ucla.invistahealth.watch_app.utils.FileUtil;
import weka.classifiers.Classifier;
import weka.core.Instance;
/**
 * Helper class wrapper around a Weka {@link Classifier} object. Implements helper functions for
 * de-serializing and decompressing Weka model files.
 *
 * @author Arjun
 * @since 3/22/17
 */
public final class WatchClassifier {
    private static final String TAG = WatchClassifier.class.getSimpleName();
    /**
     * The Weka {@link Classifier} object
     */
    private Classifier mClassifier;
    private Context mContext;

    /**
     * Construct a Weka {@link Classifier} by passing a resource ID to a model file and then
     * deserialize the model file to build a classifier.
     *
     * @param resID the resource ID for the model file
     */
    public WatchClassifier(final int resID, Context context) throws Exception {
        mClassifier = buildClassifierFromFile(resID);
        mContext = context;
    }

    /**
     * Construct a Weka {@link Classifier} by passing an already built classifier by reference.
     *
     * @param classifier an instance of a weka {@link Classifier} object
     * @throws Exception if supplied classifier is a null object
     */
    public WatchClassifier(final Classifier classifier) {
        if (classifier != null) {
            mClassifier = classifier;
        }
    }

    /**
     * Reads compressed and serialized model files into a weka {@link Classifier} using the supplied
     * file resource ID.
     *
     * @param resID {@link android.content.res.Resources} Resource identifier
     * @return {@link Classifier} serialized classifier
     * @throws Exception if unable to build classifier
     */
    private Classifier buildClassifierFromFile(final int resID) throws Exception {
        final InputStream dataStream = mContext.getResources().openRawResource(resID);
        final BufferedInputStream bfgzis = new BufferedInputStream(new GZIPInputStream(dataStream));
        Object classifier = weka.core.SerializationHelper.read(bfgzis);
        dataStream.close();
        return (Classifier) classifier;
    }

    /**
     * Helper function implementation of {@link WatchClassifier#classify(Instance)}.
     *
     * @param featureWindow {@link FeatureWindow} instance to classify
     * @return true if classification result >= 0.5, false otherwise
     * @see WatchClassifier#classify(Instance)
     */
    public final boolean classify(final FeatureWindow featureWindow) {
        return classify(featureWindow.toInstance());
    }

    /**
     * Similar to {@link WatchClassifier#classify(FeatureWindow)}, but uses weka {@link Instance}
     * object as parameters instead.
     *
     * @param featureInstance {@link Instance} weka instance to classify
     * @return true if classification result >= 0.5 or on exception, false otherwise
     */
    public final boolean classify(final Instance featureInstance) {
        try {
            return (mClassifier.classifyInstance(featureInstance) >= 0.5);
        } catch (Exception e) {
            FileUtil.writeToLog(TAG, e.getMessage());
            return true;
        }
    }
}
