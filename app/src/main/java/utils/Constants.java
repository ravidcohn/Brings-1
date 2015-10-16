package utils;

/**
 * Created by pinhas on 28/09/2015.
 */

import brings_app.BuildConfig;

/**
 * API Keys, Client Ids and Audience Ids for accessing APIs and configuring
 * Cloud Endpoints. When you deploy your solution, you need to use your own API
 * Keys and IDs. Please update config.gradle to define them.
 */
public final class Constants {

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    public static final String SENDER_ID = BuildConfig.SENDER_ID;

    /**
     * Web client ID from Google Cloud console.
     */
    public static final String WEB_CLIENT_ID = BuildConfig.WEB_CLIENT_ID;

    /**
     * The web client ID from Google Cloud Console.
     */
    public static final String AUDIENCE_ANDROID_CLIENT_ID =
            "server:client_id:" + WEB_CLIENT_ID;

    /**
     * The URL to the API. Default when running locally on your computer:
     * "http://10.0.2.2:8080/_ah/api/"
     */
    public static final String ROOT_URL = BuildConfig.ROOT_URL;

    /**
     * Defines whether authentication is required or not.
     */
    public static final boolean SIGN_IN_REQUIRED = BuildConfig.SIGN_IN_REQUIRED;

    public static final String SQL_DIR = "/data/data/some_lie.brings/databases/";
    public static final String SQL_DB_NAME = "_edata";
    /**
     * Default constructor, never called.
     */
    private Constants() { }

    public static String User_nickName;
    public static String User_Name;
    public static String Password;
    public final static String New_Event = "New_Event";
    public final static String Delete_Event = "Delete_Event";
    public final static String Update_Event = "Update_Event";
    public final static String Table_Events = "Events";
    public final static String[] Table_Events_Fields = new String[]{"Id","Name","Place","Start DATE","End Date","Description","imagePath","Update_Time"};
    public final static String Table_Tasks = "Tasks";
    public final static String Table_Events_Friends = "Events_Friends";
    public final static String Table_Friends = "Friends";
    public final static String Yes = "Yes";
    public final static String No = "No";



}