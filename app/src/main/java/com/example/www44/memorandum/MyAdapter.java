package com.example.www44.memorandum;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 适配器
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout layout;
    float downX=0,downY=0;
    float upX=0,upY=0;
    public MyAdapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        //避免重复解析布局
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.cell,viewGroup,false);
            holder=new ViewHolder();
            holder.contenttv = view.findViewById(R.id.tv_score);
            holder.timetv = view.findViewById(R.id.tv_date);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        cursor.moveToPosition(position);
        //通过游标内部对应列名来拿到各个列的数据并显示到对应的控件中
        String content = cursor.getString(cursor.getColumnIndex(NoteDB.SCORE));
        String time = cursor.getString(cursor.getColumnIndex(NoteDB.TIME));
        holder.contenttv.setText(content);
        holder.timetv.setText(time);

        return view;
    }
}
