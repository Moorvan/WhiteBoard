package com.yuechen.whiteboard.Service;

import android.util.Log;

import com.yuechen.whiteboard.DataSource.UserInfoDataSource;
import com.yuechen.whiteboard.Network.EcnuNetworkService;
import com.yuechen.whiteboard.Network.ResultEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginService {
    public static List<LoginServiceObserver> observers = new ArrayList<>();

    public static void subscribe(LoginServiceObserver observer) {
        observers.add(observer);
    }

    // 验证成功后会自动保存至 UserInfoDataSource。
    public static void loginVerify(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://application.jjaychen.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EcnuNetworkService ecnuNetworkService = retrofit.create(EcnuNetworkService.class);

        Call<ResultEntity<Boolean>> call = ecnuNetworkService.loginVerify(username, password);

        Callback<ResultEntity<Boolean>> callback = new Callback<ResultEntity<Boolean>>() {
            @Override
            public void onResponse(Call<ResultEntity<Boolean>> call, Response<ResultEntity<Boolean>> response) {
                if (response.isSuccessful()) {
                    Boolean loginVerifyResult = response.body().data;

                    UserInfoDataSource.setUsername(username);
                    UserInfoDataSource.setPassword(password);
                    Log.v("登录状况", username + ": 验证登录成功");
                    for (LoginServiceObserver observer : observers) {
                        observer.notifyLoginVerifyResult(loginVerifyResult, response.body().message);
                    }
                } else {
                    Log.v("登录状况", username + ": 验证登录失败");
                    for (LoginServiceObserver observer : observers) {
                        observer.notifyLoginVerifyResult(false, "服务器响应失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity<Boolean>> call, Throwable t) {
                System.out.println(t.toString());
                Log.v("登录状况", username + ": 验证登录失败");
                for (LoginServiceObserver observer : observers) {
                    observer.notifyLoginVerifyResult(false, "发送请求失败");
                }
            }
        };

        call.enqueue(callback);
    }
}
