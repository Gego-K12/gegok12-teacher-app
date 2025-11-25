package com.gegosoft.yourappname.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Models.RejectLeaveModel;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RejectLeaveActivity extends AppCompatActivity {
    Api api;
    Map<String,String> headermap;
    EditText entercomments;
    String leaveid;
    Intent intent;
    UserDetailsSharedPref userDetailsSharedPref;
    Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rejectleave_layout);
        entercomments=findViewById(R.id.rejectcommentsedt);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);

        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        intent=getIntent();
        if(intent.getExtras()!=null) {
            leaveid = intent.getStringExtra("leaveid");
        }
        submit=findViewById(R.id.rejectsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });


    }
    private void getdata(){
        Call<RejectLeaveModel> rejectLeaveModelCall=api.getrejectleave(headermap,leaveid,entercomments.getText().toString());
        rejectLeaveModelCall.enqueue(new Callback<RejectLeaveModel>() {
            @Override
            public void onResponse(Call<RejectLeaveModel> call, Response<RejectLeaveModel> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Leave Rejected",Toast.LENGTH_SHORT).show();
                    getOnBackPressedDispatcher().onBackPressed();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RejectLeaveModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    }

