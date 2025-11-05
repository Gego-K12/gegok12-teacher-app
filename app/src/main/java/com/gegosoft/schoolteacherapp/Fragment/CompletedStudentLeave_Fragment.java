package com.gegosoft.schoolteacherapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.CheckNetwork;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.CompletedStudentleaveModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedStudentLeave_Fragment extends Fragment {
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    RecyclerView recyclerView;
    TextView nodata;
    List<CompletedStudentleaveModel.Datum> completedlist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.recyclerview,null);
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        nodata=view.findViewById(R.id.textnodata);
        if (CheckNetwork.isInternetAvailable(getContext())){
            getcompletedlist();

        }

        return view;
    }
    private void getcompletedlist(){
        Call<CompletedStudentleaveModel> modelCall=api.getcompletedleave(headermap);
        modelCall.enqueue(new Callback<CompletedStudentleaveModel>() {
            @Override
            public void onResponse(Call<CompletedStudentleaveModel> call, Response<CompletedStudentleaveModel> response) {
                if(response.isSuccessful())
                {
                completedlist=response.body().getData();
                    if(completedlist!=null&&completedlist.size()!=0) {
                        StudentCompletedAdapter studentCompletedAdapter = new StudentCompletedAdapter(completedlist);
                        recyclerView.setAdapter(studentCompletedAdapter);
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
            public void onFailure(Call<CompletedStudentleaveModel> call, Throwable t) {

            }
        });
    }
    public class StudentCompletedAdapter extends RecyclerView.Adapter<StudentCompletedAdapter.MyViewHolder>{

        List<CompletedStudentleaveModel.Datum> datumList;

        public StudentCompletedAdapter(List<CompletedStudentleaveModel.Datum> datumList) {
            this.datumList = datumList;
        }

        @NonNull
        @Override
        public StudentCompletedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_completedstudentlist, parent, false);
            return new  StudentCompletedAdapter.MyViewHolder (view);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentCompletedAdapter.MyViewHolder holder, int position) {
            holder.name.setText(datumList.get(position).getName());
            holder.fromdate.setText(datumList.get(position).getFrom());
            holder.todate.setText(datumList.get(position).getTo());
            holder.reason.setText(datumList.get(position).getReason());
            holder.comments.setText(datumList.get(position).getComments());
            if (datumList.get(position).getStatus().equals("approved"))
            {
                holder.approved.setVisibility(View.VISIBLE);
            }else
                {
                     holder.rejected.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public int getItemCount() {
            return datumList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,fromdate,todate,comments,reason;
            ImageView approved, rejected;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.student_name_for_leave);
                fromdate=itemView.findViewById(R.id.studentleavefromdate);
                todate=itemView.findViewById(R.id.studentleavetodate);
                comments=itemView.findViewById(R.id.studentleavecomments);
                reason=itemView.findViewById(R.id.studentleavereason);
                approved = itemView.findViewById(R.id.status_approved);
                rejected = itemView.findViewById(R.id.status_rejected);
            }
        }
    }
}
