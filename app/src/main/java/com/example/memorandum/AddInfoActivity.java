package com.example.memorandum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.UriUtils;
import com.bumptech.glide.Glide;
import com.example.memorandum.db.MyDbHelper;

import java.io.File;
import java.io.IOException;

public class AddInfoActivity extends AppCompatActivity {

    private EditText edit_title,edit_context;
    private Button bnt_camera,bnt_photo,bnt_save;
    private ImageView image_preview;
    private String tmp_path,disp_path;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        initView();
        bntOnclick();
        bntsave();
        initPhotoError();
    }

    private void initView() {
        edit_context = findViewById(R.id.edit_content);
        edit_title = findViewById(R.id.edit_title);
        bnt_camera = findViewById(R.id.button_camera);
        bnt_photo = findViewById(R.id.button_photo);
        bnt_save = findViewById(R.id.bnt_save);
        image_preview = findViewById(R.id.image_preview);
        myDbHelper = new MyDbHelper(AddInfoActivity.this);
        db = myDbHelper.getWritableDatabase();
    }

    private void bntOnclick() {
        bnt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time();
                time.setToNow();
                String RandTime = time.year+(time.month+1)+time.monthDay+time.hour+time.minute+time.second+"";
                tmp_path = Environment.getExternalStorageDirectory()+ "/image"+RandTime+".jpg";
                File ImageFile = new File(tmp_path);
                try {
                    ImageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent("android.media.action.STILL_IMAGE_CAMERA");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(ImageFile));
                startActivityForResult(intent,11);
            }
        });
        bnt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,22);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK) {
                    disp_path = tmp_path;
                    Glide.with(AddInfoActivity.this).load(disp_path).into(image_preview);
                }
//                else {
//                    // 如果从相册选择时未选择任何东西，返回 loginActivity
//                    Intent yxintent3 = new Intent(AddInfoActivity.this,loginActivity.class);
//                    startActivity(yxintent3);
//                }
                break;
            case 22:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    if (imageUri!= null) {
                        disp_path = UriUtils.uri2File(imageUri).getPath();
                        Glide.with(AddInfoActivity.this).load(disp_path).into(image_preview);
                    } else {
                        // 如果从相册选择时未选择任何东西，返回 loginActivity
                       Intent yxintent = new Intent(AddInfoActivity.this,loginActivity.class);
                       startActivity(yxintent);
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // 如果从相册选择时取消操作，返回 loginActivity
                    Intent yxintent1 = new Intent(AddInfoActivity.this,loginActivity.class);
                    startActivity(yxintent1);
                }
                break;
            default:
                break;
        }
    }

    private void bntsave() {
        bnt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time();
                time.setToNow();
                ContentValues contentValues = new ContentValues();
                contentValues.put("title",edit_title.getText().toString());
                contentValues.put("content",edit_context.getText().toString());
                contentValues.put("imgpath",disp_path);
                contentValues.put("mtime",time.year+"/"+(time.month+1)+"/"+time.monthDay);
                db.insert("tb_memory",null,contentValues);
                Toast.makeText(AddInfoActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddInfoActivity.this,loginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}