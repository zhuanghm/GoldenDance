package com.goldendance.client.userinfo;

import android.text.TextUtils;

import com.goldendance.client.bean.DataResultBean;
import com.goldendance.client.bean.MessageBean;
import com.goldendance.client.bean.User;
import com.goldendance.client.http.GDHttpManager;
import com.goldendance.client.http.GDOnResponseHandler;
import com.goldendance.client.model.IUserModel;
import com.goldendance.client.model.UserModel;
import com.goldendance.client.utils.GDLogUtils;
import com.goldendance.client.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

/**
 * Created by hemingway on 2017/1/17.
 */

public class UpdateUserInfoPresenter implements IUpdateUserInfoContract.IPresenter {
    private static final String TAG = UpdateUserInfoPresenter.class.getSimpleName();
    IUpdateUserInfoContract.IView mView;
    IUserModel mModel;

    public UpdateUserInfoPresenter(IUpdateUserInfoContract.IView mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
        mModel = new UserModel();
    }

    @Override
    public void onStart() {

    }


    @Override
    public void confirm() {
        String action = mView.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case UserInfoActivity.ACTION_GENDER:
                updateGender();
                break;
            case UserInfoActivity.ACTION_ICON:
                break;
            case UserInfoActivity.ACTION_PSW:

                break;
            case UserInfoActivity.ACTION_USERNAME:
                updateUsername();
                break;
        }
    }

    private void updateGender() {
        final String gender = mView.getGender();
        if (gender.equals(User.gender)) {
            mView.updateSuccess(UserInfoActivity.ACTION_GENDER, "-1");
            return;
        }
        mView.showProgress();
        mModel.updateGender(User.userid, gender, new GDOnResponseHandler() {
            @Override
            public void onEnd() {
                mView.hideProgress();
                super.onEnd();
            }

            @Override
            public void onSuccess(int code, String json) {
                DataResultBean data = processJson(code, json);

                if (data == null) {
                    return;
                }
                User.gender = gender;
                mView.showMsg(data.getMessage());
                mView.updateSuccess(UserInfoActivity.ACTION_GENDER, gender);
                super.onSuccess(code, json);
            }
        });

    }

    private DataResultBean processJson(int code, String json) {
        if (GDHttpManager.CODE200 != code) {
            mView.showMsg("code:" + code);
            return null;
        }
        DataResultBean data = JsonUtils.fromJson(json, new TypeToken<DataResultBean>() {
        });
        if (data == null) {
            mView.showMsg("data parse error");
            return null;
        }
        if (GDHttpManager.CODE200 != data.getCode()) {
            mView.showMsg("error:" + data.getMessage());
            return null;
        }
        return data;
    }

    private void updateUsername() {
        final String username = mView.getEditText();
        if (TextUtils.isEmpty(username)) {
            mView.showMsg("请输入昵称");
            return;
        }
        mView.showProgress();
        mModel.updateUsername(User.userid, username, new GDOnResponseHandler() {
            @Override
            public void onEnd() {
                mView.hideProgress();
                super.onEnd();
            }

            @Override
            public void onSuccess(int code, String json) {
                MessageBean messageBean = JsonUtils.fromJson(json, new TypeToken<MessageBean>() {
                });
                if (GDHttpManager.CODE200 != code) {
                    mView.showMsg("网络异常：" + code);
                    return;
                }
                if (messageBean == null) {
                    mView.showMsg("data parse error");
                    return;
                }
                if (GDHttpManager.CODE200 != messageBean.getCode()) {
                    mView.showMsg("error:" + messageBean.getMessage());
                    return;
                }
                User.name = username;
                mView.showMsg(messageBean.getMessage());
                mView.updateSuccess("username", username);
                GDLogUtils.i(TAG, "json:" + json);
                super.onSuccess(code, json);
            }
        });
    }
}
