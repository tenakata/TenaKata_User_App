package com.tenakata.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tenakata.Base.BaseActivity;
import com.tenakata.Models.AddSaleCashCreditModel;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRLogger;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRPriceFormater;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.ActivityAddDailySalesBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ActivityAddDailySales extends BaseActivity {
    private Context context;
    private ActivityAddDailySalesBinding binding;
    private Uri image_uris;
    private String path = null;
   private Intent intent;
    private String selectedDob;
    private  String selectedDate= "";
    private int mYear, mDay, mMonth;
    private String countryCode;
    private String sales_purchases;

    @Override
    public void onClick(int viewId, View view) {
        switch (viewId) {
            case R.id.toolbarBackView:
                finish();
                break;

            case R.id.viewCapturImage:
                captureImage();
                break;

            case R.id.viewDate:
                dobPicker();
                break;

            case R.id.viewSave:
                int selectedId = binding.radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);

                if (validation(view,radioButton.getText().toString())) {
                    hitApi(radioButton.getText().toString());
                }
                break;
        }

    }

    private void captureImage(){
        TedPermission.with(ActivityAddDailySales.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        CropImage.activity(image_uris)
                                //.setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL)
                                .start(ActivityAddDailySales.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setDeniedMessage(getString(R.string.txt_permission_denied_message))
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_daily_sales);
        context = this;
        intent=getIntent();
        if (getIntent()!=null){
          //  Toast.makeText(this,getIntent().getStringExtra("sales_purchases"),Toast.LENGTH_LONG).show();
            sales_purchases = getIntent().getStringExtra("sales_purchases");
            binding.textView2.setText(getIntent().getStringExtra("title"));

        }

        String sp,cs;
        sp=getIntent().getStringExtra("sales_purchases");
        cs=getIntent().getStringExtra("defaultradiobutton");


        if( cs.equals("cash") && sp.equals("sales"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt cash sale)");
            binding.radioButtonCash.setChecked(true);
            binding.radioGroup.setVisibility(View.GONE);
            binding.textInputLayout.setVisibility(View.GONE);
            binding.view6.setVisibility(View.GONE);
            binding.tvSpinnerHead.setVisibility(View.GONE);
            binding.nameSpinner.setVisibility(View.GONE);
            // binding.textInputLayout3.setVisibility(View.VISIBLE);
            binding.view5.setVisibility(View.GONE);
            binding.etCashSaleLayoout.setVisibility(View.VISIBLE);

        }
        if( cs.equals("cash") && sp.equals("purchase"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt cash purchase)");
            binding.radioButtonCash.setChecked(true);
            binding.radioGroup.setVisibility(View.GONE);
            binding.textInputLayout.setVisibility(View.GONE);
            binding.ccp.setVisibility(View.GONE);
            binding.view6.setVisibility(View.VISIBLE);
            binding.tvSpinnerHead.setVisibility(View.VISIBLE);
            binding.nameSpinner.setVisibility(View.VISIBLE);
            binding.etCashSaleLayoout.setVisibility(View.GONE);
            // binding.textInputLayout3.setVisibility(View.VISIBLE);
            binding.view5.setVisibility(View.GONE);
            binding.etCashSaleLayoout.setVisibility(View.VISIBLE);

        }
        if( cs.equals("credit") && sp.equals("sales"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt credit sale)");
            binding.radioButtonCredit.setChecked(true);
            binding.radioGroup.setVisibility(View.GONE);
            binding.textInputLayout.setVisibility(View.VISIBLE);
            binding.view6.setVisibility(View.GONE);
            binding.tvSpinnerHead.setVisibility(View.GONE);
            binding.ccp.setVisibility(View.VISIBLE);
            binding.nameSpinner.setVisibility(View.GONE);
            binding.etCashSaleLayoout.setVisibility(View.GONE);
            // binding.textInputLayout3.setVisibility(View.VISIBLE);
            binding.view5.setVisibility(View.VISIBLE);
            binding.etCashSaleLayoout.setVisibility(View.VISIBLE);
        }
        if( cs.equals("credit") && sp.equals("purchase"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt credit purchase)");
            binding.radioButtonCredit.setChecked(true);
            binding.radioGroup.setVisibility(View.GONE);
            binding.textInputLayout.setVisibility(View.VISIBLE);
            binding.ccp.setVisibility(View.VISIBLE);
            binding.view6.setVisibility(View.VISIBLE);
            binding.tvSpinnerHead.setVisibility(View.VISIBLE);
            binding.nameSpinner.setVisibility(View.VISIBLE);
            binding.etCashSaleLayoout.setVisibility(View.GONE);
            // binding.textInputLayout3.setVisibility(View.VISIBLE);
            binding.view5.setVisibility(View.VISIBLE);
            binding.etCashSaleLayoout.setVisibility(View.VISIBLE);
        }










        binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.viewCapturImage.setOnClickListener(this);
        binding.viewDate.setOnClickListener(this);
        binding.viewSave.setOnClickListener(this);

        binding.ccp.registerPhoneNumberTextView(binding.viewMobile);
        countryCode = binding.ccp.getSelectedCountryCode();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                countryCode = country.getPhoneCode();
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = findViewById(selectedId);

                if (radioButton.getText()!=null && radioButton.getText().toString().equalsIgnoreCase("credit")){
                    binding.textInputLayout.setVisibility(View.VISIBLE);
                    binding.ccp.setVisibility(View.VISIBLE);
                    binding.nameSpinner.setVisibility(View.VISIBLE);
                   // binding.textInputLayout3.setVisibility(View.VISIBLE);
                    binding.view5.setVisibility(View.VISIBLE);
                }else {
                    binding.textInputLayout.setVisibility(View.GONE);
                    binding.ccp.setVisibility(View.GONE);
                    binding.nameSpinner.setVisibility(View.GONE);
                   // binding.textInputLayout3.setVisibility(View.GONE);
                    binding.view5.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {

                   /* File compressedImageFile = Compressor.getDefault(this).compressToFile(new File(result.getUri().getEncodedPath()));
                    Uri x=Uri.fromFile(new File(compressedImageFile.getPath()));
                    path=String.valueOf(x.getPath());*/
                    try {
                        path=   String.valueOf(new Compressor(context).compressToFile(
                                new File(result.getUri().getPath())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    binding.viewCapturImage.setText(HRValidationHelper.optional(path));

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri){
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e){
            return contentUri.getPath();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void dobPicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        mYear = year;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        c.set(year, monthOfYear, dayOfMonth);

                        Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, 0);
                        selectedDate = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                        binding.viewDate.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public static String removeZero(String mobile)
    {
        int i = 0;
        while (i < mobile.length() && mobile.charAt(i) == '0')
            i++;
        StringBuffer sb = new StringBuffer(mobile);
        sb.replace(0, i, "");
        return sb.toString();
    }

    private boolean validation(View view,String type){
        if (HRValidationHelper.isNull(binding.viewDate.getText().toString())){
            HRLogger.showSneckbar(view,getString(R.string.txt_select_date));
            return false;
        }else if (HRValidationHelper.isNull(binding.identeramount.getText().toString())){
            HRLogger.showSneckbar(view,getString(R.string.txt_enter_amount));
            return false;
        }else if (type.equalsIgnoreCase("credit") && HRValidationHelper.isNull(binding.viewMobile.getText().toString())){
            HRLogger.showSneckbar(view,getString(R.string.txt_enter_mobile_number));
            return false;
        } else if (type.equalsIgnoreCase("credit") && HRValidationHelper.isNull(binding.nameSpinner.getSelectedItem().toString())){
            HRLogger.showSneckbar(view,"Enter Description");
            return false;
        }else if (HRValidationHelper.isNull(binding.viewDetaillist.getText().toString())){
            HRLogger.showSneckbar(view,"Enter Detail List");
            return false;
        }else if (type.equals("cash") && HRValidationHelper.isNull(binding.etCashSaleDescription.getText().toString())){
            HRLogger.showSneckbar(view,"Enter Description");
            return false;
        }else if (path == null){
            HRLogger.showSneckbar(view,getString(R.string.txt_click_image));
            return false;
        }
        return true;
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);
        if (responseObj instanceof AddSaleCashCreditModel){

            AddSaleCashCreditModel model = (AddSaleCashCreditModel)responseObj;
            errorDialog(getString(R.string.app_name),model.getMessage());

        }
    }

    private void errorDialog(String setTitle, String setSubTitle) {
        new iOSDialogBuilder(context)
                .setTitle(setTitle)
                .setSubtitle(setSubTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        finish();
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
    }

    private void hitApi(String paymentType){

        if (paymentType.equalsIgnoreCase("cash")) {

            String apiUrl = HRAppConstants.URL_ADD_SALE_CASH_PURCHASE_CASH;
            String name = getSelectedSpinnerItem();
            String mobile=removeZero(binding.viewMobile.getText().toString());

        Authentication.multiPartRequest(HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),
                    binding.identeramount.getText().toString(),
                    binding.viewDetaillist.getText().toString(),
                    "cash",
                    getIntent().getStringExtra("sales_purchases"),
                   // binding.viewDate.getText().toString(),
                selectedDate,
                    path, apiUrl,
                    this, paymentType,mobile, binding.etCashSaleDescription.getText().toString(), name,countryCode);
          //  Toast.makeText(context,HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),Toast.LENGTH_SHORT).show();

        }else {

            String apiUrl = HRAppConstants.URL_ADD_SALE_CREDIT_PURCHASE_CASH;
           String name = getSelectedSpinnerItem();
           String mobile=removeZero(binding.viewMobile.getText().toString());

            Authentication.multiPartRequest(HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),
                    binding.identeramount.getText().toString(),
                    binding.viewDetaillist.getText().toString(),
                    "credit",
                    getIntent().getStringExtra("sales_purchases"),
                  selectedDate,
                  //  binding.viewDate.getText().toString(),
                    path,apiUrl,
                    this,paymentType,mobile,binding.etCashSaleDescription.getText().toString()
                   , name
                    ,countryCode);


        }
    }


    String getSelectedSpinnerItem()
    {
        String result="";
        String sp,cs;
        sp=getIntent().getStringExtra("sales_purchases");
        cs=getIntent().getStringExtra("defaultradiobutton");
        if(  sp.equals("sales")){
            result ="";
        }else {
            result =binding.nameSpinner.getSelectedItem().toString();
        }
        return  result;
    }

    // "http:\/\/res.cloudinary.com\/tecorb-technologies\/image\/upload\/v1586342178\/neyblsndk2g5uhvkwwxc.jpg"



}
