package ucla.invistahealth.watch_app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Objects;

import ucla.invistahealth.watch_app.BackgroundService;
import ucla.invistahealth.watch_app.R;
import ucla.invistahealth.watch_app.databinding.HomeFragmentBinding;
import ucla.invistahealth.watch_app.utils.FileUtil;
import ucla.invistahealth.watch_app.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {
    private static String TAG = HomeFragment.class.getSimpleName();
    private ArrayList<String> permissionList;
    private static final int PERMISSION_REQUEST_CODE = 111;
    private Handler handler;
    private Activity activity;
    public static String serialNumber;
    private NavController navController;
    private HomeViewModel viewModel;

    private HomeFragmentBinding binding;
    private BackgroundService mService;


    ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
    ActivityResultLauncher<String[]> resultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
        FileUtil.writeToLog("PERMISSIONS", "Launcher result: " + isGranted.toString());
        if (isGranted.containsValue(false)) {
            FileUtil.writeToLog("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
            requestPerm();
        } else {
            FileUtil.writeToLog(TAG, "launch service");
            viewModel.getBinder().observe(this.requireActivity(), new Observer<>() {
                @Override
                public void onChanged(BackgroundService.BackgroundBinder backgroundBinder) {
                    if (backgroundBinder != null) {
                        FileUtil.writeToLog(TAG, "onChanged: connected to service");
                        mService = backgroundBinder.getService();
                        mService.setHomeViewModel(viewModel);
                    } else {
                        FileUtil.writeToLog(TAG, "onChanged: unbound from service");
                        mService = null;

                    }
                }
            });
            startBackgroundService();
        }
    });

    public void requestPerm(){
        String[] permissions = permissionList.toArray(new String[0]);
        resultLauncher.launch(permissions);
    }

    private void startBackgroundService() {
        Intent serviceIntent = new Intent(this.getContext(), BackgroundService.class);
        this.requireActivity().startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this.getContext(), BackgroundService.class);
        this.requireActivity().bindService(serviceIntent, viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
        handler = new Handler();

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();


        permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.WAKE_LOCK);
        permissionList.add(Manifest.permission.BLUETOOTH_ADMIN);
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissionList.add(Manifest.permission.CHANGE_NETWORK_STATE);
        permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissionList.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        permissionList.add(Manifest.permission.BODY_SENSORS);


        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            permissionList.add(Manifest.permission.FOREGROUND_SERVICE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.home_fragment,
                container,
                false
        );
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this.getViewLifecycleOwner());

        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        requestPerm();

        handler.postDelayed(() -> {
            viewModel.setTextDisplay("Change text");
            FileUtil.writeToLog(TAG, "Change text");
        }, 5000);


    }
}
