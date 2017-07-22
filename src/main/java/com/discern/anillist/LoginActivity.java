package com.discern.anillist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.discern.anillist.background.BaseApplication;

import butterknife.BindString;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    TextInputLayout username;

    @BindView(R.id.password)
    TextInputLayout password;

    @BindView(R.id.login)
    Button login;

    Button anilistLogin;

    String clientId;

    @BindString(R.string.client_secret)
    String clientSecret;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_login);


        context = this;

        clientId = getString(R.string.client_id);

        anilistLogin = (Button) findViewById(R.id.loginAnilist);
        anilistLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFragment();
            }
        });
    }

    private void launchFragment(){
        if (BaseApplication.isInternetConnected()){
            Bundle bundle = new Bundle();
            bundle.putString("url", "https://anilist.co/api/auth/authorize?grant_type=authorization_code&client_id=" + clientId + "&response_type=code&redirect_uri=" + getString(R.string.redirect_uri));

            WebViewFrag frag = WebViewFrag.newInstance(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, frag)
                    .addToBackStack(null)
                    .commit();
        } else {
            Snackbar.make(anilistLogin.getRootView(), "You are offline", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launchFragment();
                        }
                    }).show();
        }
    }
}
