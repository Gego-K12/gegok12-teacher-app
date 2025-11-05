package com.gegosoft.schoolteacherapp.Activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Models.HolidayListModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HolidayListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    ImageView back;
    HashMap hasmonth = null;
    ArrayList holidayarraylist = new ArrayList();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holidaylist_layout);
        recyclerView=findViewById(R.id.holidaylistrecyclerview);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        back=findViewById(R.id.holidaylistbackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        getHolidays();
    }
    private void  getHolidays(){
        Call<HolidayListModel> holidayModelCall = api.getholidaymodel(headermap);
        holidayModelCall.enqueue(new Callback<HolidayListModel>() {
            @Override
            public void onResponse(Call<HolidayListModel> call, Response<HolidayListModel> response) {
                if (response.isSuccessful()){
                    List<HolidayListModel.Datum> holidayListModelList = response.body().getData();
                    if (holidayListModelList!=null&&holidayListModelList.size()!=0){

                        ArrayList<HashMap<String, String>> holidays = new ArrayList<HashMap<String, String>>();

                        hasmonth = new HashMap();
                        holidayarraylist.clear();
                        for (int i = 0; i < holidayListModelList.size(); i++) {
                            HashMap<String, String> holiday = new HashMap<String, String>();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                Date date = format.parse(holidayListModelList.get(i).getDate());
                                String dayOfTheWeek = (String) DateFormat.format("E", date); // Thursday
                                String monthString = (String) DateFormat.format("MMMM", date); // Jun
                                if (!hasmonth.containsKey(monthString)) {
                                    holidayarraylist.add(monthString);
                                    holidays = new ArrayList<HashMap<String, String>>();
                                    holiday.put("id", String.valueOf(holidayListModelList.get(i).getId()));
                                    holiday.put("name", String.valueOf(holidayListModelList.get(i).getTitle()));
                                    holiday.put("date", String.valueOf(holidayListModelList.get(i).getDate()));
                                    holiday.put("day", dayOfTheWeek);

                                    holidays.add(holiday);
                                    hasmonth.put(monthString, holidays);
                                }
                                else {
                                    holidays = (ArrayList<HashMap<String, String>>) hasmonth.get(monthString);
                                    holiday.put("id", String.valueOf(holidayListModelList.get(i).getId()));
                                    holiday.put("name", String.valueOf(holidayListModelList.get(i).getTitle()));
                                    holiday.put("date", String.valueOf(holidayListModelList.get(i).getDate()));
                                    holiday.put("day", dayOfTheWeek);
                                    holidays.add(holiday);
                                    hasmonth.put(monthString, holidays);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        HolidayAdapter holidayAdapter =  new HolidayAdapter(holidayarraylist);
                        recyclerView.setAdapter(holidayAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<HolidayListModel> call, Throwable t) {

            }
        });
    }
    public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayViewHolder>{
        ArrayList holidayarraylist;
        public HolidayAdapter(ArrayList haskeyoperator)
        {
            this.holidayarraylist =haskeyoperator;
        }

        @NonNull
        @Override
        public HolidayAdapter.HolidayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.holidayrecyclerview_layout,parent,false);
            return new HolidayAdapter.HolidayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HolidayAdapter.HolidayViewHolder holder, int position) {
            if (hasmonth.containsKey(holidayarraylist.get(position))){
                ArrayList<HashMap<String, String>> arrCampaignList = (ArrayList<HashMap<String, String>>) hasmonth.get(holidayarraylist.get(position));
                holder.month.setText(holidayarraylist.get(position).toString());
                holder.count.setText(String.valueOf(arrCampaignList.size()));

                for (int j = 0; j < arrCampaignList.size(); j++)
                {
                    final HashMap<String, String> map = (HashMap<String, String>) arrCampaignList.get(j);

                    View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.customholiday, holder.linearLayout, false);
                    holder.linearLayout.addView(view);
                    TextView  date=view.findViewById(R.id.holidaydate);
                    TextView holidayname=view.findViewById(R.id.holidayname);
                    TextView day=view.findViewById(R.id.holidayday);
                    date.setText(map.get("date"));
                    holidayname.setText(map.get("name"));
                    day.setText("("+map.get("day")+")");

                }
            }
        }

        @Override
        public int getItemCount()
        {
            return holidayarraylist.size();
        }

        public class HolidayViewHolder extends RecyclerView.ViewHolder {

            TextView count,month;

            LinearLayout linearLayout;

            public HolidayViewHolder(@NonNull View itemView) {
                super(itemView);

                month=itemView.findViewById(R.id.holidaymonth);
                linearLayout=itemView.findViewById(R.id.linearLayout);
                count=itemView.findViewById(R.id.holidaycount);

            }
        }
    }

}
