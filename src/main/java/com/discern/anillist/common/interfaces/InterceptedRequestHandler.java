package com.discern.anillist.common.interfaces;

import okhttp3.Request;
import okhttp3.Response;

public interface InterceptedRequestHandler {
    void onIntercept(Request request, Response response);
}
