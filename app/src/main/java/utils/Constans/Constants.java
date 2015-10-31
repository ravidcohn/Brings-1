package utils.Constans;

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
    public static final String bucket_name = "a_bucket";
    /**
     * Default constructor, never called.
     */
    private Constants() { }

    public static String MY_User_ID;
    public static String User_Nickname;
    public static String Password;
    public final static String New_Event = "New_Event";
    public final static String Delete_Event = "Delete_Event";
    public final static String Update_Event = "Update_Event";
    public final static String New_Attending = "New_Attending";
    public final static String Delete_Attending = "Delete_Attending";
    public final static String Update_Attending = "Update_Attending";
    public final static String New_Task = "New_Task";
    public final static String Delete_Task = "Delete_Task";
    public final static String Update_Task = "Update_Task";
    public final static String Update_Task_User_ID = "Update_Task_User_ID";
    public final static String New_Chat_Message = "New_Chat_Message";
    public final static String Delete_Chat_Message = "Delete_Chat_Message";
    public final static String Yes = "Yes";
    public final static String No = "No";
    public final static String Maybe = "Maybe";
    public final static String UnCheck = "UnCheck";
    public final static String Manager = "Manager";
    public final static String Editor = "Editor";
    public final static String Participant = "Participant";
    public final static String Update_Activity = "Update_Activity";

}