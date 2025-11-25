package com.gegosoft.yourappname.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.yourappname.Activity.PDFWebViewActivity;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.ApiSuccessModel;
import com.gegosoft.yourappname.Models.StudentSubmittedAssignmentModel;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsSubmittedAssignmentFragment extends Fragment {
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    String assignmentid;
    StudentsSubmittedAssignmentFragment.SubmittedAssignmentAdapter submittedAssignmentAdapter;
    List<StudentSubmittedAssignmentModel.Datum> Studentassignmentmodellist;
    private Bundle bundle;
    EditText marks, comments;
    TextView nodata;
    TextView name;
    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studentsubmittedassignment_layout, null);

        recyclerView = view.findViewById(R.id.studentsubmittedassignmentrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        bundle = this.getArguments();
        assignmentid = bundle.getString("assignmentid");
        nodata = view.findViewById(R.id.nodata);
        getstudentsubmittedassignment();

        return view;
    }

    private void getstudentsubmittedassignment() {
        Call<StudentSubmittedAssignmentModel> submittedassignmentcall = api.getstudentssubmittedassignment(headermap, assignmentid);
        submittedassignmentcall.enqueue(new Callback<StudentSubmittedAssignmentModel>() {
            @Override
            public void onResponse(Call<StudentSubmittedAssignmentModel> call, Response<StudentSubmittedAssignmentModel> response) {
                if (response.isSuccessful()) {
                    Studentassignmentmodellist = response.body().getData();
                    if (Studentassignmentmodellist != null && Studentassignmentmodellist.size() != 0) {
                        id = Studentassignmentmodellist.get(0).getId();
                        submittedAssignmentAdapter = new StudentsSubmittedAssignmentFragment.SubmittedAssignmentAdapter(Studentassignmentmodellist);
                        recyclerView.setAdapter(submittedAssignmentAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentSubmittedAssignmentModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public class SubmittedAssignmentAdapter extends RecyclerView.Adapter<StudentsSubmittedAssignmentFragment.SubmittedAssignmentAdapter.SubmittedAssignmentViewHolder> {
        List<StudentSubmittedAssignmentModel.Datum> assignmentModelList;

        public SubmittedAssignmentAdapter(List<StudentSubmittedAssignmentModel.Datum> assignmentModelList) {
            this.assignmentModelList = assignmentModelList;

        }

        @NonNull
        @Override
        public StudentsSubmittedAssignmentFragment.SubmittedAssignmentAdapter.SubmittedAssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentsubmittedassignment_recyclerview_layout, parent, false);
            return new StudentsSubmittedAssignmentFragment.SubmittedAssignmentAdapter.SubmittedAssignmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentsSubmittedAssignmentFragment.SubmittedAssignmentAdapter.SubmittedAssignmentViewHolder holder, int position) {
            holder.studentname.setText(assignmentModelList.get(position).getUserName());
            holder.rollno.setText(assignmentModelList.get(position).getRollNumber());
            holder.submissiondate.setText(assignmentModelList.get(position).getSubmittedOn());
            holder.marks.setText(assignmentModelList.get(position).getTotalMarks().toString());
            holder.title.setText(assignmentModelList.get(position).getTitle());

            holder.status.setText(assignmentModelList.get(position).getStatus());

            if (assignmentModelList.get(position).getUser_avatar() == null)
            {
                holder.circleImageView.setImageResource(R.drawable.profile_demopic);
            }
            else {
                Glide.with(StudentsSubmittedAssignmentFragment.this).load(assignmentModelList.get(position).getUser_avatar()).into(holder.circleImageView);
            }


            holder.addmarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View promptsView = li.inflate(R.layout.dialogue_addmarks, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    alertDialogBuilder.setView(promptsView);

                    marks = (EditText) promptsView.findViewById(R.id.edtmarks);
                    comments = (EditText) promptsView.findViewById(R.id.edtcomments);
                    name = promptsView.findViewById(R.id.name_for_particular_user);
                    name.setText(assignmentModelList.get(position).getUserName());
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Addmarks();
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
            });
            holder.pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pdfUrl="";
                    Bundle extras = new Bundle();
                    if ( assignmentModelList.get(position).getAssignmentFile().size()!=0) {
                        List<String> imageList = new ArrayList<>();
                        for (int i = 0; i < assignmentModelList.get(position).getAssignmentFile().size(); i++) {
                           imageList.add(assignmentModelList.get(position).getAssignmentFile().get(i).getPath());
                            pdfUrl = assignmentModelList.get(position).getAssignmentFile().get(i).getPath();
                        }
                        if(pdfUrl.equals("")){

                        }else {
                            extras.putString("url", pdfUrl);
                            startActivity(new Intent(requireActivity(), PDFWebViewActivity.class).putExtras(extras));
                        }
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return assignmentModelList.size();
        }

        public class SubmittedAssignmentViewHolder extends RecyclerView.ViewHolder {
            TextView studentname, rollno, submissiondate, marks,title;
            ImageView pdf;
            Button addmarks,status;
            CircleImageView circleImageView;

            public SubmittedAssignmentViewHolder(@NonNull View itemView) {
                super(itemView);
                studentname = itemView.findViewById(R.id.studentsubmittedassignmentname);
                rollno = itemView.findViewById(R.id.studentsubmittedassignmentrollno);
                marks = itemView.findViewById(R.id.totalsamarks);
                status = itemView.findViewById(R.id.studentsubmittedassignmentstatus);
                submissiondate = itemView.findViewById(R.id.studentsubmittedassignmentsubmissiondate);
                pdf = itemView.findViewById(R.id.studentsubmittedassignmentpdf);
                addmarks = itemView.findViewById(R.id.saaddmarksbtn);
                circleImageView = itemView.findViewById(R.id.propic);
                title = itemView.findViewById(R.id.assign_title);
            }
        }
    }

    private void Addmarks() {
        Call<ApiSuccessModel> successModelCall = api.getaddmarks(headermap, id, marks.getText().toString(), comments.getText().toString());
        successModelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Marks Added Succesfully", Toast.LENGTH_SHORT).show();
                    submittedAssignmentAdapter.notifyDataSetChanged();
                    getActivity().getOnBackPressedDispatcher().onBackPressed();
                }
                if (response.code() == 422)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } else
                    {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}






