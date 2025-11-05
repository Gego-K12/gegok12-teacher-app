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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.schoolteacherapp.Activity.AddHomeworkActivity;
import com.gegosoft.schoolteacherapp.Activity.PDFWebViewActivity;
import com.gegosoft.schoolteacherapp.Activity.StudentSubmittedHomeworkActivity;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.ApiSuccessModel;
import com.gegosoft.schoolteacherapp.Models.PendingHomeworkModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingHomework_Fragment extends Fragment {
    RecyclerView recyclerView;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    TextView nodata;
    List<PendingHomeworkModel.Datum> pendinghomeworklist;
    PendingHomeworkAdapter pendingHomeworkAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, null);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        nodata = view.findViewById(R.id.textnodata);
        getpendinghomework();
        return view;
    }

    private void getpendinghomework() {
        Call<PendingHomeworkModel> modelCall = api.getpendinghomework(headermap);
        modelCall.enqueue(new Callback<PendingHomeworkModel>() {
            @Override
            public void onResponse(Call<PendingHomeworkModel> call, Response<PendingHomeworkModel> response) {
                pendinghomeworklist = response.body().getData();
                if (pendinghomeworklist != null && pendinghomeworklist.size() != 0) {
                    pendingHomeworkAdapter = new PendingHomeworkAdapter(pendinghomeworklist);
                    recyclerView.setAdapter(pendingHomeworkAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PendingHomeworkModel> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class PendingHomeworkAdapter extends RecyclerView.Adapter<PendingHomeworkAdapter.Myviewholder> {
        List<PendingHomeworkModel.Datum> hwModelList;

        public PendingHomeworkAdapter(List<PendingHomeworkModel.Datum> hwModelList) {
            this.hwModelList = hwModelList;
        }

        @NonNull
        @Override
        public PendingHomeworkAdapter.Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pendinghomework_layout, parent, false);
            return new PendingHomeworkAdapter.Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PendingHomeworkAdapter.Myviewholder holder, int position) {
            holder.classname.setText(hwModelList.get(position).getClassName());

            holder.submittedhwdate.setText("Submission Date: "+hwModelList.get(position).getSubmission_date());

            if ( hwModelList.get(position).getComments() == null ) {

                holder.comments.setText(" ");
            } else {

                holder.comments.setText("Principal's comment : "+hwModelList.get(position).getComments());
            }


            if ((hwModelList.get(position).getSubjectName() == null))
            {
                holder.subject.setText("");
            }
            else
                {
                holder.subject.setText(hwModelList.get(position).getSubjectName());
            }
            holder.date.setText(hwModelList.get(position).getDate());
            holder.desc.setText(Html.fromHtml(hwModelList.get(position).getDescription()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.desc.setText(Html.fromHtml(hwModelList.get(holder.getBindingAdapterPosition()).getDescription()));
                    holder.desc.setMaxLines(Integer.MAX_VALUE);
                    holder.desc.setTextIsSelectable(true);
                }
            });

            holder.pendingcount.setText(hwModelList.get(position).getPendingCount().toString());



            if (hwModelList.get(position).getAttachment().equals("")) {
                holder.attachment.setVisibility(View.GONE);
            } else {
                holder.attachment.setVisibility(View.VISIBLE);
            }


            if (hwModelList.get(position).getStatus().equalsIgnoreCase("approved")) {
                Glide.with(getContext()).load(R.drawable.ic_new_checked).into(holder.status);
                holder.delete.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);

            } else if (hwModelList.get(position).getStatus().equalsIgnoreCase("pending")) {
                Glide.with(getContext()).load(R.drawable.new_pending).into(holder.status);
                holder.delete.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.VISIBLE);

            } else if (hwModelList.get(position).getStatus().equalsIgnoreCase("rejected")) {
                Glide.with(getContext()).load(R.drawable.ic_new_cancel).into(holder.status);
                holder.delete.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
                holder.students.setVisibility(View.GONE);
            }


            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), AddHomeworkActivity.class);
                    String homework=hwModelList.get(holder.getBindingAdapterPosition()).getId().toString();
                    intent.putExtra("homeworkid",homework);
                    intent.putExtra("edithomework","toedit");
                    startActivity(intent);
                    getpendinghomework();
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to delete the Homework ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deletehomework(hwModelList.get(holder.getBindingAdapterPosition()).getId().toString());
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
                    getpendinghomework();
                }
            });


            holder.attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle extras = new Bundle();
                    List<String> newList = new ArrayList<>();
                    newList.add(hwModelList.get(holder.getBindingAdapterPosition()).getAttachment());
                    for (int i = 0; i < newList.size(); i++) {
                        if(newList.get(i).endsWith("pdf")){
                            extras.putString("url", String.valueOf(hwModelList.get(holder.getBindingAdapterPosition()).getAttachment()));
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
                                            .load(hwModelList.get(holder.getBindingAdapterPosition()).getAttachment())
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
                public void onClick(View view) {
                    Bundle extras = new Bundle();
                    extras.putString("homeworkid", String.valueOf(hwModelList.get(holder.getBindingAdapterPosition()).getId()));
                    startActivity(new Intent(getContext(), StudentSubmittedHomeworkActivity.class).putExtras(extras));
                }
            });
        }

        private void deletehomework(final String homeworkid) {
            Call<ApiSuccessModel>modelCall=api.getdeletehomeworklist(headermap, String.valueOf(homeworkid));
            modelCall.enqueue(new Callback<ApiSuccessModel>() {
                @Override
                public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                    if(response.isSuccessful()){
                        for (int i =0;i<hwModelList.size();i++){
                            if (hwModelList.get(i).getId().equals(homeworkid)){
                                hwModelList.remove(i);
                                pendingHomeworkAdapter.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(getContext(),"Homework Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return hwModelList.size();
        }

        public class Myviewholder extends RecyclerView.ViewHolder {
            TextView classname, subject, date, desc, pendingcount,submittedhwdate;
            ImageView edit, delete;
            TextView comments;
            LinearLayout attachment;
            ImageView students, status;

            public Myviewholder(@NonNull View itemView) {
                super(itemView);
                classname = itemView.findViewById(R.id.pendinghwclass);
                subject = itemView.findViewById(R.id.pendinghwsubjectname);
                date = itemView.findViewById(R.id.pendinghwdate);
                desc = itemView.findViewById(R.id.pendinghwdesc);
                attachment = itemView.findViewById(R.id.attachmentlayout);
                pendingcount = itemView.findViewById(R.id.pendinghwcount);
                students = itemView.findViewById(R.id.viewfiless);
                status = itemView.findViewById(R.id.statusofhw);
                edit = itemView.findViewById(R.id.edit_hw);
                delete = itemView.findViewById(R.id.delete_hw);
                comments = itemView.findViewById(R.id.commentbyprincipalhw);
                submittedhwdate=itemView.findViewById(R.id.submittedhwdate);
            }
        }
    }
}
