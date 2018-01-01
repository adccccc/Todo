package com.example.administrator.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/1.
 */

public class TaskFragment extends Fragment {

    View view;
    private myDatabase db;
    private SimpleAdapter adapter_1, adapter_3, adapter_7, adapter_others;
    private ListView list_day1, list_day3, list_day7, list_other;
    private List<Map<String, Object>> mData_overdue = new ArrayList<>();
    private List<Map<String, Object>> mData_1 = new ArrayList<>();
    private List<Map<String, Object>> mData_3 = new ArrayList<>();
    private List<Map<String, Object>> mData_7 = new ArrayList<>();
    private List<Map<String, Object>> mData_other = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        setDatas();
        setClicked();
        setListViews();

        return view;
    }

    private void setDatas() {
        db = new myDatabase(getActivity());
        ArrayList<TaskInstance> list = db.getAllTasks();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("id", list.get(i).getId());
                temp.put("title", list.get(i).getTitle());
                temp.put("detail", list.get(i).getDetails());
                temp.put("deadline", list.get(i).getDeadline());
                temp.put("remind_time", list.get(i).getRemind_time());
                temp.put("completed", list.get(i).getCompleted() ? "√" : "--");
                Date date = list.get(i).getDeadline();
                Date current = new Date();
                int s = 24*3600*1000;
                long daysNum = date.getTime() / s;
                long daysCur = current.getTime() / s;
                if (daysNum < daysCur) {
                    mData_overdue.add(temp);
                } else if (daysNum == daysCur) {
                    mData_1.add(temp);
                } else if (daysNum - daysCur < 3) {
                    mData_3.add(temp);
                } else if (daysNum - daysCur < 7) {
                    mData_7.add(temp);
                } else {
                    mData_other.add(temp);
                }
            }
        }
    }
    private void setListViews() {
        list_day1 = (ListView)view.findViewById(R.id.list_today);
        list_day3 = (ListView)view.findViewById(R.id.list_threedays);
        list_day7 = (ListView)view.findViewById(R.id.list_weeks);
        list_other = (ListView)view.findViewById(R.id.list_others);

        adapter_1 = new SimpleAdapter(getActivity(), mData_1, R.layout.item_task,
                new String[]{"title", "completed"},
                new int[] {R.id.text_item_title, R.id.text_item_isCompleted});
        list_day1.setAdapter(adapter_1);

        adapter_3 = new SimpleAdapter(getActivity(), mData_3, R.layout.item_task,
                new String[]{"title", "completed"},
                new int[] {R.id.text_item_title, R.id.text_item_isCompleted});
        list_day3.setAdapter(adapter_3);

        adapter_7 = new SimpleAdapter(getActivity(), mData_7, R.layout.item_task,
                new String[]{"title", "completed"},
                new int[] {R.id.text_item_title, R.id.text_item_isCompleted});
        list_day7.setAdapter(adapter_7);

        adapter_others = new SimpleAdapter(getActivity(), mData_other, R.layout.item_task,
                new String[]{"title", "completed"},
                new int[] {R.id.text_item_title, R.id.text_item_isCompleted});
        list_other.setAdapter(adapter_others);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };
        AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        };

        list_day1.setOnItemClickListener(clickListener);
        list_day1.setOnItemLongClickListener(longClickListener);
        list_day3.setOnItemClickListener(clickListener);
        list_day3.setOnItemLongClickListener(longClickListener);
        list_day7.setOnItemClickListener(clickListener);
        list_day7.setOnItemLongClickListener(longClickListener);
        list_other.setOnItemClickListener(clickListener);
        list_other.setOnItemLongClickListener(longClickListener);
    }
    private void setClicked() {
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

}
