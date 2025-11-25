package com.gegosoft.yourappname.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gegosoft.yourappname.Fragment.StudentsCompletedAssignmentFragment;
import com.gegosoft.yourappname.Fragment.StudentsSubmittedAssignmentFragment;
import com.gegosoft.yourappname.R;

public class StudentsAssignmentActivity extends AppCompatActivity {
    Toolbar toolbar;
    RadioGroup radiogroup;
ViewPager2 viewPager2;
    String assignmentid;
    Intent intent;
    ImageView backbtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentsassignment_tab_layout);
        radiogroup = findViewById(R.id.radiogroup);
        viewPager2 = findViewById(R.id.viewpager);
        toolbar=findViewById(R.id.toolbar);

        backbtn = findViewById(R.id.backbtn_assignemtlist);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


        intent=getIntent();
        assignmentid=intent.getStringExtra("assignmentid");



        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio0){
                    viewPager2.setCurrentItem(0);

                }
                else if (checkedId==R.id.radio1){
                    viewPager2.setCurrentItem(1);

                }
            }
        });
        StudentdAssignmentAdapter studentdAssignmentAdapter = new StudentdAssignmentAdapter(this,this,2);
        viewPager2.setAdapter(studentdAssignmentAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int currentItem = viewPager2.getCurrentItem();
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



    }
    public class StudentdAssignmentAdapter extends FragmentStateAdapter {

        private Context myContext;
        int totalTabs;
        public StudentdAssignmentAdapter(FragmentActivity fragmentActivity, Context context, int totalTabs) {
            super(fragmentActivity);
            myContext = context;
            this.totalTabs = totalTabs;
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    StudentsSubmittedAssignmentFragment studentsSubmittedAssignmentFragment=new StudentsSubmittedAssignmentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("assignmentid", assignmentid);
                    studentsSubmittedAssignmentFragment.setArguments(bundle);
                    return studentsSubmittedAssignmentFragment;

                case 1:
                    StudentsCompletedAssignmentFragment studentsCompletedAssignmentFragment=new StudentsCompletedAssignmentFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("assignmentid", assignmentid);
                    studentsCompletedAssignmentFragment.setArguments(bundle1);
                    return studentsCompletedAssignmentFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return totalTabs;
        }

    }
}
