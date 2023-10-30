package com.example.genshinimpactcookbook;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.io.IOException;

import okhttp3.*;

public class HttpTest {
    @Test
    public void test01(){
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.apiopen.top/api/getMiniVideo"; // 替换为实际的URL

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // 创建自定义的回调处理程序
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 获取响应内容
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseData = responseBody.string();
                        System.out.println("Response Body: " + responseData);
                        responseBody.close(); // 关闭响应体
                    } else {
                        System.out.println("Response Body is null.");
                    }
                } else {
                    System.out.println("Request failed with code: " + response.code());
                }
                // 关闭响应
                response.close();
            }
        };

        // 使用异步调用
        client.newCall(request).enqueue(callback);

        System.out.println("31243432423");
    }

    @Test
    public void test02(){
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.apiopen.top/api/getMiniVideo"; // 替换为实际的URL

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                // 获取响应内容
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String responseData = responseBody.string();
                    System.out.println("Response Body: " + responseData);
                    responseBody.close(); // 关闭响应体
                } else {
                    System.out.println("Response Body is null.");
                }
            } else {
                System.out.println("Request failed with code: " + response.code());
            }

            // 关闭响应
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
