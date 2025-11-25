package com.gegosoft.yourappname.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Models.ApiSuccessModel;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordWithLogin extends AppCompatActivity {
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    EditText edtnewpassword,edtconfirmpassword,edtoldpassword;
    AppCompatButton submit;
    String oldpassword,newpassword,confirmpassword;
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepasswordwithlogin);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(ChangePasswordWithLogin.this);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        edtnewpassword=findViewById(R.id.edtnewpassword);
        edtconfirmpassword=findViewById(R.id.edtconfirmpassword);
        edtoldpassword=findViewById(R.id.edtoldpassword);
        submit=findViewById(R.id.submit);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()) {
                    oldpassword = edtoldpassword.getText().toString();
                    newpassword = edtnewpassword.getText().toString();
                    confirmpassword = edtconfirmpassword.getText().toString();
                    ChangePassword();
                }
            }
        });
    }
    private void ChangePassword() {

        api = ApiClient.getClient().create(Api.class);
        oldpassword = edtoldpassword.getText().toString();
        newpassword = edtnewpassword.getText().toString();
        confirmpassword = edtconfirmpassword.getText().toString();
        Call<ApiSuccessModel> modelCall = api.changepasswordwithlogin(headermap, edtoldpassword.getText().toString(), edtnewpassword.getText().toString(), edtconfirmpassword.getText().toString());
        modelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordWithLogin.this,"Password changed successfully", Toast.LENGTH_SHORT).show();
                    getOnBackPressedDispatcher().onBackPressed();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordWithLogin.this, "Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                Toast.makeText(ChangePasswordWithLogin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean Validate() {
        if (edtoldpassword.getText().length()==0){
            edtoldpassword.setError("Field cannot be left blank.");
            edtoldpassword.requestFocus();
            return false;
        }

        else if (edtnewpassword.getText().length()==0){
            edtnewpassword.setError("Field cannot be left blank.");
            edtnewpassword.requestFocus();
            return false;
        }

        else if (edtconfirmpassword.getText().length()==0){
            edtconfirmpassword.setError("Field cannot be left blank.");
            edtconfirmpassword.requestFocus();
            return false;
        }
        else if (!edtconfirmpassword.getText().toString().equalsIgnoreCase(edtnewpassword.getText().toString())){
            edtnewpassword.setError("Password Should be Same");
            edtnewpassword.requestFocus();
            return false;
        }

        else if (edtnewpassword.getText().length()<8){
            edtconfirmpassword.setError("Should be 8 characters.");
            edtconfirmpassword.requestFocus();
            return false;
        }

        else if (!edtconfirmpassword.getText().toString().equalsIgnoreCase(edtnewpassword.getText().toString())){
            edtnewpassword.setError("Password Should be Same");
            edtnewpassword.requestFocus();
            return false;
        }
        return true;
    }
}
