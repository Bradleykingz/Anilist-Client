package com.discern.anillist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.discern.anillist.background.BaseApplication;
import com.discern.anillist.common.models.AccessToken;

public class WebViewFrag extends Fragment {

    public static WebViewFrag newInstance(@Nullable Bundle args) {
        WebViewFrag fragment = new WebViewFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);
        WebView wv = (WebView) view;

        Log.d("WebDrag", getArguments().getString("url"));

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowContentAccess(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int progress){
                getActivity().setProgress(progress * 100);
            }
        });
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon){
                getActivity().setTitle(url);
            }

            @Override
            public void onPageFinished(WebView webView, String url){
                super.onPageFinished(webView, url);

                if (url.contains("?code=")){
                    Uri uri = Uri.parse(url);
                    String authCode = uri.getQueryParameter("code");
                    AccessToken accessToken = new AccessToken();
                    accessToken.setAccessToken(authCode);

                    BaseApplication.getInstance().setAccessToken(authCode);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }else {
                    Log.d("WebFrag", "Auth failed");
                }
            }
        });

        wv.loadUrl(getArguments().getString("url"));

        wv.getUrl();
        return view;
    }

}
