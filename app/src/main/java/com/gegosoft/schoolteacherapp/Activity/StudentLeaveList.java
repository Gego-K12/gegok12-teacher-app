package com.gegosoft.schoolteacherapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gegosoft.schoolteacherapp.Fragment.CompletedStudentLeave_Fragment;
import com.gegosoft.schoolteacherapp.Fragment.PendingStudentLeave_Fragment;
import com.gegosoft.schoolteacherapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudentLeaveList extends AppCompatActivity implements PendingStudentLeave_Fragment.ChangeCompletedStatus {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ImageView back;
    StudentLeaveAdapter studentLeaveAdapter;
    String[] labels = {"Submitted Leave","Completed Leave"};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentleave_tab_layout);
        tabLayout=findViewById(R.id.studentleavetablayout);
        viewPager2=findViewById(R.id.studentleaveviewpager);
        back=findViewById(R.id.studentleavebackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        studentLeaveAdapter =new StudentLeaveAdapter(this,StudentLeaveList.this,labels);
        viewPager2.setAdapter(studentLeaveAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(labels[position])).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void changestatus() {
        studentLeaveAdapter.notifyDataSetChanged();
    }

    public class StudentLeaveAdapter extends FragmentStateAdapter {

        private Context myContext;
        String[] labels;
        public StudentLeaveAdapter(FragmentActivity fragmentActivity, Context context, String[] labels) {
            super(fragmentActivity);
            myContext = context;
            this.labels = labels;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new PendingStudentLeave_Fragment();

                case 1:
                    return new CompletedStudentLeave_Fragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return labels.length;
        }
    }

}
