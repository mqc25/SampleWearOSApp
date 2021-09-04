package ucla.invistahealth.testautoupdate.fragments;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
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
import java.util.concurrent.atomic.AtomicBoolean;

import ucla.invistahealth.testautoupdate.R;
import ucla.invistahealth.testautoupdate.databinding.HomeFragmentBinding;
import ucla.invistahealth.testautoupdate.utils.HelperFunction;
import ucla.invistahealth.testautoupdate.viewmodels.HomeViewModel;

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


    ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
    ActivityResultLauncher<String[]> resultLauncher = registerForActivityResult(requestMultiplePermissionsContract, isGranted -> {
        HelperFunction.logging("PERMISSIONS", "Launcher result: " + isGranted.toString());
        if (isGranted.containsValue(false)) {
            HelperFunction.logging("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
            requestPerm();
        }
    });

    public void requestPerm(){
        String[] permissions = permissionList.toArray(new String[0]);
        resultLauncher.launch(permissions);
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
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
            HelperFunction.logging(TAG, "Change text");
        }, 5000);


    }
}
