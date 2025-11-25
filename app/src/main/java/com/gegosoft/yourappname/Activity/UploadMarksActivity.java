package com.gegosoft.yourappname.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.AddMarkStudentListModel;
import com.gegosoft.yourappname.Models.SuccessApiCallModel;
import com.gegosoft.yourappname.Models.UploadMarksModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Helper.AppUtils;

import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadMarksActivity extends AppCompatActivity {


    ImageView backbtn;
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    List<AddMarkStudentListModel.Student> data;
    int scheduleid;
    EnterMarkAdapter enterMarkAdapter;
    Button uploadmarks;
    List<UploadMarksModel> listlist;

    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Intent intent;
            intent = new Intent(UploadMarksActivity.this, ExamMarkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_marks);

        listlist = new ArrayList<>();

        uploadmarks = findViewById(R.id.upload_marks_submit);

        backbtn = findViewById(R.id.backbtn_addmarks);

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        scheduleid = getIntent().getExtras().getInt("scheduleid");

        recyclerView = findViewById(R.id.addmarks_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");


        uploadmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadcall();
            }
        });

        getstudentlistforaddmarks();

    }

    private void uploadcall() {

            Gson gson = new Gson();
            String jsonObject = gson.toJson(data);

            AddMarkStudentListModel addMarkStudentListModel = new AddMarkStudentListModel();
            addMarkStudentListModel.setStudents(data);

            Call<SuccessApiCallModel> modelCall = api.uploadmarks(headermap, scheduleid, addMarkStudentListModel);
            modelCall.enqueue(new Callback<SuccessApiCallModel>() {
                @Override
                public void onResponse(Call<SuccessApiCallModel> call, Response<SuccessApiCallModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(UploadMarksActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        Toast.makeText(UploadMarksActivity.this, "Oops...!!! Once look back and check at the details you entered!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SuccessApiCallModel> call, Throwable t) {
                    AppUtils.APIFails(UploadMarksActivity.this, t);
                    Toast.makeText(UploadMarksActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                }
            });

        }
    private void getstudentlistforaddmarks() {

        Call<AddMarkStudentListModel> modelCall = api.getstudentlistforaddmarks(headermap, scheduleid);
        modelCall.enqueue(new Callback<AddMarkStudentListModel>() {
            @Override
            public void onResponse(Call<AddMarkStudentListModel> call, Response<AddMarkStudentListModel> response) {

                if (response.isSuccessful()) {
                    data = response.body().getStudents();
                    setadapter();
                }

            }

            @Override
            public void onFailure(Call<AddMarkStudentListModel> call, Throwable t) {

            }
        });
    }
    private void setadapter() {

        enterMarkAdapter = new EnterMarkAdapter(data);
        recyclerView.setAdapter(enterMarkAdapter);
       enterMarkAdapter.notifyDataSetChanged();

    }

    public class EnterMarkAdapter extends RecyclerView.Adapter<EnterMarkAdapter.EnterMarkViewHolder> {

        List<AddMarkStudentListModel.Student> listModels;

        public EnterMarkAdapter(List<AddMarkStudentListModel.Student> listModels) {
            this.listModels = listModels;
        }


        @NotNull
        @Override
        public EnterMarkViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_mark_for_students, parent, false);
            return new EnterMarkViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull EnterMarkViewHolder holder, int position) {

            holder.marks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()!=0)
                    {
                        data.get(position).setMarks(s.toString());
                    }
                    else {
                        data.get(position).setMarks("");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.name.setText(data.get(position).getName());

            holder.id.setText(data.get(position).getUserId().toString());


            Glide.with(UploadMarksActivity.this)
                    .load(data.get(position).getAvatar())
                    .into(holder.imageView);

            holder.present_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.present_btn.setVisibility(View.GONE);
                    holder.absent_btn.setVisibility(View.VISIBLE);
                    holder.marks.setVisibility(View.GONE);
                    holder.marktv_dummy.setVisibility(View.GONE);
                    data.get(position).setStatus("absent");
                    data.get(position).setMarks("0");
                }
            });

            holder.absent_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.absent_btn.setVisibility(View.GONE);
                    holder.present_btn.setVisibility(View.VISIBLE);
                    holder.marks.setVisibility(View.VISIBLE);
                    holder.marktv_dummy.setVisibility(View.VISIBLE);


                    data.get(position).setStatus("present");

                    holder.marks.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {


                            if (s.length()!=0){
                                data.get(position).setMarks(s.toString());
                            }
                            else {
                                data.get(position).setMarks("0");
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }
            });
        }

        @Override
        public int getItemCount() {

            return data.size();

        }

        public class EnterMarkViewHolder extends RecyclerView.ViewHolder {

            TextView name, id, successtv,marktv_dummy;

            CircleImageView imageView;

            Button present_btn, absent_btn;

            public EditText marks;

            Button submit;

            public EnterMarkViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.student_name);

                id = itemView.findViewById(R.id.student_id);

                imageView = itemView.findViewById(R.id.student_dp);

                present_btn = itemView.findViewById(R.id.present_btn);

                absent_btn = itemView.findViewById(R.id.absent_id);

                marks = itemView.findViewById(R.id.marks_upload);

                submit = itemView.findViewById(R.id.submit_bt);

                successtv = itemView.findViewById(R.id.success_textview);

                marktv_dummy = itemView.findViewById(R.id.marktv_dummy);

            }
        }
    }

}