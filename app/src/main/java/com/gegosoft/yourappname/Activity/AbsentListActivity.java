package com.gegosoft.yourappname.Activity;

import android.content.Context;
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

import com.gegosoft.yourappname.Fragment.Absentlist_Afternoon_Fragment;
import com.gegosoft.yourappname.Fragment.Absentlist_Forenoon_Fragment;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AbsentListActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ImageView back;
    TextView currentdate;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    Spinner session;
    String sessionname;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absentlist);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");

        session=findViewById(R.id.absentspinner);
        viewPager2 = findViewById(R.id.absentviewpager);

        session=findViewById(R.id.absentspinner);
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
        back = findViewById(R.id.absentbackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        final StudentAbsentAdapter studentAttendanceAdapter = new StudentAbsentAdapter(AbsentListActivity.this, AbsentListActivity.this, 2);
        viewPager2.setAdapter(studentAttendanceAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                session.setSelection(position);
            }
        });
    }

    public class StudentAbsentAdapter extends FragmentStateAdapter {

        private Context myContext;
        int totalTabs;

        public StudentAbsentAdapter(FragmentActivity fragmentActivity, Context context, int totalTabs) {
            super(fragmentActivity);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new Absentlist_Forenoon_Fragment();

                case 1:
                    return new Absentlist_Afternoon_Fragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return totalTabs;
        }
    }
}
