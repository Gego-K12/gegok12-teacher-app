package com.gegosoft.yourappname.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gegosoft.yourappname.Fragment.Attendance_Afternoon_Fragment;
import com.gegosoft.yourappname.Fragment.Attendance_Forenoon_Fragment;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Models.AttendanceAbsentiesModel;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AttendanceActivity extends AppCompatActivity implements Attendance_Forenoon_Fragment.SetClass {
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    List<AttendanceAbsentiesModel.Students> AbsentList;
    List<AttendanceAbsentiesModel.Students> PresentList;
    ViewPager2 viewPager2;
    ImageView back;
    TextView currentdate,standardname;
    Spinner session;
    StudentAttendanceAdapter studentAttendanceAdapter;
    String sessionname;
    String classsteacher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_layout);

        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        Intent intent=getIntent();
        classsteacher=intent.getStringExtra("classteacher");
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        viewPager2 = findViewById(R.id.attendanceviewpager);
        standardname=findViewById(R.id.attendancestn);

        session=findViewById(R.id.attendancesessionspinner);
        session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sessionname=session.getSelectedItem().toString();
                if(sessionname.equals("Forenoon")){
                    viewPager2.setCurrentItem(0);
                }
                else if(sessionname.equals("Afternoon")) {
                    viewPager2.setCurrentItem(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        currentdate=findViewById(R.id.currentdate);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        currentdate.setText(formattedDate);
        back = findViewById(R.id.attendancebackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        AbsentList = new ArrayList<>();
        PresentList = new ArrayList<>();
        studentAttendanceAdapter = new StudentAttendanceAdapter(AttendanceActivity.this, this,2);
        viewPager2.setAdapter(studentAttendanceAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                session.setSelection(position);
            }
        });
    }

    @Override
    public void setclass(String stn) {
        standardname.setText(stn);
    }

    public class StudentAttendanceAdapter extends FragmentStateAdapter {

        private Context myContext;
        int totalTabs;

        public StudentAttendanceAdapter(FragmentActivity fragmentActivity, Context context, int totalTabs) {
            super(fragmentActivity);
            myContext = context;
            this.totalTabs = totalTabs;
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    Attendance_Forenoon_Fragment attendance_forenoon_fragment = new Attendance_Forenoon_Fragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("classteacher",classsteacher);
                    attendance_forenoon_fragment.setArguments(bundle);
                    return  attendance_forenoon_fragment;

                case 1:
                    Attendance_Afternoon_Fragment attendance_afternoon_fragment = new Attendance_Afternoon_Fragment();
                    Bundle bundle1=new Bundle();
                    bundle1.putString("classteacher",classsteacher);
                    attendance_afternoon_fragment.setArguments(bundle1);
                    return attendance_afternoon_fragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return totalTabs;
        }
    }


}
