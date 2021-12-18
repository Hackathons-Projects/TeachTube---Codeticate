package com.hackathon.teachtube.Classes;

import android.util.Log;

import com.google.gson.Gson;
import com.hackathon.teachtube.BuildConfig;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String TAG = "RetrofitInstance";
    private static Retrofit retrofit;

    public static Retrofit getInstance(String BASE_URL) {
        Gson gson = new Gson().newBuilder().serializeNulls().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String s) {
                Log.d(TAG, "log: ");
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            client.addInterceptor(httpLoggingInterceptor);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();

        return retrofit;
    }

    /*@SuppressLint("StaticFieldLeak")
    public static void sendMessage(String token, String title, String body , String name , String image) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Call<ResponseBody> commonResponseModelCall = RetrofitInstance.getInstance(AllKeyUrls.BASE_URL)
                        .create(PushNotificationInterface.class)
                        .sendMessage(token, title, body , image , name);
                commonResponseModelCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                        } else {
                            try {
                                String errorString = response.errorBody().string();
                                Log.d(TAG, "onResponse error: " + errorString);
                                switch (response.code()) {
                                    case 400: {
                                        assert response.errorBody() != null;
                                    }
                                    break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.toString());
                        //MainLoader.Loader(false, findViewById(R.id.LL_loader));
                    }
                });
                return null;
            }
        }.execute();
    }*/

}
