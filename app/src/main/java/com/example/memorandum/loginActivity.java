package com.example.memorandum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.memorandum.adapter.MemoAdapter;
import com.example.memorandum.bean.MemoBean;
import com.example.memorandum.db.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class loginActivity extends AppCompatActivity implements MemoAdapter.OnItemClickListener {
    private TextView show;
    private Button bnt_add, bnt_sousuo;
    private RecyclerView recy_view;
    private MyDbHelper myDbHelper;
    SQLiteDatabase db;
    private EditText ed_sousuo;
    // 声明 arr 为成员变量
    private List<MemoBean> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        show = findViewById(R.id.show);
        bnt_add = findViewById(R.id.bnt_add);
        recy_view = findViewById(R.id.recy_view);
        myDbHelper = new MyDbHelper(loginActivity.this);
        db = myDbHelper.getWritableDatabase();
        ed_sousuo = findViewById(R.id.ed_sousuo);
        bnt_sousuo = findViewById(R.id.bnt_sousuo);

        // 初始化 arr
        arr = new ArrayList<>();
        RecyDisplay();

        bnt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, AddInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bnt_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = ed_sousuo.getText().toString();
                searchMemos(searchText);
                if (ed_sousuo.getText().toString() == null) {
                    RecyDisplay();
                }
            }
        });
    }

    private void RecyDisplay() {
        arr.clear();
        Cursor cursor = db.rawQuery("select * from tb_memory", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String mytitle = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String mycontent = cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") String mypath = cursor.getString(cursor.getColumnIndex("imgpath"));
            @SuppressLint("Range") String mytime = cursor.getString(cursor.getColumnIndex("mtime"));
            MemoBean memoBean = new MemoBean(mytitle, mycontent, mypath, mytime);
            arr.add(memoBean);
        }
        cursor.close();
        MemoAdapter adapter = new MemoAdapter(loginActivity.this, arr);
        adapter.setOnItemClickListener(this);
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recy_view.setLayoutManager(st);
        recy_view.setAdapter(adapter);
    }

    private void searchMemos(String searchText) {
        arr.clear();
        Cursor cursor = db.rawQuery("select * from tb_memory where title like? or content like?", new String[]{"%" + searchText + "%", "%" + searchText + "%"});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String mytitle = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String mycontent = cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") String mypath = cursor.getString(cursor.getColumnIndex("imgpath"));
            @SuppressLint("Range") String mytime = cursor.getString(cursor.getColumnIndex("mtime"));
            MemoBean memoBean = new MemoBean(mytitle, mycontent, mypath, mytime);
            arr.add(memoBean);
        }
        cursor.close();
        MemoAdapter adapter = new MemoAdapter(loginActivity.this, arr);
        adapter.setOnItemClickListener(this);
        StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recy_view.setLayoutManager(st);
        recy_view.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String memoInfo = data.getStringExtra("edited_memo_info");
            if (memoInfo!= null) {
                String[] parts = memoInfo.split("\\|");
                String title = parts[0];
                String content = parts[1];
                String imgPath = parts[2];
                String time = parts[3];
                updateMemoInList(title, content, imgPath, time);
            }
        }
    }

    private void updateMemoInList(String title, String content, String imgPath, String time) {
        for (int i = 0; i < arr.size(); i++) {
            MemoBean memo = arr.get(i);
                memo.setTitle(title);
                memo.setContent(content);
                memo.setImgPath(imgPath);
                memo.setTime(time);
                // 通知适配器数据已更改
                ((MemoAdapter)recy_view.getAdapter()).notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onEditClick(int position) {
        // 获取要编辑的备忘录数据
        MemoBean memoToEdit = arr.get(position);
        Intent intent = new Intent(loginActivity.this, EditMemoActivity.class);
        intent.putExtra("title", memoToEdit.getTitle());
        intent.putExtra("content", memoToEdit.getContent());
        intent.putExtra("imgpath", memoToEdit.getImgPath());
        intent.putExtra("mtime", memoToEdit.getTime());
        startActivityForResult(intent, 1);
    }
}