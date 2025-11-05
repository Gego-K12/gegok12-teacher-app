package com.gegosoft.schoolteacherapp.Activity;

import android.content.Context;
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

import com.gegosoft.schoolteacherapp.Fragment.CompletedHomework_Fragment;
import com.gegosoft.schoolteacherapp.Fragment.PendingHomework_Fragment;
import com.gegosoft.schoolteacherapp.R;

public class HomeWorkActivity extends AppCompatActivity {
    Toolbar toolbar;
    RadioGroup radiogroup;
    ViewPager2 viewPager2;
    ImageView backbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        radiogroup = findViewById(R.id.radiogroup);
        viewPager2 = findViewById(R.id.viewpager);
        toolbar=findViewById(R.id.toolbar);

        backbtn = findViewById(R.id.backbtn_homework);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

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

        HomeWorkAdapter homeWorkAdapter = new HomeWorkAdapter(this,this,2);
        viewPager2.setAdapter(homeWorkAdapter);

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

  public class HomeWorkAdapter extends FragmentStateAdapter {

        private Context myContext;
        int totalTabs;
        public HomeWorkAdapter(FragmentActivity fragmentActivity, Context context, int totalTabs) {
            super(fragmentActivity);
            myContext = context;
            this.totalTabs = totalTabs;
        }
      @NonNull
      @Override
      public Fragment createFragment(int position) {
          switch (position) {
              case 0:
                  PendingHomework_Fragment pendingHomework_fragment=new PendingHomework_Fragment();
                  return pendingHomework_fragment;
              case 1:
                  CompletedHomework_Fragment completedHomework_fragment= new CompletedHomework_Fragment();
                  return completedHomework_fragment;
          }
          return null;
      }

      @Override
      public int getItemCount() {
          return totalTabs;
      }
  }
}
