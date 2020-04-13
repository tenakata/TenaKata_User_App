package com.tenakata.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        }
        if( cs.equals("cash") && sp.equals("purchase"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt cash purchase)");

        }
        if( cs.equals("credit") && sp.equals("sales"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt credit sale)");
        }
        if( cs.equals("credit") && sp.equals("purchase"))
        {
            binding.viewCapturImage.setHint("Captured (attach receipt credit purchase)");
        }










        binding.toolbarBackView.setOnClickListener(this);
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
        if (getIntent().getStringExtra("defaultradiobutton").equals("cash")){
            binding.radioButtonCash.setChecked(true);
            binding.textInputLayout.setVisibility(View.GONE);
            binding.ccp.setVisibility(View.GONE);
            binding.textInputLayout2.setVisibility(View.GONE);
            // binding.textInputLayout3.setVisibility(View.GONE);
            binding.view5.setVisibility(View.GONE);
        }
        else
        {
            binding.radioButtonCredit.setChecked(true);
            binding.textInputLayout.setVisibility(View.VISIBLE);
            binding.ccp.setVisibility(View.VISIBLE);
            binding.textInputLayout2.setVisibility(View.VISIBLE);
            // binding.textInputLayout3.setVisibility(View.VISIBLE);
            binding.view5.setVisibility(View.VISIBLE);

        }

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = findViewById(selectedId);

                if (radioButton.getText()!=null && radioButton.getText().toString().equalsIgnoreCase("credit")){
                    binding.textInputLayout.setVisibility(View.VISIBLE);
                    binding.ccp.setVisibility(View.VISIBLE);
                    binding.textInputLayout2.setVisibility(View.VISIBLE);
                   // binding.textInputLayout3.setVisibility(View.VISIBLE);
                    binding.view5.setVisibility(View.VISIBLE);
                }else {
                    binding.textInputLayout.setVisibility(View.GONE);
                    binding.ccp.setVisibility(View.GONE);
                    binding.textInputLayout2.setVisibility(View.GONE);
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

                    path = result.getUri().getEncodedPath();
                    Uri pathx = result.getOriginalUri();
                    Log.e("aaaaa",path);
                    Log.e("bbbb",pathx.toString());
                    binding.viewCapturImage.setText(HRValidationHelper.optional(getRealPathFromURI(result.getUri())));

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
                        /*if (minAdultAge.before(userAge)) {
                            selectedDob = null;
                            binding.tvDob.setText("");
                            Toast.makeText(context, context.getString(R.string.txt_age_message), Toast.LENGTH_SHORT).show();
                        } else {
                            selectedDob = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                            binding.tvDob.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());
                        }*/

                      //  selectedDob = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                        selectedDate = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                        binding.viewDate.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
        } else if (type.equalsIgnoreCase("credit") && HRValidationHelper.isNull(binding.viewIDNo.getText().toString())){
            HRLogger.showSneckbar(view,getString(R.string.txt_enter_ID_no));
            return false;
        } else if (type.equalsIgnoreCase("credit") && HRValidationHelper.isNull(binding.viewName.getText().toString())){
            HRLogger.showSneckbar(view,getString(R.string.txt_enter_name));
            return false;
        }else if (HRValidationHelper.isNull(binding.viewItemList.getText().toString())){
            HRLogger.showSneckbar(view,getString(R.string.txt_enter_items));
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


        Authentication.multiPartRequest(HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),
                    binding.identeramount.getText().toString(),
                    binding.viewItemList.getText().toString(),
                    "cash",
                    getIntent().getStringExtra("sales_purchases"),
                   // binding.viewDate.getText().toString(),
                selectedDate,
                    path, apiUrl,
                    this, paymentType, "", "", binding.viewName.getText().toString());
        }else {

            String apiUrl = HRAppConstants.URL_ADD_SALE_CREDIT_PURCHASE_CASH;

            Authentication.multiPartRequest(HRPrefManager.getInstance(context).getUserDetail().getResult().getId(),
                    binding.identeramount.getText().toString(),
                    binding.viewItemList.getText().toString(),
                    "credit",
                    getIntent().getStringExtra("sales_purchases"),
                  selectedDate,
                  //  binding.viewDate.getText().toString(),
                    path,apiUrl,
                    this,paymentType,binding.viewMobile.getText().toString(),
                    binding.viewIDNo.getText().toString(),
                    binding.viewName.getText().toString());
        }
    }

    // "http:\/\/res.cloudinary.com\/tecorb-technologies\/image\/upload\/v1586342178\/neyblsndk2g5uhvkwwxc.jpg"



}
