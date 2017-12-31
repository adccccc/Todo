package com.example.administrator.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/1.
 */

public class AddTaskActivity extends AppCompatActivity{
    private String title, details;
    private Date ddl, remind_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        setClicked();
    }

    private void setClicked() {
        EditText editTitle = (EditText)findViewById(R.id.edit_title);
        final TextView textDDL = (TextView)findViewById(R.id.text_ddl);
        TextView textSetDDL = (TextView)findViewById(R.id.text_set_ddl);
        final TextView textRemindTime = (TextView)findViewById(R.id.text_remind_time);
        final Switch switch_remind = (Switch)findViewById(R.id.switch_remind);
        EditText editDetails = (EditText)findViewById(R.id.edit_details);
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
                                ddl = new Date(mYear, mMonth, mDay);
                                String ss = "";
                                ss += String.valueOf(mYear) + "-";
                                if (mMonth < 9) ss += "0";
                                ss += String.valueOf(mMonth + 1) + "-";
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

            }
        });

        // 取消按钮点击事件
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
