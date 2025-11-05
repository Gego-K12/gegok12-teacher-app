package com.gegosoft.schoolteacherapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.StudentCompletedAssignmentModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsCompletedAssignmentFragment extends Fragment {
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    StudentsCompletedAssignmentFragment.CompletedAssignmentAdapter completedAssignmentAdapter;
    List<StudentCompletedAssignmentModel.Datum>studentCompletedAssignmentModelList;
    String assignmentid;
    private Bundle bundle;
    TextView nodata;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studentcompletedassignment_layout,null);
        recyclerView=view.findViewById(R.id.studentcompletedassignmentrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        bundle = this.getArguments();
        assignmentid = bundle.getString("assignmentid");
        getcompletedstudenntsdata();
        nodata=view.findViewById(R.id.nodata);
        return view;
    }
    private void getcompletedstudenntsdata() {

        Call<StudentCompletedAssignmentModel> completedAssignmentModelCall = api.getstudentscompletedassignment(headermap,assignmentid);
        completedAssignmentModelCall.enqueue(new Callback<StudentCompletedAssignmentModel>() {
            @Override
            public void onResponse(Call<StudentCompletedAssignmentModel> call, Response<StudentCompletedAssignmentModel> response) {
                if (response.isSuccessful()) {
                    studentCompletedAssignmentModelList = response.body().getData();
                    if(studentCompletedAssignmentModelList!=null&&studentCompletedAssignmentModelList.size()!=0) {
                        completedAssignmentAdapter = new CompletedAssignmentAdapter(studentCompletedAssignmentModelList);
                        recyclerView.setAdapter(completedAssignmentAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    }



                    else {
                        recyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<StudentCompletedAssignmentModel> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT);
            }
        });

    }

    public class CompletedAssignmentAdapter extends RecyclerView.Adapter<StudentsCompletedAssignmentFragment.CompletedAssignmentAdapter.CompletedAssignmentViewHolder> {
        List<StudentCompletedAssignmentModel.Datum>studentCompletedAssignmentModels;
        private CompletedAssignmentAdapter(List<StudentCompletedAssignmentModel.Datum>studentCompletedAssignmentModels){
            this.studentCompletedAssignmentModels=studentCompletedAssignmentModels;
        }

        @NonNull
        @Override
        public StudentsCompletedAssignmentFragment.CompletedAssignmentAdapter.CompletedAssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.studentcompletedassignment_recyclerview_layout,parent,false);
            return new StudentsCompletedAssignmentFragment.CompletedAssignmentAdapter.CompletedAssignmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentsCompletedAssignmentFragment.CompletedAssignmentAdapter.CompletedAssignmentViewHolder holder, int position) {
            holder.studentname.setText(studentCompletedAssignmentModels.get(position).getUserName());
            holder.rollno.setText(studentCompletedAssignmentModels.get(position).getRollNumber());
            holder.submissiondate.setText(studentCompletedAssignmentModels.get(position).getSubmittedOn());
            holder.mark.setText(studentCompletedAssignmentModels.get(position).getObtainedMarks().toString());
            holder.status.setText(studentCompletedAssignmentModels.get(position).getStatus());
            holder.comments.setText(studentCompletedAssignmentModels.get(position).getComments());
            holder.title.setText(studentCompletedAssignmentModels.get(position).getTitle());

            holder.pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( studentCompletedAssignmentModels.get(position).getAssignmentFile().size()!=0)
                    {
                        List<String> imageList = new ArrayList<>();
                        Bundle bundle = new Bundle();
                        for(int i =0 ; i<  studentCompletedAssignmentModels.get(position).getAssignmentFile().size();i++){
                            imageList.add(studentCompletedAssignmentModels.get(position).getAssignmentFile().get(i).getPath());
                        }
                        bundle.putSerializable("images", (Serializable) imageList);
                        bundle.putInt("position", position);
                        FragmentTransaction ft = getParentFragmentManager().beginTransaction(); //getFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "galleryslideshow");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return studentCompletedAssignmentModels.size();
        }

        public class CompletedAssignmentViewHolder extends RecyclerView.ViewHolder {
            TextView studentname,rollno,submissiondate,mark,status,comments;
            TextView title;
            ImageView pdf;
            public CompletedAssignmentViewHolder(@NonNull View itemView) {
                super(itemView);
                studentname=itemView.findViewById(R.id.studentcompletedassignmentname);
                rollno=itemView.findViewById(R.id.studentcompletedassignmentrollno);
                mark=itemView.findViewById(R.id.studentcompletedassignmentmark);
                status=itemView.findViewById(R.id.studentcompletedassignmentstatus);
                comments=itemView.findViewById(R.id.studentcompletedassignmentcomments);
                submissiondate=itemView.findViewById(R.id.studentcompletedassignmentsubmissiondate);
                pdf=itemView.findViewById(R.id.viewfilesofimages);
                title =itemView.findViewById(R.id.title_complete_assignment);
            }
        }
    }
}