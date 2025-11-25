package com.gegosoft.yourappname.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.InvitesModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageInvitesActivity  extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_INVITES = 1;
    RecyclerView recyclerView;
    UserDetailsSharedPref userDetailsSharedPref;
    Api api;
    Map<String,String> headermap;
    int video_conference_id;
    ImageView back;
    TextView textnodata;
    List<InvitesModel.Datum> data;
    InvitelistAdapter invitelistAdapter;
    Button add_invites;
    private final ActivityResultLauncher<Intent> addInvitesLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    textnodata.setVisibility(View.GONE);
                    data.clear();
                    invitelistAdapter.notifyDataSetChanged();
                    getInvites();
                }
            });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invites);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        data = new ArrayList<>();
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        video_conference_id = getIntent().getExtras().getInt("video_conference_id");
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        textnodata = findViewById(R.id.textnodata);
        add_invites = findViewById(R.id.add_invites);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        add_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageInvitesActivity.this, AddInvitesActivity.class);
                intent.putExtra("video_conference_id", video_conference_id);
                intent.putExtra("existing_students", (Serializable) data);
                addInvitesLauncher.launch(intent);
            }
        });
        invitelistAdapter = new InvitelistAdapter();
        recyclerView.setAdapter(invitelistAdapter);
        getInvites();

    }

    private void getInvites() {
        Call<InvitesModel> modelCall = api.getInvites(headermap,video_conference_id);
        modelCall.enqueue(new Callback<InvitesModel>() {
            @Override
            public void onResponse(Call<InvitesModel> call, Response<InvitesModel> response) {
                if (response.isSuccessful()){
                    data = response.body().getData();


                    if (data.size()!=0){
                        Collections.reverse(data);
                        invitelistAdapter.notifyDataSetChanged();
                    }
                    else {
                        textnodata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<InvitesModel> call, Throwable t) {

            }
        });
    }

    public class InvitelistAdapter extends RecyclerView.Adapter<InvitelistAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_invites,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.user_name.setText(data.get(position).getUserFullname());
            holder.roll_no.setText("Roll No : "+data.get(position).getRollNumber());
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManageInvitesActivity.this);


                    builder.setMessage("Do you want to remove "+data.get(holder.getBindingAdapterPosition()).getUserFullname()+" from this room ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteUser(data.get(holder.getBindingAdapterPosition()));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();

                    alert.show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView user_name,roll_no;
            ImageButton delete_btn;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                user_name = itemView.findViewById(R.id.user_name);
                roll_no = itemView.findViewById(R.id.roll_no);
                delete_btn = itemView.findViewById(R.id.delete_btn);
            }
        }
    }

    private void deleteUser(InvitesModel.Datum datum) {
        Call<ResponseBody> bodyCall =  api.deleteUserfromVideoConference(headermap,datum.getVidoeConferenceUserId());
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    data.remove(datum);
                    invitelistAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    
}
