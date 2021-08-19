package ucla.invistahealth.testautoupdate.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends AndroidViewModel {
    public MutableLiveData<String> textDisplay = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        textDisplay.postValue("Default");
    }

    public MutableLiveData<String> getTextDisplay(){
        return textDisplay;
    }

    public void setTextDisplay(String txt){
        textDisplay.postValue(txt);
    }


}
