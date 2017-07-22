package com.discern.anillist.background;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.discern.anillist.Constants;
import com.discern.anillist.R;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class BaseApplication extends Application {

    private static BaseApplication instance;

    private static Typeface MavenPro;
    private static Typeface roboto;
    private static Typeface Anke;

    private SharedPreferences preferences;
    private String email;
    private String accessToken;
    private String userId;

    public static final String TAG = BaseApplication.class.getSimpleName();

    private String username;
    private String name;

    private String faceUrl;
    private String userBg;

    private boolean loggedIn;

    public BaseApplication(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        roboto = setTypeface(this, "raw/Roboto_Regular.ttf");
        MavenPro = setTypeface(this, "raw/MavenPro-Medium.ttf");
        Anke = setTypeface(this, "raw/Anke.ttf");

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }


    public static void setMavenPro(TextView textView){
        textView.setTypeface(MavenPro);
    }

    public static void setRoboto(TextView textView){
        textView.setTypeface(roboto);
    }

    public static void setAnke(TextView textView){
        textView.setTypeface(Anke);
    }

    private static Typeface setTypeface(Context context, String origin){
        return Typeface.createFromAsset(context.getAssets(), origin);
    }
    public static BaseApplication getInstance ()
    {
        return instance;
    }

    public static boolean isInternetConnected () {
        return instance.checkIfHasNetwork();
    }

    private boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public void clearUser() {
        SharedPreferences.Editor editor= getSettings().edit();
        editor.remove(Constants.PREF_IS_LOGGED_IN);

        editor.clear();
        editor.apply();
    }

    public SharedPreferences getSettings(){
        if (preferences!=null){
            return preferences;
        }else {
            preferences = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
            return preferences;
        }
    }


    public String getActiveToken(){
        if (accessToken!=null){
            return accessToken;
        }else {
            accessToken = getSettings().getString(Constants.PREF_ACCESS_TOKEN, null);
            return accessToken;
        }
    }

    public String getActiveUserId(){
        if (userId!=null){
            return userId;
        }else {
            userId = getSettings().getString(Constants.PREF_USER_ID, null);
            return userId;
        }
    }

    public String getActiveUserEmail(){
        if (email!=null){
            return email;
        }else {
            email = getSettings().getString(Constants.PREF_USER_EMAIL, null);
            return email;
        }
    }

    public String getActiveUsername(){
        if (username!=null){
            return username;
        }else {
            username = getSettings().getString(Constants.PREF_USERNAME, null);
            return username;
        }
    }

    public String getActiveName(){
        if (name!=null){
            return name;
        }else {
            name = getSettings().getString(Constants.PREF_NAME, null);
            return name;
        }
    }


    public String getActiveFaceUrl(){
        if (faceUrl!=null){
            return faceUrl;
        }else {
            faceUrl = getSettings().getString(Constants.PREF_FACE_URL, null);
            return faceUrl;
        }
    }

    public String getActiveBackground(){
        if (userBg!=null){
            return userBg;
        }else {
            userBg = getSettings().getString(Constants.PREF_USER_BG, null);
            return userBg;
        }
    }

    public static boolean connectionReachable() {
        Socket socket = null;
        boolean reachable = false;
        try {
            socket = new Socket("google.com", 80);
            reachable = socket.isConnected();
        } catch (UnknownHostException e) {
            Log.w(TAG, "Error connecting to server");
            reachable = false;
        } catch (IOException e) {
            Log.w(TAG, "Error connecting to server");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.w(TAG, "Error closing connecting socket test");
                }
            }
        }
        Log.w(TAG, "Data connectivity change detected, ping test=" + String.valueOf(reachable));
        return reachable;
    }

    public void setAccessToken (String accessToken){
        if (accessToken !=null){
            putParams(Constants.PREF_ACCESS_TOKEN, accessToken);
        }
    }

    public void setLoggedIn(boolean loggedIn) throws Exception {
        if (getActiveToken()!=null)
            putParams("loggedIn", loggedIn);
        else throw new Exception("Access token was null");
    }

    private void putParams(String key, String params){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(key, params);
        editor.apply();
    }

    private void putParams(String key, Boolean params){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(key, params);
        editor.apply();
    }

    public boolean isAuthorized() throws Exception {
        boolean is = getSettings().getBoolean("loggedIn", false);
        String accessToken = getActiveToken();

        if (!TextUtils.isEmpty(accessToken) || is){
            loggedIn = true;
            setLoggedIn(true);
        } else {
            loggedIn = false;
        }

        return loggedIn;
    }

    public void logout() throws Exception {
        setLoggedIn(false);
    }

}


