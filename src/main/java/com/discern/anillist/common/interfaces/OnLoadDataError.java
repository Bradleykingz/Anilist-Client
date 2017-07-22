package com.discern.anillist.common.interfaces;

import retrofit2.Call;

public interface OnLoadDataError  {
    void onError(Call call, Throwable throwable, boolean offline);
}
