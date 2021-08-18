package ucla.invistahealth.testautoupdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.IntPredicate;

import ucla.invistahealth.testautoupdate.utils.HelperFunction;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        IntPredicate granted = value -> value == PackageManager.PERMISSION_GRANTED;
        if(Arrays.stream(grantResults).allMatch(granted)) {
            serialNumber = Build.getSerial();
            HelperFunction.logging(TAG, "SN: " + serialNumber);
        } else{
            HelperFunction.logging(TAG, "permission denied");
            requestPermissions((String[]) permissionList.toArray(), PERMISSION_REQUEST_CODE);

        }
    }
}