package com.example.administrator.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/1.
 */
// task添加页事件处理类
//  点击FAB后跳转到此Activity

public class AddTaskActivity extends AppCompatActivity{
    private String title, details;
    private Date ddl, remind_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        setClicked();
    }

    // 设置控件监听器
    private void setClicked() {
        final EditText editTitle = (EditText)findViewById(R.id.edit_title);
        final TextView textDDL = (TextView)findViewById(R.id.text_ddl);
        TextView textSetDDL = (TextView)findViewById(R.id.text_set_ddl);
        final TextView textRemindTime = (TextView)findViewById(R.id.text_remind_time);
        final Switch switch_remind = (Switch)findViewById(R.id.switch_remind);
        final EditText editDetails = (EditText)findViewById(R.id.edit_details);
        TextView textCancel = (TextView)findViewById(R.id.text_cancel);
        TextView textConfirm = (TextView)findViewById(R.id.text_confirm);

        // 点击设置时间 显示dialog
        textSetDDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                Date today = new Date();
                c.setTime(today);
                int year =  c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dateDialog = new DatePickerDialog(AddTaskActivity.this,
                       null, year, month, day);
                dateDialog.setMessage("选择日期");
                dateDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "设置",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatePicker picker = dateDialog.getDatePicker();
                                int mYear = picker.getYear();
                                int mMonth = picker.getMonth();
                                int mDay = picker.getDayOfMonth();
                                Calendar calendar = Calendar.getInstance();
                                // deadline是精确到天的；所以分，秒，毫秒都是0
                                calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), 0, 0, 0);
                                ddl = calendar.getTime();
                                String ss = "";
                                ss += String.valueOf(mYear) + "-";
                                // month是从0-11计算的，故值为9的时候实际表示10月，小于9则补零
                                if (mMonth < 9) ss += "0";
                                ss += String.valueOf(mMonth + 1) + "-";
                                // day小于10补0
                                if (mDay < 10) ss += "0";
                                ss += String.valueOf(mDay);
                                textDDL.setText(ss);
                            }
                        });
                dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dateDialog.show();
            }
        });

        //设置提醒时间
        switch_remind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Calendar c = Calendar.getInstance();
                    Date today = new Date();
                    c.setTime(today);
                    int year =  c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    final int hour = c.get(Calendar.HOUR_OF_DAY);
                    final int minute = c.get(Calendar.MINUTE);

                    final DatePickerDialog dateDialog = new DatePickerDialog(AddTaskActivity.this,
                            null, year, month, day);
                    dateDialog.setMessage("选择日期");
                    dateDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePicker picker = dateDialog.getDatePicker();
                                    final Calendar mCalendar = Calendar.getInstance();
                                    mCalendar.set(Calendar.YEAR, picker.getYear());
                                    mCalendar.set(Calendar.MONTH, picker.getMonth());
                                    mCalendar.set(Calendar.DAY_OF_MONTH, picker.getDayOfMonth());

                                    //  !!!此处BUG： 此函数被系统屏蔽，选择时间后无法回调
                                    //  故当前版本无法选择时钟时间
                                    //  未找到解决方法  待解决
                                    final TimePickerDialog timeDialog = new TimePickerDialog(AddTaskActivity.this,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                                    mCalendar.set(Calendar.HOUR, i);
                                                    mCalendar.set(Calendar.MINUTE, i1);
                                                    Log.e("timechanged", "11111");
                                                }
                                            }, hour, minute, true);

                                    timeDialog.setMessage("选择时间");
                                    timeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "设置",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    remind_time = mCalendar.getTime();
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                    textRemindTime.setText(format.format(remind_time));
                                                }
                                            });
                                    timeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch_remind.setChecked(false);
                                                    textRemindTime.setText("关");
                                                    remind_time = null;
                                                }
                                            });
                                    timeDialog.show();

                                }
                            });
                    dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch_remind.setChecked(false);
                                    textRemindTime.setText("关");
                                    remind_time = null;
                                }
                            });
                    dateDialog.show();

                } else {
                    textRemindTime.setText("关");
                    remind_time = null;
                }
            }
        });

        //  确认按钮点击事件
        textConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTitle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "任务栏不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (ddl == null) {
                    Toast.makeText(getApplicationContext(), "需要设定到期时间",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // EventBus传递信息到TaskFragment
                    EventBus.getDefault().post(new MessageEvent(
                            editTitle.getText().toString(),
                            editDetails.getText().toString(),
                            ddl, remind_time));
                    Toast.makeText(getApplicationContext(), "添加成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        // 取消按钮点击事件
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消时弹出对话框提示
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddTaskActivity.this);
                alertDialog.setTitle("取消编辑")
                        .setMessage("是否要放弃此次编辑？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            //  放弃更改，返回
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }
}
