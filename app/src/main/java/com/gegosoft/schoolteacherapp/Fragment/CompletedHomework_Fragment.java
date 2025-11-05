package com.gegosoft.schoolteacherapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.schoolteacherapp.Activity.PDFWebViewActivity;
import com.gegosoft.schoolteacherapp.Activity.StudentSubmittedHomeworkActivity;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.CompletedHomeworkModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedHomework_Fragment extends Fragment {
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    TextView nodata;
    List<CompletedHomeworkModel.Datum> completedhomeworklist;
    CompletedHomeworkAdapter completedHomeworkAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recyclerview,null);
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        nodata=view.findViewById(R.id.textnodata);
        getcompletedhomework();
        return view;
    }
    private void getcompletedhomework(){
        Call<CompletedHomeworkModel> modelCall=api.getcompletedhomework(headermap);
        modelCall.enqueue(new Callback<CompletedHomeworkModel>() {
            @Override
            public void onResponse(Call<CompletedHomeworkModel> call, Response<CompletedHomeworkModel> response) {
                completedhomeworklist=response.body().getData();
                if(completedhomeworklist!=null&&completedhomeworklist.size()!=0) {
                    completedHomeworkAdapter = new CompletedHomeworkAdapter(completedhomeworklist);
                    recyclerView.setAdapter(completedHomeworkAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<CompletedHomeworkModel> call, Throwable t) {

            }
        });
    }
    public class CompletedHomeworkAdapter extends RecyclerView.Adapter<CompletedHomeworkAdapter.Myviewholder>{
        List<CompletedHomeworkModel.Datum>assignmentModelList;

        public CompletedHomeworkAdapter(List<CompletedHomeworkModel.Datum> assignmentModelList) {
            this.assignmentModelList = assignmentModelList;
        }
        @NonNull
        @Override
        public CompletedHomeworkAdapter.Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_completedhomework_layout,parent,false);
            return new CompletedHomeworkAdapter.Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CompletedHomeworkAdapter.Myviewholder holder, int position) {

            holder.classname.setText(assignmentModelList.get(position).getClassName());
            if ((assignmentModelList.get(position).getSubjectName()==null)){
                holder.subject.setText("No Subjectname Added");
            }else {
                holder.subject.setText(assignmentModelList.get(position).getSubjectName());
            }
            holder.date.setText(assignmentModelList.get(position).getDate());
            holder.submittedhwdate.setText("Submission Date: "+assignmentModelList.get(position).getSubmission_date());
            holder.desc.setText(Html.fromHtml(assignmentModelList.get(position).getDescription()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.desc.setText(Html.fromHtml(assignmentModelList.get(holder.getBindingAdapterPosition()).getDescription()));
                    holder.desc.setMaxLines(Integer.MAX_VALUE);
                    holder.desc.setTextIsSelectable(true);
                }
            });
            holder.pendingcount.setText(assignmentModelList.get(position).getPendingCount().toString());
            if(assignmentModelList.get(position).getAttachment().equals("")){
                holder.attachment.setVisibility(View.GONE);
            }
            else {
                holder.attachment.setVisibility(View.VISIBLE);
            }
            holder.attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle extras = new Bundle();
                    List<String> newList = new ArrayList<>();
                    newList.add(assignmentModelList.get(holder.getBindingAdapterPosition()).getAttachment());
                    for (int i = 0; i < newList.size(); i++) {
                        if(newList.get(i).endsWith("pdf")){
                            extras.putString("url", String.valueOf(assignmentModelList.get(holder.getBindingAdapterPosition()).getAttachment()));
                            startActivity(new Intent(getContext(), PDFWebViewActivity.class).putExtras(extras));
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            final AlertDialog dialog = builder.create();
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogLayout = inflater.inflate(R.layout.custom_alert_imageview, null);
                            dialog.setView(dialogLayout);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface d) {
                                    ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                                    Glide.with(getContext())
                                            .load(assignmentModelList.get(holder.getBindingAdapterPosition()).getAttachment())
                                            .into(image);
                                }
                            });
                            dialog.show();
                        }
                    }

                }
            });
            holder.students.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putString("homeworkid", String.valueOf(assignmentModelList.get(holder.getBindingAdapterPosition()).getId()));
                    startActivity(new Intent(getContext(), StudentSubmittedHomeworkActivity.class).putExtras(extras));
                }
            });
        }

        @Override
        public int getItemCount() {
            return assignmentModelList.size();
        }

        public class Myviewholder extends RecyclerView.ViewHolder {
            TextView classname,subject,date,desc,pendingcount,submittedhwdate;
            LinearLayout attachment;
            ImageView students;
            public Myviewholder(@NonNull View itemView) {
                super(itemView);
                classname=itemView.findViewById(R.id.completedhwclass);
                subject=itemView.findViewById(R.id.completedhwsubjectname);
                date=itemView.findViewById(R.id.completedhwdate);
                desc=itemView.findViewById(R.id.completedhwdesc);
                attachment=itemView.findViewById(R.id.attachmentlayout);
                pendingcount=itemView.findViewById(R.id.completedhwcount);
                students=itemView.findViewById(R.id.completedsubmittedstudents);
                submittedhwdate=itemView.findViewById(R.id.submittedhwdate); //submissiondatetxt
            }
        }
    }
}
