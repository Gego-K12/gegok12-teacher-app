package com.gegosoft.yourappname.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gegosoft.yourappname.Adapter.SpinnerAdapter;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.AddAttendanceAbsentiesmodel;
import com.gegosoft.yourappname.Models.AttendanceAbsentiesModel;
import com.gegosoft.yourappname.Models.AttendanceModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Attendance_Forenoon_Fragment extends Fragment {
    RecyclerView recyclerView;
    List<AttendanceAbsentiesModel.Students> AbsentList;
    List<AttendanceAbsentiesModel.Students> PresentList;
    List<AttendanceModel.Studentlist>attendanceModelList;
    List<AttendanceModel.AbsentReasonlist>absentReasonlists;
    List<AttendanceModel.Studentlist.Attendance>studentsattendancelist;
    ArrayAdapter<String> adapter;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    AlertDialog dialog;
    LinearLayout update;
    SetClass setClass;
    String classteacher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.attendance_forenoon_layout,null);
        recyclerView=view.findViewById(R.id.attendancefnrecyclerview);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        recyclerView.setLayoutManager(manager);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        classteacher = getArguments().getString("classteacher");
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        update=view.findViewById(R.id.updatebtnlayout);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAttendance();
            }
        });
        AbsentList = new ArrayList<>();
        PresentList = new ArrayList<>();
        studentsattendancelist=new ArrayList<>();
        getstudentslist();
        return view;

    }
    private void getstudentslist () {
        Call<AttendanceModel> attendanceModelCall = api.getattendancemodel(headermap);
        attendanceModelCall.enqueue(new Callback<AttendanceModel>() {
            @Override
            public void onResponse(Call<AttendanceModel> call, Response<AttendanceModel> response) {
                if (response.isSuccessful()) {
                    attendanceModelList = response.body().getStudentlist();
                    absentReasonlists = response.body().getAbsentReasonlist();
                    setClass.setclass(userDetailsSharedPref.getString("classteacher"));
                    ForenoonAttendanceAdapter forenoonAttendanceAdapter=new ForenoonAttendanceAdapter(attendanceModelList);
                    recyclerView.setAdapter(forenoonAttendanceAdapter);
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ForenoonAttendanceAdapter extends RecyclerView.Adapter<ForenoonAttendanceAdapter.ForenoonAttendanceViewHolder>{
        List<AttendanceModel.Studentlist> attendanceModel;
        private Integer reasonId;

        public ForenoonAttendanceAdapter(List<AttendanceModel.Studentlist> attendanceModel) {
            this.attendanceModel = attendanceModel;
        }

        @NonNull
        @Override
        public ForenoonAttendanceAdapter.ForenoonAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_forenoon_recyclerview, parent, false);
            return new ForenoonAttendanceAdapter.ForenoonAttendanceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ForenoonAttendanceAdapter.ForenoonAttendanceViewHolder holder, int position) {
            holder.studentname.setText(attendanceModel.get(position).getStudentName());
            holder.studentname.setSelected(true);
            holder.studentid.setText(attendanceModel.get(position).getStudentId().toString());
            Glide.with(getActivity()).load(attendanceModelList.get(position).getAvatar()).into(holder.studentprofilepic);

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final View customLayout = getLayoutInflater().inflate(R.layout.attendance_alertdialogue, null);
                    builder.setView(customLayout);
                    EditText remarks = customLayout.findViewById(R.id.attendancefnremarks);
                    Spinner attendanceleavereason = customLayout.findViewById(R.id.attendancefnspinner);
                    TextView studentname = customLayout.findViewById(R.id.attendancefnstudentname);
                    TextView studentid = customLayout.findViewById(R.id.attendancefnstudentid);
                    Button submit = customLayout.findViewById(R.id.attendancefnsubmit);
                    studentname.setText(attendanceModel.get(holder.getBindingAdapterPosition()).getStudentName());
                    studentid.setText(attendanceModel.get(holder.getBindingAdapterPosition()).getStudentId().toString());

                    ArrayList<String> strinList = new ArrayList<>();
                    for (int  i =0 ; i < absentReasonlists.size();i++){
                        strinList.add(absentReasonlists.get(i).getTitle());
                    }
                    strinList.add(0,"Select Reason");

                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(strinList,getContext());
                    attendanceleavereason.setAdapter(spinnerAdapter);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AttendanceAbsentiesModel.Students students = new AttendanceAbsentiesModel.Students();
                            students.setStandardLinkId(attendanceModelList.get(holder.getBindingAdapterPosition()).getStandardLinkId());
                            students.setStudentId(attendanceModelList.get(holder.getBindingAdapterPosition()).getStudentId());
                            students.setStudentName(attendanceModelList.get(holder.getBindingAdapterPosition()).getStudentName());
                            String sremarks=remarks.getText().toString();

                            students.setRemarks(sremarks);
                            students.setReasonId(reasonId);
                            AbsentList.add(students);
                            holder.attendanceimg.setImageResource(R.drawable.svg_attendance_absent);

                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });
                    studentname.setText(attendanceModel.get(holder.getBindingAdapterPosition()).getStudentName());
                    studentid.setText(attendanceModel.get(holder.getBindingAdapterPosition()).getStudentId().toString());


                    attendanceleavereason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position!=0){
                                reasonId = absentReasonlists.get(position-1).getId();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    dialog = builder.create();
                    dialog.show();
                }

            });



            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            String[] days = new String[6];
            days[0] = sdf.format(date);

            for(int i = 1; i < 6; i++){
                cal.add(Calendar.DAY_OF_MONTH, -1);
                date = cal.getTime();
                days[i] = sdf.format(date);
            }
            studentsattendancelist = attendanceModelList.get(position).getAttendance();

        }

        @Override
        public int getItemCount() {
            return attendanceModel.size();
        }

        public class ForenoonAttendanceViewHolder extends RecyclerView.ViewHolder {
            TextView studentname,studentid;
            ImageView attendanceimg;
            Spinner attendanceleavereason;
            ConstraintLayout parent_layout;
            CircleImageView studentprofilepic;
            public ForenoonAttendanceViewHolder(@NonNull View itemView) {
                super(itemView);
                studentname = itemView.findViewById(R.id.attendancefnstudentname);
                studentid = itemView.findViewById(R.id.attendancefnstudentid);
                attendanceleavereason = itemView.findViewById(R.id.attendancefnspinner);
                attendanceimg = itemView.findViewById(R.id.attendancefnimage);
                parent_layout = itemView.findViewById(R.id.parent_layout);
                studentprofilepic=itemView.findViewById(R.id.attendancefnprofilepic);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
    private void AddAttendance () {

        AttendanceAbsentiesModel attendanceAbsentiesModel = new AttendanceAbsentiesModel();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        attendanceAbsentiesModel.setDate(formattedDate);
        attendanceAbsentiesModel.setSession("ForeNoon");
        attendanceAbsentiesModel.setStandardlinkid(attendanceModelList.get(0).getStandardLinkId());
        attendanceAbsentiesModel.setAbsentList(AbsentList);
        Call<AddAttendanceAbsentiesmodel> absentiesmodelCall = api.getaddattendance(headermap, attendanceAbsentiesModel);
        absentiesmodelCall.enqueue(new Callback<AddAttendanceAbsentiesmodel>() {
            @Override
            public void onResponse(Call<AddAttendanceAbsentiesmodel> call, Response<AddAttendanceAbsentiesmodel> response) {
                if ((response.isSuccessful())) {
                    Toast.makeText(getActivity(), "Attendance Added Successfull", Toast.LENGTH_SHORT).show();
                    getActivity().getOnBackPressedDispatcher().onBackPressed();

                }
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
                            if (key.equals("Session")) {
                                Toast.makeText(getContext(), "Attendance Already Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<AddAttendanceAbsentiesmodel> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface SetClass
    {
        void setclass(String classname);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setClass = (SetClass) context;
    }
}
