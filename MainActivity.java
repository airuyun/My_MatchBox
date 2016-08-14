package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.adapter.MyViewPagerAdapter;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;
import com.a360filemanager.goodsq.my_matchboxapp.implement.MySurfaceHolderCallback;
import com.a360filemanager.goodsq.my_matchboxapp.view.LoadDialog;

import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.main_sfv_movie)
    SurfaceView sv;
    @InjectView(R.id.tv_main_login)
    TextView tvMainLogin;
    @InjectView(R.id.tv_main_register)
    TextView tvMainRegister;
    @InjectView(R.id.main_ll_points)
    LinearLayout mainLlPoints;
    @InjectView(R.id.main_vp)
    ViewPager mainVp;

    @Override
    protected void init() {
        LoadDialog dialog = new LoadDialog(this);
        dialog.show();
        MySurfaceHolderCallback mshc = new MySurfaceHolderCallback(this, sv);
        mainLlPoints.getChildAt(0).setSelected(true);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(this);
        mainVp.setAdapter(adapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.tv_main_login, R.id.tv_main_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_main_login:
                jumpActivity(LoginActivity.class);
                break;
            case R.id.tv_main_register:
                jumpActivity(RegisterActivity.class);
                break;
        }
    }
}
