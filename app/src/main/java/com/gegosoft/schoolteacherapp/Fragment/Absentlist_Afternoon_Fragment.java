package com.gegosoft.schoolteacherapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.CheckNetwork;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.AbsentlisModel;
import com.gegosoft.schoolteacherapp.Models.AttendanceModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Absentlist_Afternoon_Fragment extends Fragment {


    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    RecyclerView recyclerView;
    List<AttendanceModel.Studentlist> attendanceModelList;
    List<AbsentlisModel.Datum>absentlistModels;
    TextView nodata;
    String standardid;
    TextView absent_count,total_count;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.absentlist_recyclerview,null);
        recyclerView=view.findViewById(R.id.recyclerview);
        absent_count = view.findViewById(R.id.absent_count);
        total_count = view.findViewById(R.id.total_count);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        nodata=view.findViewById(R.id.textnodata);
        if (CheckNetwork.isInternetAvailable(getContext())){
            getstudentslist();
        }

        return view;

    }
    private void getAfternoonabsent(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date c = Calendar.getInstance().getTime();
        String formattedDate = df.format(c);
        Call<AbsentlisModel> attendanceModelCall = api.getabsentlist(headermap,standardid,formattedDate,"afternoon");
        attendanceModelCall.enqueue(new Callback<AbsentlisModel>() {
            @Override
            public void onResponse(Call<AbsentlisModel> call, Response<AbsentlisModel> response) {
                if(response.isSuccessful()){
                    absentlistModels=response.body().getData();
                    absent_count.setText("Absent : "+String.valueOf(absentlistModels.size()));
                    if(absentlistModels.size()!=0){
                        AbsentAdapter absentAdapter=new AbsentAdapter(absentlistModels);
                        recyclerView.setAdapter(absentAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AbsentlisModel> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getstudentslist () {
        Call<AttendanceModel> attendanceModelCall = api.getattendancemodel(headermap);
        attendanceModelCall.enqueue(new Callback<AttendanceModel>() {
            @Override
            public void onResponse(Call<AttendanceModel> call, Response<AttendanceModel> response) {
                if (response.isSuccessful()) {
                    attendanceModelList = response.body().getStudentlist();
                    total_count.setText(String.valueOf(attendanceModelList.size()));
                    standardid=attendanceModelList.get(0).getStandardLinkId().toString();
                    getAfternoonabsent();

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<AttendanceModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class AbsentAdapter extends RecyclerView.Adapter<AbsentAdapter.MyViewHolder>{
        List<AbsentlisModel.Datum> absentmodels;
        private Integer reasonId;

        public AbsentAdapter(List<AbsentlisModel.Datum> absentmodels) {
            this.absentmodels = absentmodels;
        }
        @NonNull
        @Override
        public AbsentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_forenoonabsent, parent, false);
            return new AbsentAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AbsentAdapter.MyViewHolder holder, int position) {

            holder.name.setText(absentmodels.get(position).getUserName());
            holder.sclass.setText(absentmodels.get(position).getClass_());
            holder.reason.setText(absentmodels.get(position).getReason());
            holder.remarks.setText(absentmodels.get(position).getRemarks());

        }

        @Override
        public int getItemCount() {

            return absentmodels.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,sclass,reason,remarks;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                name=itemView.findViewById(R.id.absentstudentname);
                sclass=itemView.findViewById(R.id.absentstudentclass);
                reason=itemView.findViewById(R.id.absentstudentreason);
                remarks=itemView.findViewById(R.id.absentstudentremarks);

            }
        }
    }

}
