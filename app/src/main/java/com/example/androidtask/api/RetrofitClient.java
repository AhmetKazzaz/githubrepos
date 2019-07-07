package com.example.androidtask.api;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class that is responsible for providing the retrofit client.
 *
 */
public class RetrofitClient {

    private static final RetrofitClient ourInstance = new RetrofitClient();
    private Retrofit retrofit;
    private final static String BASE_URL = "https://api.github.com/";

    public static RetrofitClient getInstance() {
        return ourInstance;
    }

    private RetrofitClient() {

        //Creating a logging interceptor to view the http requests details in the Logcat
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); //level of logging is specified to BODY
        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .sslSocketFactory(new TLSSocketFactory(), new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    })
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
    }

    /**
     * Endpoints interface to be created as a service for retrofit.
     * @return: Endpoints interface
     */
    public Endpoints endpoints() {
        return retrofit.create(Endpoints.class);
    }
}
