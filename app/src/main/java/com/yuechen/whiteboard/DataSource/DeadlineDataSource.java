package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.DeadlineDbHelper;
import com.yuechen.whiteboard.Model.Deadline;
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

public class DeadlineDataSource {
    public static List<Deadline> deadlines = new ArrayList<>();
    public static Map<String, Deadline> deadlineMap = new HashMap<>();
    public static List<DeadlineObserver> observers = new ArrayList<>();

    public static void subscribe(DeadlineObserver observer) {
        observers.add(observer);
    }

    public static void readDeadlines(Context context) {
        DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);
        deadlines.clear();
        for (Deadline deadline : dbHelper.readDeadlines()) {
            deadlines.add(deadline);
            deadlineMap.put(deadline.id, deadline);
        }
    }

    public static void fetchNewDeadlines(Context context) {
        List<Deadline> newDeadlines = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://application.jjaychen.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EcnuNetworkService ecnuNetworkService = retrofit.create(EcnuNetworkService.class);
        long currentTimestamp = System.currentTimeMillis();
        long timeStampOneYearLater = currentTimestamp + 31536000000L; // 获取从现在起往后一年的时间的所有 Deadline

        Call<ResultEntity<ArrayList<Deadline>>> call = ecnuNetworkService.getDeadlineList(UserInfoDataSource.username,
                UserInfoDataSource.password,
                currentTimestamp,
                timeStampOneYearLater);

        Callback<ResultEntity<ArrayList<Deadline>>> callback = new Callback<ResultEntity<ArrayList<Deadline>>>() {
            @Override
            public void onResponse(Call<ResultEntity<ArrayList<Deadline>>> call, Response<ResultEntity<ArrayList<Deadline>>> response) {
                if (response.isSuccessful() && response.body().code == 0) {
                    ArrayList<Deadline> fetchedDeadlines = response.body().data;
                    DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);

                    for (Deadline fetchedDeadline : fetchedDeadlines) {
                        if (!deadlineMap.containsKey(fetchedDeadline.id)) {
                            dbHelper.insertDeadline(fetchedDeadline);
                            newDeadlines.add(fetchedDeadline);
                            deadlines.add(fetchedDeadline);
                            deadlineMap.put(fetchedDeadline.id, fetchedDeadline);
                        }
                    }

                    if (newDeadlines.size() > 0) {
                        for (DeadlineObserver observer : observers) {
                            observer.notifyInsert(newDeadlines);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity<ArrayList<Deadline>>> call, Throwable t) {
                System.out.println(t.toString());
            }
        };

        call.enqueue(callback);
    }
}