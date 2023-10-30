package com.example.genshinimpactcookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.genshinimpactcookbook.adapter.ListViewAdapter;
import com.example.genshinimpactcookbook.assets.ProjectSubPath;
import com.example.genshinimpactcookbook.domain.VideoMessage;
import com.example.genshinimpactcookbook.utils.DateUtil;
import com.example.genshinimpactcookbook.utils.HttpsManager;
import com.example.genshinimpactcookbook.utils.RegexUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OnlineVideoPlayerMainPage extends AppCompatActivity {

    private ProgressDialog mDialog;
    private ArrayList<VideoMessage> message = new ArrayList<>();
    private String defaultPictureAddress;

    private ListView listView;

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
        int defaultAvatarResId = getResources().getIdentifier("default_avatar", "drawable", getPackageName());
        String defaultPhotoPath = getFilesDir() + File.separator + "default_avatar.png"; // 默认头像保存路径
        try {
            InputStream inputStream = getResources().openRawResource(defaultAvatarResId);
            OutputStream outputStream = new FileOutputStream(defaultPhotoPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defaultPhotoPath;
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
        HttpsManager.getVideoMessage(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO 请求失败直接在列表控件上显示一个失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 请求失败的提示信息
                        String errorMsg = "请求失败";
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 在这里储存需要把存储地址修改为默认图片的图片索引
                ArrayList<Integer> recordAddressToModify = new ArrayList<>();
                String respData = Objects.requireNonNull(response.body()).string();
                // 视频上传者名
                ArrayList<String> alias = RegexUtil.getAlias(respData);
                // 视频上传者头像地址，就他会404，如果404就把他指向一个默认头像静态资源
                ArrayList<String> pictureUrl = RegexUtil.getPictureUrl(respData);
                // 视频封面地址
                ArrayList<String> previewPictureUrl = RegexUtil.getPreviewPictureUrl(respData);
                // 视频内容地址
                ArrayList<String> contentUrl = RegexUtil.getContentUrl(respData);
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

                // TODO 把message渲染到一个adaptor里面，最后渲染为ListView控件
                listView=findViewById(R.id.list_view);
                ListViewAdapter listViewAdapter=new ListViewAdapter(OnlineVideoPlayerMainPage.this,R.layout.listview_item,message);
                listView.setAdapter(listViewAdapter);
            }
        });
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

    private class ListViewHandler implements AdapterView.OnItemClickListener{
//      listview单击事件，跳转至播放界面
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            VideoMessage videoMessage = message.get(position);
            Intent intent = new Intent(OnlineVideoPlayerMainPage.this, OnlineVideoPlayer.class);
            intent.putExtra("selected_item", videoMessage.getContentUrl());//传递选中项的数据
            startActivity(intent);
        }
    }
}