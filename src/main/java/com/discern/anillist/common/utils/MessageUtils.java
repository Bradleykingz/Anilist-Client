package com.discern.anillist.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.discern.anillist.R;


public class MessageUtils {

    protected MessageUtils(){

    }
        public static void toastAuthError(Context context){
            Toast.makeText(context, R.string.auth_errors, Toast.LENGTH_SHORT).show();
        }

        public static void toastError(Context context){
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
        }

        public static void toastFakeUser(Context context){
            Toast.makeText(context, R.string.fake_user, Toast.LENGTH_SHORT).show();
        }
        public static void toastWrongCredentials(Context context){
            Toast.makeText(context, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
        }

        public static void toastOffline(Context context){
            Toast.makeText(context, R.string.offline_error, Toast.LENGTH_LONG).show();
        }

        public static void toastSuccess(Context context){
            Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show();
        }

        public static void toastNullServer(Context context){
            Toast.makeText(context, R.string.null_server, Toast.LENGTH_SHORT).show();
        }

        public static void toastLocalisedMessage(Context context, Throwable throwable){
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        public static void toastMessage(Context context, String message){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }

