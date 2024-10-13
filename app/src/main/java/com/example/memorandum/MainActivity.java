package com.example.memorandum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText login_username, login_pas;
    private Button bnt_enter, bnt_concel,bnt_exit;
    private CheckBox ch_pas;
    String name;
    String password;
    Boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_pas = findViewById(R.id.login_pas);
        login_username = findViewById(R.id.login_username);
        bnt_enter = findViewById(R.id.login_enter);
        bnt_concel = findViewById(R.id.login_concel);
        bnt_exit = findViewById(R.id.login_exit);
        ch_pas = findViewById(R.id.memo_pas);

        // 从 SharedPreferences 中读取上次保存的登录信息
        SharedPreferences sharedPreferences = getSharedPreferences("login_info", 0);
        name = sharedPreferences.getString("name", "");
        password = sharedPreferences.getString("password", "");
        state = sharedPreferences.getBoolean("memo_pas", false);

        // 根据保存的状态设置输入框内容和复选框状态
        if (state) {
            login_username.setText(name);
            login_pas.setText(password);
            ch_pas.setChecked(true);
        } else {
            login_username.setText("");
            login_pas.setText("");
            ch_pas.setChecked(false);
        }

        bnt_concel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yxname = login_username.getText().toString();
                String yxpas = login_pas.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("login_info", 0).edit();
                editor.putString("name", yxname);
                editor.putString("password", yxpas);
                editor.putBoolean("memo_pas", ch_pas.isChecked());
                editor.commit();
                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                login_username.setText("");
                login_pas.setText("");
            }
        });

        bnt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yxname = login_username.getText().toString();
                String yxpas = login_pas.getText().toString();
                if(yxname.equals("")&&yxpas.equals(""))
                {
                    Toast.makeText(MainActivity.this, "密码或用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (name.equals(yxname) && password.equals(yxpas)) {
                        Intent intent = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                        // 登录成功后根据复选框状态保存登录信息
                        SharedPreferences.Editor editor = getSharedPreferences("login_info", 0).edit();
                        editor.putString("name", login_username.getText().toString());
                        editor.putString("password", login_pas.getText().toString());
                        editor.putBoolean("memo_pas", ch_pas.isChecked());
                        editor.commit();
                    } else {
                        Toast.makeText(MainActivity.this, "密码或用户名不正确，重新输入", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        bnt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确认退出");
                builder.setMessage("你确定要退出应用吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}