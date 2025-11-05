package com.gegosoft.schoolteacherapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Models.RequestedLeaveListModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLeaveActivityWithPermission extends AppCompatActivity {


    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    List<RequestedLeaveListModel.Datum> myleavearraylist;
    MyLeaveAdapter myLeaveAdapter;
    ImageView back;
    TextView nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_leave_with_permission);
        recyclerView = findViewById(R.id.myleaverecyclerview);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MyLeaveActivityWithPermission.this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        nodata = findViewById(R.id.no_data_id);

        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        back=findViewById(R.id.myleavebackbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        getdata();

    }

    private void getdata(){
        api = ApiClient.getClient().create(Api.class);
        final ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("message","Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(),"loading screen");
        Call<RequestedLeaveListModel> myLeaveModelCall=api.getrequestedleave(headermap);
        myLeaveModelCall.enqueue(new Callback<RequestedLeaveListModel>() {
            @Override
            public void onResponse(Call<RequestedLeaveListModel> call, Response<RequestedLeaveListModel> response) {
                if(response.isSuccessful()){
                    myleavearraylist = response.body().getData();
                    if (myleavearraylist.size()== 0 && myleavearraylist.isEmpty())
                    {
                        nodata.setVisibility(View.VISIBLE);
                    }
                    myLeaveAdapter = new MyLeaveAdapter(myleavearraylist);
                    recyclerView.setAdapter(myLeaveAdapter);
                    myLeaveAdapter.notifyDataSetChanged();
                }

               progressDialogFragment.dismiss();


            }

            @Override
            public void onFailure(Call<RequestedLeaveListModel> call, Throwable t) {
                progressDialogFragment.dismiss();
            }
        });

    }
    public class MyLeaveAdapter extends RecyclerView.Adapter<MyLeaveAdapter.MyLeaveViewHolder> {

        List<RequestedLeaveListModel.Datum>datumList;
        public MyLeaveAdapter(List<RequestedLeaveListModel.Datum>datumList){
            this.datumList=datumList;


        }
        @NonNull
        @Override
        public MyLeaveAdapter.MyLeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myleave_recyclerview_permission,parent,false);
            return new MyLeaveAdapter.MyLeaveViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyLeaveAdapter.MyLeaveViewHolder holder, final int position) {


            holder.fromdate.setText(datumList.get(position).getFromDate());
            holder.todate.setText(datumList.get(position).getToDate());
            holder.reason.setText(datumList.get(position).getReason());
            holder.remarks.setText(datumList.get(position).getRemarks());
            holder.leavetype.setText(datumList.get(position).getLeaveType());
            holder.teachername.setText(datumList.get(position).getTeacherName());
            holder.leavecount.setText("No of leaves: "+datumList.get(position).getLeave_days());
            holder.session.setText(datumList.get(position).getSession());

            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyLeaveActivityWithPermission.this,ApproveLeaveActivity.class);
                    String leaveid=datumList.get(position).getId().toString();
                    intent.putExtra("leaveid",leaveid);
                    startActivity(intent);
                    getdata();

                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyLeaveActivityWithPermission.this,RejectLeaveActivity.class);
                    String leaveid=datumList.get(position).getId().toString();
                    intent.putExtra("leaveid",leaveid);
                    startActivity(intent);
                    getdata();

                }
            });
        }

        @Override
        public int getItemCount() {
            return datumList.size();
        }

        public class MyLeaveViewHolder extends RecyclerView.ViewHolder {

            TextView teachername,fromdate,todate,reason,remarks,leavetype,status,colourdot,session;

            TextView reject,approve,leavecount;

            ImageView edit;


            public MyLeaveViewHolder(@NonNull View itemView) {
                super(itemView);

                teachername=itemView.findViewById(R.id.teacher_name_for_leave);
                fromdate=itemView.findViewById(R.id.myleavefromdate);
                todate=itemView.findViewById(R.id.myleavetodate);
                reason=itemView.findViewById(R.id.myleavereason);
                remarks=itemView.findViewById(R.id.myleaveremarks);
                leavetype=itemView.findViewById(R.id.myleavetype);
                leavecount = itemView.findViewById(R.id.leave_count);
                session=itemView.findViewById(R.id.myleavesession);
                status=itemView.findViewById(R.id.myleavestatus);
                edit=itemView.findViewById(R.id.editleave);
                approve=itemView.findViewById(R.id.approveleavebtn);
                reject = itemView.findViewById(R.id.rejectleavebtn);
                colourdot=itemView.findViewById(R.id.myleavestatuscolour);

            }
        }
    }

}