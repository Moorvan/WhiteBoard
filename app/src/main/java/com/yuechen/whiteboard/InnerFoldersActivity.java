package com.yuechen.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class InnerFoldersActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_folders);

        Bundle bundle = this.getIntent().getExtras();
        int id = bundle.getInt("id");
        Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.second_toolbar);
        toolbar.setTitle("课程");
    }
}