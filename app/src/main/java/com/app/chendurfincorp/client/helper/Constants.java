package com.app.chendurfincorp.client.helper;

import android.content.SharedPreferences;

public class Constants {

    //sharedPreference
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    //baseUrl
    public static String BASE_URL = "http://chendur.shadowws.in/jsons/";

    //keywords
    public static String id="id";
    public static String name="name";
    public static String username="username";
    public static String password="pass";

    //pageUrl
    public static String LOGIN= "client_login.php?";
    public static String VIEW_NETWORK= "client_tree.php?";
    public static String TODAY_REVENUE= "get_daily.php?";
    public static String INVESTMENT= "get_amount.php?";
    public static String TOTAL_PROFIT= "total_daily.php?";

}
