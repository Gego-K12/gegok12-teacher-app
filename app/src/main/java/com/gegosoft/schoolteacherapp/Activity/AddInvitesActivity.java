package com.gegosoft.schoolteacherapp.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Adapter.SpinnerAdapter;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.ApiSuccessModel;
import com.gegosoft.schoolteacherapp.Models.InvitesModel;
import com.gegosoft.schoolteacherapp.Models.StandardList;
import com.gegosoft.schoolteacherapp.Models.StudentList;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInvitesActivity extends AppCompatActivity {
    List<StandardList.Datum> standardlist;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    Spinner selectclassspinner;
    String standard_id;
    List<StudentList.Datum> studentlist;
    RecyclerView recyclerView;
    List<InvitesModel.Datum> existing_students;
    TextView total_selected_text;
    CheckedTextView select_all_checkbox,select_none_checkbox;
    StudentAdapter studentAdapter;
    Button btn_submit;
    int video_conference_id;
    ImageView back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invites);
        video_conference_id = getIntent().getExtras().getInt("video_conference_id");

        existing_students = (List<InvitesModel.Datum>) getIntent().getExtras().getSerializable("existing_students");
        total_selected_text= findViewById(R.id.total_selected_text);
        selectclassspinner= findViewById(R.id.selectclassspinner);
        select_none_checkbox =  findViewById(R.id.select_none_checkbox);
        back =  findViewById(R.id.back);
        select_all_checkbox =  findViewById(R.id.select_all_checkbox);
        btn_submit =  findViewById(R.id.btn_submit);
        recyclerView =  findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        total_selected_text.setText("Selected "+String.valueOf(existing_students.size()));
        getClassData();

        selectclassspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    standard_id = standardlist.get(position-1).getId().toString();
                    getStudentsList();
                }
                else {
                    standard_id = "0" ;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_all_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_all_checkbox.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                select_none_checkbox.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                for (int i = 0;i<studentlist.size();i++){
                    studentlist.get(i).setStudentSelected(true);
                }
                studentAdapter.notifyDataSetChanged();
                total_selected_text.setText("Selected "+String.valueOf(studentlist.size()));
            }
        });
        select_none_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_all_checkbox.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                select_none_checkbox.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                for (int i = 0;i<studentlist.size();i++){
                    studentlist.get(i).setStudentSelected(false);
                }
                studentAdapter.notifyDataSetChanged();
                total_selected_text.setText("Selected 0");

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInvites();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void addInvites() {
        List<Integer> student_ids = new ArrayList<>();


        for (int i =0;i<studentlist.size();i++){
            if (studentlist.get(i).isStudentSelected()){
                student_ids.add(studentlist.get(i).getId());
            }
        }
        Call<ApiSuccessModel> modelCall = api.addInvitestoRoom(headermap,student_ids,video_conference_id);
        modelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AddInvitesActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {

            }
        });
    }

    private void getStudentsList() {
        Call<StudentList> studentListCall = api.getStudentList(headermap,standard_id);
        studentListCall.enqueue(new Callback<StudentList>() {
            @Override
            public void onResponse(Call<StudentList> call, Response<StudentList> response) {
                if (response.isSuccessful()){
                    studentlist =  response.body().getData();
                    if (studentlist.size()!=0){

                        for (int i = 0 ; i< studentlist.size();i++){
                            for (int j = 0;  j<existing_students.size();j++){
                                if (studentlist.get(i).getRollNumber().equalsIgnoreCase(existing_students.get(j).getRollNumber())){
                                    studentlist.get(i).setStudentSelected(true);
                                }

                            }
                        }
                        studentAdapter = new StudentAdapter();
                        recyclerView.setAdapter(studentAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<StudentList> call, Throwable t) {
                AppUtils.APIFails(AddInvitesActivity.this,t);
            }
        });
    }

    private void getClassData() {

        Call<StandardList> listCall = api.getStandardList(headermap);
        listCall.enqueue(new Callback<StandardList>() {
            @Override
            public void onResponse(Call<StandardList> call, Response<StandardList> response) {
                if (response.isSuccessful()){
                    standardlist = response.body().getData();
                    ArrayList<String> objects = new ArrayList<>();
                    for (int i =0 ; i < standardlist.size();i++){
                        objects.add(standardlist.get(i).getStandardSection());
                    }
                    objects.add(0,"Select Class");
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(objects,AddInvitesActivity.this);
                    selectclassspinner.setAdapter(spinnerAdapter);

                }
            }

            @Override
            public void onFailure(Call<StandardList> call, Throwable t) {
                AppUtils.APIFails(AddInvitesActivity.this,t);

            }
        });

    }
    private class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_student_list,parent,false);

            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.name.setText(studentlist.get(position).getFullname());
            holder.check_box.setChecked(studentlist.get(position).isStudentSelected());


            holder.check_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.check_box.isChecked()){
                        studentlist.get(position).setStudentSelected(true);
                    }
                    else {
                        studentlist.get(position).setStudentSelected(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return studentlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            CheckBox check_box;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                check_box = itemView.findViewById(R.id.check_box);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


}
