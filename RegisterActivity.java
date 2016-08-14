package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a360filemanager.goodsq.my_matchboxapp.MyApp;
import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;
import com.a360filemanager.goodsq.my_matchboxapp.bean.User;
import com.a360filemanager.goodsq.my_matchboxapp.utils.BoxUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/8/4.
 */
public class RegisterActivity extends BaseActivity implements TextWatcher, TextView.OnEditorActionListener {

    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.register_et_phonenumber)
    EditText registerEtPhonenumber;
    @InjectView(R.id.register_tv_submit)
    TextView registerTvSubmit;
    @InjectView(R.id.register_iv_clear)
    ImageView registerIvClear;

    @Override
    protected void init() {
        //给edittext做监听
        //2个监听
        // 11位  按钮变成enable >1 clear显示
        registerEtPhonenumber.addTextChangedListener(this);
        registerEtPhonenumber.setOnEditorActionListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.register_iv_clear, R.id.register_tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_iv_clear:
                registerEtPhonenumber.setText("");
                registerTvSubmit.setEnabled(false);
                break;
            case R.id.register_tv_submit:
                sendCode();
                break;
        }
    }

    Dialog dialog;

    private void sendCode() {   //判断手机号
        if (MyApp.getInstance().isSendCode()) {
            showToast("30秒内不允许给同一个手机发送验证码");
            return;
        }
        if (registerEtPhonenumber.getText().toString().replace(" ", "").matches("[1][34578][\\d]{9}")) {
            //发送验证码
            dialog = BoxUtils.getProgressDialog(this, "提示", "正在发送验证码，请稍候。。。。");
            SMSSDK.getVerificationCode("+86", registerEtPhonenumber.getText().toString().replace(" ", ""));
        } else {
            showToast("请填写正确的手机号码");
        }
        //trim  replace(" ","");
        //  a b c    //trim去掉两边的空格
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int a, int a1, int a2) {
        //1.
        if (charSequence.length() > 0) {
            registerIvClear.setVisibility(View.VISIBLE);
            if (charSequence.toString().replace(" ", "").length() == 11) {
                //当输入完11位电话号码时，隐藏软键盘
                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                registerTvSubmit.setEnabled(true);
            } else {
                registerTvSubmit.setEnabled(false);
            }
        } else
            registerIvClear.setVisibility(View.INVISIBLE);

        if (charSequence == null || charSequence.length() == 0)
            return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charSequence.length(); i++) {
            if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(charSequence.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(charSequence.toString())) {
            int index = a + 1;
            if (sb.charAt(a) == ' ') {
                if (a1 == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (a1 == 1) {
                    index--;
                }
            }
            registerEtPhonenumber.setText(sb.toString());
            registerEtPhonenumber.setSelection(index);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {//回车
        sendCode();
        Log.e("TAG", "--------------------");
        return false;
    }

    /**
     * 注册回调
     **/
    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(event);
    }

    EventHandler event = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    showToast("发送验证成功");
                    //跳转
                    User user = new User();
                    user.setUsername(registerEtPhonenumber.getText().toString().replace(" ", ""));
                    MyApp.getInstance().setUser(user);
                    jumpActivity(VerifyActivity.class);
                }
            } else {
                ((Throwable) data).printStackTrace();
                showToast("发送验证失败");
            }

        }
    };
}