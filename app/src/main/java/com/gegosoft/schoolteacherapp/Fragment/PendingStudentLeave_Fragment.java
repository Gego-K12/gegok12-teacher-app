package com.gegosoft.schoolteacherapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Helper.CheckNetwork;
import com.gegosoft.schoolteacherapp.Models.Addcomments;
import com.gegosoft.schoolteacherapp.Models.PendingStudentleaveModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingStudentLeave_Fragment extends Fragment {
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    RecyclerView recyclerView;
    TextView nodata;
    EditText  comments,rejectcomments;
    List<PendingStudentleaveModel.Datum> pendinglist;
    ChangeCompletedStatus changeCompletedStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, null);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        nodata = view.findViewById(R.id.textnodata);
        if (CheckNetwork.isInternetAvailable(getContext())) {
            getpendinglist();

        }

        return view;
    }

    private void getpendinglist() {
        Call<PendingStudentleaveModel> modelCall = api.getpendingleave(headermap);
        modelCall.enqueue(new Callback<PendingStudentleaveModel>() {
            @Override
            public void onResponse(Call<PendingStudentleaveModel> call, Response<PendingStudentleaveModel> response) {
                if (response.isSuccessful()) {
                    pendinglist = response.body().getData();
                    if (pendinglist != null && pendinglist.size() != 0) {
                        StudentPendingAdapter studentPendingAdapter = new StudentPendingAdapter(pendinglist);
                        recyclerView.setAdapter(studentPendingAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingStudentleaveModel> call, Throwable t) {

            }
        });
    }

    public class StudentPendingAdapter extends RecyclerView.Adapter<StudentPendingAdapter.MyViewHolder> {

        List<PendingStudentleaveModel.Datum> datumList;

        public StudentPendingAdapter(List<PendingStudentleaveModel.Datum> datumList) {
            this.datumList = datumList;
        }

        @NonNull
        @Override
        public StudentPendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pendingstudentlist, parent, false);
            return new StudentPendingAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentPendingAdapter.MyViewHolder holder, int position) {
            holder.name.setText(datumList.get(position).getName());
            holder.fromdate.setText(datumList.get(position).getFrom());
            holder.todate.setText(datumList.get(position).getTo());
            holder.reason.setText(datumList.get(position).getReason());
            holder.remarks.setText(datumList.get(position).getRemarks());
            holder.status.setText(datumList.get(position).getStatusName());

            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    LayoutInflater li = LayoutInflater.from(getContext());
                    View promptsView = li.inflate(R.layout.custom_enter_comments, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setTitle(" Teacher Comments");


                    comments = promptsView.findViewById(R.id.comments);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Call<Addcomments> modelCall = api.getleaveapprovecomments(headermap, pendinglist.get(holder.getBindingAdapterPosition()).getId().toString(),  comments.getText().toString());
                                    modelCall.enqueue(new Callback<Addcomments>() {
                                        @Override
                                        public void onResponse(Call<Addcomments> call, Response<Addcomments> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getContext(), "Leave Approved Successfully", Toast.LENGTH_SHORT).show();
                                                getpendinglist();
                                                changeCompletedStatus.changestatus();
                                                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Addcomments> call, Throwable t) {
                                            AppUtils.APIFails(getContext(), t);
                                        }
                                    });
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

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(getContext());
                    View promptsView = li.inflate(R.layout.custom_reject_comments, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setTitle(" Teacher Comments");

                    rejectcomments = promptsView.findViewById(R.id.reject_comments);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Call<Addcomments> modelCall = api.getleaverejectcomments(headermap, pendinglist.get(holder.getBindingAdapterPosition()).getId().toString(), rejectcomments.getText().toString());
                                    modelCall.enqueue(new Callback<Addcomments>() {
                                        @Override
                                        public void onResponse(Call<Addcomments> call, Response<Addcomments> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getContext(), "Leave Rejected Successfully", Toast.LENGTH_SHORT).show();
                                                getpendinglist();
                                                changeCompletedStatus.changestatus();
                                                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<Addcomments> call, Throwable t) {
                                            AppUtils.APIFails(getContext(), t);
                                        }
                                    });
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
        }

        @Override
        public int getItemCount()
        {
            return datumList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, fromdate, todate, remarks, reason, status;
            Button approve, reject;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.student_name_for_leave);
                fromdate = itemView.findViewById(R.id.studentleavefromdate);
                todate = itemView.findViewById(R.id.studentleavetodate);
                remarks = itemView.findViewById(R.id.studentleaveremarks);
                reason = itemView.findViewById(R.id.studentleavereason);
                status = itemView.findViewById(R.id.studentleavestatus);
                approve = itemView.findViewById(R.id.approveleavebtn);
                reject = itemView.findViewById(R.id.rejectleavebtn);

            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChangeCompletedStatus) {
            changeCompletedStatus = (ChangeCompletedStatus) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCommunicationListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        changeCompletedStatus = null;
    }

    public interface ChangeCompletedStatus{
        void changestatus();
    }
}
