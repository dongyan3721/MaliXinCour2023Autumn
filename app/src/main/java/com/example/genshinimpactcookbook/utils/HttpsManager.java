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
    public static String getVideoMessage() throws IOException {
        Request request = new Request.Builder().url(resUrl).build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public static void download(int index, String url, String outputPath, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                File outputFile = new File(outputPath);
                BufferedSink sink = Okio.buffer(Okio.sink(outputFile));
                BufferedSource source = Objects.requireNonNull(response.body()).source();
                sink.writeAll(source);
                sink.close();
                source.close();
                response.close();
            }else{
                if(listener!=null){
                    listener.onDownloadFailed("网络请求错误！", index);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public interface OnDownloadListener {
        void onDownloadFailed(String errorMessage, int index);
    }


}
