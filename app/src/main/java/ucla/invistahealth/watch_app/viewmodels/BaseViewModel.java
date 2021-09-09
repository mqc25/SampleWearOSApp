package ucla.invistahealth.watch_app.viewmodels;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.utils.FileUtil;

public class BaseViewModel extends ViewModel {
    private static final String TAG = BaseViewModel.class.getSimpleName();

    private MutableLiveData<BackgroundService.BackgroundBinder> mBinder = new MutableLiveData<>();


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FileUtil.writeToLog(TAG, "onServiceConnected: connected to service");
            BackgroundService.BackgroundBinder binder = (BackgroundService.BackgroundBinder) service;
            mBinder.postValue(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder.postValue(null);
        }
    };

    public LiveData<BackgroundService.BackgroundBinder> getBinder() {
        return mBinder;
    }
    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }
}
