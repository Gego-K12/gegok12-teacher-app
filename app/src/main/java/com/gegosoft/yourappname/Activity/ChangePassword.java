package com.gegosoft.yourappname.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.ApiSuccessModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    EditText edtnewpassword,edtconfirmpassword;
    Intent intent;
    String otp;
    AppCompatButton submit;
    int usergroup  =5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        intent=getIntent();
        otp=intent.getStringExtra("otp");
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        edtnewpassword=findViewById(R.id.edtnewpassword);
        edtconfirmpassword=findViewById(R.id.edtconfirmpassword);
        submit=findViewById(R.id.submitbtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ChangePassword();
            }
        });

    }
    private void ChangePassword(){
        Call<ApiSuccessModel>modelCall=api.changepassword(headermap,userDetailsSharedPref.getString("mobileno"),otp,edtnewpassword.getText().toString(),usergroup,edtconfirmpassword.getText().toString());
        modelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ChangePassword.this,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ChangePassword.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t)
            {
                Toast.makeText(ChangePassword.this,"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
