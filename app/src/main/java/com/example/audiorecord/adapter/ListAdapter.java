package com.example.audiorecord.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.audiorecord.R;
import com.example.audiorecord.bean.AudioBean;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<AudioBean, BaseViewHolder> {

    public ListAdapter(int layoutResId, @Nullable List<AudioBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AudioBean item) {
        helper.setText(R.id.tv_filePath, item.getFilePath());
        helper.setText(R.id.tv_time, "" + item.getTime());
    }
}
