package com.example.genshinimpactcookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genshinimpactcookbook.domain.MultiplyDivideTasks;
import com.example.genshinimpactcookbook.domain.XXSCalcResult;
import com.example.genshinimpactcookbook.utils.SQLiteOperator;

import java.util.ArrayList;
import java.util.List;

public class XXSCalcStimulate extends AppCompatActivity {

    private List<MultiplyDivideTasks> tasks = new ArrayList<>();
    private int winRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xxscalc_stimulate);
        initWinRecord();
        init(0);
    }

    @SuppressLint("Range")
    private void initWinRecord(){
        SQLiteOpenHelper instance = SQLiteOperator.getInstance(this);
        SQLiteDatabase wdb = instance.getWritableDatabase();
        SQLiteDatabase rdb = instance.getReadableDatabase();
        String initTable = "create table if not exists right_record(_id integer primary key autoincrement, record text)";
        wdb.execSQL(initTable);
        String sql = "select record from right_record where _id = 1";
        @SuppressLint("Recycle") Cursor cursor = rdb.rawQuery(sql, null);
        if(cursor==null){
            String addRec = "insert into right_record (record) values (0)";
            wdb.execSQL(addRec);
            wdb.close();
            this.winRecord = 0;
            System.out.println(this.winRecord);
        }else{
            while (cursor.moveToNext()){
                this.winRecord = cursor.getInt(cursor.getColumnIndex("record"));
            }
            cursor.close();
        }
        rdb.close();
    }

    private void taskCreate(int index, int id){
        StringBuilder sb = new StringBuilder();
        MultiplyDivideTasks buf = this.tasks.get(index);
        sb.append(buf.getNum1());
        if(buf.getOperator()==0)
            sb.append("*");
        else
            sb.append("/");
        sb.append(buf.getNum2());
        TextView view = findViewById(id);
        view.setText(sb.toString());
    }

    private void clearEditText(int id){
        EditText et = findViewById(id);
        et.setText("");
    }

    @SuppressLint("DefaultLocale")
    private void init(int addedRightTasksNum){
        this.winRecord+=addedRightTasksNum;
        this.tasks.clear();
        SQLiteOpenHelper instance = SQLiteOperator.getInstance(this);
        SQLiteDatabase db = instance.getReadableDatabase();
        if(db.isOpen()){
            String sql = "select * from tasks order by RANDOM() limit 5";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") int num1 = cursor.getInt(cursor.getColumnIndex("num1"));
                @SuppressLint("Range") int num2 = cursor.getInt(cursor.getColumnIndex("num2"));
                @SuppressLint("Range") int operator = cursor.getInt(cursor.getColumnIndex("operator"));
                @SuppressLint("Range") int result = cursor.getInt(cursor.getColumnIndex("result"));
                @SuppressLint("Range") int exceed = cursor.getInt(cursor.getColumnIndex("exceed"));
                MultiplyDivideTasks task = new MultiplyDivideTasks(_id, num1,  num2, operator, result, exceed);
                this.tasks.add(task);
            }
            cursor.close();
        }
        taskCreate(0, R.id.tv_11);
        taskCreate(1, R.id.tv_21);
        taskCreate(2, R.id.tv_31);
        taskCreate(3, R.id.tv_41);
        taskCreate(4, R.id.tv_51);
        db.close();
        SQLiteDatabase wdb = instance.getWritableDatabase();
        clearEditText(R.id.et_12);
        clearEditText(R.id.et_13);
        clearEditText(R.id.et_22);
        clearEditText(R.id.et_23);
        clearEditText(R.id.et_32);
        clearEditText(R.id.et_33);
        clearEditText(R.id.et_42);
        clearEditText(R.id.et_43);
        clearEditText(R.id.et_52);
        clearEditText(R.id.et_53);
        String addNewWinNum = String.format("update right_record set record = %d where _id = 1", this.winRecord);
        wdb.execSQL(addNewWinNum);
        wdb.close();
