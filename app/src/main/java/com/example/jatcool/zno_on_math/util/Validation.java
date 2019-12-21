package com.example.jatcool.zno_on_math.util;

import android.widget.Toast;

import com.example.jatcool.zno_on_math.constants.RegexC;
import com.example.jatcool.zno_on_math.connection.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Validation {
    private static boolean flagEmailExist=true;

    public static boolean isValidEmail(String str){
        Pattern pattern=Pattern.compile(RegexC.VALID_EMAIL);
        Matcher matcher=pattern.matcher(str);
        return matcher.find();
    }
    public static boolean isValidName(String str){
        Pattern pattern=Pattern.compile(RegexC.VALID_NAME);
        Matcher matcher=pattern.matcher(str);
        return matcher.find();
    }
    public static boolean isValidPasswor(String str){
        Pattern pattern=Pattern.compile(RegexC.VALID_PASSWORD);
        Matcher matcher=pattern.matcher(str);
        return matcher.find();
    }
    public static boolean isEqualsPassword(String str1,String str2){
        return  str1.equals(str2);
    }
}