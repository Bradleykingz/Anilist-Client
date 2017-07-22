package com.discern.anillist.network;


import com.discern.anillist.common.models.AccessToken;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("/auth/authorize")
    Call<String > login(@Query("grant_type") String grantType,
                             @Query("client_id") String clientId,
                             @Query("redirect_uri") String redirect_uri,
                             @Query("response_type") String response_type);

    @POST("/auth/access_token")
    Call<AccessToken> getAuthCode(@Query("grant_type") String grantType,
                                  @Query("client_id") String clientId,
                                  @Query("client_secret") String clientSecret,
                                  @Query("redirect_uri") String redirect_uri,
                                  @Query("code") String code);
}
