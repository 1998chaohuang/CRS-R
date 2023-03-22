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

public class MainAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout layout;
    float downX=0,downY=0;
    float upX=0,upY=0;
    public MainAdapter(Context context,Cursor cursor){
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
            view=LayoutInflater.from(context).inflate(R.layout.item_main,viewGroup,false);
            holder=new ViewHolder();
            holder.contenttv = view.findViewById(R.id.tv_score);
            holder.tvName = view.findViewById(R.id.et_name);
            holder.tvHappenDay = view.findViewById(R.id.tv_happen_day);
            holder.tvHospitalDay = view.findViewById(R.id.tv_hospital_day);
            holder.tvReason = view.findViewById(R.id.et_reason);
            holder.tvResult = view.findViewById(R.id.et_result);

            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        cursor.moveToPosition(position);
        //通过游标内部对应列名来拿到各个列的数据并显示到对应的控件中
        String room= cursor.getString(cursor.getColumnIndex(NoteDB.ROOM));
        holder.contenttv.setText("# "+room);

        holder.tvName.setText(cursor.getString(cursor.getColumnIndex(NoteDB.NAME)));
        holder.tvResult.setText(cursor.getString(cursor.getColumnIndex(NoteDB.RESULT)));
        holder.tvReason.setText(cursor.getString(cursor.getColumnIndex(NoteDB.REASON)));
        holder.tvHappenDay.setText(cursor.getString(cursor.getColumnIndex(NoteDB.HAPPEN_DAY)));
        holder.tvHospitalDay.setText(cursor.getString(cursor.getColumnIndex(NoteDB.HOSPITAL_DAY)));
        return view;
    }
}
