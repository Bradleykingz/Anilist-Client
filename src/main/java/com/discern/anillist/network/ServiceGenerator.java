package com.discern.anillist.network;

import com.discern.anillist.Constants;
import com.discern.anillist.background.BaseApplication;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.Interceptor.Chain;

public class ServiceGenerator {

    public interface OnConnectionTimeoutListener {
        void onConnectionTimeout();
    }

    private static String BASE_URL = Constants.DOMAIN_NAME;
    private static final String CACHE_CONTROL = "Cache-Control";

    private static OnConnectionTimeoutListener listener;

    private static SessionExpiredInterceptor expiredInterceptor = new SessionExpiredInterceptor();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(expiredInterceptor);


    private static Retrofit.Builder getRetrofit(String BASE_URL){
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL);
    }


    public static <S> S forgottenPassword(Class<S> sClass, String email){
        if (email!=null){
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder()
                            .header("accept", "application/json")
                            .method(request.method(), request.body());

                    Request emailRequest = builder.build();

                    return chain.proceed(emailRequest);

                }
            });
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getRetrofit(BASE_URL)
                .client(client)
                .build();

        return  retrofit.create(sClass);
    }


    public static <S> S createService(Class<S> sClass){
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .header("authorization", "basic")
                        .addHeader("accept", "application/json")
                        .build();

                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getRetrofit(BASE_URL)
                .client(client)
                .build();

        return retrofit.create(sClass);
    }

    public static <B> B anonymousQuery(Class<B> bClass, final String token){
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .header("Authorization", token)
                        .header("user-Agent","Soko-Nyumbani App")
                        .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client =  httpClient.build();

        Retrofit retrofit = getRetrofit(BASE_URL)
                .client(client)
                .build();

        return retrofit.create(bClass);
    }


    public static <B> B queryDB(Class<B> bClass, final String token){
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .header("Authorization", "Basic, "+ token)
                        .build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client =  httpClient.build();

        Retrofit retrofit = getRetrofit(BASE_URL)
                .client(client)
                .build();

        return retrofit.create(bClass);
    }

    public static <S> S loginService(Class<S> sClass){
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder()
                            .header("Accept", "application/JSON")
                            .header("Authorization", "Basic")
                            .method(request.method(), request.body());

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getRetrofit(BASE_URL)
                .client(client)
                .build();


        return retrofit.create(sClass);
    }


    public static <S> S registerService(Class<S> serviceClass){
//            String credentials = username + ':' + password;
//            final String basic = "Basic" + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder builder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", "basic")
                            .method(original.method(), original.body());

                    Request request = builder.build();
                    return chain.proceed(request);
                }
            });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getRetrofit(BASE_URL)
                .client(client)
                .build();

        return  retrofit.create(serviceClass);
    }

    private Response onInterceptChain(Chain chain)throws IOException {
        try{
            Request request = chain.request();
            Response response = chain.proceed(request);
            String content = "fff";
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (listener!=null){
                listener.onConnectionTimeout();
            }
            return chain.proceed(chain.request());
        }
    }

    private static Cache getCache() {
        Cache cache = null;
        try {
            cache = new Cache( new File( BaseApplication.getInstance().getCacheDir(), "http-cache" ),
                    10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e) {
            FirebaseCrash.report(e);
        }
        return cache;
    }

    private static Interceptor getCacheInterceptor () {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Response response = chain.proceed( chain.request() );

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(10, TimeUnit.MINUTES )
                        .build();

                return response.newBuilder()
                        .header( CACHE_CONTROL, cacheControl.toString() )
                        .build();
            }
        };
    }

    private static Interceptor getOfflineCacheInterceptor() {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();

                if (!BaseApplication.isInternetConnected()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(10, TimeUnit.MINUTES)
                            .build();

                    request = request.newBuilder()
                            .cacheControl( cacheControl )
                            .build();
                }

                return chain.proceed( request );
            }
        };
    }

    private static OkHttpClient getOkHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(getOfflineCacheInterceptor())
                .addNetworkInterceptor(getCacheInterceptor())
                .cache(getCache())
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(expiredInterceptor)
                .build();

    }

}
