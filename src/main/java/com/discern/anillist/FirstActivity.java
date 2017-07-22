package com.discern.anillist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.discern.anillist.background.BaseApplication;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.Visibility;

import static com.discern.anillist.background.BaseApplication.isInternetConnected;

public class FirstActivity extends AppCompatActivity implements Constants {

    private static final String TAG = FirstActivity.class.getSimpleName();

    private ViewGroup logoView;
    private ViewGroup offlineView;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        context = this;

        logoView = (ViewGroup)findViewById(R.id.logo_view);
        offlineView = (ViewGroup)findViewById(R.id.offline_view);
        Button retryButton = (Button) findViewById(R.id.retryButton);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncRun();
            }
        });

        asyncRun();
    }

    private void asyncRun(){
        TransitionManager.beginDelayedTransition(offlineView, new Fade(Visibility.MODE_OUT));
        offlineView.setVisibility(offlineView.isShown()? View.GONE: View.GONE);

        TransitionManager.beginDelayedTransition(logoView, new Fade(Visibility.MODE_IN));
        logoView.setVisibility(logoView.isShown()? View.VISIBLE: View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initActivity();
            }
        },1500);
    }

    private void initActivity() {
        if(isInternetConnected()){
            if (isLoggedIn()){
                Log.d("FirstActivity", "Started mainActivity");
                Intent mainActivity = new Intent(context, MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                finish();
            }else {
                Log.d("FirstActivity", "Started LoginActivity");
                startActivity(new Intent(context, LoginActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                finish();
            }
        }else {
            TransitionManager.beginDelayedTransition(logoView, new Fade(Fade.OUT));
            logoView.setVisibility(logoView.isShown()? View.GONE: View.GONE);

            TransitionManager.beginDelayedTransition(offlineView, new Fade(Fade.IN));
            offlineView.setVisibility(offlineView.isShown()? View.VISIBLE: View.VISIBLE);

        }
    }


    public boolean isLoggedIn(){
        boolean status = false;
        try {
            status = BaseApplication.getInstance().isAuthorized();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
