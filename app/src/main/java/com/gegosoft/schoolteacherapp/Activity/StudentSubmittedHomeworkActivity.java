package com.gegosoft.schoolteacherapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Fragment.SlideshowDialogFragment;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Helper.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Models.Addcomments;
import com.gegosoft.schoolteacherapp.Models.StudentSubmittedHomeworkModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.CheckNetwork;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentSubmittedHomeworkActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textnodata, toolbartext;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    Api api;
    MyAdapter myAdapter;
    ImageView back;
    List<StudentSubmittedHomeworkModel.Datum> data;
    String homeworkid;
    Intent intent;
    AppCompatEditText emojiEditText;
    ViewGroup rootview;
    TextView name_for_alertdialog;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentsubmittedhw_recyclerview);
        recyclerView = findViewById(R.id.recyclerview);
        textnodata = findViewById(R.id.textnodata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(StudentSubmittedHomeworkActivity.this);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        back = findViewById(R.id.backbtn);
        toolbar = findViewById(R.id.toolbar);
        toolbartext = findViewById(R.id.toolbarname);
        toolbartext.setText("Submitted Students");
        EmojiManager.install(new GoogleEmojiProvider());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        intent = getIntent();
        homeworkid = intent.getStringExtra("homeworkid");
        if (CheckNetwork.isInternetAvailable(StudentSubmittedHomeworkActivity.this)) {
            getdata();
        }
    }

    private void getdata() {
        api = ApiClient.getClient().create(Api.class);
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(StudentSubmittedHomeworkActivity.this.getSupportFragmentManager(), "loading screen");
        Call<StudentSubmittedHomeworkModel> ModelCall = api.getsubmittedhomework(headermap, homeworkid);
        ModelCall.enqueue(new Callback<StudentSubmittedHomeworkModel>() {
            @Override
            public void onResponse(Call<StudentSubmittedHomeworkModel> call, Response<StudentSubmittedHomeworkModel> response) {
                if (response.isSuccessful()) {
                    data = response.body().getData();
                    if (data.size() != 0) {
                        myAdapter = new MyAdapter(data, StudentSubmittedHomeworkActivity.this);
                        recyclerView.setAdapter(myAdapter);

                    } else {
                        textnodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (response.code() == 401) {
                        AppUtils.SessionExpired(StudentSubmittedHomeworkActivity.this);
                    }
                }
                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<StudentSubmittedHomeworkModel> call, Throwable t) {
                AppUtils.APIFails(StudentSubmittedHomeworkActivity.this, t);
                progressDialogFragment.dismiss();
            }
        });

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<StudentSubmittedHomeworkModel.Datum> data;
        Context context;

        public MyAdapter(List<StudentSubmittedHomeworkModel.Datum> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_studentsubmittedhw, parent, false);
            return new MyAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

            holder.name.setText(data.get(position).getUserFullname());
            holder.date.setText(data.get(position).getDate());
            holder.studentscomment.setText(data.get(position).getReplycomment());
            holder.viewfiles2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (data.get(holder.getBindingAdapterPosition()).getAttachments().size() != 0) {
                        List<String> imageList = new ArrayList<>();
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < data.get(holder.getBindingAdapterPosition()).getAttachments().size(); i++) {
                            imageList.add(data.get(holder.getBindingAdapterPosition()).getAttachments().get(i).getPath());
                        }
                        bundle.putSerializable("images", (Serializable) imageList);
                        bundle.putInt("position", holder.getBindingAdapterPosition());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "galleryslideshow");
                    }



                }
            });

            holder.attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.get(holder.getBindingAdapterPosition()).getAttachments().size() != 0) {
                        List<String> imageList = new ArrayList<>();
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < data.get(holder.getBindingAdapterPosition()).getAttachments().size(); i++) {
                            imageList.add(data.get(holder.getBindingAdapterPosition()).getAttachments().get(i).getPath());
                        }
                        bundle.putSerializable("images", (Serializable) imageList);
                        bundle.putInt("position", holder.getBindingAdapterPosition());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "galleryslideshow");
                    }

                }
            });
            holder.status.setText(data.get(holder.getBindingAdapterPosition()).getStatus());
            if (holder.status.getText().toString().equals("Checked")) {
                holder.comments.setVisibility(View.GONE);
                holder.checked.setVisibility(View.VISIBLE);
                holder.unchecked.setVisibility(View.GONE);
                holder.attachment.setVisibility(View.GONE);
                holder.viewfiles2.setVisibility(View.VISIBLE);
                holder.studentscomment.setVisibility(View.VISIBLE);

            } else {
                holder.comments.setVisibility(View.VISIBLE);
                holder.checked.setVisibility(View.GONE);
                holder.unchecked.setVisibility(View.VISIBLE);
                holder.viewfiles2.setVisibility(View.GONE);
                holder.attachment.setVisibility(View.VISIBLE);
                holder.studentscomment.setVisibility(View.GONE);
            }
            holder.comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater li = LayoutInflater.from(StudentSubmittedHomeworkActivity.this);
                    View promptsView = li.inflate(R.layout.custom_adding_comments_for_student, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentSubmittedHomeworkActivity.this);

                    alertDialogBuilder.setView(promptsView);
                    AppCompatImageView emojie = promptsView.findViewById(R.id.emojie);
                    emojiEditText = promptsView.findViewById(R.id.edtcomments_for_new);
                    name_for_alertdialog = promptsView.findViewById(R.id.name_for_user);
                    rootview=promptsView.findViewById(R.id.rootview);
                    final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(rootview).build(emojiEditText);
                    emojie.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(emojiPopup.isShowing()){
                                emojie.setImageResource(R.drawable.ic_emoji);
                                emojiPopup.toggle();


                            }
                            else {
                                emojie.setImageResource(R.drawable.ic_keyboard);
                                emojiPopup.toggle();

                            }
                        }
                    });

                    name_for_alertdialog.setText(data.get(holder.getBindingAdapterPosition()).getUserFullname());

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Call<Addcomments> modelCall = api.getaddcomments(headermap, data.get(holder.getBindingAdapterPosition()).getId().toString(), emojiEditText.getText().toString());
                                    modelCall.enqueue(new Callback<Addcomments>() {
                                        @Override
                                        public void onResponse(Call<Addcomments> call, Response<Addcomments> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(StudentSubmittedHomeworkActivity.this, "Comments Added Successfully", Toast.LENGTH_SHORT).show();
                                                getOnBackPressedDispatcher().onBackPressed();
                                            }
                                            else {
                                                if(response.code()==422){
                                                    try {
                                                        String s = response.errorBody().string();
                                                        JSONObject jsonObject = new JSONObject(s);
                                                        String error = jsonObject.getString("errors");
                                                        JSONObject jsonObject1 = new JSONObject(error);
                                                        Iterator keys = jsonObject1.keys();
                                                        while (keys.hasNext()) {
                                                            Object key = keys.next();
                                                            JSONArray value = jsonObject1.getJSONArray((String) key);
                                                            String errormessage = value.getString(0);
                                                        }
                                                    }
                                                    catch (IOException | JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Addcomments> call, Throwable t) {
                                            AppUtils.APIFails(StudentSubmittedHomeworkActivity.this, t);
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
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, date, status,studentscomment;
            Button comments;
            ImageView attachment;
            ImageView checked, unchecked;
            ImageView viewfiles2;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.submittedstudentname);
                date = itemView.findViewById(R.id.submittedhwdate);
                attachment = itemView.findViewById(R.id.viewfiles);
                comments = itemView.findViewById(R.id.addcomments);
                status = itemView.findViewById(R.id.submittedstatus);
                checked = itemView.findViewById(R.id.checked_true);
                unchecked = itemView.findViewById(R.id.checked_false);
                viewfiles2 = itemView.findViewById(R.id.viewfiles2);
                studentscomment=itemView.findViewById(R.id.studenntreplycomment);
            }
        }
    }

}




