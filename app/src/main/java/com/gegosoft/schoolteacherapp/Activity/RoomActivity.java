package com.gegosoft.schoolteacherapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Helper.CheckNetwork;
import com.gegosoft.schoolteacherapp.Helper.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Helper.PurchaseHelper;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.ApiSuccessModel;
import com.gegosoft.schoolteacherapp.Models.JoinRoomModel;
import com.gegosoft.schoolteacherapp.Models.RoomListModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity {

    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    ImageView back;
    int conferenceid;

    Api api;
    RecyclerView roomlist;
    TextView textnodata;
    ImageView btn_create_room;

    boolean isCreateRoom = false;
    boolean isEditRoom = false;
    boolean isVideoRoom = false;

    ConstraintLayout roomContainer;
    FrameLayout purchaseContainer;

    private final ActivityResultLauncher<Intent> roomLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (isCreateRoom){
                        getRoomlist();
                        isCreateRoom = false;
                    }
                    if (isEditRoom){
                        getRoomlist();
                        isEditRoom = false;
                    }
                    if (isVideoRoom){
                        getRoomlist();
                        isVideoRoom = false;
                    }
                }
            });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomactivity);
        roomlist = findViewById(R.id.recyclerview);
        api = ApiClient.getClient().create(Api.class);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        roomlist.setLayoutManager(linearLayoutManager);
        roomlist.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        textnodata=findViewById(R.id.textnodata);
        btn_create_room = findViewById(R.id.btn_create_room);
        back= findViewById(R.id.back);
        roomContainer = findViewById(R.id.room_container);
        purchaseContainer = findViewById(R.id.purchaseContainer);
        if (CheckNetwork.isInternetAvailable(this)){
            getRoomlist();
        }
        btn_create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCreateRoom = true;
                roomLauncher.launch(new Intent(RoomActivity.this,CreateRoomActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void getRoomlist()
    {
        Call<RoomListModel> listModelCall = api.getListofRooms(headermap);
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message","Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(),"loading screen");
        listModelCall.enqueue(new Callback<RoomListModel>() {
            @Override
            public void onResponse(Call<RoomListModel> call, Response<RoomListModel> response) {
                if (response.isSuccessful()){
                    purchaseContainer.setVisibility(View.GONE);
                    roomContainer.setVisibility(View.VISIBLE);
                    List<RoomListModel.Datum> data = response.body().getData();
                    if (data.size()!=0){
                        RoomAdapter roomAdapter = new RoomAdapter(data);
                        roomlist.setAdapter(roomAdapter);
                    }
                    else {
                        roomlist.setVisibility(View.GONE);
                        textnodata.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    if (response.code()==401){
                        AppUtils.SessionExpired(RoomActivity.this);
                    }
                    if(response.code() == 404){
                        runOnUiThread(() -> {
                            showPurchaseCard();
                        });
                    }
                }
                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<RoomListModel> call, Throwable t) {
                AppUtils.APIFails(RoomActivity.this,t);
                progressDialogFragment.dismiss();
            }
        });

    }

    private void showPurchaseCard() {
        purchaseContainer.setVisibility(View.VISIBLE);
        roomContainer.setVisibility(View.GONE);
        purchaseContainer.removeAllViews();
        View card = PurchaseHelper.createPurchaseCard(this, purchaseContainer, "Online class", v -> {
            Toast.makeText(this, "Redirect to purchase flow", Toast.LENGTH_SHORT).show();
        });
        card.setAlpha(0f);
        purchaseContainer.addView(card);
        card.animate().alpha(1f).setDuration(300).start();
    }

    private void getAccessToken(String roomname, String slug, Integer roomId)
    {
        Call<JoinRoomModel> joinRoomModelCall = api.getTokentoJoinRoom(headermap,slug,userDetailsSharedPref.getString("userId"));
        joinRoomModelCall.enqueue(new Callback<JoinRoomModel>() {
            @Override
            public void onResponse(Call<JoinRoomModel> call, Response<JoinRoomModel> response) {
                if (response.isSuccessful()){
                    String accessToken =  response.body().getData().getAccessToken();
                 if (accessToken!=null&&!accessToken.isEmpty()) {
                     isVideoRoom = true;
                     roomLauncher.launch(new Intent(RoomActivity.this, VideoActivity.class).putExtra("roomName",roomname)
                             .putExtra("accessToken",accessToken).putExtra("video_conference_id",conferenceid));
                 }
                }
            }

            @Override
            public void onFailure(Call<JoinRoomModel> call, Throwable t) {
                Log.d("error",t.getLocalizedMessage());

            }
        });



    }

    public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder>{
        List<RoomListModel.Datum> data;
        public RoomAdapter(List<RoomListModel.Datum> data) {
            this.data= data;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customroomlist,parent,false);

            return new MyViewHolder(view);

        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.roomname.setText(data.get(position).getTitle());
            holder.subject.setText(data.get(position).getSubject());
            holder.roomdescription.setText(data.get(position).getDescription());
            holder.date.setText(data.get(position).getJoining_date());
            holder.classandstd_name.setText(data.get(position).getStandard_name());
            holder.status.setText(data.get(position).getStatus());

            if (!data.get(position).getStart_time().isEmpty()){
                holder.timing.setText(data.get(position).getStart_time() + " to "+data.get(position).getEnd_time());
            }

            if (data.get(position).getAvatar() == null)
            {
                holder.circleImageView.setImageResource(R.drawable.profile_demopic);
            }
            else
                {
                Glide.with(RoomActivity.this).load(data.get(position).getAvatar()).into(holder.circleImageView);
            }

            if (data.get(position).getCreated_by().equals(1) ){
                holder.btn_delete.setVisibility(View.VISIBLE);
                holder.manage_invites.setVisibility(View.VISIBLE);
                holder.btn_edit.setVisibility(View.VISIBLE);

                holder.txt_created_by.setText("Created by: you");
            }
            else {
                holder.txt_created_by.setText("Created by: Admin");
                holder.btn_delete.setVisibility(View.GONE);
                holder.classdummytextview.setVisibility(View.GONE);
                holder.subjectdummytextview.setVisibility(View.GONE);
                holder.manage_invites.setVisibility(View.GONE);
                holder.btn_edit.setVisibility(View.GONE);
            }

            if (holder.status.getText().toString().equalsIgnoreCase("Scheduled"))
            {
                holder.manage_invites.setVisibility(View.VISIBLE);
                holder.joinroombtn.setVisibility(View.VISIBLE);
            }
            if (holder.status.getText().toString().equalsIgnoreCase("Class Over")){
                holder.manage_invites.setVisibility(View.GONE);
                holder.joinroombtn.setVisibility(View.GONE);
                holder.btn_edit.setVisibility(View.GONE);
                holder.btn_delete.setVisibility(View.GONE);
            }


            holder.joinroombtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conferenceid= Integer.parseInt(data.get(holder.getBindingAdapterPosition()).getId().toString());

                    getAccessToken(data.get(holder.getBindingAdapterPosition()).getTitle(),data.get(holder.getBindingAdapterPosition()).getSlug(),data.get(holder.getBindingAdapterPosition()).getId());
                }
            });



            holder.manage_invites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(RoomActivity.this,ManageInvitesActivity.class).putExtra("video_conference_id",data.get(holder.getBindingAdapterPosition()).getId()));

                }
            });
            holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isEditRoom = true;
                    roomLauncher.launch(new Intent(RoomActivity.this,EditRoomActivity.class)
                            .putExtra("room_data", (Serializable) data.get(holder.getBindingAdapterPosition())));

                }
            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);

                    builder.setMessage("Do you want to delete "+data.get(holder.getBindingAdapterPosition()).getTitle()+" room ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteRoom(data.get(holder.getBindingAdapterPosition()).getId());
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
            TextView roomname,status,roomdescription,txt_created_by,date,timing,subject,classandstd_name;
            Button joinroombtn,manage_invites;
            ImageView btn_edit,btn_delete;
            CircleImageView circleImageView;
            TextView subjectdummytextview, classdummytextview;
            Button record;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_created_by = itemView.findViewById(R.id.txt_created_by);
                btn_edit = itemView.findViewById(R.id.btn_edit);
                btn_delete = itemView.findViewById(R.id.btn_delete);
                date = itemView.findViewById(R.id.date);
                timing = itemView.findViewById(R.id.timing);
                manage_invites = itemView.findViewById(R.id.manage_invites);
                roomname = itemView.findViewById(R.id.roomname);
                status = itemView.findViewById(R.id.status);
                roomdescription = itemView.findViewById(R.id.roomdescription);
                joinroombtn = itemView.findViewById(R.id.joinroombtn);
                circleImageView = itemView.findViewById(R.id.avathar);
                subject = itemView.findViewById(R.id.subject_new_api);
                classandstd_name = itemView.findViewById(R.id.class_id_room);
                subjectdummytextview = itemView.findViewById(R.id.subject_new);
                classdummytextview = itemView.findViewById(R.id.class_dumytv);
                record = itemView.findViewById(R.id.recordcheckbtn);
            }
        }
    }

    private void deleteRoom(Integer id) {
        Call<ApiSuccessModel> modelCall = api.deletevideoRoom(headermap,id);
        modelCall.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if (response.isSuccessful()){
                    Toast.makeText(RoomActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    getRoomlist();
                }
            }

            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {

            }
        });

    }

}
