package com.tenakata.Utilities;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HRValidationHelper {

    private static final String ERROR_MIN_PASS = "Password is short.";
    private static final String ERROR_MAX_PASS = "Password is long.";
    private static final String ERROR_MIN_PINCODE = "Password is short.";

    public static boolean isValidEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.toLowerCase().trim().matches(emailPattern);
    }

    public static boolean isValidAccountNumber(String accNumber){
        //String accountNumberPattern = "d{4}+-d{4}+-d{4}+-d{4}";
        return accNumber.length()>0;
    }

    public static boolean isValidPassword(EditText passwordET, String password){

        int minLength = 6, maxLength = 20;

        if(password.length()>=minLength&&password.length()<=maxLength){
            return true;
        }else if(password.length()<minLength){
            passwordET.setError(ERROR_MIN_PASS);
            return false;
        }else if(password.length()>maxLength){
            passwordET.setError(ERROR_MAX_PASS);
            return false;
        }else {
            return false;
        }
    }

    public static boolean isValidPassword(String password){
        int minLength = 6, maxLength = 20;
        return password!=null && password.length()>=minLength && password.length()<=maxLength;
    }

    public static boolean isValidPinCode(EditText pinCodeEditText, String pinNumber){
        int minLength=6 ;
        if(pinNumber.length()<6){
            pinCodeEditText.setError(ERROR_MIN_PINCODE);
        }else{
            return false;
        }
        return true;
    }

    public static boolean isNull(String input) {
         return (input == null || input.trim().equals("") || input.length() < 1 || input.trim().equals("null"));
    }

    public static String optional(String input){
        if(input==null||input.trim().equals("")||input.length()<1||input.trim().equals("null")){
            return "";
        }else {
            return input;
        }
    }

    public static String optionalBlank(String input){
        if(input==null||input.trim().equals("")||input.length()<1||input.trim().equals("null")){
            return "";
        }else {
            return input;
        }
    }

    public static String NullPrice(String input){
        if(input==null||input.trim().equals("")||input.length()<1||input.trim().equals("null")){
            return "0.0";
        }else {
            return input;
        }
    }

    public static String optional(String input, @NonNull String optionalValue){
        if(input==null||input.trim().equals("")||input.length()<1||input.trim().equals("null")){
            return optionalValue;
        }else {
            return input;
        }
    }



    public static boolean licenseExpiryValidation(String input){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
        Date expiry = null;
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(simpleDateFormat.parse(input));
            c.add(Calendar.MONTH, 1);
            String expiry1 = simpleDateFormat.format(c.getTime());
            expiry=simpleDateFormat.parse(expiry1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert expiry != null;
        return expiry.before(new Date());
    }

    public static boolean isVehiclePlateNumberValid(String input){
        return input.matches("[A-Z]{3}+[0-9]{1,4}");
    }

}
