/**
 * @author Santa Antilles
 * @Time 2023-10-28 13:47
 */
package com.example.genshinimpactcookbook.utils;

import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HttpsManager {
    // 这是接口，访问一次获得10条视频和预览图、头像、作者名称的信息
    private static final String resUrl = "https://api.apiopen.top/api/getMiniVideo";
    private static final OkHttpClient client = new OkHttpClient();

    // 获取视频信息
    public static void getVideoMessage(Callback callback){
        Request request = new Request.Builder().url(resUrl).build();
        client.newCall(request).enqueue(callback);
    }

    public static void download(int index, String url, String outputPath, final OnDownloadListener listener){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 处理下载失败的情况
                if (listener != null) {
                    listener.onDownloadFailed("请检查网络连接或授予应用程序以网络权限！", index);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 处理下载失败的情况
                    if (listener != null) {
                        listener.onDownloadFailed("未知错误：" + response, index);
                    }
                    return;
                }

                // 保存文件
                File outputFile = new File(outputPath);
                BufferedSink sink = Okio.buffer(Okio.sink(outputFile));
                BufferedSource source = Objects.requireNonNull(response.body()).source();
                sink.writeAll(source);
                sink.close();
                source.close();
            }
        });
    }

    public interface OnDownloadListener {
        void onDownloadFailed(String errorMessage, int index);
    }


}
