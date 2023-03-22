package com.example.www44.memorandum;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;

/**
 * 建档
 */
public class CreatFileActivity extends AppCompatActivity {

    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbwriter;

    private Cursor cursor;
    private long rowId;
    private TextView  tvHappenDay, tvHospitaDay;
    private EditText etName, etResult, etReason,etRoom,etNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_creatfile);
        //以写的方式打开database 打开数据库 待会要添加数据
        noteDB = new NoteDB(this);
        dbwriter = noteDB.getWritableDatabase();
        setActionBar();
        initView();
    }

    private void initView() {
        etRoom = findViewById(R.id.et_room);
        etNum = findViewById(R.id.tv_num);
        etName = findViewById(R.id.et_name);
        etReason = findViewById(R.id.et_reason);
        etResult = findViewById(R.id.et_result);
        tvHappenDay = findViewById(R.id.tv_happen_day);
        tvHospitaDay = findViewById(R.id.tv_hospital_day);
        tvHappenDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在点击事件部分调用，chooseBirthday为点击事件的控件
                showDatePickDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tvHappenDay.setText(year + " - " + (month + 1) + " - " + day );
                    }
                }, tvHappenDay.getText().toString());
            }
        });

        tvHospitaDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在点击事件部分调用，chooseBirthday为点击事件的控件
                showDatePickDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tvHospitaDay.setText(year + " - " + (month + 1) + " - " + day );
                    }
                }, tvHospitaDay.getText().toString());
            }
        });

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
        actionBar.setTitle("病人建档");
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
        if (TextUtils.isEmpty(etResult.getText().toString())) {
            Toast.makeText(this, etResult.getHint().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etRoom.getText().toString())) {
            Toast.makeText(this, etResult.getHint().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etNum.getText().toString())) {
            Toast.makeText(this, etResult.getHint().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etReason.getText().toString())) {
            Toast.makeText(this, etReason.getHint().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tvHospitaDay.getText().toString())) {
            Toast.makeText(this, tvHospitaDay.getHint().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tvHappenDay.getText().toString())) {
            Toast.makeText(this, tvHappenDay.getHint().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSubmit()) {
            Toast.makeText(this, "该病人已建档~", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存一条数据
        addDB();
        Toast.makeText(this, rowId != -1 ? "建档成功" : "保存失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void addDB() {
        dbwriter = noteDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.ROOM, etRoom.getText().toString());
        cv.put(NoteDB.NAME, etName.getText().toString());
        cv.put(NoteDB.REASON, etReason.getText().toString());
        cv.put(NoteDB.NUM, etNum.getText().toString());
        cv.put(NoteDB.RESULT, etResult.getText().toString());
        cv.put(NoteDB.HAPPEN_DAY, tvHappenDay.getText().toString());
        cv.put(NoteDB.HOSPITAL_DAY, tvHospitaDay.getText().toString());

        /*
         * insert方法得参数列表：
         *              第一个为数据库表名，
         *              第二个为希望插入值为空的列名（如果插入值没有为空得列，全是有数据得就写null），
         *              第三个为一个ContentValues对象   键值对
         *  插入操作会返回以一个行id值提示是否插入成功
         */
        rowId = dbwriter.insert(NoteDB.TABLE_USER, null, cv);

        //操作完成关闭数据库
        dbwriter.close();
    }


    //数据库查询 查出病人是否已经登记建档
    public boolean isSubmit() {
        /*
         * 通过query来实现查询
         * 查询返回的是一个游标（cursor）
         */
        boolean isSubmit = false;
        dbReader = noteDB.getReadableDatabase();
        cursor = dbReader.query(noteDB.TABLE_USER, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(NoteDB.NAME));
                if (etName.getText().toString().equals(name)) {
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

    private static Random strGen = new Random();
    private static Random numGen = new Random();
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();;
    private static char[] numbers = ("0123456789").toCharArray();;
    /** * 产生随机字符串 * */
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[strGen.nextInt(61)];
        }
        return new String(randBuffer);
    }

    /** * 产生随机数值字符串 * */
    public static final String randomNumStr(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbers[numGen.nextInt(9)];
        }
        return new String(randBuffer);
    }
}


