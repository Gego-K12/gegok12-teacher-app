package com.gegosoft.yourappname.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gegosoft.yourappname.Helper.PurchaseHelper;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.ExamListModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Helper.AppUtils;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamMarkActivity extends AppCompatActivity {


    ImageView backbtn;
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    List<ExamListModel.Datum> datumList;
    List<MyExamList> myExamLists;
    TextView nodatatxtvw;
    ConstraintLayout examContainer;
    FrameLayout purchaseContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_mark);
        myExamLists =  new ArrayList<>();

        backbtn = findViewById(R.id.backbtn_exam);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.examlist_recyclerview);
        nodatatxtvw = findViewById(R.id.textnodata);
        examContainer = findViewById(R.id.exam_container);
        purchaseContainer = findViewById(R.id.purchaseContainer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");

        if (isExamPurchased()) {
            getexamlists();
        } else {
            showPurchaseCard();
        }
    }
    private boolean isExamPurchased() {
        return userDetailsSharedPref.getBoolean("exam", false);
    }

    private void getexamlists() {

        Call<ExamListModel> modelCall = api.getexamlist(headermap);
        modelCall.enqueue(new Callback<ExamListModel>() {
            @Override
            public void onResponse(Call<ExamListModel> call, Response<ExamListModel> response) {
                if (response.isSuccessful())
                {
                    purchaseContainer.setVisibility(View.GONE);
                    examContainer.setVisibility(View.VISIBLE);
                    datumList = response.body().getData();
                    if(datumList.isEmpty()){
                        nodatatxtvw.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        nodatatxtvw.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < datumList.size(); i++) {
                            myExamLists.add(new MyExamList(datumList.get(i).getName(), datumList.get(i).getSchedule()));

                        }
                        setadapter();
                    }
                } else if (response.code() == 404) {
                    runOnUiThread(() -> {
                        showPurchaseCard();
                    });
                }
            }

            @Override
            public void onFailure(Call<ExamListModel> call, Throwable t) {
                AppUtils.APIFails(ExamMarkActivity.this,t);
            }
        });
    }
    private void showPurchaseCard() {
        purchaseContainer.setVisibility(View.VISIBLE);
        examContainer.setVisibility(View.GONE);
        purchaseContainer.removeAllViews();
        View card = PurchaseHelper.createPurchaseCard(this, purchaseContainer, "Exam", v -> {
            Toast.makeText(this, "Redirect to purchase flow", Toast.LENGTH_SHORT).show();
        });
        card.setAlpha(0f);
        purchaseContainer.addView(card);
        card.animate().alpha(1f).setDuration(300).start();
    }
    private void setadapter() {
        MyExamListAdapter   examListAdapter = new MyExamListAdapter(myExamLists);
        recyclerView.setAdapter(examListAdapter);
        examListAdapter.notifyDataSetChanged();
    }

    public  class MyExamList extends ExpandableGroup<ExamListModel.Datum.Schedule> {

        public MyExamList(String title, List<ExamListModel.Datum.Schedule> items) {
            super(title, items);
        }
    }

    private class MyExamListAdapter extends ExpandableRecyclerViewAdapter<MyExamListAdapter.FirstViewHolder,MyExamListAdapter.ChildHolder> {

        List<? extends ExpandableGroup> groups;

        public MyExamListAdapter(List<? extends ExpandableGroup> groups) {
            super(groups);
            this.groups = groups;
        }


        @Override
        public FirstViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_parent_task_layout, parent, false);
            return new FirstViewHolder(view);
        }

        @Override
        public ChildHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_exam_list, parent, false);
            return new ChildHolder(view);
        }

        @Override
        public void onBindChildViewHolder(ChildHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            ExamListModel.Datum.Schedule schedule = (ExamListModel.Datum.Schedule) group.getItems().get(childIndex);


            if ( schedule.getUploadStaus().equals("0") && schedule.getCount()>0)
            {
                holder.uploadmarks.setVisibility(View.GONE);
                holder.viewmarks.setVisibility(View.VISIBLE);
            }
            else if (schedule.getUploadStaus().equals("1")&& schedule.getCount()==0){
                holder.viewmarks.setVisibility(View.GONE);
                holder.uploadmarks.setVisibility(View.VISIBLE);
            }

                holder.class_name.setText(schedule.getClass_());
                holder.subject.setText(schedule.getSubjectName());
                holder.portion.setText(schedule.getPortion());
                holder.examdateandtime.setText(schedule.getExamDate().toString());
                holder.examtiming.setText(schedule.getExamTime().toString());
                holder.absentnew.setText(schedule.getAbsentCount().toString());
                holder.presentnew.setText(schedule.getPresentCount().toString());

            CharSequence foo = schedule.getSubjectName();
            String bar = foo.toString();
            String desiredString = bar.substring(0,3);

                holder.subjectnew.setText(desiredString);



                Bundle bundle = new Bundle();
                bundle.putInt("scheduleid",schedule.getScheduleId());

                holder.viewmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(ExamMarkActivity.this,ViewExamMarkActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


                holder.uploadmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ExamMarkActivity.this,UploadMarksActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });




        }

        @Override
        public void onBindGroupViewHolder(FirstViewHolder holder, int flatPosition, ExpandableGroup group) {

            holder.parent_title.setText(group.getTitle());

        }

        public class FirstViewHolder extends GroupViewHolder {
            TextView parent_title;
            ImageView arrow;
            public FirstViewHolder(View itemView) {
                super(itemView);
                parent_title = itemView.findViewById(R.id.parent_title);
                arrow = itemView.findViewById(R.id.arrow);
            }
        }

    public class ChildHolder extends ChildViewHolder {


        TextView class_name, examdateandtime, subject, portion, exam_title;
        TextView subjectnew, absentnew,presentnew,examtiming;

            Button uploadmarks;
            Button viewmarks;


        public ChildHolder(View itemView) {
            super(itemView);
                class_name = itemView.findViewById(R.id.classname_for_examlist);
                examdateandtime = itemView.findViewById(R.id.examdateandtim_for_exam);
                subject = itemView.findViewById(R.id.subject_for_exam);
                portion = itemView.findViewById(R.id.portion_for_exam);
                uploadmarks = itemView.findViewById(R.id.uploadmark_for_exam);
                exam_title = itemView.findViewById(R.id.exam_title);
                viewmarks =itemView.findViewById(R.id.viewmark_for_exam);
                subjectnew = itemView.findViewById(R.id.textView4_subject);
                absentnew = itemView.findViewById(R.id.textView6_count_absent);
                presentnew = itemView.findViewById(R.id.textView5_count_present);
                examtiming = itemView.findViewById(R.id.examtiming);
        }
    }
    }
}
