package com.example.www44.memorandum;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 保存当天记录
 */
public class SaveActivity extends AppCompatActivity implements View.OnClickListener {

    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbwriter;

    private Cursor cursor;
    private String date;
    private long rowId;
    private TextView tvDate;
    private int scoreListener = 0, scoreVison = 0, scoreSport = 0, scoreSay = 0, scoreJiaoliu = 0, scoreAwake = 0, totalScore = 0;

    private TextView tvScore, etName;
    private RadioGroup groupListener, groupVision, groupSport, groupSay, groupComum, groupAwake;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);
        //以写的方式打开database 打开数据库 待会要添加数据
        noteDB = new NoteDB(this);
        dbwriter = noteDB.getWritableDatabase();
        setActionBar();
        ininView();
    }

    private void ininView() {
        tvScore = findViewById(R.id.tv_score);
        groupListener = findViewById(R.id.radio_listener);
        groupVision = findViewById(R.id.radio_vision);
        groupSport = findViewById(R.id.radio_sport);
        groupSay = findViewById(R.id.radio_say);
        groupComum = findViewById(R.id.radio_communication);
        groupAwake = findViewById(R.id.radio_awake);
        etName = findViewById(R.id.et_name);
        etName.setText(getIntent().getStringExtra("name"));

        findViewById(R.id.btn_listener).setOnClickListener(this);
        findViewById(R.id.btn_vision).setOnClickListener(this);
        findViewById(R.id.btn_awake).setOnClickListener(this);
        findViewById(R.id.btn_communication).setOnClickListener(this);
        findViewById(R.id.btn_sport).setOnClickListener(this);
        findViewById(R.id.btn_say).setOnClickListener(this);
        groupListener.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.listener_0:
                        scoreListener = 0;
                        break;
                    case R.id.listener_1:
                        scoreListener = 1;
                        break;
                    case R.id.listener_2:
                        scoreListener = 2;
                        break;
                    case R.id.listener_3:
                        scoreListener = 3;
                        break;
                    case R.id.listener_4:
                        scoreListener = 4;
                        break;
                }
                totalScore();
            }
        });

        groupVision.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.vision_0:
                        scoreVison = 0;
                        break;
                    case R.id.vision_01:
                        scoreVison = 1;
                        break;
                    case R.id.vision_02:
                        scoreVison = 2;
                        break;
                    case R.id.vision_03:
                        scoreVison = 3;
                        break;
                    case R.id.vision_04:
                        scoreVison = 4;
                        break;
                    case R.id.vision_05:
                        scoreVison = 5;
                        break;
                }
                totalScore();
            }
        });
        groupSport.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sport_0:
                        scoreSport = 0;
                        break;
                    case R.id.sport_01:
                        scoreSport = 1;
                        break;
                    case R.id.sport_02:
                        scoreSport = 2;
                        break;
                    case R.id.sport_03:
                        scoreSport = 3;
                        break;
                    case R.id.sport_04:
                        scoreSport = 4;
                        break;
                    case R.id.sport_05:
                        scoreSport = 5;
                        break;
                    case R.id.sport_06:
                        scoreSport = 6;
                        break;
                }
                totalScore();
            }
        });
        groupSay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.say_0:
                        scoreSay = 0;
                        break;
                    case R.id.say_01:
                        scoreSay = 1;
                        break;
                    case R.id.say_02:
                        scoreSay = 2;
                        break;
                    case R.id.say_03:
                        scoreSay = 3;
                        break;
                }
                totalScore();
            }
        });
        groupComum.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.communication_0:
                        scoreJiaoliu = 0;
                        break;
                    case R.id.communication_1:
                        scoreJiaoliu = 1;
                        break;
                    case R.id.communication_2:
                        scoreJiaoliu = 2;
                        break;
                }
                totalScore();
            }
        });

        groupAwake.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.awake_0:
                        scoreAwake = 0;
                        break;
                    case R.id.awake_1:
                        scoreAwake = 1;
                        break;
                    case R.id.awake_2:
                        scoreAwake = 2;
                        break;
                    case R.id.awake_3:
                        scoreAwake = 3;
                        break;
                }
                totalScore();
            }
        });
        date = getTime();
        tvDate = findViewById(R.id.tv_date);
        tvDate.setText(date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在点击事件部分调用，chooseBirthday为点击事件的控件
                showDatePickDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tvDate.setText(year + " - " + (month + 1) + " - " + day);
                    }
                }, tvDate.getText().toString());
            }
        });
    }

    private void totalScore() {
        totalScore = scoreListener + scoreVison + scoreSport + scoreSay + scoreJiaoliu + scoreAwake;
        tvScore.setText(totalScore + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("JFK昏迷恢复量表");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            case R.id.menu_save:
                save();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            Toast.makeText(this, "请输入患者姓名~", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isSubmit()) {
            Toast.makeText(this, "该患者今天已登记过~", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存一条数据
        addDB();
        Toast.makeText(this, rowId != -1 ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void addDB() {
        dbwriter = noteDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.SCORE, totalScore + "");
        cv.put(NoteDB.NAME, etName.getText().toString());
        cv.put(NoteDB.USERID, getIntent().getIntExtra("id", 0));
        cv.put(NoteDB.TIME, tvDate.getText().toString());

        /*
         * insert方法得参数列表：
         *              第一个为数据库表名，
         *              第二个为希望插入值为空的列名（如果插入值没有为空得列，全是有数据得就写null），
         *              第三个为一个ContentValues对象   键值对
         *  插入操作会返回以一个行id值提示是否插入成功
         */
        rowId = dbwriter.insert(NoteDB.TABLE_NAME, null, cv);

        //操作完成关闭数据库
        dbwriter.close();
    }

    /*
        获取当前时间作为储存是的时间设置
     */
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy - MM - dd");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }

    //数据库查询 查出是否当天已经登记
    public boolean isSubmit() {
        date = tvDate.getText().toString();
        /*
         * 通过query来实现查询
         * 查询返回的是一个游标（cursor）
         */
        boolean isSubmit = false;
        dbReader = noteDB.getReadableDatabase();
        cursor = dbReader.query(noteDB.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String time = cursor.getString(cursor.getColumnIndex(NoteDB.TIME));
                int id = cursor.getInt(cursor.getColumnIndex(NoteDB.USERID));
                if (time.equals(date) && getIntent().getIntExtra("id", 0) == id) {
                    isSubmit = true;
                }
            }
        }

        cursor.close();
        return isSubmit;
    }


    /**
     * 日期选择
     *
     * @param listener
     * @param curDate
     */
    public void showDatePickDialog(DatePickerDialog.OnDateSetListener listener, String curDate) {
        Calendar calendar = Calendar.getInstance();
        int year = 0, month = 0, day = 0;
        try {
            year = Integer.parseInt(curDate.substring(0, curDate.indexOf("-")));
            month = Integer.parseInt(curDate.substring(curDate.indexOf("-") + 1, curDate.lastIndexOf("-"))) - 1;
            day = Integer.parseInt(curDate.substring(curDate.lastIndexOf("-") + 1, curDate.length()));
        } catch (Exception e) {
            e.printStackTrace();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_listener:
                intent=new Intent(this,ListenerAct.class);
                startActivity(intent);
                break;
            case R.id.btn_vision:
                intent=new Intent(this,VisionAct.class);
                startActivity(intent);
                break;
            case R.id.btn_awake:
                intent=new Intent(this,AwakeAct.class);
                startActivity(intent);
                break;
            case R.id.btn_communication:
                intent=new Intent(this,CommunicateAct.class);
                startActivity(intent);
                break;
            case R.id.btn_sport:
                intent=new Intent(this,SportAct.class);
                startActivity(intent);
                break;
            case R.id.btn_say:
                intent=new Intent(this,SayAct.class);
                startActivity(intent);
                break;

        }


    }
}


