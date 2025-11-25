package com.gegosoft.yourappname.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gegosoft.yourappname.Fragment.ProgressDialogFragment;
import com.gegosoft.yourappname.Fragment.TimeTable_Mon_Fragment;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Helper.PurchaseHelper;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.TImeTable_Model;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableActivity extends AppCompatActivity {
    UserDetailsSharedPref userDetailsSharedaPref;
    Api api;
    Map<String,String> headermap;
    TabLayout timetabletabLayout;
    ImageView back;
    TImeTable_Model timeTableModels;
    JSONObject dayObj;
    JSONArray day_arr;
    ViewPager2 viewPager2;
    FrameLayout contentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout);
        userDetailsSharedaPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedaPref.getString("token"));
        headermap.put("Accept","application/json");
        timetabletabLayout=findViewById(R.id.timetabletablayout);
        viewPager2 = findViewById(R.id.timetableviewpager);
        contentContainer = findViewById(R.id.contentContainer);

        ActionBar actionBar = getSupportActionBar();
        back=findViewById(R.id.timetableback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        timetabletabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        if (isTimeTablePurchased()) {
            gettimetable();
        } else {
            showPurchaseCard();
        }
    }
    private boolean isTimeTablePurchased() {
        return userDetailsSharedaPref.getBoolean("timetable", false);
    }
    private void gettimetable(){
        api = ApiClient.getClient().create(Api.class);
        final ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("message","Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(),"loading screen");
        Call<ResponseBody>tImeTable_modelCall=api.gettimetablemodel(headermap);
        tImeTable_modelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            timeTableModels = new Gson().fromJson(jsonObject.toString(),
                                    new TypeToken<TImeTable_Model>() {
                                    }.getType());

                            dayObj = jsonObject.getJSONObject("data");
                            day_arr = dayObj.names();
                            String[] labels = new String[day_arr.length()];
                            for (int i = 0; i < day_arr.length (); i++) {
                                String key = day_arr.getString (i);
                                timetabletabLayout.addTab(timetabletabLayout.newTab().setText(day_arr.getString(i).substring(0,3)));
                                labels[i] = day_arr.getString(i).substring(0, 3);
                            }
                            final TimeTableAdapter timetableAdapter =new TimeTableAdapter(TimeTableActivity.this,TimeTableActivity.this, labels.length); //timetabletabLayout.getTabCount()
                            viewPager2.setAdapter(timetableAdapter);

                            new TabLayoutMediator(timetabletabLayout, viewPager2, (tab, position) -> tab.setText(labels[position])).attach();

                            for (int i = 0 ; i< day_arr.length ();i++){
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                Date d = new Date();
                                String dayOfTheWeek = sdf.format(d);
                                if (dayOfTheWeek.equalsIgnoreCase(day_arr.getString(i))){
                                    viewPager2.setCurrentItem(i);
                                }
                            }

                            contentContainer.removeAllViews();
                            if (viewPager2.getParent() != null) {
                                ((ViewGroup) viewPager2.getParent()).removeView(viewPager2);
                            }
                            contentContainer.addView(viewPager2);
                            viewPager2.setVisibility(View.VISIBLE);


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    progressDialogFragment.dismiss();
                    }else if(response.code() == 404){
                    progressDialogFragment.dismiss();
                    runOnUiThread(() -> {
                        showPurchaseCard();
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    progressDialogFragment.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialogFragment.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showPurchaseCard() {
        contentContainer.removeAllViews();
        View card = PurchaseHelper.createPurchaseCard(this, contentContainer, "Timetable", v -> {
            Toast.makeText(this, "Redirect to purchase flow", Toast.LENGTH_SHORT).show();
        });
        card.setAlpha(0f);
        contentContainer.addView(card);
        card.animate().alpha(1f).setDuration(300).start();
    }

    private class TimeTableAdapter extends FragmentStateAdapter {
        Context context;
        int totaltabs;

        public TimeTableAdapter(FragmentActivity fragmentActivity, Context context, int totaltabs) {
            super(fragmentActivity);
            this.context=context;
            this.totaltabs=totaltabs;
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            TimeTable_Mon_Fragment timeTable_mon_fragment = new TimeTable_Mon_Fragment();
            Bundle bundle = new Bundle();
            try {
                bundle.putString("dayname",day_arr.getString (position) );
                if (day_arr.getString (position).equalsIgnoreCase("Monday")){
                    bundle.putParcelableArrayList(day_arr.getString (position), (ArrayList<? extends Parcelable>) timeTableModels.getData().getMonday());
                }
                else if (day_arr.getString (position).equalsIgnoreCase("Tuesday")){
                    bundle.putParcelableArrayList(day_arr.getString (position), (ArrayList<? extends Parcelable>) timeTableModels.getData().getTuesday());
                }
                else if (day_arr.getString (position).equalsIgnoreCase("Wednesday")){
                    bundle.putParcelableArrayList(day_arr.getString (position), (ArrayList<? extends Parcelable>) timeTableModels.getData().getWednesday());
                }
                else if (day_arr.getString (position).equalsIgnoreCase("Thursday")){
                    bundle.putParcelableArrayList(day_arr.getString (position), (ArrayList<? extends Parcelable>) timeTableModels.getData().getThursday());
                }
                else if (day_arr.getString (position).equalsIgnoreCase("Friday")){
                    bundle.putParcelableArrayList(day_arr.getString (position), (ArrayList<? extends Parcelable>) timeTableModels.getData().getFriday());
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            timeTable_mon_fragment.setArguments(bundle);
            return timeTable_mon_fragment;
        }

        @Override
        public int getItemCount() {
            return totaltabs;
        }
    }
}


