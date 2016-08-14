package com.a360filemanager.goodsq.my_matchboxapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a360filemanager.goodsq.my_matchboxapp.MyApp;
import com.a360filemanager.goodsq.my_matchboxapp.R;
import com.a360filemanager.goodsq.my_matchboxapp.adapter.HeadPicAdapter;
import com.a360filemanager.goodsq.my_matchboxapp.base.BaseActivity;
import com.a360filemanager.goodsq.my_matchboxapp.base.Callback;
import com.a360filemanager.goodsq.my_matchboxapp.bean.PopupBean;
import com.a360filemanager.goodsq.my_matchboxapp.utils.ConstanUtils;
import com.a360filemanager.goodsq.my_matchboxapp.utils.FileUtils;
import com.a360filemanager.goodsq.my_matchboxapp.utils.LogUtils;
import com.a360filemanager.goodsq.my_matchboxapp.view.ScaleImageView;
import com.a360filemanager.goodsq.my_matchboxapp.view.ShowHeadPopuoWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/5.
 */

/**
 * 返回一张图片给调用它的地方
 */
public class SetHeadActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.tv_back)
    TextView tvBack;
    @InjectView(R.id.tv_title_allpic)
    TextView tvTitleAllpic;
    @InjectView(R.id.sethead_tv_finish)
    TextView setheadTvFinish;
    @InjectView(R.id.sethead_siv)
    ScaleImageView setheadSiv;
    @InjectView(R.id.sethead_gv)
    GridView setheadGv;
    @InjectView(R.id.sethead_rl)
    View sethead_rl;

    List<File> pics;

    List<File> gridFiles = new ArrayList<>();

    List<PopupBean> mList = new ArrayList<>();

    HeadPicAdapter adapter;


    @Override
    protected void init() {
        if (MyApp.getInstance().getAllPic() != null) {
            pics = MyApp.getInstance().getAllPic();
        } else {
            pics = FileUtils.getAllPicture(Environment.getExternalStorageDirectory());
            MyApp.getInstance().setAllPic(pics);
        }
        initSIV();
        initData();
        initGridView();
    }

    private void initGridView() {
        gridFiles.addAll(pics);
        adapter = new HeadPicAdapter(this, gridFiles);
        setheadGv.setAdapter((ListAdapter) adapter);
        setheadGv.setOnItemClickListener(this);
    }

    private void initData() {
        //文件夹名称
        HashSet<String> set = new HashSet();//Set 无序 不重复
        for (int i = 0; i < pics.size(); i++) {
            set.add(pics.get(i).getParent());
        }
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String name = it.next();
            File file = new File(name);
            PopupBean bean = new PopupBean();
            bean.setName(file.getName());
            bean.setAbsPath(name);
            int index = 0;
            for (int i = 0; i < pics.size(); i++) {
                if (pics.get(i).getParent().equals(name)) {
                    if (index == 0)
                        bean.setFirstPic(pics.get(i).getAbsolutePath());
                    index++;
                }
            }
            bean.setCount(index);
            mList.add(bean);
        }
        PopupBean bean = new PopupBean();
        bean.setName("所有图片");
        bean.setFirstPic(pics.get(0).getAbsolutePath());
        bean.setChecked(true);
        bean.setAbsPath(Environment.getExternalStorageDirectory().getAbsolutePath());
        bean.setCount(pics.size());
        mList.add(0, bean);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ScrollView sc = (ScrollView) findViewById(R.id.sv);
        sc.fullScroll(ScrollView.FOCUS_UP);
    }

    private void initSIV() {
        int width = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        setheadSiv.setLayoutParams(params);
        setheadSiv.setImageBitmap(BitmapFactory.decodeFile(pics.get(0).getAbsolutePath()));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sethead;
    }


    @OnClick({R.id.tv_back, R.id.tv_title_allpic, R.id.sethead_tv_finish})
    public void onClick(View view) {
        LogUtils.e("-------------点击");
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_title_allpic:
                showPopupWindow();
                break;
            case R.id.sethead_tv_finish:
                new Thread() {
                    @Override
                    public void run() {
                        String path = setheadSiv.saveBitmap();
                        Intent intent = getIntent();
                        intent.putExtra("path", path);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }.start();
                break;
        }
    }


    int dirIndex = 0;

    private void showPopupWindow() {
        tvTitleAllpic.setSelected(true);
        final ShowHeadPopuoWindow window = new ShowHeadPopuoWindow(this, mList);

        window.setCallback(new Callback<Integer>() {
            @Override
            public void onSucess(Integer integer) {
                gridFiles.clear();
                tvTitleAllpic.setText(mList.get(integer).getName());
                //改变GridView的数据
                if (mList.get(integer).getAbsPath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()))
                    gridFiles.addAll(pics);
                else {
                    for (int i = 0; i < pics.size(); i++) {
                        if (mList.get(integer).getAbsPath().equals(pics.get(i).getParent())) {
                            gridFiles.add(pics.get(i));
                        }
                    }
                }
                mList.get(dirIndex).setChecked(false);
                mList.get(integer).setChecked(true);
                dirIndex = integer;
                adapter.notifyDataSetChanged();
                window.dismiss();
            }

            @Override
            public void onFinish() {

            }
        });
        window.showAsDropDown(sethead_rl);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvTitleAllpic.setSelected(false);
            }
        });
    }


    public static final int CAMERA_CODE = 9991;

    /**
     * GridView点击事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    String filePath;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            //打开摄像头
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filePath = ConstanUtils.IMAGE_PATH + "/" + System.currentTimeMillis() + ".jpg";
            startActivityForResult(intent, CAMERA_CODE);
        } else {
            adapter.setIndex(i);
            setheadSiv.setImageBitmap(BitmapFactory.decodeFile(gridFiles.get(i - 1).getAbsolutePath()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("-----   " + resultCode + "   ");
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            setheadSiv.setImageBitmap(bitmap);
        }
    }
}
