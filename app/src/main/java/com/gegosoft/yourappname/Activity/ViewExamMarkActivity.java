package com.gegosoft.yourappname.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.ViewMarkModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewExamMarkActivity extends AppCompatActivity {


    ImageView backbtn;
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    int scheduleid;
    List<ViewMarkModel.Datum> data;

    ViewMarkAdapter markAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam_mark);

        backbtn = findViewById(R.id.backbtn_viewmarkexam);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        scheduleid = getIntent().getExtras().getInt("scheduleid");


        recyclerView = findViewById(R.id.viewmarkexamlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");


        viewmarklists();


    }

    private void viewmarklists() {

        Call<ViewMarkModel> call = api.showmarks(headermap, scheduleid);
        call.enqueue(new Callback<ViewMarkModel>() {
            @Override
            public void onResponse(Call<ViewMarkModel> call, Response<ViewMarkModel> response) {
                if (response.isSuccessful()) {
                    data = response.body().getData();
                    setadapter();

                }
            }

            @Override
            public void onFailure(Call<ViewMarkModel> call, Throwable t) {

            }
        });


    }

    private void setadapter() {

        markAdapter = new ViewMarkAdapter(data);
        recyclerView.setAdapter(markAdapter);
        markAdapter.notifyDataSetChanged();

    }


    public class ViewMarkAdapter extends RecyclerView.Adapter<ViewMarkAdapter.MarkviewHolder> {
        List<ViewMarkModel.Datum> list;

        public ViewMarkAdapter(List<ViewMarkModel.Datum> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MarkviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_exam_mark_list, parent, false);
            return new MarkviewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MarkviewHolder holder, int position) {

            holder.exam_name.setText(data.get(position).getExamName());
            holder.subject.setText(data.get(position).getSubjectName());
            holder.mark.setText(data.get(position).getObtainedMarks());
            holder.stu_name.setText(data.get(position).getStudentName());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MarkviewHolder extends RecyclerView.ViewHolder {

            TextView stu_name, exam_name, subject, mark, grade;


            public MarkviewHolder(@NonNull View itemView) {
                super(itemView);


                stu_name = itemView.findViewById(R.id.studentname_viewmark);

                exam_name = itemView.findViewById(R.id.examname_viewmark);

                subject = itemView.findViewById(R.id.subject_for_viewexam);

                mark = itemView.findViewById(R.id.mark_viewexam);

                grade = itemView.findViewById(R.id.grade_viewexam);


            }
        }
    }


}