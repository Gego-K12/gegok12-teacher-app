package com.gegosoft.schoolteacherapp.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.schoolteacherapp.Models.AssignmentListModel;
import com.gegosoft.schoolteacherapp.Models.DeleteAssignmentModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentList extends AppCompatActivity {
    RecyclerView recyclerView;
    Map<String, String> headermap;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    List<AssignmentListModel.Datum> assignmentListModels;
    AssignmentListAdapter assignmentListAdapter;
    ImageView back;
    TextView nodatavw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignmentlist_layout);
        recyclerView = findViewById(R.id.assignmentlistrecyclerview);
        nodatavw = findViewById(R.id.nodatatxt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        assignmentListModels = new ArrayList<>();
        api = ApiClient.getClient().create(Api.class);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        headermap = new HashMap<>();

        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });

        Call<AssignmentListModel> listModelCall = api.getassignmentlist(headermap);
        listModelCall.enqueue(new Callback<AssignmentListModel>()
        {
            @Override
            public void onResponse(Call<AssignmentListModel> call, Response<AssignmentListModel> response) {
                if (response.isSuccessful()) {
                    assignmentListModels = response.body().getData();
                    if(assignmentListModels.size()!=0) {
                        setadapter();
                        recyclerView.setVisibility(View.VISIBLE);
                        nodatavw.setVisibility(View.GONE);
                    }else {
                        recyclerView.setVisibility(View.GONE);
                        nodatavw.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(AssignmentList.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssignmentListModel> call, Throwable t) {
                Toast.makeText(AssignmentList.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setadapter() {
        assignmentListAdapter = new AssignmentListAdapter(assignmentListModels);
        recyclerView.setAdapter(assignmentListAdapter);
        assignmentListAdapter.notifyDataSetChanged();

    }

    private void deleteassignment(final Integer assignmentid) {


        Call<DeleteAssignmentModel> deleteAssignmentModelCall = api.getdeleteassignmt(headermap, String.valueOf(assignmentid));
        deleteAssignmentModelCall.enqueue(new Callback<DeleteAssignmentModel>() {
            @Override
            public void onResponse(Call<DeleteAssignmentModel> call, Response<DeleteAssignmentModel> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < assignmentListModels.size(); i++) {
                        if (assignmentListModels.get(i).getId().equals(assignmentid)) {
                            assignmentListModels.remove(i);
                            assignmentListAdapter.notifyDataSetChanged();

                        }
                    }
                    Toast.makeText(AssignmentList.this, "Assignment Deleted Successfully", Toast.LENGTH_SHORT).show();
                    getOnBackPressedDispatcher().onBackPressed();
                    finish();

                } else {
                    Toast.makeText(AssignmentList.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteAssignmentModel> call, Throwable t) {
                Toast.makeText(AssignmentList.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.AssignmentViewHolder> {
        List<AssignmentListModel.Datum> assignmentListModelList;

        public AssignmentListAdapter(List<AssignmentListModel.Datum> assignmentListModelList) {
            this.assignmentListModelList = assignmentListModelList;


        }

        @NonNull
        @Override
        public AssignmentListAdapter.AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignmentlist_recyclerview_layout, parent, false);
            return new AssignmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AssignmentListAdapter.AssignmentViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.Class.setText(assignmentListModelList.get(position).getClass_());
            holder.assigneddate.setText(assignmentListModelList.get(position).getAssignedDate());

            holder.title.setText(assignmentListModelList.get(position).getTitle());

            holder.submissiondate.setText(assignmentListModelList.get(position).getSubmissionDate());

            holder.mark.setText(assignmentListModelList.get(position).getMarks().toString());
            holder.description.setText(assignmentListModelList.get(position).getDescription());
            holder.commetsbyprince.setText("Comments : "+assignmentListModelList.get(position).getComments());


            holder.subject.setText(assignmentListModelList.get(position).getSubject());

            if(assignmentListModelList.get(position).getAttachment().equals("")){
                holder.documentview.setVisibility(View.GONE);
            }

            if (assignmentListModelList.get(position).getStatus().equals("approved")) {
                Glide.with(AssignmentList.this).load(R.drawable.ic_new_checked).into(holder.status);
                holder.delete.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
                holder.commetsbyprince.setVisibility(View.VISIBLE);

            } else if (assignmentListModelList.get(position).getStatus().equals("rejected")){

                Glide.with(AssignmentList.this).load(R.drawable.ic_new_cancel).into(holder.status);
                holder.delete.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
                holder.commetsbyprince.setVisibility(View.VISIBLE);

            }

            else  if (assignmentListModelList.get(position).getStatus().equals("pending"))
                {
                Glide.with(AssignmentList.this).load(R.drawable.new_pending).into(holder.status);
                holder.delete.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.VISIBLE);
                holder.commentsection.setVisibility(View.GONE);

            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentList.this);
                    builder.setTitle("Are you sure")
                            .setMessage("You want to delete the assignment")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteassignment(assignmentListModelList.get(position).getId());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            holder.documentview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    List<String> newList = new ArrayList<>();
                    newList.add(assignmentListModelList.get(position).getAttachment());
                    for (int i = 0; i < newList.size(); i++) {
                        if(newList.get(i).endsWith("pdf")){
                            extras.putString("url", String.valueOf(assignmentListModelList.get(position).getAttachment()));
                            startActivity(new Intent(AssignmentList.this, PDFWebViewActivity.class).putExtras(extras));
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentList.this);
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
                                    Glide.with(AssignmentList.this)
                                            .load(assignmentListModelList.get(position).getAttachment())
                                            .into(image);
                                }
                            });
                            dialog.show();
                        }
                    }
                }
            });


            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AssignmentList.this, Add_AssigmentActivity.class);
                    String assignment = assignmentListModelList.get(position).getId().toString();
                    intent.putExtra("assignmentid", assignment);
                    intent.putExtra("editassignment", "toedit");
                    startActivity(intent);
                    finish();
                }
            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(AssignmentList.this, StudentsAssignmentActivity.class);
                    String assignment = assignmentListModelList.get(position).getId().toString();
                    intent.putExtra("assignmentid", assignment);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return assignmentListModelList.size();
        }

        public class AssignmentViewHolder extends RecyclerView.ViewHolder {
            TextView Class, title, assigneddate, submissiondate, mark, description,subject;
            ImageView delete, edit, view;
            ImageView status;
            ImageView documentview;
            LinearLayout commentsection;
            TextView commetsbyprince;

            public AssignmentViewHolder(@NonNull View itemView) {
                super(itemView);
                Class = itemView.findViewById(R.id.assignmentlistclass);
                title = itemView.findViewById(R.id.assignmentlisttitle);
                assigneddate = itemView.findViewById(R.id.assignmentlistassigneddate);
                submissiondate = itemView.findViewById(R.id.assignmentlistsubmissiondate);
                mark = itemView.findViewById(R.id.assignmentlistmark);
                description = itemView.findViewById(R.id.assignmentlistdescription);
                delete = itemView.findViewById(R.id.assignmentlistdeletebtn);
                edit = itemView.findViewById(R.id.assignmentlisteditbtn);
                view = itemView.findViewById(R.id.assignmentlistviewbtn);
                status = itemView.findViewById(R.id.assignmentstatus);
                documentview = itemView.findViewById(R.id.attachment_by_teacher);
                subject = itemView.findViewById(R.id.assignmentlistsubject);
                commentsection = itemView.findViewById(R.id.commentsection);
                commetsbyprince = itemView.findViewById(R.id.commentsbyprincipal);
            }
        }
    }

}
