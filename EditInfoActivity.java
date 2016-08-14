package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class EditInfoActivity extends BaseActivity implements TextView.OnEditorActionListener, TextWatcher {
    private static final int HEAD_CODE = 15185;
    @InjectView(R.id.editinfo_civ_head)
    CircleImageView editinfoCivHead;
    @InjectView(R.id.editinfo_et)
    EditText editinfoEt;
    @InjectView(R.id.password_iv_clear)
    ImageView editinfoIvClear;
    @InjectView(R.id.line)
    View line;
    @InjectView(R.id.editinfo_tv_submit)
    TextView editinfoTvSubmit;

    @Override
    protected void init() {
        editinfoEt.setOnEditorActionListener(this);
        editinfoEt.addTextChangedListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_editinfo;
    }

    @OnClick({R.id.editinfo_civ_head, R.id.password_iv_clear, R.id.editinfo_tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editinfo_civ_head:
                startActivityForResult(new Intent(this, SetHeadActivity.class), HEAD_CODE);
                break;
            case R.id.password_iv_clear:
                editinfoEt.setText("");
                break;
            case R.id.editinfo_tv_submit:
                submit();
                break;
        }
    }

    public void submit() {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        submit();
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 1) {
            if (charSequence.length() > 2)
                editinfoTvSubmit.setEnabled(true);
            editinfoIvClear.setVisibility(View.VISIBLE);
        } else {
            editinfoIvClear.setVisibility(View.INVISIBLE);
            editinfoTvSubmit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = data.getStringExtra("path");
            editinfoCivHead.setImageURI(Uri.fromFile(new File(path)));
        }
    }
}
