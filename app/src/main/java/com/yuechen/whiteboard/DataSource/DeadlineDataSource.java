package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.DeadlineDbHelper;
import com.yuechen.whiteboard.Model.Deadline;
import com.yuechen.whiteboard.Network.EcnuNetworkService;
import com.yuechen.whiteboard.Network.ResultEntity;

import java.time.LocalDateTime;
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
    public static Map<String, List<Deadline>> deadlineCourseMap = new HashMap<>();
    public static Map<String, Deadline> deadlineIdMap = new HashMap<>();
    public static List<DeadlineObserver> observers = new ArrayList<>();

    public static void subscribe(DeadlineObserver observer) {
        observers.add(observer);
    }

    public static void readDeadlines(Context context) {
        DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);
        deadlines.clear();
        deadlineIdMap.clear();
        deadlineCourseMap.clear();
        for (Deadline deadline : dbHelper.readDeadlines()) {
            deadlines.add(deadline);
            deadlineIdMap.put(deadline.id, deadline);
            if (!deadlineCourseMap.containsKey(deadline.calendarID.substring(0, 17))) {
                deadlineCourseMap.put(deadline.calendarID.substring(0, 17), new ArrayList<>());
            }
            deadlineCourseMap.get(deadline.calendarID.substring(0, 17)).add(deadline);
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

        Call<ResultEntity<ArrayList<Deadline>>> call = ecnuNetworkService.getDeadlineList(UserInfoDataSource.getUsername(),
                UserInfoDataSource.getPassword(),
                currentTimestamp,
                timeStampOneYearLater);

        Callback<ResultEntity<ArrayList<Deadline>>> callback = new Callback<ResultEntity<ArrayList<Deadline>>>() {
            @Override
            public void onResponse(Call<ResultEntity<ArrayList<Deadline>>> call, Response<ResultEntity<ArrayList<Deadline>>> response) {
                if (response.isSuccessful() && response.body().code == 0) {
                    ArrayList<Deadline> fetchedDeadlines = response.body().data;
                    DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);

                    for (Deadline fetchedDeadline : fetchedDeadlines) {
                        if (!deadlineIdMap.containsKey(fetchedDeadline.id)) {
                            dbHelper.insertDeadline(fetchedDeadline);
                            newDeadlines.add(fetchedDeadline);
                            deadlines.add(fetchedDeadline);
                            deadlineIdMap.put(fetchedDeadline.id, fetchedDeadline);
                            if (!deadlineCourseMap.containsKey(fetchedDeadline.calendarID.substring(0, 17))) {
                                deadlineCourseMap.put(fetchedDeadline.calendarID.substring(0, 17), new ArrayList<>());
                            }
                            deadlineCourseMap.get(fetchedDeadline.calendarID.substring(0, 17)).add(fetchedDeadline);
                        }
                    }

                    if (newDeadlines.size() > 0) {
                        for (DeadlineObserver observer : observers) {
                            observer.notifyDeadlinesInsert(newDeadlines);
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

    public static long insertDeadline(Context context, String courseId, String title, String endDateTime, String note) {
        DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);
        Deadline deadline = new Deadline();
        deadline.id = LocalDateTime.now().toString();
        deadline.calendarID = courseId;
        deadline.title = title;
        deadline.endDateTime = endDateTime;
        deadline.note = note;
        deadlines.add(deadline);
        deadlineIdMap.put(deadline.id, deadline);
        if (!deadlineCourseMap.containsKey(deadline.calendarID.substring(0, 17))) {
            deadlineCourseMap.put(deadline.calendarID.substring(0, 17), new ArrayList<>());
        }
        deadlineCourseMap.get(deadline.calendarID.substring(0, 17)).add(deadline);
        return dbHelper.insertDeadline(deadline);
    }

    public static long updateDeadline(Context context, String id, boolean finished) {
        for (Deadline deadline : deadlines) {
            if (deadline.id == id) {
                deadline.finished = finished;

                DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);
                return dbHelper.updateDeadline(deadline);
            }
        }

        return -1;
    }

    public static long updateDeadline(Context context, String id, String note, boolean finished) {
        for (Deadline deadline : deadlines) {
            if (deadline.id == id) {
                deadline.note = note;
                deadline.finished = finished;

                DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);
                return dbHelper.updateDeadline(deadline);
            }
        }

        return -1;
    }

    public static void deleteDeadline(Context context, String id) {
        for (Deadline deadline : deadlines) {
            if (deadline.id == id) {
                deadlines.remove(deadline);
                deadlineCourseMap.get(deadline.calendarID.substring(0, 17)).remove(deadline);
                deadlineIdMap.remove(deadline.id);

                DeadlineDbHelper dbHelper = new DeadlineDbHelper(context);
                dbHelper.deleteDeadline(id);
            }
        }
    }
}
