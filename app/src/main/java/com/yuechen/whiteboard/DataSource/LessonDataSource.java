package com.yuechen.whiteboard.DataSource;

import android.content.Context;
import android.util.Log;

import com.yuechen.whiteboard.Database.LessonDbHelper;
import com.yuechen.whiteboard.Model.Lesson;
import com.yuechen.whiteboard.Network.EcnuNetworkService;
import com.yuechen.whiteboard.Network.ResultEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LessonDataSource {
    public static final int WEEK_NUM = 20;
    public static final int DAY_NUM = 5;
    public static List<Lesson>[][] lessonTable = new ArrayList[20][5];
    public static List<LessonObserver> observers = new ArrayList<>();

    // 初始化Table
    static {
        for (int i = 0 ; i < WEEK_NUM; i++) {
            for (int j = 0; j < DAY_NUM; j++) {
                lessonTable[i][j] = new ArrayList<>();
            }
        }
    }

    /**
     * 判断Table是否为空
     * @return 为空则返回 true
     */
    public static boolean isEmpty() {
        for (int i = 0; i < WEEK_NUM; i++) {
            for (int j = 0; j < DAY_NUM; j++) {
                if (!lessonTable[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 清除Table中的所有元素
     */
    private static void clear() {
        for (int i = 0; i < WEEK_NUM; i++) {
            for (int j = 0; j < DAY_NUM; j++) {
                lessonTable[i][j].clear();
            }
        }
    }

    /**
     * @return 返回 Table 中的元素个数，即 Lesson 的个数
     */
    public static int count() {
        int cnt = 0;
        for (int i = 0; i < WEEK_NUM; i++) {
            for (int j = 0; j < DAY_NUM; j++) {
                cnt += lessonTable[i][j].size();
            }
        }
        return cnt;
    }

    public static void subscribe(LessonObserver observer) {
        observers.add(observer);
    }

    public static void readLessons(Context context) {
        LessonDbHelper dbHelper = new LessonDbHelper(context);
        clear();
        for (Lesson lesson : dbHelper.readLessons()) {
            lessonTable[lesson.weekOffset][lesson.dayOffset].add(lesson);
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
                    clear();
                    Log.d("lessonsSize", String.valueOf(fetchedLessons.size()));

                    for (Lesson fetchedLesson : fetchedLessons) {
                        lessonTable[fetchedLesson.weekOffset][fetchedLesson.dayOffset].add(fetchedLesson);
                    }

                    if (fetchedLessons.size() > 0) {
                        dbHelper.insertLessons(fetchedLessons);
                        for (LessonObserver observer : observers) {
                            observer.notifyUpdate();
                        }
                    }

                    // test
                    int cnt = 0;
                    for (int i = 0; i < WEEK_NUM; i++) {
                        for (int j = 0; j < DAY_NUM; j++) {
                            for (int k = 0; k < lessonTable[i][j].size(); k++) {
                                Log.d("lessons", lessonTable[i][j].get(k).toString());
                                cnt++;
                            }
                        }
                    }
                    Log.d("lessonsCount", String.valueOf(cnt));
                }

            }

            @Override
            public void onFailure(Call<ResultEntity<ArrayList<Lesson>>> call, Throwable t) {
                System.out.println(t.toString());
                Log.d("lessons", "FAIL");
            }
        };

        call.enqueue(callback);
    }
}
