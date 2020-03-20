package com.tenakata.Utilities;

public interface HRAppConstants {

    String FILTER_NOTIFICATIONS = "example.filter.notification";
    String LOCATION_NOTIFICATION_BROADCAST = "com.example.location";
    String CHANNEL_ID = "com.example";
    String CHANNEL_NAME = "Notification";
    String CHANNEL_DESCRIPTION = "Example Partner Notifications";

    String kResponseCode = "status";
    String kResponseMsg = "message";
    String kRESPONSE_ERROR = "errors";

    int kResponseOK = 200;
    int kResponseSignUp = 201;
    int kResponseUserUpdated = 205;
    int kResponseSessionExpire = 345;
    int kResponseDLNotExit = 425;
    int kResponseContactExist = 409;
    int kResponseContactNotExist = 400;
    int kResponsePendingRide = 303;
    int kResponseUpdate = 101;
    int kResponseForceUpdate = 102;
    int kResponseNotAccess = 401;
    int kUpdateDevice = 205;
    int kRESPONSE_NO_TOKEN = 403;

    String URL_LOGIN ="book/login";
    String URL_CHECK_USER ="book/check_user";
    String URL_FORGOT_PASSWORD ="book/forget_password";
    String URL_LOGIN_WITH_MPIN ="book/mp_pin";
    String URL_LOGOUT ="book/logout";
    String URL_CASH_CREDIT_SALES ="book/sale_purchases_list";
    String URL_SORTING ="book/sorting";
    String URL_CASH_CREDIT_Purchase ="book/credit_sale_purchases_list";
    String URL_ADD_SALE_CASH_PURCHASE_CASH ="book/dailysale_purchases";
    String URL_ADD_SALE_CREDIT_PURCHASE_CASH ="book/credit_sale_purchases";
    String URL_HOME ="book/home";
    String URL_REMIND="book/remind";
    String URL_PAY_AMOUNT ="book/confirm_payment";

    String URL_SALEVIEWDETAIL="book/credit_view_details";
    String URL_PURCHASEVIEWDETAIL="book/purchase_view_details";
    String URL_CREATE_MPIN ="book/set_mp_pin";
    String URL_PROFILE="book/profile";
    String URL_TRAINING="book/training";
    String URL_TRAINING_VIEW="book/training_viewDetails";
    String URL_TRAINING_RATING="book/training_rating";
}