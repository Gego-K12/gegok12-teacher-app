package com.gegosoft.yourappname.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Models.AddLeaveModel;
import com.gegosoft.yourappname.Models.EditLeaveModel;
import com.gegosoft.yourappname.Models.LeaveTypeModel;
import com.gegosoft.yourappname.Models.ShowLeaveModel;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.R;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveActivity extends AppCompatActivity {
    Spinner reason,leavetype,session;
    EditText remarks;
    TextView fromdate,todate,toolbarname;
    ImageView back;
    AppCompatButton submit;
    private List<LeaveTypeModel.Leave> leavetypemodelarraylist;
    private List<LeaveTypeModel.Reason> leavereasonemodelarraylist;
    private List<LeaveTypeModel.Session> leavesessionmodelarraylist;
    Map<String,String> headermap;
    DatePickerDialog fromdatePickerDialog,todatePickerDialog;
    Api api;
    String leavereasonid,sessionid;
    int leavetypeid;
    UserDetailsSharedPref userDetailsSharedPref;
    String leaveid,fromeditleave="addleave";
    List<ShowLeaveModel.Data>showLeaveModelList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachersleave_layout);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        fromdate=findViewById(R.id.fromdateedt);
        todate=findViewById(R.id.todateedt);
        remarks=findViewById(R.id.remarksedt);
        reason=findViewById(R.id.reasonspinner);
        session=findViewById(R.id.sessionspinner);
        leavetype=findViewById(R.id.leavetypespinner);
        toolbar=findViewById(R.id.toolbar);
     toolbarname=findViewById(R.id.toolbar_name_leave);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getOnBackPressedDispatcher().onBackPressed();
            }
        });

        submit=findViewById(R.id.submitbtn);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");
        Call<LeaveTypeModel> leaveTypeModelCall = api.getleavetype(headermap);
        Intent intent=getIntent();
        if (intent.getExtras()!=null){
            leaveid=intent.getStringExtra("leaveid");
            fromeditleave=intent.getStringExtra("editleave");

            if(fromeditleave.equals("toleaveedit")) {
                submit.setText("Update");
                toolbarname.setText(" Update Leave");
                showleave();

            }
        }
        leaveTypeModelCall.enqueue(new Callback<LeaveTypeModel>() {
            @Override
            public void onResponse(Call<LeaveTypeModel> call, Response<LeaveTypeModel> response) {
                if (response.isSuccessful()){
                    leavereasonemodelarraylist=new ArrayList<>();
                    leavetypemodelarraylist=new ArrayList<>();
                    leavesessionmodelarraylist=new ArrayList<>();
                    leavereasonemodelarraylist=response.body().getReasonlist();
                    leavetypemodelarraylist = response.body().getLeavelist();
                    leavesessionmodelarraylist=response.body().getSession();
                    leavetypemodelspinner();
                    leavereasonmodelspinner();
                    leavesessionmodelspinner();
                }


            }

            @Override
            public void onFailure(Call<LeaveTypeModel> call, Throwable t) {

            }
        });
        leavetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leavetypeid = leavetypemodelarraylist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leavereasonid = leavereasonemodelarraylist.get(position).getId().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sessionid=leavesessionmodelarraylist.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(fromeditleave.equals("toleaveedit")) {
                    editleave();
                }
                else {
                    Addleave();
                }
            }


        });

        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Calendar calendar=Calendar.getInstance();
                    int year=calendar.get(Calendar.YEAR);
                    int month=calendar.get(Calendar.MONTH);
                    int date=calendar.get(Calendar.DATE);

                    todatePickerDialog=new DatePickerDialog(LeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            todate.setError(null);
                            todate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        }
                    },year,month,date);

                    todatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    todatePickerDialog.show();
                }
            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int date=calendar.get(Calendar.DATE);

                fromdatePickerDialog=new DatePickerDialog(LeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fromdate.setError(null);
                        fromdate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }
                },year,month,date);
                fromdatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromdatePickerDialog.show();
            }
        });

    }



    private void editleave() {
        String editfromdate=fromdate.getText().toString();
        String edittodate=todate.getText().toString();
        String editremarks=remarks.getText().toString();
        Call<EditLeaveModel>editLeaveModelCall=api.geteditleavemodel(headermap,leaveid,editfromdate,edittodate, Integer.parseInt(leavereasonid),editremarks,sessionid,leavetypeid);
        editLeaveModelCall.enqueue(new Callback<EditLeaveModel>() {
            @Override
            public void onResponse(Call<EditLeaveModel> call, Response<EditLeaveModel> response) {
                if(response.isSuccessful()){

                    Toast.makeText(LeaveActivity.this, "Leave Updated successfully", Toast.LENGTH_SHORT).show();

                    backtolist();

                    getOnBackPressedDispatcher().onBackPressed();

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
                            if (key.equals("from_date")) {
                                fromdate.setError(errormessage);
                                fromdate.requestFocus();
                            }
                            if (key.equals("to_date")) {
                                todate.setError(errormessage);
                                todate.requestFocus();
                            }

                            if (key.equals("remarks")) {
                                remarks.setError(errormessage);
                                remarks.requestFocus();
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditLeaveModel> call, Throwable t) {
                Toast.makeText(LeaveActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void backtolist() {
        Intent intent;
        intent = new Intent(this, MyLeaveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void showleave(){
        Call<ShowLeaveModel>showLeaveModelCall=api.getshowleave(headermap,leaveid);
        showLeaveModelCall.enqueue(new Callback<ShowLeaveModel>() {
            @Override
            public void onResponse(Call<ShowLeaveModel> call, Response<ShowLeaveModel> response) {
                if(response.isSuccessful()){

                    ShowLeaveModel.Data showAssignmentModel=response.body().getData();
                    showLeaveModelList=new ArrayList<>();

                    String showfromdate=showAssignmentModel.getFromDate();
                    fromdate.setText(showfromdate);

                    String showtodate=showAssignmentModel.getToDate();
                    todate.setText(showtodate);

                    String showremarks=showAssignmentModel.getRemarks().toString();
                    remarks.setText(showremarks);

                }
                else {
                    Toast.makeText(LeaveActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowLeaveModel> call, Throwable t) {
                Toast.makeText(LeaveActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void leavetypemodelspinner(){

        String[] items = new String[leavetypemodelarraylist.size()];
        for(int i=0; i<leavetypemodelarraylist.size(); i++){
            items[i] = leavetypemodelarraylist.get(i).getName();

        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter(LeaveActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        leavetype.setAdapter(adapter);
    }

    private void leavereasonmodelspinner(){

        String[] strings =new String[leavereasonemodelarraylist.size()];


        for (int i=0;i<leavereasonemodelarraylist.size();i++){
            strings[i]=leavereasonemodelarraylist.get(i).getTitle();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter(LeaveActivity.this, android.R.layout.simple_spinner_dropdown_item, strings);
        reason.setAdapter(adapter);
    }
    private void leavesessionmodelspinner(){
        String[] strings =new String[leavesessionmodelarraylist.size()];


        for (int i=0;i<leavesessionmodelarraylist.size();i++){
            strings[i]=leavesessionmodelarraylist.get(i).getName();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter(LeaveActivity.this, android.R.layout.simple_spinner_dropdown_item, strings);
        session.setAdapter(adapter);
    }
    private void Addleave() {
        if (validate()) {

            Call<AddLeaveModel> call = api.addleavemodel(headermap,fromdate.getText().toString(), todate.getText().toString(), Integer.parseInt(leavereasonid), remarks.getText().toString(),sessionid,
                    leavetypeid);
            call.enqueue(new Callback<AddLeaveModel>() {
                @Override
                public void onResponse(Call<AddLeaveModel> call, Response<AddLeaveModel> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(LeaveActivity.this, "Leave added successfully", Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();

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
                                if (key.equals("from_date")) {
                                    Toast.makeText(LeaveActivity.this, "Leave Request Already Exists For This Date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddLeaveModel> call, Throwable t) {
                    Toast.makeText(LeaveActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validate(){
        if(fromdate.getText().toString().isEmpty()){
            fromdate.requestFocus();
            fromdate.setError("enter the from date");
            return false;

        }
        else if (todate.getText().toString().equals("")){
            todate.setError("enter the from date");
            todate.requestFocus();
            return false;
        }
        else if (remarks.getText().toString().isEmpty()){
            remarks.setError("enter the remarks");
            remarks.requestFocus();
            return false;
        }
        else {

            todate.setError(null);
            remarks.setError(null);
        }
        return true;
    }
}
