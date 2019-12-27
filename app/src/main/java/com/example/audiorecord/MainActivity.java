package com.example.audiorecord;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.audiorecord.adapter.ListAdapter;
import com.example.audiorecord.bean.AudioBean;
import com.example.audiorecord.rv.LManagerUtil;
import com.library.audiorecord.audio.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.record_button)
    AudioRecorderButton audioRecorderButton;

    private ListAdapter adapter;
    private List<AudioBean> audioBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        checkAudioPermission();

        initAdapter();

        // 语音发送
        audioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                long time = (long) seconds;
                AudioBean bean = new AudioBean(filePath, time);
                Log.e(TAG, "录音文件 -> filePath: " + filePath + " ,time: " + time);
                Toast.makeText(MainActivity.this, "录音文件: " + bean.toString(), Toast.LENGTH_SHORT).show();

                // 添加数据
                audioBeanList.add(bean);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void checkAudioPermission() {
            if (Build.VERSION.SDK_INT >= 23) {
                List<String> permissions = null;
                if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) !=
                        PackageManager.PERMISSION_GRANTED) {
                    permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                }
                if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (permissions == null) {
                        permissions = new ArrayList<>();
                    }
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (permissions == null) {
                   // do thing
                } else {
                    String[] permissionArray = new String[permissions.size()];
                    permissions.toArray(permissionArray);
                    // Request the permission. The result will be received
                    // in onRequestPermissionResult()
                    this.requestPermissions(permissionArray, 0);
                }
            } else {
                // do thing
            }
    }

    private void initAdapter() {
        adapter = new ListAdapter(R.layout.item_list, audioBeanList);
        // 设置adapter
        LManagerUtil layoutManagerUtil = new LManagerUtil();
        layoutManagerUtil.setVerticalRvItemDecoration(this, recyclerView, adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

    }

}
