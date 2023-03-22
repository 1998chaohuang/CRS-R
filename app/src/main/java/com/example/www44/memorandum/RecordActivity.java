package com.example.www44.memorandum;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;

import static com.example.www44.memorandum.NoteDB.TABLE_NAME;


public class RecordActivity extends AppCompatActivity {
    private Context context;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private MyAdapter adapter;
    private ListView lv;
    private Cursor cursor;
    private TextView tv_noitem,tv_total_score;
    private SearchView mSearchView;
    private int id;
    private String name;
    private TextView  tvName, tvNum,tvReason, tvResult, tvHappenDay, tvHospitalDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2);
        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        setActionBar();
        context = this;
        initView();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

    //初始化界面，绑定控件
    private void initView() {
        mSearchView = findViewById(R.id.searchView);
        tvName = findViewById(R.id.tv_room);
        tv_total_score = findViewById(R.id.tv_total_score);
        tvNum = findViewById(R.id.tv_num);
        tvHappenDay = findViewById(R.id.tv_happen_day);
        tvHospitalDay = findViewById(R.id.tv_hospital_day);
        tvReason = findViewById(R.id.et_reason);
        tvResult = findViewById(R.id.et_result);
        tvName.setText(getIntent().getStringExtra(NoteDB.ROOM));
        tvResult.setText(getIntent().getStringExtra(NoteDB.RESULT));
        tvNum.setText(getIntent().getStringExtra(NoteDB.NUM));
        tvReason.setText(getIntent().getStringExtra(NoteDB.REASON));
        tvHappenDay.setText(getIntent().getStringExtra(NoteDB.HAPPEN_DAY));
        tvHospitalDay.setText(getIntent().getStringExtra(NoteDB.HOSPITAL_DAY));

        lv = findViewById(R.id.listview);
        tv_noitem = findViewById(R.id.tv_noitem);
        noteDB = new NoteDB(context);
        /*
         * 通过query来实现查询
         * 查询返回的是一个游标（cursor）
         */
        dbReader = noteDB.getReadableDatabase();
        mSearchView.onActionViewExpanded();
    }

    //监听事件以及点击事件
    private void initEvent() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    selectDB();
                } else {
                    search(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    selectDB();
                }
                return false;
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SaveActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //  长按事件",删除记录
                cursor.moveToPosition(position);

                dbReader = noteDB.getWritableDatabase();
                int rowid = dbReader.delete(TABLE_NAME, "_id=" + cursor.getInt(cursor.getColumnIndex(NoteDB.ID)), null);
                if (rowid != 0) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
                selectDB();
                return true;
            }
        });


    }

    private void search(String key) {
        String current_sql_sel = "SELECT * FROM " + TABLE_NAME + " where " + NoteDB.TIME + " like '%" + key + "%'" + " and " + NoteDB.USERID + " = " + id+ " order by "+NoteDB.TIME;
        ;
        cursor = dbReader.rawQuery(current_sql_sel, null);
        adapter = new MyAdapter(context, cursor);
        lv.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            tv_noitem.setVisibility(View.VISIBLE);
        } else {
            tv_noitem.setVisibility(View.GONE);
            tv_noitem.setHeight(0);
        }
        adapter.notifyDataSetChanged();
    }

    private   int totalScore=0;
    public void selectDB() {
        String current_sql_sel = "SELECT * FROM " + TABLE_NAME + " where " + NoteDB.USERID + " = " + id+ " ORDER BY "+NoteDB.TIME;
        cursor = dbReader.rawQuery(current_sql_sel, null);
        totalScore=0;
        adapter = new MyAdapter(context, cursor);
        lv.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            tv_noitem.setVisibility(View.VISIBLE);

        } else {
            tv_noitem.setVisibility(View.GONE);
            tv_noitem.setHeight(0);
        }
        adapter.notifyDataSetChanged();

        if(cursor!=null&&cursor.moveToFirst()){
            do{
                String score= cursor.getString(cursor.getColumnIndex(NoteDB.SCORE));
                totalScore+=Integer.parseInt(score);
            }while(cursor.moveToNext());

        }
        tv_total_score.setText(totalScore+"");
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("患者记录 - " + name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
