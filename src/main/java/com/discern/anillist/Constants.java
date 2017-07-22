package com.discern.anillist;

public interface Constants {

    String KSH = "Ksh. %s";

    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    //The trailing slash is necessary for use with Retrofit
    String DOMAIN_NAME =    "https://anilist.co/api/";
    String SOCKET_DOMAIN =  "https://anilist.co/api//";
    String PACKAGE_NAME =   "com.discern.anilist";

    //Constants to be used in SharedPreferences
    String PREF_USER_ID = "user_id";
    String PREF_IS_LOGGED_IN = "is_logged_in";
    String PREF_ACCESS_TOKEN = "access_token";
    String PREF_USERNAME = "user_name";
    String PREF_NAME = "users_name";
    String PREF_USER_EMAIL = "user_email";
    String PREF_USER_BG = "user_background";
    String PREF_FACE_URL= "face_url";
    String PREF_FB_ACCECSS_TOKEN = "fb_access_token";

    //Constants used for bundles
    String B_SHOP_ID ="shop_id";
    String B_PRODUCT_IMAGE = "product_image_url";
    String B_PRODUCT_NAME = "product_title";
    String B_PRODUCT_PRICE = "product_price";
    String B_PRODUCT_DESCRIPTION = "product_description";
    String B_PRODUCT_ID = "product_id";
    String B_USERNAME = "user_name";
    String B_USER_ID = "user_guy_id";
    String B_SHOP_DESCRIPTION = "shop_description";

    String S_BUNDLE = "service_bundle";
    String S_ONLINE_BUNDLE = "isConnectionAvailable";

    String SERVICE_ACTION = PACKAGE_NAME + ".service.AppService";
    String B_TOKEN = "bundle_token";
    String B_USER_IMAGE = "user_image";
    String B_USER_LOGO = "user_logo";
    String B_CHAT_ID = "chat_id";
    String IS_ONLINE = "isOnline";

    String B_OTHER_USER_NAME = "other_user_name";
    String B_OTHER_IMAGE = "other_user_image";
    String ALL_SOKO_IMAGE = "allSokoImage";
    String S_UNREAD_COUNT = "unread_count";
    String S_MESSAGES = "unread_messages";
    String S_MESSAGE_CREATOR = "message_from";

    String ACTION_NETWORK_STATUS = "com.app.sokonyumbani.background.action.networkStatus";

}
