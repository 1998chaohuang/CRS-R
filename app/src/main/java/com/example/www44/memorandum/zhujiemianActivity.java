package com.example.www44.memorandum;

import static com.example.www44.memorandum.NoteDB.TABLE_NAME;
import static com.example.www44.memorandum.NoteDB.TABLE_USER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class zhujiemianActivity extends AppCompatActivity {
    private AlertDialog alertDialog;

    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private int REQUEST_PERMISSION_CODE = 1000;

    Button btn_url, btn_url2, btn_url3, btn_url4;

    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private File file;

    //请求权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                Log.e("", "requestPermission:" + "用户之前已经授予了权限！");
            } else {
                requestPermissions(permissions, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("", "申请成功");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("permission");
                builder.setMessage("点击允许才可以使用");
                builder.setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        ActivityCompat.requestPermissions(zhujiemianActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }

    private void showDialogTipUserRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhujiemian);

        btn_url = findViewById(R.id.bt1);
//        btn_url2=findViewById(R.id.bt2);
        btn_url3 = findViewById(R.id.bt3);
        btn_url4 = findViewById(R.id.bt4);
        noteDB = new NoteDB(this);
        /*
         * 通过query来实现查询
         * 查询返回的是一个游标（cursor）
         */
        dbReader = noteDB.getReadableDatabase();

        btn_url3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(zhujiemianActivity.this, "正在导出数据...", Toast.LENGTH_SHORT).show();
                export();
            }
        });

        btn_url = findViewById(R.id.bt1);

        btn_url.setOnClickListener(new View.OnClickListener() {

            //主界面的jfk按钮跳转到昏迷量表
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(zhujiemianActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

        btn_url4 = findViewById(R.id.bt4);
        btn_url4.setOnClickListener(new View.OnClickListener() {

            //主界面的各个方案表按钮跳转到方案表
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(zhujiemianActivity.this, fanganActivity.class);
                startActivity(intent);
            }

        });


    }

    private List<ExcelData> userData = new ArrayList<>();
    private Cursor cursor;

    @SuppressLint("Range")
    private void selectUser() {
        String current_sql_sel = "SELECT * FROM " + TABLE_USER;
        cursor = dbReader.rawQuery(current_sql_sel, null);
        userData.clear();
        while (cursor.moveToNext()) {
            ExcelData user = new ExcelData();
            //根据列名获取列索引
            //通过游标内部对应列名来拿到各个列的数据并显示到对应的控件中
            user.userId = cursor.getInt(cursor.getColumnIndex(NoteDB.ID)) + "";
            user.roomNum = cursor.getString(cursor.getColumnIndex(NoteDB.ROOM));
            user.name = cursor.getString(cursor.getColumnIndex(NoteDB.NAME));
            user.num = cursor.getString(cursor.getColumnIndex(NoteDB.NUM));
            user.result = (cursor.getString(cursor.getColumnIndex(NoteDB.RESULT)));
            user.reason = (cursor.getString(cursor.getColumnIndex(NoteDB.REASON)));
            user.fabingDate = (cursor.getString(cursor.getColumnIndex(NoteDB.HAPPEN_DAY)));
            user.ruyuanDate = (cursor.getString(cursor.getColumnIndex(NoteDB.HOSPITAL_DAY)));
            userData.add(user);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
        selectUser();
    }

    @SuppressLint("Range")
    private void export() {

//        String filePath = Environment.getExternalStorageDirectory() + "/AndroidExcelExport";
//        String filePath =  getApplicationContext().getFilesDir().getAbsolutePath()+"/AndroidExcelExport";

//        Context context=new Context()
//        String filePath = context.getCacheDir()+"/AndroidExcelExport";


//        String filePath = getApplicationContext().getFilesDir().getAbsolutePath()+"/AndroidExcelExport";
//        String f=getApplicationContext().getFilesDir().getAbsolutePath()+"/AndroidExcelExport";
//
//        //        Log.v("debug", "message............"+filePath);
//
//        File file = new File(filePath);
//        if (!file.exists()) {
//            file.mkdirs();
//
//        }
//        String excelFileName = "/" + System.currentTimeMillis() + "data.xls";

        file = new File(Environment.getExternalStorageDirectory().toString(),"/jkf");
        file.mkdirs();
        String excelFileName = "/" + System.currentTimeMillis() + "data.xls";


        String[] title = {"姓名", "房号", "病历号", "诊断", "病因", "发病日期", "入院日期", "登记日期", "分数"};
        String sheetName = "JFK数据";

        List<ExcelData> list = new ArrayList<>();
        for (ExcelData data : userData) {
            String current_sql_sel = "SELECT * FROM " + TABLE_NAME + " where " + NoteDB.USERID + " = " + data.userId + " ORDER BY " + NoteDB.TIME;

            Cursor cursor = dbReader.rawQuery(current_sql_sel, null);
            if (cursor.getCount()==0){
                data.writeDate="暂未登记";
                data.score="暂无登记分数";
                list.add(data);
            }
            while (cursor.moveToNext()) {
                ExcelData user = new ExcelData();
                //根据列名获取列索引
                //通过游标内部对应列名来拿到各个列的数据并显示到对应的控件中
                user.userId = data.userId;
                user.roomNum = data.roomNum;
                user.name = data.name;
                user.num = data.num;
                user.result = data.result;
                user.reason = data.reason;
                user.fabingDate = data.fabingDate;
                user.ruyuanDate = data.ruyuanDate;
                user.score = cursor.getString(cursor.getColumnIndex(NoteDB.SCORE));
                user.writeDate = cursor.getString(cursor.getColumnIndex(NoteDB.TIME));
                list.add(user);
            }
        }


//        filePath = filePath + excelFileName;
//
//        ExcelUtil.initExcel(filePath, sheetName, title);
//
////
//        ExcelUtil.writeObjListToExcel(list, filePath, this);
        String f= file.toString();
        f = f + excelFileName;
        ExcelUtil.initExcel(f,sheetName ,title);


        ExcelUtil.writeObjListToExcel(list,f, this);

    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdirs();
    }

    public String getSDPath() {
        File sdDir = null;
//        String filepath;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.DIRECTORY_DOWNLOADS);
        if (sdCardExist) {
            sdDir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        String dir = sdDir.toString();
        return dir;

    }


}
