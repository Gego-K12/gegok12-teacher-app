package com.gegosoft.yourappname.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gegosoft.yourappname.Fragment.MyActiveTask_Fragment;
import com.gegosoft.yourappname.Fragment.MyCompletedTask_Fragment;
import com.gegosoft.yourappname.R;

public class To_Do_List_Activity extends AppCompatActivity {

    RadioGroup radiogroup;
    ViewPager2 viewpager2;
    private static int REQUEST_TASK = 1;
    ImageView back;
    Toolbar toolbar;
    TodoListAdapter todoListAdapter;
    ImageView add_task_btn;
    AppCompatSpinner selection_of_views;
    RadioButton active, completed;

    private ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_layout);
        radiogroup = findViewById(R.id.radiogroup);
        add_task_btn = findViewById(R.id.add_task_btn);
        toolbar = findViewById(R.id.toolbar);
        viewpager2 = findViewById(R.id.viewpager);
        back = findViewById(R.id.back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        active = findViewById(R.id.radio0);
        completed = findViewById(R.id.radio1);


        String[] data = {"Self", "Others"};
        selection_of_views = findViewById(R.id.assignedby_ids);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);

        selection_of_views.setAdapter(adapter);

        selection_of_views.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    todoListAdapter = new TodoListAdapter(To_Do_List_Activity.this, To_Do_List_Activity.this, 2, "Self");
                    viewpager2.setAdapter(todoListAdapter);
                    radiogroup.check(R.id.radio0);
                } else if (position == 1) {
                    todoListAdapter = new TodoListAdapter(To_Do_List_Activity.this, To_Do_List_Activity.this, 2, "Others");
                    viewpager2.setAdapter(todoListAdapter);
                    radiogroup.check(R.id.radio0);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    viewpager2.setCurrentItem(0);

                } else if (checkedId == R.id.radio1) {
                    viewpager2.setCurrentItem(1);

                }
            }
        });
        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskLauncher.launch(new Intent(To_Do_List_Activity.this, ToDoList_AddTask_Activity.class));
            }
        });
        todoListAdapter = new TodoListAdapter(this, this, 2, "self");
        viewpager2.setAdapter(todoListAdapter);
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int currentItem = viewpager2.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        radiogroup.check(R.id.radio0);
                        break;
                    case 1:
                        radiogroup.check(R.id.radio1);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
    public class TodoListAdapter extends FragmentStateAdapter {

        private Context myContext;
        int totalTabs;
        private String type;

        public TodoListAdapter(FragmentActivity fragmentActivity, Context context, int totalTabs, String type) {
            super(fragmentActivity);
            myContext = context;
            this.totalTabs = totalTabs;
            this.type = type;
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            switch (position) {
                case 0:
                    MyActiveTask_Fragment myActiveTask_fragment = new MyActiveTask_Fragment();
                    myActiveTask_fragment.setArguments(bundle);
                    return myActiveTask_fragment;

                case 1:
                    MyCompletedTask_Fragment myCompletedTask_fragment = new MyCompletedTask_Fragment();
                    myCompletedTask_fragment.setArguments(bundle);
                    return myCompletedTask_fragment;

                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        }

        @Override
        public int getItemCount() {
            return totalTabs;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TASK && resultCode == RESULT_OK) {
            viewpager2.setCurrentItem(0);
            radiogroup.check(R.id.radio0);
            TodoListAdapter todoListAdapter = new TodoListAdapter(To_Do_List_Activity.this, this, 2, "self");
            viewpager2.setAdapter(todoListAdapter);
        }

    }
}