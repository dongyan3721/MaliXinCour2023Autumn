package com.example.genshinimpactcookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SpOperator extends AppCompatActivity {

    private SharedPreferences projectConfig;
    private EditText nameEdit, pwdEdit;
    private CheckBox checkBoxAutoLogin, checkBoxRememberPwd;
    private Button bt_login, bt_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_operator);
        projectConfig = getSharedPreferences("config", Context.MODE_PRIVATE);
        initView();
        boolean rememberPwd = projectConfig.getBoolean("rememberPwd", false);
        boolean autoLogin = projectConfig.getBoolean("autoLogin", false);
        if(rememberPwd){
            String name = projectConfig.getString("name", "");
            String pwd = projectConfig.getString("pwd", "");
            nameEdit.setText(name);
            pwdEdit.setText(pwd);
            checkBoxRememberPwd.setChecked(true);
        }
        if(autoLogin){
            checkBoxAutoLogin.setChecked(true);
            Toast.makeText(this, "我自动登录了", Toast.LENGTH_SHORT).show();
        }
    }



    private void initView(){
        nameEdit = findViewById(R.id.nameEdit);
        pwdEdit = findViewById(R.id.pwdEdit);
        checkBoxAutoLogin = findViewById(R.id.operatorCB1);
        checkBoxRememberPwd = findViewById(R.id.operatorCB2);
        bt_login = findViewById(R.id.bt_login);
        bt_register = findViewById(R.id.bt_register);

        bt_login.setOnClickListener(action->{
            String name = nameEdit.getText().toString().trim();
            String pwd = pwdEdit.getText().toString().trim();
            if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)){
                Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            }else{
                if(checkBoxRememberPwd.isChecked()){
                    SharedPreferences.Editor editor = projectConfig.edit();
                    editor.putString("name", name);
                    editor.putString("pwd", pwd);
                    editor.putBoolean("rememberPwd", true);
                    editor.apply();
                }
                if(checkBoxAutoLogin.isChecked()){
                    SharedPreferences.Editor editor = projectConfig.edit();
                    editor.putBoolean("autoLogin", true);
                    editor.apply();
                }
            }
        });
    }
}