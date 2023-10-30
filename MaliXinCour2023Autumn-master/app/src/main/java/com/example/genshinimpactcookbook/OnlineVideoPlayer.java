package com.example.genshinimpactcookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class OnlineVideoPlayer extends AppCompatActivity {
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_video_player);
        initData();
        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse(url);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }
    private void initData() { //数据初始化
        if (getIntent() != null && getIntent().getExtras() != null) {
            //视频url
            url = getIntent().getExtras().getString("url"); //获取传递的视频URL
        }

    }

}