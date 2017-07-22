package com.discern.anillist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.discern.anillist.background.BaseApplication;
import com.discern.anillist.common.models.AccessToken;
import com.discern.anillist.common.utils.MessageUtils;
import com.discern.anillist.network.APIService;
import com.discern.anillist.network.ServiceGenerator;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private String clientId;
    private String clientSecret;
    private TextView textView;
    private String authCode;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        clientId = getString(R.string.client_id);
        clientSecret = getString(R.string.client_secret);
        textView = (TextView) findViewById(R.id.d);


        if (!isAuthorized()){
            Log.d("Main", "Not authorized");
            Log.d("Main", "Proceeding with code: " + authCode);
            getAuthorizationCode();
        }
    }

    private void getAuthorizationCode() {
        Log.d("Main", "getting auth token");
        APIService apiService = ServiceGenerator.createService(APIService.class);
        retrofit2.Call<AccessToken> tokenCall = apiService.getAuthCode("authorization_code", clientId, clientSecret, "anilist://main.com", authCode);
        Log.d("Main", tokenCall.request().url().encodedPath());
        tokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.d("Main", "Response came. Code: " + response.code());
                if (response.isSuccessful()){
                    Log.d("Main", "Got token: " + response.body().getAccessToken());
                    if (response.body()!=null){
                        MessageUtils.toastMessage(context, "Got code: "+response.body().getAccessToken());
                        BaseApplication.getInstance().setAccessToken(response.body().getAccessToken());
                    } else Log.d("Main", "Empty body : " + new Gson().toJson(response.body()));
                } else Log.d("Main", "Request unsuccessful. Response code " + response.code());
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.d("Main", "Call Failed", t);

            }
        });
    }

    private boolean isAuthorized() {
        boolean is = false;
        try {
            is = BaseApplication.getInstance().isAuthorized();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }
}
