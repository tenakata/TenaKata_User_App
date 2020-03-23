package com.tenakata.Network;


import com.tenakata.Application.App;
import com.tenakata.Models.AddSaleCashCreditModel;
import com.tenakata.Models.CashSalesCreditModel;
import com.tenakata.Models.HomeModel;
import com.tenakata.Models.LoginModel;
import com.tenakata.Models.ModelSuccess;
import com.tenakata.Models.PayAmountModel;
import com.tenakata.Models.ProfileModel;
import com.tenakata.Models.TrainingListModel;
import com.tenakata.Models.TrainingViewModel;
import com.tenakata.Models.ViewDetailsModel;
import com.tenakata.Utilities.HRAppConstants;
import com.tenakata.Utilities.HRUrlFactory;

public class ResponseParser {

    public static Object parse(String url, String response) {

        url = url.replaceAll(HRUrlFactory.getBaseUrlWithApiVersioning(), "");

        if (url.contains("?user_id")) {
            url = "events/";
        } else if (url.contains("?page"))
            url = "events?";

        try {
            switch (url) {

                /*========= parse model like bottom ================*/

                case HRAppConstants.URL_LOGIN:
                case HRAppConstants.URL_LOGIN_WITH_MPIN:
                case HRAppConstants.URL_PROFILE :
                    return App.getInstance().getGson().fromJson(response, LoginModel.class);

                case HRAppConstants.URL_CHECK_USER:
                case HRAppConstants.URL_FORGOT_PASSWORD:
                case HRAppConstants.URL_LOGOUT:
                case HRAppConstants.URL_REMIND:
                case HRAppConstants.URL_TRAINING_RATING:
                    return App.getInstance().getGson().fromJson(response, ModelSuccess.class);

                case HRAppConstants.URL_CASH_CREDIT_SALES:
                case HRAppConstants.URL_SORTING:
                case HRAppConstants.URL_FILTER :
                case HRAppConstants.URL_CASH_CREDIT_Purchase:
                    return App.getInstance().getGson().fromJson(response, CashSalesCreditModel.class);

                case HRAppConstants.URL_PAY_AMOUNT:
                    return App.getInstance().getGson().fromJson(response, PayAmountModel.class);


                case HRAppConstants.URL_ADD_SALE_CASH_PURCHASE_CASH:
                case HRAppConstants.URL_ADD_SALE_CREDIT_PURCHASE_CASH:
                    return App.getInstance().getGson().fromJson(response, AddSaleCashCreditModel.class);

                case HRAppConstants.URL_HOME:
                    return App.getInstance().getGson().fromJson(response, HomeModel.class);

                case HRAppConstants.URL_PURCHASEVIEWDETAIL:
                    return App.getInstance().getGson().fromJson(response, ViewDetailsModel.class);
                case HRAppConstants.URL_SALEVIEWDETAIL:
                    return App.getInstance().getGson().fromJson(response, ViewDetailsModel.class);

                case HRAppConstants.URL_TRAINING :
                    return App.getInstance().getGson().fromJson(response, TrainingListModel.class);
                case HRAppConstants.URL_TRAINING_VIEW :
                    return App.getInstance().getGson().fromJson(response, TrainingViewModel.class);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
