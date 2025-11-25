package com.gegosoft.yourappname.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Helper.AppUtils;
import com.gegosoft.yourappname.Helper.ProgressDialogFragment;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.MyProfileModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {
    TextView name,gender,email,dob,age,desg,phno,bloodgroup,address,pincode;
    ImageView profilepic,back;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    List<MyProfileModel.Data>myprofilelist;
    ProgressDialogFragment progressDialogFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilelayout);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        name=findViewById(R.id.myprofilemyname);
        gender=findViewById(R.id.myprofilegender);
        email=findViewById(R.id.myprofileemail);
        dob=findViewById(R.id.myprofiledob);
        age=findViewById(R.id.myprofileage);
        desg=findViewById(R.id.myprofiledesg);
        phno=findViewById(R.id.myprofilephno);
        bloodgroup=findViewById(R.id.myprofilebloodgroup);
        address=findViewById(R.id.myprofileaddress);
        pincode=findViewById(R.id.myprofilepincode);
        profilepic=findViewById(R.id.myprofilepic);
        back=findViewById(R.id.myprofileback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");

        progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message","Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(),"loading screen");
        getprofiledetails();
    }
    private void getprofiledetails(){

        Call<MyProfileModel>myProfileModelCall=api.getprofilemodel(headermap);
        myProfileModelCall.enqueue(new Callback<MyProfileModel>() {
            @Override
            public void onResponse(Call<MyProfileModel> call, Response<MyProfileModel> response) {
                if(response.isSuccessful()){
                    MyProfileModel.Data data = response.body().getData();
                    myprofilelist=new ArrayList<>();
                    String mydob =data.getDateOfBirth();
                    dob.setText(mydob);

                    String mygender=data.getGender();
                    gender.setText(mygender);

                    String myemail=data.getEmailId();
                    email.setText(myemail);

                    String myname=data.getFullname();
                    name.setText(myname);

                    String myage=data.getAge().toString();
                    age.setText(myage);

                    String mydesg=data.getDesignation();
                    desg.setText(mydesg);

                    String myphno=data.getMobileNo();
                    phno.setText(myphno);

                    String mybloodgroup=data.getBloodGroup();
                    bloodgroup.setText(mybloodgroup.toUpperCase());

                    String myaddress=data.getAddress();
                    address.setText(myaddress);

                    String mypincode=data.getPincode();
                    pincode.setText(mypincode);


                    Glide.with(MyProfileActivity.this).load(data.getAvatar()).into(profilepic);


                }
                else {
                    if (response.code()==401){
                        AppUtils.SessionExpired(MyProfileActivity.this);
                    }
                    Toast.makeText(MyProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<MyProfileModel> call, Throwable t) {
                Toast.makeText(MyProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialogFragment.dismiss();
            }
        });

    }
}
