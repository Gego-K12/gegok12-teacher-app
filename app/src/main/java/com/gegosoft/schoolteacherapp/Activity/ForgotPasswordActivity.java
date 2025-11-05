package com.gegosoft.schoolteacherapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gegosoft.schoolteacherapp.Models.ApiSuccessModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtmbleno;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    EditText edtotp;
    TextView submit;
    int usergroup = 5;
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        edtmbleno=findViewById(R.id.edtmbleno);
        submit=findViewById(R.id.submitbutton);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resetpassword();
            }
        });


    }
    private void Resetpassword()
    {
        Call<ApiSuccessModel>modelCall=api.resetpassword(headermap,edtmbleno.getText().toString(),usergroup);

        userDetailsSharedPref.saveString("mobileno",edtmbleno.getText().toString());
        modelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if(response.isSuccessful()){
                    LayoutInflater li = LayoutInflater.from(ForgotPasswordActivity.this);
                    View promptsView = li.inflate(R.layout.dialogue_enterotp, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgotPasswordActivity.this);

                    alertDialogBuilder.setView(promptsView);

                    edtotp = (EditText) promptsView.findViewById(R.id.edtotp);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    EnterOtp();
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();

                }
                if(response.code()==422){
                    try {
                        String s = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(s);
                        String error = jsonObject.getString("errors");
                        JSONObject jsonObject1 = new JSONObject(error);
                        Iterator keys = jsonObject1.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = jsonObject1.getJSONArray((String) key);
                            String errormessage = value.getString(0);

                                Toast.makeText(ForgotPasswordActivity.this, errormessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void EnterOtp(){
        Call<ApiSuccessModel>modelCall=api.enterop(headermap,userDetailsSharedPref.getString("mobileno"),edtotp.getText().toString(),usergroup);
        modelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgotPasswordActivity.this,ChangePassword.class);
                    intent.putExtra("otp",edtotp.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