//        initWinRecord();

        TextView tv_end = findViewById(R.id.tv_end);
        tv_end.setText(String.format("累计答对%d题，针不戳！", this.winRecord));
    }

    public void createNewTasks(View view) {
        SQLiteOpenHelper instance = SQLiteOperator.getInstance(this);
        SQLiteDatabase db = instance.getWritableDatabase();
        if(db.isOpen()){
            for(int i=0;i<80;++i){
                int num1 = (int)(Math.random()*(100-50+1)+50);
                int num2 = (int)(Math.random()*(40-2+1)+2);
                int operator = (int)(Math.random()*(2));
                if(operator==0){
                    int result = num1*num2;
                    int exceed = 0;
                    @SuppressLint("DefaultLocale") String sql = String.format("insert into tasks(num1, num2, operator, result, exceed) values (%d, %d, %d, %d, %d)", num1, num2,operator, result, exceed);
                    db.execSQL(sql);
                }else{
                    int result = num1/num2;
                    int exceed = num1%num2;
                    @SuppressLint("DefaultLocale") String sql = String.format("insert into tasks(num1, num2, operator, result, exceed) values (%d, %d, %d, %d, %d)", num1, num2,operator, result, exceed);
                    db.execSQL(sql);
                }
            }
        }
        db.close();
    }

    public void renewCurrentTasks(View view) {
        this.tasks.clear();
        SQLiteOpenHelper instance = SQLiteOperator.getInstance(this);
        SQLiteDatabase db = instance.getReadableDatabase();
        if(db.isOpen()){
            String sql = "select * from tasks order by RANDOM() limit 5";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") int num1 = cursor.getInt(cursor.getColumnIndex("num1"));
                @SuppressLint("Range") int num2 = cursor.getInt(cursor.getColumnIndex("num2"));
                @SuppressLint("Range") int operator = cursor.getInt(cursor.getColumnIndex("operator"));
                @SuppressLint("Range") int result = cursor.getInt(cursor.getColumnIndex("result"));
                @SuppressLint("Range") int exceed = cursor.getInt(cursor.getColumnIndex("exceed"));
                MultiplyDivideTasks task = new MultiplyDivideTasks(_id, num1,  num2, operator, result, exceed);
                this.tasks.add(task);
            }
            cursor.close();
        }
        taskCreate(0, R.id.tv_11);
        taskCreate(1, R.id.tv_21);
        taskCreate(2, R.id.tv_31);
        taskCreate(3, R.id.tv_41);
        taskCreate(4, R.id.tv_51);
        db.close();
    }

    private XXSCalcResult getResult(int id1, int id2){
        EditText et1 = findViewById(id1);
        EditText et2 = findViewById(id2);
        if(TextUtils.isEmpty(et2.getText().toString())){
//            System.out.println(new XXSCalcResult(Integer.parseInt(et1.getText().toString()), 0));
            return new XXSCalcResult(Integer.parseInt(et1.getText().toString()), 0);
        }else{
//            System.out.println(new XXSCalcResult(Integer.parseInt(et1.getText().toString()), Integer.parseInt(et2.getText().toString())));
            return new XXSCalcResult(Integer.parseInt(et1.getText().toString()), Integer.parseInt(et2.getText().toString()));
        }
    }

    public void submit(View view) {
        int correct = 0;
        List<XXSCalcResult> results = new ArrayList<>();
        results.add(getResult(R.id.et_12, R.id.et_13));
        results.add(getResult(R.id.et_22, R.id.et_23));
        results.add(getResult(R.id.et_32, R.id.et_33));
        results.add(getResult(R.id.et_42, R.id.et_43));
        results.add(getResult(R.id.et_52, R.id.et_53));
        System.out.println(results);
        System.out.println(this.tasks);
        for(int i=0;i<5;++i){
            if(this.tasks.get(i).getResult()==results.get(i).getResult()
            &&this.tasks.get(i).getExceed()==results.get(i).getExceed()){
                ++correct;
            }
        }
        Toast.makeText(this, "一共做对"+correct+"题，继续加油！", Toast.LENGTH_SHORT).show();
        init(correct);
    }
}