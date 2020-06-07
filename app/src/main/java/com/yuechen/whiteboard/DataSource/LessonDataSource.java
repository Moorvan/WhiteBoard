package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.LessonDbHelper;
import com.yuechen.whiteboard.Model.Lesson;
import com.yuechen.whiteboard.Network.EcnuNetworkService;
import com.yuechen.whiteboard.Network.ResultEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonDataSource {
    public static List<Lesson> lessons = new ArrayList<>();
    public static List<LessonObserver> observers = new ArrayList<>();

    public static void subscribe(LessonObserver observer) {
        observers.add(observer);
    }

    public static void readLessons(Context context) {
        LessonDbHelper dbHelper = new LessonDbHelper(context);
        lessons.clear();
        for (Lesson lesson : dbHelper.readLessons()) {
            lessons.add(lesson);
        }
    }

    public static void fetchLessons(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://application.jjaychen.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EcnuNetworkService ecnuNetworkService = retrofit.create(EcnuNetworkService.class);

        Call<ResultEntity<ArrayList<Lesson>>> call = ecnuNetworkService.getLessonList(UserInfoDataSource.username,
                UserInfoDataSource.password,
                UserInfoDataSource.year,
                UserInfoDataSource.semesterIndex);

        Callback<ResultEntity<ArrayList<Lesson>>> callback = new Callback<ResultEntity<ArrayList<Lesson>>>() {
            @Override
            public void onResponse(Call<ResultEntity<ArrayList<Lesson>>> call, Response<ResultEntity<ArrayList<Lesson>>> response) {
                if (response.isSuccessful() && response.body().code == 0) {
                    ArrayList<Lesson> fetchedLessons = response.body().data;
                    LessonDbHelper dbHelper = new LessonDbHelper(context);
                    dbHelper.clearLessons();
                    lessons.clear();

                    for (Lesson fetchedLesson : fetchedLessons) {
                        lessons.add(fetchedLesson);
                    }

                    if (fetchedLessons.size() > 0) {
                        dbHelper.insertLessons(fetchedLessons);
                        for (LessonObserver observer : observers) {
                            observer.notifyUpdate();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity<ArrayList<Lesson>>> call, Throwable t) {
                System.out.println(t.toString());
            }
        };

        call.enqueue(callback);
    }
}
