package com.discern.anillist.network;


import com.discern.anillist.common.interfaces.InterceptedRequestHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Connect this interceptor to okhttp3 line to intercept all 401 answers from service and get callback
 */

public final class SessionExpiredInterceptor implements Interceptor {
    InterceptedRequestHandler listener;

    public void setListener(InterceptedRequestHandler listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) { // got "session expired" answer, call the handler
            if (listener != null) {
                listener.onIntercept(request, response);
            }
        }
        return response;
    }
}