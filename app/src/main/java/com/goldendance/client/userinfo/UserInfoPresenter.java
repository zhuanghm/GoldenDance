package com.goldendance.client.userinfo;

import com.goldendance.client.model.IUserModel;
import com.goldendance.client.model.UserModel;

/**
 * Created by hemingway on 2017/1/17.
 */

public class UserInfoPresenter implements IUserInfoContract.IPresenter {
    IUserInfoContract.IView mView;
    IUserModel mModel;

    public UserInfoPresenter(IUserInfoContract.IView mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
        mModel = new UserModel();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void updateIcon(String iconBase64) {

    }

    @Override
    public void updateUsername(String username) {

    }

    @Override
    public void updateSex(String sex) {

    }

    @Override
    public void updatePassword(String md5Psw) {

    }
}