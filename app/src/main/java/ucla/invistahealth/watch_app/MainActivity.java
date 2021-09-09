package ucla.invistahealth.watch_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        IntPredicate granted = value -> value == PackageManager.PERMISSION_GRANTED;
//        if(Arrays.stream(grantResults).allMatch(granted)) {
//            serialNumber = Build.getSerial();
//            HelperFunction.logging(TAG, "SN: " + serialNumber);
//        } else{
//            HelperFunction.logging(TAG, "permission denied");
//            requestPermissions((String[]) permissionList.toArray(), PERMISSION_REQUEST_CODE);
//
//        }
//    }
}