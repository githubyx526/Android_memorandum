package com.example.memorandum.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorandum.R;
import com.example.memorandum.bean.MemoBean;
import com.example.memorandum.db.MyDbHelper;

import java.util.List;
import java.util.Random;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private Context mcontext;
    private List<MemoBean> arr1;
    private MyDbHelper myDbHelper1;
    private SQLiteDatabase db;
    private OnItemClickListener onItemClickListener;

    public MemoAdapter(Context mcontext, List<MemoBean> arr1) {
        this.mcontext = mcontext;
        this.arr1 = arr1;
    }

    @NonNull
    @RequiresApi(api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.recy_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        final MemoBean memoBean = arr1.get(i);
        holder.item_title.setText(memoBean.getTitle());
        holder.item_content.setText(memoBean.getContent());
        holder.item_time.setText(memoBean.getTime());
        Glide.with(mcontext).load(memoBean.getImgPath()).into(holder.item_img);

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(10f);
        gradientDrawable.setColor(color);
        holder.item_layout.setBackground(gradientDrawable);

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setTitle("操作选择");
                builder.setItems(new CharSequence[]{"修改", "删除","取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // 修改操作
                            if (onItemClickListener!= null) {
                                onItemClickListener.onEditClick(i);
                            }
                        } else if(which == 1){
                            // 删除操作
                            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(mcontext);
                            deleteBuilder.setMessage("你确定删除吗?");
                            deleteBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int abc) {
                                    myDbHelper1 = new MyDbHelper(mcontext);
                                    db = myDbHelper1.getWritableDatabase();
                                    db.delete("tb_memory", "title=?", new String[]{arr1.get(i).getTitle()});
                                    arr1.remove(i);
                                    notifyItemRemoved(i);
                                    dialog.dismiss();
                                }
                            });
                            deleteBuilder.setNegativeButton("取消", null);
                            deleteBuilder.setCancelable(false);
                            deleteBuilder.create();
                            deleteBuilder.show();
                        }
                        else
                        {
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title, item_content, item_time;
        ImageView item_img;
        LinearLayout item_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_content = itemView.findViewById(R.id.item_content);
            item_title = itemView.findViewById(R.id.item_title);
            item_time = itemView.findViewById(R.id.item_time);
            item_img = itemView.findViewById(R.id.item_image);
            item_layout = itemView.findViewById(R.id.item_layout);
        }
    }

    public interface OnItemClickListener {
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
}