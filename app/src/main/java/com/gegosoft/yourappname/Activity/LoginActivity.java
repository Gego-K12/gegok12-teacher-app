package com.gegosoft.yourappname.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gegosoft.yourappname.Helper.ApiClient;

import com.gegosoft.yourappname.Helper.ProgressDialogFragment;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.ApiSuccessModel;
import com.gegosoft.yourappname.Models.LoginModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edtemail, edtpassword;
    TextView login,forgotpassword;
    UserDetailsSharedPref userDetailsSharedPref;
    String email,password;
    Api api;
    private int STORAGE_PERMISSION_CODE = 1;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        forgotpassword=findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        edtemail = findViewById(R.id.emailedit);
        edtpassword = findViewById(R.id.passwordedt);
        login = findViewById(R.id.loginbtn);

        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestStoragePermission();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });
        if (userDetailsSharedPref.isLoggedIn()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
    }
    private  void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void userlogin() {
        if (validate()){
            api = ApiClient.getClient().create(Api.class);
            String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            final ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("message","Loading");
            progressDialogFragment.setArguments(bundle);
            progressDialogFragment.show(getSupportFragmentManager(),"loading screen");

            Call<LoginModel> jsonObjectCall = api.login(email,password,userDetailsSharedPref.getString("firebasetoken"),deviceID);
            jsonObjectCall.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()){

                        userDetailsSharedPref.saveString("token",response.body().getToken());
                        userDetailsSharedPref.saveString("userId", String.valueOf(response.body().getId()));
                        userDetailsSharedPref.createLoginSession();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    }
                    else {
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
                                    if (key.equals("device_id")) {
                                        builder = new AlertDialog.Builder(LoginActivity.this);

                                        builder.setMessage("Are you sure you want to logout from all other devices?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                  Call<ApiSuccessModel>modelCall=api.getlogoutalldevices(email);
                                                  modelCall.enqueue(new Callback<ApiSuccessModel>() {
                                                      @Override
                                                      public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                                                          if(response.isSuccessful()){
                                                              Toast.makeText(LoginActivity.this,"Successfully Logged out from all devices",Toast.LENGTH_SHORT).show();

                                                          }else {
                                                              Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();

                                                          }
                                                      }

                                                      @Override
                                                      public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                                                          Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                                                      }
                                                  });
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();

                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.setTitle("Log Out");
                                        alert.show();
                                    }
                                        Toast.makeText(LoginActivity.this, errormessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                             catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (response.code()==500){
                            login.setEnabled(true);
                            String s  = null;
                            try {
                                s = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String errors =   jsonObject1.getString("errors");
                                JSONObject jsonObject2 = new JSONObject(errors);
                                JSONArray jsonArray = jsonObject2.getJSONArray("email");
                                String errorstring = jsonArray.getString(0);
                                Toast.makeText(LoginActivity.this, errorstring,Toast.LENGTH_LONG).show();

                                if(errorstring.equalsIgnoreCase("Invalid User")){
                                    edtemail.setError(errorstring);
                                }
                                else if (errorstring.equalsIgnoreCase("Incorrect Password")){
                                    edtpassword.setError(errorstring);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();

                        }

                    }
                    progressDialogFragment.dismiss();
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialogFragment.dismiss();
                }
            });

        }


    }
    private boolean validate(){
        email = edtemail.getText().toString().trim();
        password = edtpassword.getText().toString().trim();
        if (email.isEmpty()) {
            edtemail.setError("MobileNo is required");
            edtemail.requestFocus();
            return false;
        }


        if (password.isEmpty()) {
            edtpassword.setError("Password Required");
            edtpassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            edtpassword.setError("password should be atleast 6 character long");
            edtpassword.requestFocus();
            return false;
        }
        return true;
    }

}