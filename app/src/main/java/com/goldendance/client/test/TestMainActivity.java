package com.goldendance.client.test;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;

import com.goldendance.client.R;
import com.goldendance.client.base.BaseActivity;
import com.goldendance.client.http.GDImageLoader;

import java.util.ArrayList;

public class TestMainActivity extends BaseActivity {

    @Override
    public void initIntent() {
    }

    ArrayList<String> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(2000));
        }
        list.add(GDImageLoader.class.getSimpleName());

        RecyclerView rvTest = (RecyclerView) findViewById(R.id.rvTest);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvTest.setLayoutManager(manager);
        rvTest.setAdapter(new TestMainAdapter(list, this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode().setDuration(2000));
        }
    }
}
