package com.goldendance.client.userinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goldendance.client.R;
import com.goldendance.client.base.BaseActivity;
import com.goldendance.client.bean.User;
import com.goldendance.client.http.GDImageLoader;
import com.goldendance.client.login.LoginActivity;
import com.goldendance.client.utils.GDLogUtils;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static final String ACTION_ICON = "icon";
    public static final String ACTION_GENDER = "gender";
    public static final String ACTION_PSW = "psw";
    public static final String ACTION_USERNAME = "username";

    private static final int REQUEST_USERINFO = 10000;
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private TextView tvNickname;
    private TextView tvSex;
    private ImageView ivIcon;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_info);
        if (TextUtils.isEmpty(User.tokenid)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            onBackPressed();
            return;
        }
        setUserinfo();
        findViewById(R.id.flIcon).setOnClickListener(this);
        findViewById(R.id.flGender).setOnClickListener(this);
        findViewById(R.id.flUsername).setOnClickListener(this);
        findViewById(R.id.flPsw).setOnClickListener(this);
        findViewById(R.id.flMobile).setOnClickListener(this);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GDImageLoader.setImageView(this, User.icon, ivIcon);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("我的资料");
    }

    private void setUserinfo() {
        tvNickname = (TextView) findViewById(R.id.tvNickname);
        tvNickname.setText(User.name);
        //性别   sex:0,（0:男 1:女）
        tvSex = (TextView) findViewById(R.id.tvSex);
        String gender = User.gender;

        TextView tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvMobile.setText(User.tel);
        if (!TextUtils.isEmpty(gender)) {
            switch (gender) {
                case "0":
                    tvSex.setText("男");
                    break;
                case "1":
                    tvSex.setText("女");
                    break;
                default:
                    tvSex.setText("不确定");
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.flIcon:
                intent.setClass(this, UpdateUserInfoActivity.class);
                intent.putExtra("action", ACTION_ICON);
                startActivityForResult(intent, REQUEST_USERINFO);
                break;
            case R.id.flGender:
                intent.setClass(this, UpdateUserInfoActivity.class);
                intent.putExtra("action", ACTION_GENDER);
                startActivityForResult(intent, REQUEST_USERINFO);
                break;
            case R.id.flPsw:
                intent.setClass(this, UpdatePasswordActivity.class);
                intent.putExtra("action", ACTION_PSW);
                startActivityForResult(intent, REQUEST_USERINFO);
                break;
            case R.id.flUsername:
                intent.setClass(this, UpdateUserInfoActivity.class);
                intent.putExtra("action", ACTION_USERNAME);
                startActivityForResult(intent, REQUEST_USERINFO);
                break;
            case R.id.flMobile:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("换绑手机");
                builder.setMessage("如需换绑手机号，请到各个门店进行操作");
                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        String action = data.getStringExtra("action");
        switch (action) {
            case ACTION_USERNAME:
                String username = data.getStringExtra(ACTION_USERNAME);
                tvNickname.setText(username);
                break;
            case ACTION_GENDER:
                String gender = data.getStringExtra(ACTION_GENDER);
                if ("-1".equals(gender)) {
                    return;
                }
                switch (gender) {
                    case "0":
                        tvSex.setText("男");
                        break;
                    case "1":
                        tvSex.setText("女");
                        break;
                }
                break;
            case ACTION_ICON:
                String icon = data.getStringExtra(ACTION_ICON);
//                Uri uri = data.getData();
//                GDLogUtils.i(TAG, "uri:" + icon);
                Bitmap bitmap = BitmapFactory.decodeFile(icon);
                ivIcon.setImageBitmap(bitmap);
//                GDImageLoader.setImageView(this, icon, ivIcon);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
