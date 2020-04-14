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

    String URL_LOGIN ="index.php/login";
    String URL_CHECK_USER ="index.php/check_user";
    String URL_FORGOT_PASSWORD ="index.php/forget_password";
    String URL_LOGIN_WITH_MPIN ="index.php/mp_pin";
    String URL_LOGOUT ="index.php/logout";
    String URL_CASH_CREDIT_SALES ="index.php/sale_purchases_list";
    String URL_SORTING ="index.php/sorting";
    String URL_CASH_CREDIT_Purchase ="index.php/credit_sale_purchases_list";
    String URL_ADD_SALE_CASH_PURCHASE_CASH ="index.php/dailysale_purchases";
    String URL_ADD_SALE_CREDIT_PURCHASE_CASH ="index.php/credit_sale_purchases";
    String URL_HOME ="index.php/home";
    String URL_REMIND="index.php/remind";
    String URL_PAY_AMOUNT ="index.php/confirm_payment";

    String URL_SALEVIEWDETAIL="index.php/credit_view_details";
    String URL_PURCHASEVIEWDETAIL="index.php/purchase_view_details";
    String URL_CREATE_MPIN ="index.php/set_mp_pin";
    String URL_PROFILE="index.php/profile";
    String URL_TRAINING="index.php/training";
    String URL_TRAINING_VIEW="index.php/training_viewDetails";
    String URL_TRAINING_RATING="index.php/training_rating";
    String URL_FILTER="index.php/filter";
    String URL_GRAPH="index.php/graph";
}


