package com.yuechen.whiteboard.Network;

import com.yuechen.whiteboard.Model.Course;
import com.yuechen.whiteboard.Model.Deadline;
import com.yuechen.whiteboard.Model.Lesson;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EcnuNetworkService {
    /**
     * Get semester dates.
     *
     * @return
     */
    @GET("/ecnu-service/semester-dates")
    Call<ResultEntity<Map<String, Map<String, String>>>> getSemesterDates();

    /**
     * Get course list for the specific semester.
     *
     * @param username      Username.
     * @param password      Password.
     * @param year          2019: 2019 - 2020 学年
     * @param semesterIndex 1: 第一学期, 2: 第二学期, 3: 暑期小学期
     * @return
     */
    @GET("/ecnu-service/course-list")
    Call<ResultEntity<ArrayList<Course>>> getCourseList(@Query("username") String username,
                                                        @Query("password") String password,
                                                        @Query("year") long year,
                                                        @Query("semesterIndex") long semesterIndex);

    /**
     * Get Lesson list for the specific semester.
     *
     * @param username      Username.
     * @param password      Password.
     * @param year          2019: 2019 - 2020 学年
     * @param semesterIndex 1: 第一学期, 2: 第二学期, 3: 暑期小学期
     * @return
     */
    @GET("/ecnu-service/lesson-list")
    Call<ResultEntity<ArrayList<Lesson>>> getLessonList(@Query("username") String username,
                                                        @Query("password") String password,
                                                        @Query("year") long year,
                                                        @Query("semesterIndex") long semesterIndex);


    /**
     * Get deadline list from elearning.ecnu.edu.cn in the specific time range.
     *
     * @param username       Username.
     * @param password       Password.
     * @param startTimestamp Start timestamp, e.g: 1587830400000
     * @param endTimestamp   End timestamp, e.g: 1591459200000
     * @return
     */
    @GET("/ecnu-service/deadline-list")
    Call<ResultEntity<ArrayList<Deadline>>> getDeadlineList(@Query("username") String username,
                                                            @Query("password") String password,
                                                            @Query("startTimestamp") long startTimestamp,
                                                            @Query("endTimestamp") long endTimestamp);

    //   大部分国内安卓系统不支持 webcal 协议，还未解决适配问题。
//    /**
//     * Get deadline calendar subscription url.
//     *
//     * @param username Username.
//     * @param password Password.
//     * @return
//     */
//    @GET("/ecnu-service/deadline-calendar")
//    Call<ResultEntity> getDeadlineCalendarURL(@Query("username") String username,
//                                              @Query("password") String password);

    // 还未定义对应的 Result
//    /**
//     * Get Course calendar for the specific semester.
//     *
//     * @param username      Username.
//     * @param password      Password.
//     * @param year          2019: 2019 - 2020 学年
//     * @param semesterIndex 1: 第一学期, 2: 第二学期, 3: 暑期小学期
//     * @return
//     */
//    @GET("/ecnu-service/course-calendar")
//    Call<ResultEntity> getCourseCalendarURL(@Query("username") String username,
//                                            @Query("password") String password,
//                                            @Query("year") long year,
//                                            @Query("semesterIndex") long semesterIndex);
}
