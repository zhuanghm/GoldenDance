package com.goldendance.client.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goldendance.client.R;
import com.goldendance.client.bean.DataResultBean;
import com.goldendance.client.bean.User;
import com.goldendance.client.card.CardActivity;
import com.goldendance.client.course.CourseActivity;
import com.goldendance.client.http.GDHttpManager;
import com.goldendance.client.http.GDImageLoader;
import com.goldendance.client.http.GDOnResponseHandler;
import com.goldendance.client.login.LoginActivity;
import com.goldendance.client.model.SettingModel;
import com.goldendance.client.userinfo.UserInfoActivity;
import com.goldendance.client.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView ivAvatar;
    private ImageView ivHead;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(this);

        //四大板块

        view.findViewById(R.id.tvAdultCourse).setOnClickListener(this);
        view.findViewById(R.id.tvChildClass).setOnClickListener(this);
        view.findViewById(R.id.tvVIP).setOnClickListener(this);
        view.findViewById(R.id.tvFavirote).setOnClickListener(this);


        setUserInfo();

        ivHead = (ImageView) view.findViewById(R.id.ivHead);
        getHomeHeadImage();
    }

    private void getHomeHeadImage() {
        new SettingModel().getHomeHead(new GDOnResponseHandler() {
            @Override
            public void onSuccess(int code, String json) {
                super.onSuccess(code, json);
                if (GDHttpManager.CODE200 != code) {
                    return;
                }
                DataResultBean<String> resultBean = JsonUtils.fromJson(json, new TypeToken<DataResultBean<String>>() {
                });
                if (resultBean == null) {
                    return;
                }

                int code1 = resultBean.getCode();
                if (GDHttpManager.CODE200 != code1) {
                    return;
                }

                String data = resultBean.getData();

                if (!TextUtils.isEmpty(data))
                    GDImageLoader.setImageView(getActivity(), data, ivHead);
            }
        });
    }

    public void setUserInfo() {
        if (ivAvatar != null)
            GDImageLoader.setImageView(getActivity(), User.icon, ivAvatar);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivAvatar:
                gotoLogin();
                break;
            case R.id.tvChildClass:
                gotoCourse(CourseActivity.TYPE_COURSE_CHILD);
                break;
            case R.id.tvFavirote:
                gotoCourse(CourseActivity.TYPE_COURSE_INTEREST);
                break;
            case R.id.tvVIP:
//                gotoCourse();
                Intent intent = new Intent(getActivity(), CardActivity.class);
                startActivity(intent);
                break;
            case R.id.tvAdultCourse:
                gotoCourse(CourseActivity.TYPE_COURSE_ADULT);
                break;
        }
    }

    private static final int REQUEST_LOGIN = 1000;

    private void gotoLogin() {
        Intent intent = new Intent();
        if (TextUtils.isEmpty(User.tokenid)) {
            intent.setClass(getActivity(), LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            intent.setClass(getActivity(), UserInfoActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    if (getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).getUserInfo();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void gotoCourse(String type) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CourseActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
