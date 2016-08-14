package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.a360filemanager.goodsq.my_matchboxapp.MyApp;
import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;
import com.a360filemanager.goodsq.my_matchboxapp.utils.BoxUtils;
import com.a360filemanager.goodsq.my_matchboxapp.utils.LogUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/8/4.
 */
public class VerifyActivity extends BaseActivity implements TextView.OnEditorActionListener, TextWatcher {
    //备课神器
    @InjectView(R.id.verify_et)
    EditText verifyEt;
    @InjectView(R.id.verify_tv_submit)
    TextView verifyTvSubmit;
    @InjectView(R.id.verify_tv_time)
    TextView verifyTvTime;

    MyCountDownTimer countDownTimer;


    @Override
    protected void init() {
        verifyEt.setOnEditorActionListener(this);
        verifyEt.addTextChangedListener(this);
        countDownTimer = new MyCountDownTimer();
        countDownTimer.start();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_verify;
    }


    @OnClick(value = {R.id.verify_tv_submit, R.id.verify_tv_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify_tv_time:

                SMSSDK.getVoiceVerifyCode("+86", MyApp.getInstance().getUser().getUsername());
                countDownTimer = new MyCountDownTimer();
                countDownTimer.start();
                break;
            case R.id.verify_tv_submit:
                submit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long time;
        if (countDownTimer != null) {
            time = countDownTimer.shengyushijian;
            if (time != 0) {
                MyApp.getInstance().shengyushijian(time);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        LogUtils.e("---------------------回车");
        submit();
        //true:执行当前方法2次，
        //false：执行一次 keyDown keyUP
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 4) {
            verifyTvSubmit.setEnabled(true);
        } else {
            verifyTvSubmit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private Dialog dialog;

    private EventHandler handler = new EventHandler() {
        @Override
        public void afterEvent(int code, int result, Object data) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (code == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交成功
                    //跳转
                    jumpActivity(SetPasswordActivity.class);
                }
            } else {
                showToast("验证失败");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(handler);
    }

    private void submit() {
        if (verifyEt.getText().toString().length() >= 4) {
            dialog = BoxUtils.getProgressDialog(this, "提示", "正在验证，请稍候。。。。");
            SMSSDK.submitVerificationCode("+86", MyApp.getInstance().getUser().getUsername(), verifyEt.getText().toString());
        } else {
            showToast("请输入正确的验证码");
        }
    }


    class MyCountDownTimer extends CountDownTimer {

        long shengyushijian;

        public MyCountDownTimer() {
            super(30000, 1000);
            verifyTvTime.setEnabled(false);
        }

        @Override
        public void onTick(long l) {
            verifyTvTime.setText("已发送:" + l / 1000 + "秒");
            this.shengyushijian = l;
        }

        @Override
        public void onFinish() {
            verifyTvTime.setEnabled(true);
            verifyTvTime.setText("接受语音验证码");
            shengyushijian = 0;
        }
    }


}
