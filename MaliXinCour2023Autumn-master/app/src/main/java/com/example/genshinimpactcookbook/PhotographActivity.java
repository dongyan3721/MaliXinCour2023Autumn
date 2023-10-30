package com.example.genshinimpactcookbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.example.genshinimpactcookbook.adapter.ShootingAdapter;
import com.example.genshinimpactcookbook.widget.CameraView;

import java.util.ArrayList;

/**
 * Created by ouyangshen on 2017/11/4.
 */
public class PhotographActivity extends AppCompatActivity implements OnClickListener {
    private static final int REQUEST_CAMERA_PERMISSION = 0x000012;
    private static final int REQUEST_LOCATION_PERMISSION = 0x000013;
    private static final String TAG = "PhotographActivity";
    private FrameLayout fl_content; // 声明一个框架布局对象
    private ImageView iv_photo; // 声明一个图像视图对象
    private GridView gv_shooting; // 声明一个网格视图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph);
        // 从布局文件中获取名叫fl_content的框架布局
        fl_content = findViewById(R.id.fl_content);
        // 从布局文件中获取名叫iv_photo的图像视图
        iv_photo = findViewById(R.id.iv_photo);
        // 从布局文件中获取名叫gv_shooting的网格视图
        gv_shooting = findViewById(R.id.gv_shooting);
        findViewById(R.id.btn_catch_behind).setOnClickListener(this);
        findViewById(R.id.btn_catch_front).setOnClickListener(this);
    }

    // 处理Camera拍照页面的返回结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult. requestCode=" + requestCode + ", resultCode=" + resultCode);
        Bundle resp = intent.getExtras(); // 获取返回的包裹
        String is_null = resp.getString("is_null");
        if (!TextUtils.isEmpty(is_null) && !is_null.equals("yes")) { // 有发生拍照动作
            int type = resp.getInt("type");
            Log.d(TAG, "type=" + type);
            if (type == 0) { // 单拍。一次只拍一张
                iv_photo.setVisibility(View.VISIBLE);
                gv_shooting.setVisibility(View.GONE);
                String path = resp.getString("path");
                fillBitmap(BitmapFactory.decodeFile(path, null));
            } else if (type == 1) { // 连拍。一次连续拍了好几张
                iv_photo.setVisibility(View.GONE);
                gv_shooting.setVisibility(View.VISIBLE);
                ArrayList<String> pathList = resp.getStringArrayList("path_list");
                Log.d(TAG, "pathList.size()=" + pathList.size());
                // 通过网格视图展示连拍的数张照片
                ShootingAdapter adapter = new ShootingAdapter(this, pathList);
                gv_shooting.setAdapter(adapter);
            }
        }
    }

    // 以合适比例显示照片
    private void fillBitmap(Bitmap bitmap) {
        Log.d(TAG, "fillBitmap width=" + bitmap.getWidth() + ",height=" + bitmap.getHeight());
        // 位图的高度大于框架布局的高度，则按比例调整图像视图的宽高
        if (bitmap.getHeight() > fl_content.getMeasuredHeight()) {
            LayoutParams params = iv_photo.getLayoutParams();
            params.height = fl_content.getMeasuredHeight();
            params.width = bitmap.getWidth() * fl_content.getMeasuredHeight() / bitmap.getHeight();
            // 设置iv_photo的布局参数
            iv_photo.setLayoutParams(params);
        }
        // 设置iv_photo的拉伸类型为居中
        iv_photo.setScaleType(ScaleType.FIT_CENTER);
        // 设置iv_photo的位图对象
        iv_photo.setImageBitmap(bitmap);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkFineLocationPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // 显示权限说明对话框
            // 可以使用一个对话框或其他方式向用户解释为什么需要相机权限，并在用户同意后请求权限
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void requestFineLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // 显示权限说明对话框
            // 可以使用一个对话框或其他方式向用户解释为什么需要相机权限，并在用户同意后请求权限
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    // 获取用户给予权限之后的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto(CameraView.CAMERA_BEHIND);
            } else {
                // 相机权限被拒绝，可以显示一条消息或执行其他操作
            }
        }
    }

    private void takePhoto(int camKind){
        // 打开后置摄像头（未指定摄像头编号的话，默认就是打开后置摄像头）
        Camera mCamera = Camera.open();
        if (mCamera != null) {
            mCamera.release(); // 释放摄像头
            // 前往Camera的拍照页面
            Intent intent = new Intent(this, TakePictureActivity.class);
            // 类型为后置摄像头
            intent.putExtra("type", camKind);
            // 需要处理拍照页面的返回结果
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(this, camKind==CameraView.CAMERA_BEHIND?"当前设备不支持后置摄像头":"当前设备不支持前置摄像头", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        if(checkCameraPermission()&&checkFineLocationPermission()){
            if(v.getId()==R.id.btn_catch_behind){
                takePhoto(CameraView.CAMERA_BEHIND);
            }else if(v.getId()==R.id.btn_catch_front){
                takePhoto(CameraView.CAMERA_FRONT);
            }
        }else if(checkCameraPermission()){
            requestFineLocationPermission();
        }else if(checkFineLocationPermission()){
            requestCameraPermission();
        }else{
            requestFineLocationPermission();
            requestCameraPermission();
        }
    }

}
