package com.gegosoft.schoolteacherapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Models.ApproveLeaveModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveLeaveActivity extends AppCompatActivity {
    Api api;
    Map<String, String> headermap;
    EditText entercomments;
    String leaveid;
    Intent intent;
    UserDetailsSharedPref userDetailsSharedPref;
    Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approveleave_layout);
        entercomments=findViewById(R.id.approvecommentsedt);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        intent=getIntent();
        if(intent.getExtras()!=null) {
            leaveid=intent.getExtras().getString("leaveid");
        }
        submit=findViewById(R.id.approvesubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveleave();
            }
        });


    }
    private void approveleave(){
        Call<ApproveLeaveModel>approveLeaveModelCall=api.getapproveleave(headermap,leaveid,entercomments.getText().toString());
        approveLeaveModelCall.enqueue(new Callback<ApproveLeaveModel>() {
            @Override
            public void onResponse(Call<ApproveLeaveModel> call, Response<ApproveLeaveModel> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Leave Approved",Toast.LENGTH_SHORT).show();
                     getOnBackPressedDispatcher().onBackPressed();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApproveLeaveModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
