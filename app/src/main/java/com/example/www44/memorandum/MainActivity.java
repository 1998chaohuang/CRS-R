package com.example.www44.memorandum;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.example.www44.memorandum.NoteDB.TABLE_NAME;
import static com.example.www44.memorandum.NoteDB.TABLE_USER;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private MainAdapter adapter;
    private ListView lv;
    private Cursor cursor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_noitem;
    private SearchView mSearchView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (swipeRefreshLayout.isRefreshing()) {
                        adapter.notifyDataSetChanged();
                        selectDB();
//                    Toast.makeText(context,"调用scrollstatechange更新",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);//设置不刷新
                    }
                    break;
                case 2:
                    if (swipeRefreshLayout.isRefreshing()) {
                        adapter.notifyDataSetChanged();
                        selectDB();
//                    Toast.makeText(context,"调用swipeRefreshLayout更新",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);//设置不刷新
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
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
        lv = findViewById(R.id.listview);
        swipeRefreshLayout = findViewById(R.id.main_srl);
        tv_noitem = findViewById(R.id.tv_noitem);
        noteDB = new NoteDB(context);
        /*
         * 通过query来实现查询
         * 查询返回的是一个游标（cursor）
         */
        dbReader = noteDB.getReadableDatabase();
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
                // 去建档
                Intent intent = new Intent(context, CreatFileActivity.class);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, RecordActivity.class);
                cursor.moveToPosition(position);
                intent.putExtra("id",cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));
                intent.putExtra(NoteDB.NAME,cursor.getString(cursor.getColumnIndex(NoteDB.NAME)));
                intent.putExtra(NoteDB.ROOM,cursor.getString(cursor.getColumnIndex(NoteDB.ROOM)));
                intent.putExtra(NoteDB.REASON,cursor.getString(cursor.getColumnIndex(NoteDB.REASON)));
                intent.putExtra(NoteDB.NUM,cursor.getString(cursor.getColumnIndex(NoteDB.NUM)));
                intent.putExtra(NoteDB.RESULT,cursor.getString(cursor.getColumnIndex(NoteDB.RESULT)));
                intent.putExtra(NoteDB.HOSPITAL_DAY,cursor.getString(cursor.getColumnIndex(NoteDB.HOSPITAL_DAY)));
                intent.putExtra(NoteDB.HAPPEN_DAY,cursor.getString(cursor.getColumnIndex(NoteDB.HAPPEN_DAY)));

                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //  长按事件",删除记录
                cursor.moveToPosition(position);
                dbReader = noteDB.getWritableDatabase();
                int rowid = dbReader.delete(TABLE_USER, "_id=" + cursor.getInt(cursor.getColumnIndex(NoteDB.ID)), null);
                if (rowid != 0) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
                selectDB();
                dbReader.close();
                return true;
            }
        });

        // 设置刷新时进度动画变换的颜色，接收参数为可变长度数组。也可以使用setColorSchemeColors()方法。
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    public void run() {
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }

                    ;
                }.start();
            }
        });
    }

    private void search(String key) {
        String current_sql_sel = "SELECT * FROM " + TABLE_USER + " where name like '%" + key + "%'"+ " order by "+NoteDB.ID+" desc";
        cursor = dbReader.rawQuery(current_sql_sel, null);
        adapter = new MainAdapter(context, cursor);
        lv.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            tv_noitem.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            tv_noitem.setVisibility(View.GONE);
            tv_noitem.setHeight(0);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    //Cursor cursor=db.execure("select * from table_name where name like '%"+searcherFilter "%'")
    //数据库查询 查出所有记录 按照最新排序
    public void selectDB() {
        String current_sql_sel = "SELECT * FROM " + TABLE_USER + " order by "+NoteDB.ID +" desc";
        cursor = dbReader.rawQuery(current_sql_sel, null);
        adapter = new MainAdapter(context, cursor);
        lv.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            tv_noitem.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            tv_noitem.setVisibility(View.GONE);
            tv_noitem.setHeight(0);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(false);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("JFK昏迷恢复量表");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbReader.close();
    }
}
