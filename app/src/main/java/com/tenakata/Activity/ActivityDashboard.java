package com.tenakata.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.tenakata.Adapters.DrawerAdapter;
import com.tenakata.Adapters.HomeViewPagerAdapter;
import com.tenakata.Base.BaseActivity;
import com.tenakata.CallBacks.BaseCallBacks;
import com.tenakata.Dialog.ProgressDialog;
import com.tenakata.Fragments.FragmentCashFlow;
import com.tenakata.Fragments.FragmentHome;
import com.tenakata.Fragments.FragmentProfile;
import com.tenakata.Fragments.FragmentPurchaseFlow;
import com.tenakata.Fragments.FragmentTraning;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Network.Authentication;
import com.tenakata.R;
import com.tenakata.Utilities.DimensionHelper;
import com.tenakata.Utilities.FontFamily;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRPrefManager;
import com.tenakata.Utilities.HRUrlFactory;
import com.tenakata.Utilities.HRValidationHelper;
import com.tenakata.databinding.ActivityDashboardBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.transform.ElevationTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.tenakata.Dialog.ProgressDialog.progressDialog;

public class ActivityDashboard extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener , BaseCallBacks , FragmentHome.CallBackAgain,FragmentProfile.Callback{



    public static String profilepicpath = null;
    private Context context;
    private ActivityDashboardBinding binding;
    private SlidingRootNav drawerLayout;

