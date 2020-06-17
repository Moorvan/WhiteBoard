package com.yuechen.whiteboard;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.*;
import com.yuechen.whiteboard.Adapter.LessonAdapter;
import com.yuechen.whiteboard.DataSource.LessonDataSource;
import com.yuechen.whiteboard.DataSource.LessonObserver;
import com.yuechen.whiteboard.DataSource.SemesterDateDataSource;
import com.yuechen.whiteboard.DataSource.SemesterDateObserver;
import com.yuechen.whiteboard.DataSource.UserInfoDataSource;
import com.yuechen.whiteboard.Model.Lesson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment
        implements View.OnClickListener,
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        LessonObserver, SemesterDateObserver {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;

    private LocalDate startDate;

    private LessonAdapter lessonAdapter;

    private List<Lesson> lessons;



    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);

    }

    private void initView() {
        mTextMonthDay = getView().findViewById(R.id.tv_month_day);
        mTextYear = getView().findViewById(R.id.tv_year);
        mTextLunar = getView().findViewById(R.id.tv_lunar);
        mRelativeTool = getView().findViewById(R.id.rl_tool);
        mCalendarView = getView().findViewById(R.id.calendarView);
        mTextCurrentDay = getView().findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mYear == 0) {
                    mYear = mCalendarView.getCurYear();
                }
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        getView().findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mCalendarLayout = getView().findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
//        mRecyclerView = getView().findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new GroupItemDecoration<String,Article>());
//        mRecyclerView.setAdapter(new ArticleAdapter(this));
//        mRecyclerView.notifyDataSetChanged();

    }

    private void initData() {

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
//        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
//                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
//        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
//                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
//        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
//                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
//        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
//                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
//        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
//                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
//        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
//                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
//        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
//                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
//        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
//                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
//        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
//                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);


        UserInfoDataSource.initialize(getContext());
        UserInfoDataSource.setUsername("10175101152");
        UserInfoDataSource.setPassword("Thwf1858");
        UserInfoDataSource.setYear(2019);
        UserInfoDataSource.setSemesterIndex(2);
        LessonDataSource.subscribe(this);
        SemesterDateDataSource.subscribe(this);
        if (SemesterDateDataSource.semesterDates.isEmpty()) {
            SemesterDateDataSource.readSemesterDates(getContext());
            if (SemesterDateDataSource.semesterDates.isEmpty()) {
                SemesterDateDataSource.fetchSemesterDates(getContext());
            } else {
                initStartDate();
            }
        }
        // 判断需不需要从数据库读
        if (LessonDataSource.isEmpty()) {
            LessonDataSource.readLessons(getContext());
            Log.d("lessonsRead", String.valueOf(LessonDataSource.count()));
            // 若数据库中没有，fetch
            if (LessonDataSource.isEmpty()) {
                LessonDataSource.fetchLessons(getContext());
            } else {
                initCurrentLessons();
            }
        }
    }

    // 初始化学期开始日期
    private void initStartDate() {
        String year = String.valueOf(UserInfoDataSource.getYear());
        String semester = String.valueOf(UserInfoDataSource.getSemesterIndex());
        Map<String, String> semesterDate = SemesterDateDataSource.semesterDates.get(year);
        if (semesterDate != null) {
            String date = semesterDate.get(semester);
            if (date != null) {
                startDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }
        Log.d("lessonsStart", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    // 初始化当前日期的课程
    private void initCurrentLessons() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int day = mCalendarView.getCurDay();
        setCurrentLessons(year, month, day);
    }


    // 更新选中日期的课程
    private void setCurrentLessons(int year, int month, int day) {
        LocalDate currentDate = LocalDate.of(year, month, day);
        long duration = ChronoUnit.DAYS.between(startDate, currentDate);
        int weekOffset = (int) duration / 7;
        Log.d("lessonsDuration", String.valueOf(duration));
        int weekDay = currentDate.getDayOfWeek().getValue() - 1;
        if (weekOffset >= 0 && weekOffset < 20 && weekDay >= 0 && weekDay < 5) {
            lessons = LessonDataSource.lessonTable[weekOffset][weekDay];
            Log.d("lessons", LessonDataSource.lessonTable[weekOffset][weekDay].toString());
            lessons.sort((l1, l2) ->
                    (Integer.compare(l1.startTimeOffset, l2.startTimeOffset))
            );
            Log.d("lessonsInflator", String.valueOf(lessons.size()));
            lessonAdapter = new LessonAdapter(getContext(), R.layout.lesson_item_layout, lessons);
        } else {
            lessons.clear();
            lessonAdapter = new LessonAdapter(getContext(), R.layout.lesson_item_layout, lessons);
        }
        ListViewScroll listView = (ListViewScroll) getView().findViewById(R.id.lesson_list);
        listView.setAdapter(lessonAdapter);
    }

    @Override
    public void notifySemesterDatesUpdate() {
        initStartDate();
    }

    @Override
    public void notifyLessonsUpdate() {
        initCurrentLessons();
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        setCurrentLessons(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }
}
