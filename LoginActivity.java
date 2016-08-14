package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.content.Intent;
import android.util.Log;

import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;
import com.a360filemanager.goodsq.my_matchboxapp.fragment.ThirdPartyFragment;
import com.tencent.connect.common.Constants;

/**
 * Created by goodsq on 2016/8/3.
 */
public class LoginActivity extends BaseActivity {

    ThirdPartyFragment login_fragment;

    @Override
    protected void init() {
        login_fragment = (ThirdPartyFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN){
            login_fragment.onActivityResult(requestCode,resultCode,data);
            Log.e("TAH","--123----------");
        }
    }
}
