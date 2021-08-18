package ucla.invistahealth.testautoupdate.fragments;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import ucla.invistahealth.testautoupdate.utils.HelperFunction;

public class HomeFragment extends Fragment {
    private static String TAG = HomeFragment.class.getSimpleName();
    private ArrayList<String> permissionList;
    private static final int PERMISSION_REQUEST_CODE = 111;
    private Handler handler;
    private Activity activity;
    public static String serialNumber;
    private NavController navController;


    ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
    ActivityResultLauncher<String[]> resultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
        HelperFunction.logging("PERMISSIONS", "Launcher result: " + isGranted.toString());
        if (isGranted.containsValue(false)) {
            HelperFunction.logging("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
            resultLauncher.launch(PERMISSIONS);
        }
    });



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
        handler = new Handler();

        permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.WAKE_LOCK);
        permissionList.add(Manifest.permission.BLUETOOTH_ADMIN);
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissionList.add(Manifest.permission.CHANGE_NETWORK_STATE);
        permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissionList.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        mPermissionResult.

        requestPermissions((String[]) permissionList.toArray(), PERMISSION_REQUEST_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
