package com.example.genshinimpactcookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.genshinimpactcookbook.assets.ProjectSubPath;
import com.example.genshinimpactcookbook.domain.VideoMessage;
import com.example.genshinimpactcookbook.utils.DateUtil;
import com.example.genshinimpactcookbook.utils.HttpsManager;
import com.example.genshinimpactcookbook.utils.RegexUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OnlineVideoPlayerMainPage extends AppCompatActivity {

    private ProgressDialog mDialog;
    private List<VideoMessage> message = new ArrayList<>();
    private String defaultPictureAddress;

    // 创建一个处理器对象
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        // 在收到消息时触发
        public void handleMessage(Message msg) {
            // 定义的终止加载动作符
            if (msg.what == 1) {
                post(mCloseDialog);
            }
        }
    };

    private final Runnable mCloseDialog = new Runnable() {
        @Override
        public void run() {
            if (mDialog.isShowing()) { // 对话框仍在显示
                mDialog.dismiss(); // 关闭对话框
            }
        }
    };

    private String initDefaultPhoto(){
        // TODO 在这里初始化一张默认用于显示头像的图片，并返回这张图片在硬盘上的地址
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online_video_player_main_page);
        this.defaultPictureAddress = initDefaultPhoto();
        startLoading();
        new tempVideoPictureGrabber().start();

    }

    private void sendHttpRequestForVideoDetail(){
        try {
            String respResult = HttpsManager.getVideoMessage();
            ArrayList<Integer> recordAddressToModify = new ArrayList<>();
            // 视频上传者名
            ArrayList<String> alias = RegexUtil.getAlias(respResult);
            // 视频上传者头像地址，就他会404，如果404就把他指向一个默认头像静态资源
            ArrayList<String> pictureUrl = RegexUtil.getPictureUrl(respResult);
            // 视频封面地址
            ArrayList<String> previewPictureUrl = RegexUtil.getPreviewPictureUrl(respResult);
            // 视频内容地址
            ArrayList<String> contentUrl = RegexUtil.getContentUrl(respResult);
            HttpsManager.OnDownloadListener listener = (errorMsg, index)->{
                // 一般也不会下载失败，这里只用Toast做一个提醒
                Toast.makeText(OnlineVideoPlayerMainPage.this, "有一张图片没有获取到...", Toast.LENGTH_SHORT).show();
                // 记录一下需要修改的头像下标
                recordAddressToModify.add(index);
            };
            for (int i = 0; i < alias.size(); i++) {

                VideoMessage v = new VideoMessage();
                v.setAlias(alias.get(i));
                // 设置头像、封面图、视频的下载地址
                String avatarAddress = Environment.DIRECTORY_DOWNLOADS + ProjectSubPath.AVATAR + DateUtil.getNowDateTimeFull() + ".jpg";
                String previewAddress = Environment.DIRECTORY_DOWNLOADS + ProjectSubPath.PREVIEW + DateUtil.getNowDateTimeFull() + ".jpg";
                String videoAddress = Environment.DIRECTORY_DOWNLOADS + ProjectSubPath.VIDEO + DateUtil.getNowDateTimeFull() + ".mp4";

                // 开始下载
                HttpsManager.download(i, pictureUrl.get(i), avatarAddress, listener);
                HttpsManager.download(i, previewPictureUrl.get(i), previewAddress, listener);
                HttpsManager.download(i, contentUrl.get(i), videoAddress, listener);

                v.setContentUrl(videoAddress); // 拿到视频的下载路径
                v.setPictureUrl(avatarAddress); // 拿到头像的下载路径
                v.setPreviewPictureUrl(previewAddress); // 拿到视频预览图的下载路径
                message.add(v);
            }

            // 修改
            for (int i:recordAddressToModify) {
                message.get(i).setPictureUrl(defaultPictureAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class tempVideoPictureGrabber extends Thread{

        @Override
        public void run() {
            sendHttpRequestForVideoDetail();
            mHandler.sendEmptyMessage(1);
        }
    }

    private void startLoading(){
        mDialog = ProgressDialog.show(this, "请稍候", "正在拼命加载中O.o");
    }
}