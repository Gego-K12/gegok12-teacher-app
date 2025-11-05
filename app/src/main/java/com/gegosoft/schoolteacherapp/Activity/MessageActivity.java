package com.gegosoft.schoolteacherapp.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Helper.CheckNetwork;
import com.gegosoft.schoolteacherapp.Helper.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.MessageModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textnodata,messagecount;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    Api api;
    MyAdapter myAdapter;
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView=findViewById(R.id.messagerecyclerview);
        textnodata = findViewById(R.id.textnodata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(MessageActivity.this);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        back=findViewById(R.id.back);
        messagecount=findViewById(R.id.privatemsgtotal);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });



        if (CheckNetwork.isInternetAvailable(MessageActivity.this)){
            getdata();
        }

    }
    private void getdata(){
        api= ApiClient.getClient().create(Api.class);
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message","Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(MessageActivity.this.getSupportFragmentManager(),"loading screen");
        Call<MessageModel> messageModelCall = api.getMessageList(headermap);

        messageModelCall.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()){
                    List<MessageModel.Datum> data =response.body().getData();
                    MessageModel example = response.body();
                    messagecount.setText("("+String.valueOf(example.getCount())+")");
                    if (data.size()!=0){
                        myAdapter=new MyAdapter(data);
                        recyclerView.setAdapter(myAdapter);
                    }
                    else {
                        textnodata.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    if (response.code()==401){
                        AppUtils.SessionExpired(MessageActivity.this);
                    }
                }
                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                AppUtils.APIFails(MessageActivity.this,t);
                progressDialogFragment.dismiss();
            }
        });



    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        List<MessageModel.Datum> data;
        public MyAdapter(List<MessageModel.Datum> data){
            this.data=data;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message,parent,false);
            return new MyAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            holder.heading.setText(data.get(position).getSubject());
            holder.subheading.setText(data.get(position).getMessage());
            holder.time.setText(data.get(position).getSentAt());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView heading,subheading,time;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                heading=itemView.findViewById(R.id.heading);
                subheading=itemView.findViewById(R.id.subheading);
                time=itemView.findViewById(R.id.notificationtime);

            }
        }
    }
}
