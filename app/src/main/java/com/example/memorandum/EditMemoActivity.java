package com.example.memorandum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.memorandum.bean.MemoBean;
import com.example.memorandum.db.MyDbHelper;

public class EditMemoActivity extends AppCompatActivity {

    private EditText editTitle, editContent, editImagePath,editTime;
    private Button saveButton;
    private MyDbHelper myDbHelper;
    SQLiteDatabase db;
    private MemoBean memoBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        editTitle = findViewById(R.id.edit_title1);
        editContent = findViewById(R.id.edit_content1);
        editImagePath = findViewById(R.id.edit_image_path1);
        saveButton = findViewById(R.id.save_button1);
        editTime = findViewById(R.id.edit_time);
        myDbHelper = new MyDbHelper(this);
        db = myDbHelper.getWritableDatabase();
        // 获取传递过来的备忘录数据并显示在编辑框中
        Intent intent = getIntent();
        if (intent!= null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String imgPath = intent.getStringExtra("imgpath");
            String time = intent.getStringExtra("mtime");
            editTitle.setText(title);
            editContent.setText(content);
            editImagePath.setText(imgPath);
            editTime.setText(time);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取编辑后的信息
                String editedTitle = editTitle.getText().toString();
                String editedContent = editContent.getText().toString();
                String editedImagePath = editImagePath.getText().toString();
                String editedTime = editTime.getText().toString();
                // 将备忘录对象的信息拼接成字符串传递
                String memoInfo = editedTitle + "|" + editedContent + "|" + editedImagePath + "|" + editedTime;
                ContentValues values = new ContentValues();
                values.put("title",editedTitle);
                values.put("content",editedContent);
                values.put("imgpath",editedImagePath);
                values.put("mtime",editedTime);
                db.update("tb_memory",values,"imgpath=?",new String[]{editedImagePath});
                // 返回编辑后的结果给调用方
                Toast.makeText(EditMemoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent(EditMemoActivity.this,loginActivity.class);
                resultIntent.putExtra("edited_memo_info", memoInfo);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}