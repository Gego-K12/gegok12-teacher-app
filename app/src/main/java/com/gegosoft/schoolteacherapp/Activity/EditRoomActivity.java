package com.gegosoft.schoolteacherapp.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Adapter.SpinnerAdapter;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.EditRoomModel;
import com.gegosoft.schoolteacherapp.Models.RoomListModel;
import com.gegosoft.schoolteacherapp.Models.StandardList;
import com.gegosoft.schoolteacherapp.Models.StudentList;
import com.gegosoft.schoolteacherapp.Models.VideoRoomSubjectModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRoomActivity extends AppCompatActivity {
    EditText edtroomname,edtdescription,edtduration;
    Spinner selectclassspinner,selectsubjectspinner;
    Button btn_submit;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    List<StandardList.Datum> standardlist;
    List<VideoRoomSubjectModel.Datum> subjectlist;
    String standard_id,subject_id,duration;
    List<StudentList.Datum> studentlist;
    String roomname,roomdesc,date;
    ImageView back,scheduledatecalendar;
    TextView scheduledatetxt;
    RoomListModel.Datum datum ;
    EditRoomModel.Data editdata;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        edtroomname= findViewById(R.id.edtroomname);
        edtdescription= findViewById(R.id.edtdescription);
        edtduration= findViewById(R.id.edtduration);
        selectclassspinner= findViewById(R.id.selectclassspinner);
        selectsubjectspinner= findViewById(R.id.selectsubjectspinner);
        scheduledatetxt= findViewById(R.id.scheduledatetxt);
        scheduledatecalendar = findViewById(R.id.scheduledatecalendar);
        btn_submit= findViewById(R.id.btn_submit);
        back= findViewById(R.id.back);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        datum = (RoomListModel.Datum) getIntent().getExtras().getSerializable("room_data");

        getEditData();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    btn_submit.setEnabled(false);
                    EditRoom();

                }
            }
        });
        selectclassspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    standard_id = standardlist.get(position-1).getId().toString();
                    getStudentsList();
                    getSubjectList();
                }
                else {
                    standard_id = "0" ;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectsubjectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    subject_id = subjectlist.get(position-1).getId().toString();

                }
                else {
                    subject_id = "0" ;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        scheduledatecalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(v);
            }
        });

    }

    private void getEditData() {
        Call<EditRoomModel> editRoomModelCall = api.editroomdata(headermap,datum.getId());
        editRoomModelCall.enqueue(new Callback<EditRoomModel>() {
            @Override
            public void onResponse(Call<EditRoomModel> call, Response<EditRoomModel> response) {
                if (response.isSuccessful()){
                    editdata = response.body().getData();
                    edtroomname.setText(editdata.getTitle());
                    edtdescription.setText(editdata.getDescription());
                    edtduration.setText(editdata.getDuration().toString());
                    scheduledatetxt.setText(editdata.getJoiningDate());
                    getClassData();
                }
            }

            @Override
            public void onFailure(Call<EditRoomModel> call, Throwable t) {

            }
        });
    }

    public void openDatePickerDialog(final View v) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int  mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        date = date +" "+hourOfDay + ":" + minute + ":"+"00";
                        scheduledatetxt.setText(date);
                    }
                }, mHour, mMinute, false);



        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        timePickerDialog.show();
                        date = String.valueOf(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void EditRoom() {

        List<Integer> student_ids = new ArrayList<>();

        if (studentlist!=null){
            for (int i =0;i<studentlist.size();i++){
                if (studentlist.get(i).isStudentSelected()){
                    student_ids.add(studentlist.get(i).getId());
                }
            }
        }
        else {
            student_ids = editdata.getStudents();
        }
        Call<ResponseBody> bodyCall = api.editRoom(headermap,roomdesc,standard_id,student_ids,datum.getId(),date,duration,subject_id);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(EditRoomActivity.this,"Success",Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    getOnBackPressedDispatcher().onBackPressed();
                }
                else{
                    btn_submit.setEnabled(true);
                    if (response.code()==422){
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
                                if (key.equals("description")){
                                    edtdescription.setError(errormessage);
                                    edtdescription.requestFocus();
                                }
                                else if (key.equals("students[]")){
                                    Toast.makeText(EditRoomActivity.this,errormessage,Toast.LENGTH_LONG).show();
                                }
                                else if (key.equals("standard")){
                                    Toast.makeText(EditRoomActivity.this,errormessage,Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Toast.makeText(EditRoomActivity.this,"Error",Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppUtils.APIFails(EditRoomActivity.this,t);
                btn_submit.setEnabled(true);

            }
        });
    }

    private boolean validate() {
        roomname = edtroomname.getText().toString();
        roomdesc = edtdescription.getText().toString();
        duration = edtduration.getText().toString();

        if (roomname.isEmpty()){
            edtroomname.setError("Enter Room Name");
            edtroomname.requestFocus();
            return false;
        }
        if (roomdesc.isEmpty()){
            edtdescription.setError("Enter Room Description");
            edtdescription.requestFocus();
            return false;
        }
        if (duration.isEmpty()){
            edtduration.setError("Enter Time Duration");
            edtduration.requestFocus();
            return false;
        }
        return true;
    }

    private void getStudentsList() {
        Call<StudentList> studentListCall = api.getStudentList(headermap,standard_id);
        studentListCall.enqueue(new Callback<StudentList>() {
            @Override
            public void onResponse(Call<StudentList> call, Response<StudentList> response) {
                if (response.isSuccessful()){
                    studentlist =  response.body().getData();
                    if (studentlist.size()!=0){

                        for (int i =0 ; i<studentlist.size();i++){
                            for (int j = 0 ; j<editdata.getStudents().size();j++){
                                if (studentlist.get(i).getId().equals(editdata.getStudents().get(j))){
                                    studentlist.get(i).setStudentSelected(true);
                                    break;
                                }
                            }
                        }
                        showAlertDialogButtonClicked();

                    }
                }
            }

            @Override
            public void onFailure(Call<StudentList> call, Throwable t) {
                AppUtils.APIFails(EditRoomActivity.this,t);
            }
        });
    }

    private void getClassData() {

        Call<StandardList> listCall = api.getStandardList(headermap);
        listCall.enqueue(new Callback<StandardList>() {
            @Override
            public void onResponse(Call<StandardList> call, Response<StandardList> response) {
                if (response.isSuccessful()){
                    standardlist = response.body().getData();
                    ArrayList<String>  objects = new ArrayList<>();
                    for (int i =0 ; i < standardlist.size();i++){
                        objects.add(standardlist.get(i).getStandardSection());
                    }
                    objects.add(0,"Select Class");
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(objects,EditRoomActivity.this);
                    selectclassspinner.setAdapter(spinnerAdapter);

                    for (int i =0; i< standardlist.size();i++){
                        if (standardlist.get(i).getId().equals(editdata.getStandard())){
                            selectclassspinner.setSelection(i+1);
                            spinnerAdapter.notifyDataSetChanged();
                            break;

                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<StandardList> call, Throwable t) {
                AppUtils.APIFails(EditRoomActivity.this,t);

            }
        });

    }

    public void showAlertDialogButtonClicked()
    {

        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);

        final View customLayout = getLayoutInflater().inflate(
                R.layout.student_list,
                null);

        builder.setView(customLayout);
        RecyclerView recyclerview = customLayout.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerview.setHasFixedSize(true);
        StudentAdapter studentAdapter  = new StudentAdapter();
        recyclerview.setAdapter(studentAdapter);

        CheckedTextView none = customLayout.findViewById(R.id.none);
        CheckedTextView select_all = customLayout.findViewById(R.id.select_all);
        TextView done_textview = customLayout.findViewById(R.id.done_textview);
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_all.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                none.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                for (int i = 0;i<studentlist.size();i++){
                    studentlist.get(i).setStudentSelected(false);
                }
                studentAdapter.notifyDataSetChanged();

            }
        });
        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_all.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                none.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                for (int i = 0;i<studentlist.size();i++){
                    studentlist.get(i).setStudentSelected(true);
                }
                studentAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog
                = builder.create();
        done_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_student_list,parent,false);

            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.name.setText(studentlist.get(position).getFullname());
            holder.check_box.setChecked(studentlist.get(position).isStudentSelected());
            holder.check_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.check_box.isChecked()){
                        studentlist.get(position).setStudentSelected(true);
                    }
                    else {
                        studentlist.get(position).setStudentSelected(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return studentlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            CheckBox check_box;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                check_box = itemView.findViewById(R.id.check_box);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
    private void getSubjectList() {
        Call<VideoRoomSubjectModel> subjectModelCall = api.getVideoRoomSubjectlist(headermap,standard_id);
        subjectModelCall.enqueue(new Callback<VideoRoomSubjectModel>() {
            @Override
            public void onResponse(Call<VideoRoomSubjectModel> call, Response<VideoRoomSubjectModel> response) {
                if (response.isSuccessful()){
                    subjectlist = response.body().getData();
                    ArrayList<String>  objects = new ArrayList<>();
                    for (int i =0 ; i < subjectlist.size();i++){
                        objects.add(subjectlist.get(i).getSubjectName());
                    }
                    objects.add(0,"Select Subject");

                    SpinnerAdapter spinnerAdapter=new SpinnerAdapter(objects,EditRoomActivity.this);
                    selectsubjectspinner.setAdapter(spinnerAdapter);
                    for (int i =0; i< subjectlist.size();i++){
                        if (subjectlist.get(i).getSubjectName().equalsIgnoreCase(datum.getSubject())){
                            selectsubjectspinner.setSelection(i+1);
                            spinnerAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<VideoRoomSubjectModel> call, Throwable t) {

            }
        });
    }

}
