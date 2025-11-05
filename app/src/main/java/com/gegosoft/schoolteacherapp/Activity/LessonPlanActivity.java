package com.gegosoft.schoolteacherapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Models.LessonPlanModel;
import com.gegosoft.schoolteacherapp.Models.MyProfileModel;
import com.gegosoft.schoolteacherapp.Models.ShowLessonPlanModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  LessonPlanActivity extends AppCompatActivity {
    RecyclerView lessonplanrecyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    List<LessonPlanModel.Datum> lessonPlanModelList;
    LessonPlanAdapter lessonPlanAdapter;
    Integer lessonid;
    ImageView back;
    TextView nodata,profilename;
    CircleImageView profileimage;
    List<MyProfileModel> myProfileModelList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_plan);
        lessonplanrecyclerView = findViewById(R.id.recycler_view_lessonplan);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(LessonPlanActivity.this,RecyclerView.VERTICAL,false);
        lessonplanrecyclerView.setLayoutManager(linearLayoutManager);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        nodata=findViewById(R.id.nodata);
        back=findViewById(R.id.lessonplanback);
        profileimage=findViewById(R.id.lpprofilepic);
        profilename=findViewById(R.id.lpprofilename);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        calllessonplan();
        getprofiledetails();
    }
    private void calllessonplan() {
        Call<LessonPlanModel> lessonPlanModelCall = api.getlessonplanmodel(headermap);
        lessonPlanModelCall.enqueue(new Callback<LessonPlanModel>() {
            @Override
            public void onResponse(Call<LessonPlanModel> call, Response<LessonPlanModel> response) {
                if (response.isSuccessful()) {
                    lessonPlanModelList = response.body().getData();
                    if(lessonPlanModelList.size()!=0) {
                        lessonid = lessonPlanModelList.get(0).getId();
                        lessonPlanAdapter = new LessonPlanAdapter(lessonPlanModelList);
                        lessonplanrecyclerView.setAdapter(lessonPlanAdapter);
                        lessonplanrecyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    }
                    else {
                        lessonplanrecyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(LessonPlanActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<LessonPlanModel> call, Throwable t) {
                Toast.makeText(LessonPlanActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getprofiledetails() {

        Call<MyProfileModel> myProfileModelCall = api.getprofilemodel(headermap);
        myProfileModelCall.enqueue(new Callback<MyProfileModel>() {
            @Override
            public void onResponse(Call<MyProfileModel> call, Response<MyProfileModel> response) {
                if (response.isSuccessful()) {
                    MyProfileModel.Data data = response.body().getData();
                    myProfileModelList = new ArrayList<>();
                    profilename.setText(data.getFullname());
                    Glide.with(LessonPlanActivity.this).load(data.getAvatar()).into(profileimage);


                } else {
                    Toast.makeText(LessonPlanActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyProfileModel> call, Throwable t) {
                Toast.makeText(LessonPlanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class LessonPlanAdapter extends RecyclerView.Adapter<LessonPlanAdapter.LessonPlanViewHolder>
    {
        List<LessonPlanModel.Datum> datumList;
        public LessonPlanAdapter(List<LessonPlanModel.Datum> datumList)
        {
            this.datumList = datumList;
        }
        @NonNull
        @Override
        public LessonPlanAdapter.LessonPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lessonplan_recyclerview,parent,false);
            return new LessonPlanAdapter.LessonPlanViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull LessonPlanAdapter.LessonPlanViewHolder holder, int position) {
            holder.subject.setText(datumList.get(position).getSubject());
            holder.unitno.setText(datumList.get(position).getUnitNo());
            holder.title.setText(datumList.get(position).getTitle());
            holder.estimatedtime.setText(datumList.get(position).getDuration());
            holder.viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getviewmore();
                }
            });
        }
        @Override
        public int getItemCount() {
            return datumList.size();
        }
        public class LessonPlanViewHolder extends RecyclerView.ViewHolder {
            TextView subject,unitno,title,estimatedtime;
            ImageView viewmore;
            public LessonPlanViewHolder(@NonNull View itemView) {
                super(itemView);
                subject = itemView.findViewById(R.id.lpsubject);
                unitno = itemView.findViewById(R.id.lpunitno);
                title = itemView.findViewById(R.id.lpunittitle);
                estimatedtime = itemView.findViewById(R.id.lpestimatedtime);
                viewmore=itemView.findViewById(R.id.lpviewmoretxt);
            }
        }
    }
    private void getviewmore(){

        Call<ShowLessonPlanModel>showLessonPlanModelCall=api.getshowlessonplanmodel(headermap,lessonid);
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message","Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(),"loading screen");
        showLessonPlanModelCall.enqueue(new Callback<ShowLessonPlanModel>() {
            @Override
            public void onResponse(Call<ShowLessonPlanModel> call, Response<ShowLessonPlanModel> response) {
                if(response.isSuccessful()){
                    ShowLessonPlanModel showLessonPlanModels =response.body();
                    if (!showLessonPlanModels.getData().isEmpty()){
                        Bundle extras = new Bundle();

                        extras.putString("url", showLessonPlanModels.getData());
                        startActivity(new Intent(LessonPlanActivity.this,PDFWebViewActivity.class).putExtras(extras));
                    }
                    progressDialogFragment.dismiss();
                }

                else {
                    Toast.makeText(LessonPlanActivity.this, "Error", Toast.LENGTH_LONG).show();
                    progressDialogFragment.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ShowLessonPlanModel> call, Throwable t) {
                Toast.makeText(LessonPlanActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressDialogFragment.dismiss();
            }
        });

    }

}
