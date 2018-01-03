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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/1.
 */
// 任务Fragment的事件处理类
// 主要逻辑都在这里

public class TaskFragment extends Fragment {

    View view;
    private myDatabase db;

    // 4个栏目对应4个listView
    /* 所以有4个datas （还有一个data_overDue是用来保存过期的Task的，
        它的listView不在这个Fragment，目前想法是弄一个新的页面来显示过期的Task）*/
    // 所以还有4个adapter
    // 这是一种很蠢的实现方法
    // 不过没有办法 我不会RecyclerView的嵌套

    private SimpleAdapter adapter_1, adapter_3, adapter_7, adapter_others;
    private ListView list_day1, list_day3, list_day7, list_other;
    private List<Map<String, Object>> mData_overdue;
    private List<Map<String, Object>> mData_1;
    private List<Map<String, Object>> mData_3;
    private List<Map<String, Object>> mData_7;
    private List<Map<String, Object>> mData_other;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        setDatas();
        setClicked();
        setListViews();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }
    //从数据库获取所有Task，根据DDL分类放到各个datas中
    private void setDatas() {
        db = new myDatabase(getActivity());
        ArrayList<TaskInstance> list = db.getAllTasks();
        mData_1 = new ArrayList<>();
        mData_3 = new ArrayList<>();
        mData_7 = new ArrayList<>();
        mData_other = new ArrayList<>();
        mData_overdue = new ArrayList<>();

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
    // 配置各ListView
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

        // 所有ListView共用两个监听器
        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2018/1/4
                // 点击Item 跳转到详细信息页面 或者直接弹出Dialog显示详细信息？
                // 待设计
            }
        };
        AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2018/1/4
                // 长按出现弹出式菜单，选项： [标记为已完成/未完成]，[删除此任务]   (还有其它选项可以自行添加)
                // 标记完成的处理函数：要更新数据库，同时要更新mDatas的completed值，已完成则为"√"，未完成则为"--"，然后notifyDatachanged
                // 删除任务的处理函数：根据id删除数据库中的Task，同时删除mDatas中对应的值，然后notify
                // 待完成

                return false;
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
    // FAB监听器以及4个栏目的click监听
    private void setClicked() {
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // 点击栏目，显示/隐藏对应的listView；
        View layout1 = view.findViewById(R.id.layout_day1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_day1.getVisibility() == View.VISIBLE) {
                    list_day1.setVisibility(View.GONE);
                } else {
                    list_day1.setVisibility(View.VISIBLE);
                }
            }
        });
        View layout3 = view.findViewById(R.id.layout_day3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_day3.getVisibility() == View.VISIBLE) {
                    list_day3.setVisibility(View.GONE);
                } else {
                    list_day3.setVisibility(View.VISIBLE);
                }
            }
        });
        View layout7 = view.findViewById(R.id.layout_day7);
        layout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_day7.getVisibility() == View.VISIBLE) {
                    list_day7.setVisibility(View.GONE);
                } else {
                    list_day7.setVisibility(View.VISIBLE);
                }
            }
        });
        View layout_others = view.findViewById(R.id.layout_dayothers);
        layout_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_other.getVisibility() == View.VISIBLE) {
                    list_other.setVisibility(View.GONE);
                } else {
                    list_other.setVisibility(View.VISIBLE);
                }
            }
        });

        // TODO: 2018/1/4
        // 添加需求：在隐藏listView状态下，对应栏目要显示此listView的count数(一个带圈圈背景的数字，修改TextView的背景即可实现)
        // 显示listView状态下，对应栏目显示一个“收起”符号 （例如：▽）
        // 实现原理：将栏目右边的textView的文本改成对应要显示的值即可
    }

    // 处理由EventBus传递回来的信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        myDatabase db = new myDatabase(getActivity());
        db.insert(event.getTitle(), event.getDetails(), event.getDdl(), event.getRemind_time(), false);
        int _id = db.getNewestID();
        Map<String, Object> temp = new HashMap<>();
        temp.put("id", _id);
        temp.put("title", event.getTitle());
        temp.put("detail", event.getDetails());
        temp.put("deadline", event.getDdl());
        temp.put("remind_time", event.getRemind_time());
        temp.put("completed", "--");

        Date date = event.getDdl();
        Date current = new Date();
        int s = 24*3600*1000;
        long daysNum = date.getTime() / s;
        long daysCur = current.getTime() / s;
        if (daysNum < daysCur) {
            mData_overdue.add(temp);
        } else if (daysNum == daysCur) {
            mData_1.add(temp);
            adapter_1.notifyDataSetChanged();
        } else if (daysNum - daysCur < 3) {
            mData_3.add(temp);
            adapter_3.notifyDataSetChanged();
        } else if (daysNum - daysCur < 7) {
            mData_7.add(temp);
            adapter_7.notifyDataSetChanged();
        } else {
            mData_other.add(temp);
            adapter_others.notifyDataSetChanged();
        }
    }
}