    private boolean isHome = false;
    private boolean isSale = true;
    private boolean isPurchage = true;
    ProgressDialog progressDialog ;
    private TextView userName;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard);
        context = this;
        new FragmentProfile( this);

        binding.includedToolbar.viewUserName.setText("Hello "+HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));


        initDrawer(binding.includedToolbar.toolbarDashboard,binding.includedToolbar.toolbarMenuView);
        getSupportFragmentManager().beginTransaction().add(R.id.dashboardFrame,new FragmentHome(this)).commit();


        binding.includedFooter.img1.setOnClickListener(this);
        binding.includedFooter.img2.setOnClickListener(this);
        binding.includedFooter.img3.setOnClickListener(this);
        binding.includedFooter.tv1.setOnClickListener(this);
        binding.includedFooter.tv2.setOnClickListener(this);
        binding.includedFooter.tv3.setOnClickListener(this);
        progressDialog = new ProgressDialog(context);
    }

    public void initDrawer(View toolbar, View actionBarToggle) {


        binding.includedFooter.tv1.setTextColor(getResources().getColor(R.color.colorBlue));
        binding.includedFooter.tv2.setTextColor(getResources().getColor(R.color.grey_normal));
        binding.includedFooter.tv3.setTextColor(getResources().getColor(R.color.grey_normal));

        binding.includedFooter.img1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
        binding.includedFooter.img2.setImageDrawable(getResources().getDrawable(R.drawable.offer_unsel));
        binding.includedFooter.img3.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_unsel));
        binding.includedToolbar.toolbarDashboard.setVisibility(View.VISIBLE);

        isHome = false;
        isSale = true;
        isPurchage = true;



        View menuView = LayoutInflater.from(this).inflate(R.layout.view_drawer, null, false);

        drawerLayout = new SlidingRootNavBuilder(this)
                .withDragDistance(DimensionHelper.getDrawerDragDistance(this)) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.8f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .addRootTransformation(new ElevationTransformation(2))
                .withGravity(SlideGravity.LEFT)
                .withContentClickableWhenMenuOpened(false)
                .withMenuView(menuView)
                .withToolbarRootView(toolbar)
                .withActionBarToggleView(actionBarToggle)
                .inject();

        ListView drawerLV = menuView.findViewById(R.id.drawer_menu_lv);
        drawerLV.setAdapter(new DrawerAdapter());
        addDrawerHeader(drawerLV);
        drawerLV.setOnItemClickListener(this);
        binding.includedToolbar.toolbarMenuView.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        closeMenu();

        switch (position) {

            case 0:
            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.includedToolbar.viewUserName.setText("Profile");
                        loadFragment(new FragmentProfile(this));
                    }
                }, 225);
                break;

            case 1:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFragment(new FragmentHome(this));
                    }
                }, 225);
                break;

            case 3:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.includedToolbar.viewUserName.setText("Training");
                        loadFragment(new FragmentTraning());
                    }
                }, 225);
                break;

            case 4:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                    }
                }, 225);
                break;

            case 5:

                break;


            case 6:

                break;

            case 7:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getApplicationContext(),ActivityCreateMpin.class);
                        startActivity(intent);
                    }
                }, 225);
                break;

            case 8:
                Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                break;


            case 9:
                Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                break;

            case 10:
                Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                break;

            case 11:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logOutDialog(context);
                    }
                }, 225);
                break;

            default:
                Toast.makeText(context, "Under Development", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onClick(int viewId, View view) {

        switch (viewId) {

            case R.id.toolbarMenuView:
                if (isMenuOpened()) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;

            case R.id.img1:
            case R.id.tv1:
                if (isHome) {
                    try {
                        loadFragment(new FragmentHome(this));
                        binding.includedFooter.tv1.setTextColor(getResources().getColor(R.color.colorBlue));
                        binding.includedFooter.tv2.setTextColor(getResources().getColor(R.color.grey_normal));
                        binding.includedFooter.tv3.setTextColor(getResources().getColor(R.color.grey_normal));
                        
                        binding.includedFooter.img1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                        binding.includedFooter.img2.setImageDrawable(getResources().getDrawable(R.drawable.offer_unsel));
                        binding.includedFooter.img3.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_unsel));
                        binding.includedToolbar.toolbarDashboard.setVisibility(View.VISIBLE);

                        isHome = false;
                        isSale = true;
                        isPurchage = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.img2:
            case R.id.tv2:
                    if (isSale) {
                        try {
                            loadFragment(new FragmentCashFlow(this));
                            binding.includedFooter.tv1.setTextColor(getResources().getColor(R.color.grey_normal));
                            binding.includedFooter.tv2.setTextColor(getResources().getColor(R.color.colorBlue));
                            binding.includedFooter.tv3.setTextColor(getResources().getColor(R.color.grey_normal));

                            binding.includedFooter.img1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_unsel));
                            binding.includedFooter.img2.setImageDrawable(getResources().getDrawable(R.drawable.offer_sel));
                            binding.includedFooter.img3.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_unsel));
                            binding.includedToolbar.toolbarDashboard.setVisibility(View.GONE);

                            isHome = true;
                            isSale = false;
                            isPurchage = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                break;

            case R.id.img3:
            case R.id.tv3:
                    if (isPurchage) {

                        try {
                            loadFragment(new FragmentPurchaseFlow(this));
                            binding.includedFooter.tv1.setTextColor(getResources().getColor(R.color.grey_normal));
                            binding.includedFooter.tv2.setTextColor(getResources().getColor(R.color.grey_normal));
                            binding.includedFooter.tv3.setTextColor(getResources().getColor(R.color.colorBlue));


                            binding.includedFooter.img1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_unsel));
                            binding.includedFooter.img2.setImageDrawable(getResources().getDrawable(R.drawable.offer_unsel));
                            binding.includedFooter.img3.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_sel));

                            binding.includedToolbar.toolbarDashboard.setVisibility(View.GONE);

                            isHome = true;
                            isSale = true;
                            isPurchage = false;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                break;

        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {

            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(binding.dashboardFrame.getId(), fragment);
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openMenu() {
        drawerLayout.openMenu(true);
    }

    public void closeMenu() {
        drawerLayout.closeMenu(true);
    }

    public boolean isMenuOpened() {
        return drawerLayout.isMenuOpened();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (isMenuOpened()) closeMenu();
            else
                super.onBackPressed();

        }

    }

    private void addDrawerHeader(ListView drawerList){
        View headerView = ((LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_header, null, false);
        userName = headerView.findViewById(R.id.viewUserName);
        userImage = headerView.findViewById(R.id.imageView4);
        userName.setText(HRValidationHelper.optional("Hello "+HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));
        Glide.with(this)
                .load(HRPrefManager.getInstance(context).getUserDetail().getResult().getImage())
                .apply(new RequestOptions()
                        .transform(new CircleCrop(),new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                .into(userImage);

        drawerList.addHeaderView(headerView);
    }


    private void setHeaderNameIimage(){
        if (userName!=null)
        userName.setText(HRValidationHelper.optional("Hello "+HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));

        if (userImage!=null) {
            Glide.with(this)
                    .load(HRPrefManager.getInstance(context).getUserDetail().getResult().getImage())
                    .apply(new RequestOptions()
                            .transform(new CircleCrop(), new RoundedCorners(30)).placeholder(R.drawable.avator_profile))
                    .into(userImage);
        }
    }


    private void logOutDialog(final Context context) {

        new iOSDialogBuilder(context)
                .setTitle(context.getString(R.string.app_name))
                .setSubtitle(getString(R.string.txt_want_to_logout))
                .setBoldPositiveLabel(false)
                .setFont(FontFamily.getPtRegular())
                .setTitleFont(FontFamily.getMonBold())
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                        hitApi();
                    }
                }).setNegativeListener(context.getResources().getString(R.string.cancel_text), new iOSDialogClickListener() {
            @Override
            public void onClick(iOSDialog dialog) {
                dialog.dismiss();
            }
        }).build().show();
    }

    private void hitApi() {
        progressDialog = new ProgressDialog(context);
        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessiontoken", HRPrefManager.getInstance(context).getUserDetail().getResult().getToken());
            jsonObject.put("role", "user");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.objectRequestAccount(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_LOGOUT),
                this, jsonObject, HRUrlFactory.getDefaultHeaders());

    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        if (!isFinishing() && progressDialog.isShowing()) progressDialog.dismiss();
        if (responseObj instanceof ModelSuccess) {
            HRPrefManager.getInstance(context).clearPrefs();
            HRPrefManager.getInstance(context).setKeyIsStart(true);
            HRPrefManager.getInstance(context).setKeyIsLoggedIn(false);
            startActivity(new Intent(context, ActivityBioMetricLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finishAffinity();

        }

    }

    @Override
    public void onTaskError(String errorMsg) {
        if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
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

    @Override
    public void onSaleClick() {
        try {
            loadFragment(new FragmentCashFlow(this));
            binding.includedFooter.tv1.setTextColor(getResources().getColor(R.color.grey_normal));
            binding.includedFooter.tv2.setTextColor(getResources().getColor(R.color.colorBlue));
            binding.includedFooter.tv3.setTextColor(getResources().getColor(R.color.grey_normal));

            binding.includedFooter.img1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_unsel));
            binding.includedFooter.img2.setImageDrawable(getResources().getDrawable(R.drawable.offer_sel));
            binding.includedFooter.img3.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_unsel));
            binding.includedToolbar.toolbarDashboard.setVisibility(View.GONE);

            isHome = true;
            isSale = false;
            isPurchage = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPurchaseClick() {
        try {
            loadFragment(new FragmentPurchaseFlow(this));
            binding.includedFooter.tv1.setTextColor(getResources().getColor(R.color.grey_normal));
            binding.includedFooter.tv2.setTextColor(getResources().getColor(R.color.grey_normal));
            binding.includedFooter.tv3.setTextColor(getResources().getColor(R.color.colorBlue));


            binding.includedFooter.img1.setImageDrawable(getResources().getDrawable(R.drawable.home_icon_unsel));
            binding.includedFooter.img2.setImageDrawable(getResources().getDrawable(R.drawable.offer_unsel));
            binding.includedFooter.img3.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_sel));

            binding.includedToolbar.toolbarDashboard.setVisibility(View.GONE);

            isHome = true;
            isSale = true;
            isPurchage = false;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onChangeName() {
        //initDrawer(binding.includedToolbar.toolbarDashboard,binding.includedToolbar.toolbarMenuView );
        setHeaderNameIimage();
    }
}
