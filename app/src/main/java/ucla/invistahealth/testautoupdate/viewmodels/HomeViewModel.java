package ucla.invistahealth.testautoupdate.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends AndroidViewModel {
    public MutableLiveData<String> textDisplay = new MutableLiveData<>("Default");

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }


}
