package com.gegosoft.yourappname.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.gegosoft.yourappname.Adapter.SpinnerAdapter;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Helper.CheckNetwork;
import com.gegosoft.yourappname.Helper.NotificationWorker;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.Models.Reminders;
import com.gegosoft.yourappname.Models.StudentListModel;
import com.gegosoft.yourappname.Models.TeacherListModel;
import com.gegosoft.yourappname.Models.ToDolistAdd_SpinnerModel;
import com.gegosoft.yourappname.R;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoList_AddTask_Activity extends AppCompatActivity {

    Spinner assignto,studentclass,reminder;
    UserDetailsSharedPref userDetailsSharedPref;
    Api api;
    Map<String,String> headermap;
    ImageView back;
    String date,assign,sstudentclass,sreminder;
    TextView txt_date,submit;
    LinearLayout buttonLL;
    List<ToDolistAdd_SpinnerModel.TaskAssignee>assigneeList;
    List<ToDolistAdd_SpinnerModel.Standardlink>standardlinks;
    List<ToDolistAdd_SpinnerModel.TaskReminder>reminderList;
    ToDolistAdd_SpinnerModel datalist;
    String standard_id;
    List<StudentListModel.Datum>studentListModels;
    AutoCompleteTextView teachers;
    List<TeacherListModel.Datum>teacherList;
    LinearLayout teacherlayout,classlayout,datelayout;
    EditText title,desc;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_addtask_layout);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        teacherList = new ArrayList<>();
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        back=findViewById(R.id.backbtn);
        datelayout=findViewById(R.id.datelayout);
        txt_date=findViewById(R.id.datetxt);
        assignto=findViewById(R.id.assignedtospinner);
        teachers=findViewById(R.id.teacherspinner);
        studentclass=findViewById(R.id.studentclassspinner);
        reminder=findViewById(R.id.reminderspinner);
        submit=findViewById(R.id.submitbutton);
        buttonLL = findViewById(R.id.buttonLinearLayout);
        teacherlayout=findViewById(R.id.teacherlayout);
        classlayout=findViewById(R.id.classlayout);
        title=findViewById(R.id.title);
        desc=findViewById(R.id.description);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addtask();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        datelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openToDatePickerDialog();
            }
        });
        assignto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                assign=assigneeList.get(position).getName();
                if(assign.equals("Class"))
                {
                    teacherlayout.setVisibility(View.GONE);
                    classlayout.setVisibility(View.VISIBLE);
                }
                else if(assign.equals("Self"))
                {
                    teacherlayout.setVisibility(View.GONE);
                    classlayout.setVisibility(View.GONE);
                }
                else if(assign.equals("Student")){
                    teacherlayout.setVisibility(View.GONE);
                    classlayout.setVisibility(View.VISIBLE);
                }
                else if(assign.equals("Teachers")){
                    teacherlayout.setVisibility(View.VISIBLE);
                    classlayout.setVisibility(View.GONE);
                    getteacherlist();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        studentclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    standard_id=standardlinks.get(position-1).getId().toString();
                    sstudentclass=standardlinks.get(position-1).getStandardSection();
                    getstudentlist();
                }
                else {
                    standard_id = "0" ;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        reminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sreminder=reminderList.get(position).getId();
                validateReminder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(CheckNetwork.isInternetAvailable(ToDoList_AddTask_Activity.this)){
            getdata();
        }
        teachers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getteacherlist();
            }
        });

        teachers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
    private void Addtask(){

        List<Integer> student_ids = new ArrayList<>();
        if (studentListModels != null) {

            for (int i = 0; i < studentListModels.size(); i++) {
                if (studentListModels.get(i).isStudentSelected()) {
                    student_ids.add(studentListModels.get(i).getId());
                }
            }
        }
        List<Integer> teacher_ids = new ArrayList<>();
        if (teacherList!=null){
            for (int i =0;i<teacherList.size();i++) {
                teacher_ids.add(teacherList.get(i).getId());

            }
        }

        Call<ResponseBody> bodyCall = api.addtask(headermap,assign,standard_id,student_ids,teacher_ids,title.getText().toString(),desc.getText().toString(),date,sreminder);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ToDoList_AddTask_Activity.this,"Success",Toast.LENGTH_LONG).show();
                    Reminders reminders = new Reminders();
                    reminders.setMessage(title.getText().toString());
                    String date = txt_date.getText().toString().trim();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date remind = null;
                    try {
                        remind = format.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    reminders.setRemindDate(remind);

                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    calendar.setTime(remind);
                    calendar.set(Calendar.SECOND,0);

                    Calendar reminderCalendar = Calendar.getInstance();
                    reminderCalendar.setTime(remind);
                    switch (sreminder) {
                        case "one_hour_before_the_task":
                            reminderCalendar.add(Calendar.HOUR, -1);
                            break;
                        case "one_day_before_the_task":
                            reminderCalendar.add(Calendar.DAY_OF_YEAR, -1);
                            break;
                        case "two_days_before_the_task":
                            reminderCalendar.add(Calendar.DAY_OF_YEAR, -2);
                            break;
                    }

                    Date reminderTime = reminderCalendar.getTime();
                    reminders.setRemindDate(reminderTime);

                    scheduleNotification(reminders);
                    
                    setResult(RESULT_OK);
                    getOnBackPressedDispatcher().onBackPressed();
                }
                else {
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
                                Toast.makeText(ToDoList_AddTask_Activity.this,errormessage,Toast.LENGTH_LONG).show();
                            }


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Toast.makeText(ToDoList_AddTask_Activity.this,"Error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ToDoList_AddTask_Activity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void scheduleNotification(Reminders reminders) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reminders.getRemindDate());

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

        Data data = new Data.Builder()
                .putString(NotificationWorker.TITLE_KEY, "Reminder")
                .putString(NotificationWorker.MESSAGE_KEY, reminders.getMessage())
                .build();

        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(notificationWork);
    }

    private void validateReminder() {
        String dateStr = txt_date.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date dueDate = null;

        try {
            dueDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dueDate == null) return;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.setTime(dueDate);

        switch (sreminder) {
            case "one_hour_before_the_task":
                reminderCalendar.add(Calendar.HOUR, -1);
                break;
            case "one_day_before_the_task":
                reminderCalendar.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case "two_days_before_the_task":
                reminderCalendar.add(Calendar.DAY_OF_YEAR, -2);
                break;
            default:
                return;
        }

        Date reminderTime = reminderCalendar.getTime();

        if (reminderTime.before(new Date())) {
            Toast.makeText(this, "Reminder time cannot be before the current time", Toast.LENGTH_LONG).show();
            submit.setVisibility(View.GONE);
            buttonLL.setVisibility(View.GONE);
        }else {
            submit.setVisibility(View.VISIBLE);
            buttonLL.setVisibility(View.VISIBLE);
        }
    }

    private void getteacherlist(){
        Call<TeacherListModel>modelCall=api.getteacherlist(headermap);
        modelCall.enqueue(new Callback<TeacherListModel>() {
            @Override
            public void onResponse(Call<TeacherListModel> call, Response<TeacherListModel> response) {
                if(response.isSuccessful()) {
                    teacherList=response.body().getData();
                    if (teacherList!=null&&teacherList.size()!=0){
                        List<String> list = new ArrayList<>();
                        for (TeacherListModel.Datum datum : teacherList){
                            list.add(datum.getFullname());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (ToDoList_AddTask_Activity.this, android.R.layout.select_dialog_item, list);
                        teachers.setThreshold(1);
                        teachers.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<TeacherListModel> call, Throwable t) {

            }
        });
    }
    private void getstudentlist(){
        Call<StudentListModel>modelCall=api.getstudentslist(headermap,standard_id);
        modelCall.enqueue(new Callback<StudentListModel>() {
            @Override
            public void onResponse(Call<StudentListModel> call, Response<StudentListModel> response) {
                if(response.isSuccessful()){

                    studentListModels=response.body().getData();
                    if (studentListModels.size()!=0){
                        showAlertDialogButtonClicked();

                    }
                }
            }

            @Override
            public void onFailure(Call<StudentListModel> call, Throwable t) {

            }
        });

    }

    private void showAlertDialogButtonClicked()
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
                for (int i = 0;i<studentListModels.size();i++){
                    studentListModels.get(i).setStudentSelected(false);
                }
                studentAdapter.notifyDataSetChanged();

            }
        });
        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_all.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                none.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                for (int i = 0;i<studentListModels.size();i++){
                    studentListModels.get(i).setStudentSelected(true);
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


    private void getdata(){
        Call<ToDolistAdd_SpinnerModel>modelCall=api.gettodospinners(headermap);
        modelCall.enqueue(new Callback<ToDolistAdd_SpinnerModel>() {
            @Override
            public void onResponse(Call<ToDolistAdd_SpinnerModel> call, Response<ToDolistAdd_SpinnerModel> response) {
                if (response.isSuccessful()) {
                    assigneeList=new ArrayList<>();
                    reminderList=new ArrayList<>();
                    standardlinks=new ArrayList<>();
                    datalist=response.body();
                    assigneeList=response.body().getTaskAssigneeList();
                    reminderList=response.body().getTaskReminderList();
                    standardlinks=response.body().getStandardlinks();

                    getassignto();
                    getreminder();
                    getclass();
                }

            }

            @Override
            public void onFailure(Call<ToDolistAdd_SpinnerModel> call, Throwable t) {

            }
        });
    }
    private void getassignto() {
        String[] items = new String[assigneeList.size()];
        for (int i = 0; i < assigneeList.size(); i++) {
            items[i] = assigneeList.get(i).getName();
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter(ToDoList_AddTask_Activity.this, android.R.layout.simple_list_item_1, items);
            assignto.setAdapter(adapter);
        }
    }

    private void getclass() {
        ArrayList<String>  objects = new ArrayList<>();
        for (int i =0 ; i < standardlinks.size();i++){
            objects.add(standardlinks.get(i).getStandardSection());
        }
        objects.add(0,"Select Class");
        SpinnerAdapter spinnerAdapter=new SpinnerAdapter(objects,ToDoList_AddTask_Activity.this);
        studentclass.setAdapter(spinnerAdapter);
    }

    private void getreminder(){
        String[] items = new String[reminderList.size()];
        for (int i = 0; i < reminderList.size(); i++) {
            items[i] = reminderList.get(i).getName();
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter(ToDoList_AddTask_Activity.this, android.R.layout.simple_spinner_dropdown_item, items);
            reminder.setAdapter(adapter);
        }}
    public void openToDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ToDoList_AddTask_Activity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        date = date + " " + String.format("%02d:%02d:00", hourOfDay, minute);
                        txt_date.setText(date);
                        validateReminder();

                    }
                }, mHour, mMinute, false);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ToDoList_AddTask_Activity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        timePickerDialog.show();
                        date = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{
        List<StudentListModel.Datum>datumList;

        @NonNull
        @Override
        public StudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_student_list,parent,false);

            return new StudentAdapter.MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull StudentAdapter.MyViewHolder holder, int position) {
            holder.name.setText(studentListModels.get(position).getName());
            holder.check_box.setChecked(studentListModels.get(position).isStudentSelected());
            holder.check_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.check_box.isChecked()){
                        studentListModels.get(holder.getBindingAdapterPosition()).setStudentSelected(true);
                    }
                    else {
                        studentListModels.get(holder.getBindingAdapterPosition()).setStudentSelected(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return studentListModels.size();
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
}