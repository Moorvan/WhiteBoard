package com.yuechen.whiteboard.DataSource;

import android.content.Context;

import com.yuechen.whiteboard.Database.UserInfoDbHelper;

import java.util.Map;

public class UserInfoDataSource {
    private static String username = "";
    private static String password = "";
    private static long year = 0;
    private static long semesterIndex = 0;
    private static UserInfoDbHelper sharedDbHelper = null;

    /**
     * App 启动后必须先调用，否则无法执行任何数据操作。
     *
     * @param context
     */
    public static void initialize(Context context) {
        sharedDbHelper = new UserInfoDbHelper(context);
        readUserInfo();
    }

    private static void readUserInfo() {
        Map<String, String> userInfo = sharedDbHelper.readUserInfo();
        if (userInfo.containsKey("username")) username = userInfo.get("username");
        if (userInfo.containsKey("password")) password = userInfo.get("password");
        if (userInfo.containsKey("year")) year = Long.parseLong(userInfo.get("year"));
        if (userInfo.containsKey("semesterIndex"))
            semesterIndex = Long.parseLong(userInfo.get("semesterIndex"));

    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static long getYear() {
        return year;
    }

    public static long getSemesterIndex() {
        return semesterIndex;
    }

    // 如果你不确定用户输入的账号密码是否正确，请不要调用。
    // 请通过 LoginService.loginVerify 来验证，验证通过会自动保存。
    public static void setUsername(String username) {
        UserInfoDataSource.username = username;
        sharedDbHelper.updateUserInfo();
    }

    // 如果你不确定用户输入的账号密码是否正确，请不要调用。
    // 请通过 LoginService.loginVerify 来验证，验证通过会自动保存。
    public static void setPassword(String password) {
        UserInfoDataSource.password = password;
        sharedDbHelper.updateUserInfo();
    }

    public static void setYear(long year) {
        UserInfoDataSource.year = year;
        sharedDbHelper.updateUserInfo();
    }

    public static void setSemesterIndex(long semesterIndex) {
        UserInfoDataSource.semesterIndex = semesterIndex;
        sharedDbHelper.updateUserInfo();
    }
}
