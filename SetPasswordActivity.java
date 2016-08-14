package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a360filemanager.goodsq.my_matchboxapp.MyApp;
import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;
import com.a360filemanager.goodsq.my_matchboxapp.bean.User;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/4.
 */
public class SetPasswordActivity extends BaseActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {
    @InjectView(R.id.password_et)

            
    EditText passwordEt;
    @InjectView(R.id.password_iv_show)
    CheckBox passwordIvShow;
    @InjectView(R.id.password_iv_clear)
    ImageView passwordIvClear;
    @InjectView(R.id.line)
    View line;
    @InjectView(R.id.password_tv_submit)
    TextView passwordTvSubmit;

    @Override
    protected void init() {
        passwordEt.addTextChangedListener(this);
        passwordEt.setOnEditorActionListener(this);
        passwordIvShow.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_password;
    }

    //怎么判断中文的大小
    // str.length == str.getbytes.leng;

    @OnClick({R.id.password_iv_clear, R.id.password_tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_iv_clear:
                passwordEt.setText("");
                break;
            case R.id.password_tv_submit:
                setPassword();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0) {
            passwordIvClear.setVisibility(View.VISIBLE);
            if (charSequence.length() >= 6) {
                passwordTvSubmit.setEnabled(true);
            } else
                passwordTvSubmit.setEnabled(false);
        } else {
            passwordIvClear.setVisibility(View.INVISIBLE);
            passwordTvSubmit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //切换眼睛
        if (b) {
            passwordEt.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void setPassword() {
        User user = MyApp.getInstance().getUser();
        user.setPassword(passwordEt.getText().toString());
        MyApp.getInstance().setUser(user);
        //跳转
        jumpActivity(EditInfoActivity.class);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        setPassword();
        return false;
    }
}
