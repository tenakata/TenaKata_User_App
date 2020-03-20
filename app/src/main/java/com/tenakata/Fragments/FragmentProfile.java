package com.tenakata.Fragments;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tenakata.Activity.ActivityAddDailySales;
import com.tenakata.Activity.ActivityDashboard;
import com.tenakata.Base.BaseFragment;
import com.tenakata.Models.LoginModel;
import com.tenakata.Models.ProfileModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.Utilities.IntentHelper;
import com.tenakata.databinding.FragmentProfileBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

public class FragmentProfile extends BaseFragment {
    private Context context;
    private FragmentProfileBinding binding;
    String name,mobile,email;
    private Uri image_uris;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();


        if (HRPrefManager.getInstance(context).getUserDetail().getResult()!=null){

            binding.editButtonn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editFunctionality(name,mobile,email);
                }
            });
            binding.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    apiHit();
                }
            });

            binding.changeprofiletv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });




            binding.viewUserName.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));
            binding.tvUserid.setText("ID : "+HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getId()));
            binding.tvMobile.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getPhone()));
            binding.tvMobile.setText(HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getEmail()));
             name=binding.viewUserName.getText().toString();
            mobile=binding.tvMobile.getText().toString();
             email=binding.tvEmail.getText().toString();

        }
    }

    private void apiHit() {
        Toast.makeText(getActivity(),ActivityDashboard.profilepicpath,Toast.LENGTH_LONG).show();

        Authentication.ProfilemultiPartRequest(ActivityDashboard.profilepicpath, (HRAppConstants.URL_PROFILE),this,binding.mobileEditText.getText().toString(),HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),binding.viewUserNameEditText.getText().toString(),binding.emailEditText.getText().toString(),"user");

    }

    private void editFunctionality(String name, String mobile, String email) {

        binding.emailEditText.setHint(email);
        binding.mobileEditText.setHint(mobile);
        binding.viewUserNameEditText.setHint(name);
        binding.view1111.setVisibility(View.VISIBLE);
        binding.view11.setVisibility(View.GONE);
        binding.view13.setVisibility(View.GONE);
        binding.view1313.setVisibility(View.VISIBLE);
        binding.emailEditText.setVisibility(View.VISIBLE);
        binding.mobileEditText.setVisibility(View.VISIBLE);
        binding.viewUserNameEditText.setVisibility(View.VISIBLE);
        binding.tvEmail.setVisibility(View.GONE);
        binding.tvMobile.setVisibility(View.GONE);
        binding.viewUserName.setVisibility(View.GONE);
        binding.button3.setVisibility(View.VISIBLE);
        binding.editButtonn.setVisibility(View.GONE);

    }

    private void captureImage(){
        TedPermission.with(getActivity())
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        CropImage.activity(image_uris)
                                //.setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL)
                                .start(getActivity());
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        getActivity().finish();
                    }
                })
                .setDeniedMessage(getString(R.string.txt_permission_denied_message))
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        dismissLoader();


        if (responseObj instanceof LoginModel) {
            LoginModel model=(LoginModel) responseObj;
            onSaved();
            binding.tvEmail.setText(model.getResult().getEmail());
            binding.viewUserName.setText(model.getResult().getName());
            binding.profileImage.setImageURI(Uri.parse(model.getResult().getImage()));
            HRPrefManager.getInstance(context).setUserDetail((LoginModel) responseObj);
            HRPrefManager.getInstance(context).setKeyIsLoggedIn(true);

            startActivity(IntentHelper.getDashboard(context));
        }


    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
        dismissLoader();
        Toast.makeText(getActivity(),"noo",Toast.LENGTH_LONG).show();
    }



    private void onSaved() {

        binding.emailEditText.setHint(email);
        binding.mobileEditText.setHint(mobile);
        binding.viewUserNameEditText.setHint(name);
        binding.view1111.setVisibility(View.GONE);
        binding.view11.setVisibility(View.VISIBLE);
        binding.view13.setVisibility(View.VISIBLE);
        binding.view1313.setVisibility(View.GONE);
        binding.emailEditText.setVisibility(View.GONE);
        binding.mobileEditText.setVisibility(View.GONE);
        binding.viewUserNameEditText.setVisibility(View.GONE);
        binding.tvEmail.setVisibility(View.VISIBLE);
        binding.tvMobile.setVisibility(View.VISIBLE);
        binding.viewUserName.setVisibility(View.VISIBLE);
        binding.button3.setVisibility(View.GONE);
        binding.editButtonn.setVisibility(View.VISIBLE);

    }
}
