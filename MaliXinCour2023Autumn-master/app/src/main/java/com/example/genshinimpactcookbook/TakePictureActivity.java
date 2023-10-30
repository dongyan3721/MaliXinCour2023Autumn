package com.example.genshinimpactcookbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.genshinimpactcookbook.widget.CameraView;


@SuppressLint("DefaultLocale")
public class TakePictureActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "TakePictureActivity";
    private CameraView camera_view; // 声明一个相机视图对象
    private int mTakeType = 0; // 拍照类型。0为单拍，1为连拍
    private String mLocation = "";
    private LocationManager mLocationMgr; // 声明一个定位管理器对象
    private Criteria mCriteria = new Criteria(); // 声明一个定位准则对象
    private Handler mHandler = new Handler(); // 声明一个处理器
    private boolean isLocationEnable = false; // 定位服务是否可用
    private String locationTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        // 获取前一个页面传来的摄像头类型
        int camera_type = getIntent().getIntExtra("type", CameraView.CAMERA_BEHIND);
        // 从布局文件中获取名叫camera_view的相机视图
        camera_view = findViewById(R.id.camera_view);
        // 设置相机视图的摄像头类型
        camera_view.setCameraType(camera_type);
        findViewById(R.id.btn_shutter).setOnClickListener(this);
        findViewById(R.id.btn_shooting).setOnClickListener(this);
//        mHandler.postDelayed(mRefresh, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.removeCallbacks(mRefresh); // 移除定位刷新任务
        initLocation();
        mHandler.postDelayed(mRefresh, 100); // 延迟100毫秒启动定位刷新任务
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(); // 创建一个新意图
        Bundle bundle = new Bundle(); // 创建一个新包裹
        String photo_path = camera_view.getPhotoPath(); // 获取照片的保存路径
        bundle.putInt("type", mTakeType);
        if (photo_path == null && mTakeType == 0) { // 未发生拍照动作
            bundle.putString("is_null", "yes");
        } else { // 有发生拍照动作
            bundle.putString("is_null", "no");
            if (mTakeType == 0) { // 单拍。一次只拍一张
                bundle.putString("path", photo_path);
            } else if (mTakeType == 1) { // 连拍。一次连续拍了好几张
                bundle.putStringArrayList("path_list", camera_view.getShootingList());
            }
        }
        intent.putExtras(bundle); // 往意图中存入包裹
        setResult(Activity.RESULT_OK, intent); // 携带意图返回前一个页面
        finish(); // 关闭当前页面
    }

    @Override
    public void onClick(View v) {
        camera_view.setLocation(this.locationTemp);
        if (v.getId() == R.id.btn_shutter) { // 点击了单拍按钮
            mTakeType = 0;
            // 命令相机视图执行单拍操作
            camera_view.doTakePicture();
            // 拍照需要完成对焦、图像捕获、图片保存等一系列动作，因而要留足时间给系统处理
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TakePictureActivity.this, "已完成拍照，按返回键回到上页查看照片。", Toast.LENGTH_SHORT).show();
                }
            }, 1500);
        } else if (v.getId() == R.id.btn_shooting) { // 点击了连拍按钮
            mTakeType = 1;
            // 命令相机视图执行连拍操作
            camera_view.doTakeShooting();
        }
    }

    private void initLocation() {
        // 从系统服务中获取定位管理器
        mLocationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 设置定位精确度。Criteria.ACCURACY_COARSE表示粗略，Criteria.ACCURACY_FIN表示精细
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否需要海拔信息
        mCriteria.setAltitudeRequired(true);
        // 设置是否需要方位信息
        mCriteria.setBearingRequired(true);
        // 设置是否允许运营商收费
        mCriteria.setCostAllowed(true);
        // 设置对电源的需求
        mCriteria.setPowerRequirement(Criteria.POWER_HIGH);
        // 获取定位管理器的最佳定位提供者
        String bestProvider = mLocationMgr.getBestProvider(mCriteria, true);
        if (mLocationMgr.isProviderEnabled(bestProvider)) { // 定位提供者当前可用
            this.locationTemp = "正在获取" + bestProvider + "定位对象";
            mLocation = String.format("定位类型=%s", bestProvider);
            beginLocation(bestProvider);
            isLocationEnable = true;
        } else { // 定位提供者暂不可用
            this.locationTemp = "\n" + bestProvider + "定位不可用";
            isLocationEnable = false;
        }
    }

    private void setLocationText(Location location) {
        if (location != null) {
            String desc = String.format("经度：%f，纬度：%f", location.getLongitude(), location.getLatitude());
            Log.d(TAG, desc);
            this.locationTemp = desc;
//            Log.d(TAG, "==========: ======"+desc);
        } else {
            this.locationTemp = mLocation + "\n暂未获取到定位对象";
        }
    }

    // 开始定位
    private void beginLocation(String method) {
        // 检查当前设备是否已经开启了定位功能
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授予定位权限并开启定位功能", Toast.LENGTH_SHORT).show();
            return;
        }
        // 设置定位管理器的位置变更监听器
        mLocationMgr.requestLocationUpdates(method, 1000, 0, mLocationListener);
        // 获取最后一次成功定位的位置信息
        Location location = mLocationMgr.getLastKnownLocation(method);
        setLocationText(location);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setLocationText(location);
        }

        @Override
        public void onProviderDisabled(String arg0) {}

        @Override
        public void onProviderEnabled(String arg0) {}

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
    };

    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            if (!isLocationEnable) {
                initLocation();
                mHandler.postDelayed(this, 1000);
            }
        }
    };

}
