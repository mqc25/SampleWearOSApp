package ucla.invistahealth.watch_app.viewmodels;

import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends BaseViewModel {
    public MutableLiveData<String> textDisplay = new MutableLiveData<>();

    public MutableLiveData<String> heartRate = new MutableLiveData<>();
    public MutableLiveData<String> heartRateAcc = new MutableLiveData<>();
    public MutableLiveData<String> app_version = new MutableLiveData<>();
    public MutableLiveData<String> serialNumber = new MutableLiveData<>();
    public MutableLiveData<String> time = new MutableLiveData<>();
    public MutableLiveData<String> am_pm = new MutableLiveData<>();
    public MutableLiveData<String> location = new MutableLiveData<>();
    public MutableLiveData<String> steps = new MutableLiveData<>();
    public MutableLiveData<String> battery_lvl = new MutableLiveData<>();
    public MutableLiveData<String> status = new MutableLiveData<>();


    public HomeViewModel() {
        textDisplay.postValue("Default");
        heartRate.postValue("--");
        heartRateAcc.postValue("--");
        app_version.postValue("0.00.00");
        serialNumber.postValue("TKQ000000000");
        time.postValue("12:00");
        am_pm.postValue("AM");
        location.postValue("--");
        steps.postValue("--");
        battery_lvl.postValue("not charging");
        status.postValue("");
    }

    public MutableLiveData<String> getTextDisplay(){
        return textDisplay;
    }

    public void setTextDisplay(String txt){
        textDisplay.postValue(txt);
    }

    public MutableLiveData<String> getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate.postValue(heartRate);
    }

    public MutableLiveData<String> getHeartRateAcc() {
        return heartRateAcc;
    }

    public void setHeartRateAcc(String heartRateAcc) {
        this.heartRateAcc.postValue(heartRateAcc);
    }

    public MutableLiveData<String> getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version.postValue(app_version);
    }

    public MutableLiveData<String> getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.postValue(serialNumber);
    }

    public MutableLiveData<String> getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time.postValue(time);
    }

    public MutableLiveData<String> getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm.postValue(am_pm);
    }

    public MutableLiveData<String> getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location.postValue(location);
    }

    public MutableLiveData<String> getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps.postValue(steps);
    }

    public MutableLiveData<String> getBattery_lvl() {
        return battery_lvl;
    }

    public void setBattery_lvl(String battery_lvl) {
        this.battery_lvl.postValue(battery_lvl);
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status.postValue(status);
    }
}
