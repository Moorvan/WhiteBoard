package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuechen.whiteboard.DataSource.UserInfoDataSource;
import com.yuechen.whiteboard.Service.LoginService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;


    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private ArrayList<Fragment> fragments;
    private int lastIndex;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserInfoDataSource.initialize(getApplicationContext());
        LoginService.loginVerify("10175101148", "密码");
//        String username = UserInfoDataSource.getUsername();
//        String password = UserInfoDataSource.getPassword();

        toolbar = findViewById(R.id.toolbar);
        initData();
        initBottomNavigation();


        pref = getSharedPreferences("msgSave", MODE_PRIVATE);
        boolean flag = pref.getBoolean("flag", false);
        if(!flag) {
            setFragmentPosition(2);
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        } else {
            setFragmentPosition(0);
        }

    }

    public void initData() {
        setSupportActionBar(toolbar);
        fragments = new ArrayList<>();
        fragments.add(new ToDoListFragment());
        fragments.add(new CalendarFragment());
        fragments.add(new AccountFragment());
    }

    public void initBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_to_do_list:
                    setFragmentPosition(0);
                    toolbar.setTitle("任务清单");
                    break;
                case R.id.menu_calendar:
                    setFragmentPosition(1);
                    toolbar.setTitle("课程日历");
                    break;
                case R.id.menu_me:
                    setFragmentPosition(2);
                    toolbar.setTitle("个人账号");
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragments.get(position);
        Fragment lastFragment = fragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if(!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.ll_frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }
}
