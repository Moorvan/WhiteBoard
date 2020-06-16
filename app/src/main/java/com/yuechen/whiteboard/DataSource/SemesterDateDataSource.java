package com.yuechen.whiteboard.DataSource;

import android.content.Context;
import android.util.Log;

import com.yuechen.whiteboard.Database.SemesterDateDbHelper;
import com.yuechen.whiteboard.Network.EcnuNetworkService;
import com.yuechen.whiteboard.Network.ResultEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SemesterDateDataSource {
    public static Map<String, Map<String, String>> semesterDates = new HashMap<>();
    public static List<SemesterDateObserver> observers = new ArrayList<>();

    public static void subscribe(SemesterDateObserver observer) {
        observers.add(observer);
    }

    public static void readSemesterDates(Context context) {
        SemesterDateDbHelper dbHelper = new SemesterDateDbHelper(context);
        semesterDates.clear();
        semesterDates = dbHelper.readSemesterDates();
        Log.d("lessonsStart", String.valueOf(semesterDates.size()));
    }

    public static void fetchSemesterDates(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://application.jjaychen.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EcnuNetworkService ecnuNetworkService = retrofit.create(EcnuNetworkService.class);

        Call<ResultEntity<Map<String, Map<String, String>>>> call = ecnuNetworkService.getSemesterDates();

        Callback<ResultEntity<Map<String, Map<String, String>>>> callback = new Callback<ResultEntity<Map<String, Map<String, String>>>>() {
            @Override
            public void onResponse(Call<ResultEntity<Map<String, Map<String, String>>>> call, Response<ResultEntity<Map<String, Map<String, String>>>> response) {
                if (response.isSuccessful() && response.body().code == 0) {
                    Map<String, Map<String, String>> fetchedSemesterDates = response.body().data;
                    SemesterDateDbHelper dbHelper = new SemesterDateDbHelper(context);
                    if (fetchedSemesterDates.size() > 0) {
                        dbHelper.clearSemesterDates();
                        
                        semesterDates.clear();
                        semesterDates = fetchedSemesterDates;

                        dbHelper.insertSemesterDates(fetchedSemesterDates);
                        for (SemesterDateObserver observer : observers) {
                            observer.notifySemesterDatesUpdate();
                        }
                    }
                    Log.d("lessonsStart", String.valueOf(semesterDates.size()));
                }
            }

            @Override
            public void onFailure(Call<ResultEntity<Map<String, Map<String, String>>>> call, Throwable t) {
                System.out.println(t.toString());
            }
        };

        call.enqueue(callback);
    }
}
