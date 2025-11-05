package com.gegosoft.schoolteacherapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Fragment.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Models.DeleteLeaveModel;
import com.gegosoft.schoolteacherapp.Models.MyLeaveModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLeaveActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    List<MyLeaveModel.Datum> myleavearraylist;
    MyLeaveAdapter myLeaveAdapter;
    ImageView back;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myleave_layout);
        recyclerView = findViewById(R.id.myleaverecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyLeaveActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);

        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        back = findViewById(R.id.myleavebackbtn);
        nodata = findViewById(R.id.nodata);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        getdata();

    }

    private void getdata() {
        api = ApiClient.getClient().create(Api.class);
        final ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("message", "Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(), "loading screen");
        Call<MyLeaveModel> myLeaveModelCall = api.getmyleave(headermap);
        myLeaveModelCall.enqueue(new Callback<MyLeaveModel>() {
            @Override
            public void onResponse(Call<MyLeaveModel> call, Response<MyLeaveModel> response) {
                if (response.isSuccessful()) {
                    myleavearraylist = response.body().getData();
                    if (myleavearraylist.size() != 0)
                    {
                        myLeaveAdapter = new MyLeaveAdapter(myleavearraylist);
                        recyclerView.setAdapter(myLeaveAdapter);
                        myLeaveAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    } else
                        {
                        recyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                }
                progressDialogFragment.dismiss();


            }

            @Override
            public void onFailure(Call<MyLeaveModel> call, Throwable t) {
                progressDialogFragment.dismiss();
            }
        });

    }

    private void deleteleave(final Integer leaveid) {
        Call<DeleteLeaveModel> deleteLeaveModelCall = api.getdeleteleave(headermap, String.valueOf(leaveid));
        deleteLeaveModelCall.enqueue(new Callback<DeleteLeaveModel>() {
            @Override
            public void onResponse(Call<DeleteLeaveModel> call, Response<DeleteLeaveModel> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < myleavearraylist.size(); i++) {
                        if (myleavearraylist.get(i).getId().equals(leaveid)) {
                            myleavearraylist.remove(i);
                            myLeaveAdapter.notifyDataSetChanged();
                        }
                    }
                    Toast.makeText(MyLeaveActivity.this, " Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    Toast.makeText(MyLeaveActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteLeaveModel> call, Throwable t) {
                Toast.makeText(MyLeaveActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        {

        }


    }

    public class MyLeaveAdapter extends RecyclerView.Adapter<MyLeaveAdapter.MyLeaveViewHolder> {

        List<MyLeaveModel.Datum> datumList;

        public MyLeaveAdapter(List<MyLeaveModel.Datum> datumList) {
            this.datumList = datumList;


        }

        @NonNull
        @Override
        public MyLeaveAdapter.MyLeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myleave_recyclerview_layout, parent, false);
            return new MyLeaveViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyLeaveAdapter.MyLeaveViewHolder holder, final int position) {
            holder.fromdate.setText(datumList.get(position).getFromDate());
            holder.todate.setText(datumList.get(position).getToDate());
            holder.reason.setText(datumList.get(position).getReason());
            holder.remarks.setText(datumList.get(position).getRemarks());
            holder.leavetype.setText(datumList.get(position).getLeaveType());
            holder.session.setText(datumList.get(position).getSession());
            holder.comments.setText(datumList.get(position).getComments());
            holder.countdays.setText(datumList.get(position).getLeave_days());

            if (datumList.get(position).getStatus().equals("Approved")) {

                holder.edit.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                holder.commentsset.setVisibility(View.VISIBLE);
                holder.countset.setVisibility(View.VISIBLE);
                holder.approval.setVisibility(View.VISIBLE);
                holder.rejected.setVisibility(View.GONE);
                holder.pending.setVisibility(View.GONE);

            }
            else if (datumList.get(position).getStatus().equals("Pending")) {
                holder.countset.setVisibility(View.GONE);
                holder.commentsset.setVisibility(View.GONE);
                holder.edit.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.approval.setVisibility(View.GONE);
                holder.rejected.setVisibility(View.GONE);
                holder.pending.setVisibility(View.VISIBLE);
            }
            else if (datumList.get(position).getStatus().equals("Cancelled")) {

                holder.rejected.setVisibility(View.VISIBLE);
                holder.commentsset.setVisibility(View.VISIBLE);
                holder.countset.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
                holder.approval.setVisibility(View.GONE);
                holder.pending.setVisibility(View.GONE);

            }


            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    deleteleave(datumList.get(position).getId());

                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyLeaveActivity.this, LeaveActivity.class);
                    String leaveid = datumList.get(position).getId().toString();
                    intent.putExtra("leaveid", leaveid);
                    intent.putExtra("editleave", "toleaveedit");
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {

            return datumList.size();
        }

        public class MyLeaveViewHolder extends RecyclerView.ViewHolder {
            TextView fromdate, todate, reason, remarks, leavetype, session,comments,countdays;

            ImageView edit, approval, pending, rejected, delete;

            LinearLayout commentsset;
            LinearLayout countset;

            public MyLeaveViewHolder(@NonNull View itemView) {
                super(itemView);
                leavetype = itemView.findViewById(R.id.myleavetype);
                fromdate = itemView.findViewById(R.id.myleavefromdate);
                todate = itemView.findViewById(R.id.myleavetodate);
                reason = itemView.findViewById(R.id.myleavereason);
                remarks = itemView.findViewById(R.id.myleaveremarks);
                session = itemView.findViewById(R.id.myleavesession);
                edit = itemView.findViewById(R.id.editleave);
                delete = itemView.findViewById(R.id.delete_leave);
                approval = itemView.findViewById(R.id.status_confirmed);
                pending = itemView.findViewById(R.id.status_pending);
                rejected = itemView.findViewById(R.id.status_rejected);
                commentsset = itemView.findViewById(R.id.commentsset);
                comments = itemView.findViewById(R.id.mycomments);
                countset = itemView.findViewById(R.id.countset);
                countdays=itemView.findViewById(R.id.myleavecount);

            }
        }
    }

}