package com.example.genshinimpactcookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.genshinimpactcookbook.R;
import com.example.genshinimpactcookbook.domain.VideoMessage;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<VideoMessage> {
    private Context mcontext;
    private int mresource;
    private ArrayList<VideoMessage> video;
    public ListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<VideoMessage> video) {
        super(context, resource, video);
        this.video = video;
        this.mcontext=context;
        this.mresource=resource;
    }

    //修改每个item的信息
    public View getView(int position, View convertView, ViewGroup parent){
        convertView= LayoutInflater.from(this.mcontext).inflate(this.mresource,parent,false);
        ImageView preimage=convertView.findViewById(R.id.preimage);
        TextView title=convertView.findViewById(R.id.title);
        ImageView image=convertView.findViewById(R.id.image);
        title.setText(getItem(position).getAlias());//视频发布者的名字
        //用glide获取image
        Glide.with(this.getContext()).load(getItem(position).getPreviewPictureUrl()).into(preimage);
        Glide.with(this.getContext()).load(getItem(position).getPictureUrl()).into(image);
        return convertView;
    }
}
