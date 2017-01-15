package com.goldendance.client.course;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.goldendance.client.R;
import com.goldendance.client.base.BaseActivity;

public class CourseActivity extends BaseActivity implements CourseFragment.OnFragmentInteractionListener {
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_course);

        CourseFragment fragment = (CourseFragment) getSupportFragmentManager().findFragmentById(R.id.activity_course);
        if (fragment == null) {
            fragment = CourseFragment.newInstance("", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.activity_course, fragment);
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
