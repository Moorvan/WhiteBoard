package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.CourseDbHelper;
import com.yuechen.whiteboard.Model.Course;
import com.yuechen.whiteboard.Network.EcnuNetworkService;
import com.yuechen.whiteboard.Network.ResultEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseDataSource {
    public static List<Course> courses = new ArrayList<>();
    public static List<CourseObserver> observers = new ArrayList<>();

    public static void subscribe(CourseObserver observer) {
        observers.add(observer);
    }

    public static void readCourses(Context context) {
        CourseDbHelper dbHelper = new CourseDbHelper(context);
        courses.clear();
        for (Course course : dbHelper.readCourses()) {
            courses.add(course);
        }
    }

    public static void fetchCourses(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://application.jjaychen.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EcnuNetworkService ecnuNetworkService = retrofit.create(EcnuNetworkService.class);

        Call<ResultEntity<ArrayList<Course>>> call = ecnuNetworkService.getCourseList(UserInfoDataSource.getUsername(),
                UserInfoDataSource.getPassword(),
                UserInfoDataSource.getYear(),
                UserInfoDataSource.getSemesterIndex());

        Callback<ResultEntity<ArrayList<Course>>> callback = new Callback<ResultEntity<ArrayList<Course>>>() {
            @Override
            public void onResponse(Call<ResultEntity<ArrayList<Course>>> call, Response<ResultEntity<ArrayList<Course>>> response) {
                if (response.isSuccessful() && response.body().code == 0) {
                    ArrayList<Course> fetchedCourses = response.body().data;
                    CourseDbHelper dbHelper = new CourseDbHelper(context);
                    dbHelper.clearCourses();
                    courses.clear();

                    for (Course fetchedCourse : fetchedCourses) {
                        courses.add(fetchedCourse);
                    }

                    if (fetchedCourses.size() > 0) {
                        dbHelper.insertCourses(fetchedCourses);
                        for (CourseObserver observer : observers) {
                            observer.notifyUpdate(fetchedCourses);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity<ArrayList<Course>>> call, Throwable t) {
                System.out.println(t.toString());
            }
        };

        call.enqueue(callback);
    }
}
