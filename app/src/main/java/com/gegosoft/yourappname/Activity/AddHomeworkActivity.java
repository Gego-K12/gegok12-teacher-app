package com.gegosoft.yourappname.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gegosoft.yourappname.Helper.AppUtils;
import com.gegosoft.yourappname.Helper.FileUtils;
import com.gegosoft.yourappname.Models.AddAssignmentModel;
import com.gegosoft.yourappname.Models.AddHomeworklistModel;
import com.gegosoft.yourappname.Models.ShowHomeworklistModel;
import com.gegosoft.yourappname.Models.StandardLinkListModel;
import com.gegosoft.yourappname.Models.SubjectListModel;
import com.gegosoft.yourappname.Storage.UserDetailsSharedPref;
import com.gegosoft.yourappname.Helper.ApiClient;
import com.gegosoft.yourappname.Interface.Api;
import com.gegosoft.yourappname.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHomeworkActivity extends AppCompatActivity {

    ImageView assigneddatecalendar, submissiondatecalendar, choosefilebutton, back;
    TextView assigneddatetext, submissiondatetext, choosefiletxt;
    TextView submit, filename;
    Spinner standardspinner, subjectspinner;
    EditText  enterdescription;
    DatePickerDialog assigneddatePickerDialog;
    UserDetailsSharedPref userDetailsSharedPref;
    Api api;
    Map<String, String> headermap;
    List<StandardLinkListModel.Datum> standardlist;
    List<SubjectListModel.Datum> subjectlist;
    int subjectid, standardid;
    RequestBody rbdescription, rbsubjectid, rbstandardid, rbassigneddate, rbsubmissiondate;
    MultipartBody.Part requestFile = null;
    DatePickerDialog submissiondatePickerDialog;
    String homeworkid, fromedithomework = "edithomework";
    List<ShowHomeworklistModel> showHomeworklistModels;
    String edit_subid, edit_class;
    TextView toolbartextview;

    ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new
            ActivityResultContracts.RequestMultiplePermissions(), result -> {
        boolean allGranted = result.containsValue(Boolean.TRUE);
        if (allGranted) {

        } else {
            Toast.makeText(this, "Permission denied. Please allow access to proceed.", Toast.LENGTH_SHORT).show();
            showDialogForPdfPicker();
        }
    });

    private void showDialogForPdfPicker() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("We need Storage permission to proceed, Please permit the permission through Settings screen. -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                settingsLauncher.launch(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(AddHomeworkActivity.this, "Need Storage permission to proceed...", Toast.LENGTH_SHORT).show();
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        builder.show();
    }

    private final ActivityResultLauncher<Intent> manageAllFilesAccessLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {

                }
            });

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        uploadFile(uri);
                    }
                }
            });
    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_home_work);
        submit = findViewById(R.id.addassignmentsubmitbtn);
        toolbartextview = findViewById(R.id.toolbartextview);

        choosefilebutton = findViewById(R.id.Choosefilebtn);
        userDetailsSharedPref = userDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        standardspinner = findViewById(R.id.selectclassspinner);
        subjectspinner = findViewById(R.id.selecthwsubjectspinner);
        enterdescription = findViewById(R.id.enterdescriptionedt);
        back = findViewById(R.id.addassignmentback);
        filename = findViewById(R.id.filename);
        toolbartextview.setText("Add Homework");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
                finish();
            }
        });
        choosefiletxt = findViewById(R.id.choosefiletext);
        choosefiletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissionExternalStorage()) {
                    requestPermissionForExternalStorage();
                } else {
                    openFile();
                }
            }
        });

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            homeworkid = intent.getStringExtra("homeworkid");
            fromedithomework = intent.getStringExtra("edithomework");

            if (fromedithomework.equals("toedit")) {
                toolbartextview.setText("Update Homework");
                submit.setText("Update");
                showhomework();

            }
        }

        subjectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectid = subjectlist.get(position).getSubjectId();
                rbsubjectid = RequestBody.create(String.valueOf(subjectid),MultipartBody.FORM);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        standardspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                standardid = standardlist.get(position).getStandardLinkId();
                rbstandardid = RequestBody.create(String.valueOf(standardid), MultipartBody.FORM);

                Call<SubjectListModel> subjectListModelCall = api.subjectlist(headermap, String.valueOf(standardid));
                subjectListModelCall.enqueue(new Callback<SubjectListModel>() {
                    @Override
                    public void onResponse(Call<SubjectListModel> call, Response<SubjectListModel> response) {
                        subjectlist = new ArrayList<>();
                        subjectlist = response.body().getSubjectdata();
                        subjectspinner();

                    }

                    @Override
                    public void onFailure(Call<SubjectListModel> call, Throwable t) {
                        AppUtils.APIFails(AddHomeworkActivity.this, t);
                    }

                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        choosefilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissionExternalStorage()) {
                    requestPermissionForExternalStorage();
                } else {
                    openFile();
                }
            }
        });

        Call<StandardLinkListModel> standardLinkListModelCall = api.standardlinklist(headermap);
        standardLinkListModelCall.enqueue(new Callback<StandardLinkListModel>() {
            @Override
            public void onResponse(Call<StandardLinkListModel> call, Response<StandardLinkListModel> response) {
                standardlist = new ArrayList<>();
                standardlist = response.body().getStandarddata();
                standardspinner();
            }

            @Override
            public void onFailure(Call<StandardLinkListModel> call, Throwable t) {

            }
        });
         submissiondatetext=findViewById(R.id.submissiondatetxt);
        assigneddatecalendar = findViewById(R.id.assigneddatecalendar);
        submissiondatecalendar=findViewById(R.id.submissiondatecalendar);
        submissiondatecalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);

                submissiondatePickerDialog = new DatePickerDialog(AddHomeworkActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        submissiondatetext.setError(null);
                        submissiondatetext.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                        rbsubmissiondate = RequestBody.create(submissiondatetext.getText().toString(),MultipartBody.FORM);
                    }
                }, year, month, date);
                submissiondatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                submissiondatePickerDialog.show();
            }
        });
        assigneddatetext = findViewById(R.id.assigneddatetxt);
        assigneddatecalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);

                assigneddatePickerDialog = new DatePickerDialog(AddHomeworkActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        assigneddatetext.setError(null);
                        assigneddatetext.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                        rbassigneddate = RequestBody.create(assigneddatetext.getText().toString(),MultipartBody.FORM);
                    }
                }, year, month, date);
                assigneddatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                assigneddatePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromedithomework.equals("toedit")) {
                    edithomework();
                } else {

                        AddHomework();

                    }

            }
        });
        if (!checkPermissionExternalStorage()) {
            requestPermissionForExternalStorage();
        }
    }

    private void edithomework() {

        edit_class = rbstandardid.toString();
        edit_subid = rbsubjectid.toString();
        rbdescription = RequestBody.create(enterdescription.getText().toString(), MultipartBody.FORM);
        rbassigneddate =RequestBody.create(assigneddatetext.getText().toString(), MultipartBody.FORM);

        Call<AddHomeworklistModel> edithomework = api.getedit(headermap, rbstandardid, rbsubjectid, rbdescription, rbassigneddate, homeworkid, requestFile);
        
        edithomework.enqueue(new Callback<AddHomeworklistModel>() {
            @Override
            public void onResponse(Call<AddHomeworklistModel> call, Response<AddHomeworklistModel> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(AddHomeworkActivity.this, "Homework Updated Successfully...!", Toast.LENGTH_SHORT).show();
                    getOnBackPressedDispatcher().onBackPressed();
                } 
                else 
                    {
                        submit.setEnabled(true);
                        if (response.code() == 422) {
                            try {
                                String s = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(s);
                                JSONObject errors = jsonObject.getJSONObject("errors");
                                Iterator keys = errors.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    JSONArray value = errors.getJSONArray((String) key);
                                    String errormessage = value.getString(0);
                                    if (key.equals("date")) {
                                        assigneddatetext.setError(errormessage);
                                        assigneddatetext.requestFocus();
                                    } else if (key.equals("submission_date")) {
                                        submissiondatetext.setError(errormessage);
                                        submissiondatetext.requestFocus();
                                    }
                                    Toast.makeText(AddHomeworkActivity.this, errormessage, Toast.LENGTH_SHORT).show();

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }

                }
            }

            @Override
            public void onFailure(Call<AddHomeworklistModel> call, Throwable t) {
                Toast.makeText(AddHomeworkActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    
    private void showhomework() {
        Call<ShowHomeworklistModel> homeworklistModelCall = api.getshowhomework(headermap, homeworkid);
        homeworklistModelCall.enqueue(new Callback<ShowHomeworklistModel>() {
            @Override
            public void onResponse(Call<ShowHomeworklistModel> call, Response<ShowHomeworklistModel> response) {
                if (response.isSuccessful()) {
                    ShowHomeworklistModel data = response.body();
                    showHomeworklistModels = new ArrayList<>();

                    String standard = String.valueOf(data.getStandardLinkId());


                    String showdescription = data.getDescription();
                    enterdescription.setText(showdescription);

                    String showassigneddate = data.getDate();
                    assigneddatetext.setText(showassigneddate);

                }
            }

            @Override
            public void onFailure(Call<ShowHomeworklistModel> call, Throwable t) {
                Toast.makeText(AddHomeworkActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void AddHomework() {
        if (validate()) {
            rbdescription = RequestBody.create(enterdescription.getText().toString(), MultipartBody.FORM);
            Call<AddAssignmentModel> addAssignmentModelCall = api.gethomework(headermap, rbstandardid, rbsubjectid, rbdescription, rbassigneddate, rbsubmissiondate,requestFile);

            addAssignmentModelCall.enqueue(new Callback<AddAssignmentModel>() {
                @Override
                public void onResponse(Call<AddAssignmentModel> call, Response<AddAssignmentModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddHomeworkActivity.this, "HomeWork added successfully", Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        submit.setEnabled(true);
                        if (response.code() == 422) {
                            try {
                                String s = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(s);
                                JSONObject errors = jsonObject.getJSONObject("errors");
                                Iterator keys = errors.keys();
                                while (keys.hasNext()) {
                                    Object key = keys.next();
                                    JSONArray value = errors.getJSONArray((String) key);
                                    String errormessage = value.getString(0);
                                    if (key.equals("date")) {
                                        assigneddatetext.setError(errormessage);
                                        assigneddatetext.requestFocus();
                                    } else if (key.equals("submission_date")) {
                                        submissiondatetext.setError(errormessage);
                                        submissiondatetext.requestFocus();
                                    }
                                    Toast.makeText(AddHomeworkActivity.this, errormessage, Toast.LENGTH_SHORT).show();

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<AddAssignmentModel> call, Throwable t) {
                     Toast.makeText(AddHomeworkActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    private void openFile() {
        String[] mimeTypes = {"image/*", "application/pdf"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        filePickerLauncher.launch(intent);
    }

    private void uploadFile(Uri fileUri) {
        try {
            String path = FileUtils.getPath(this, fileUri);

            if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".jpeg")) {
                File file = new File(path);
                RequestBody reqFile = RequestBody.create(file, MediaType.parse("application/pdf"));
                requestFile = MultipartBody.Part.createFormData("attachment", file.getName(), reqFile);
                filename.setText(file.getName());
                filename.setVisibility(View.VISIBLE);
                filename.setError(null);
                Toast.makeText(this, "Image added successfully", Toast.LENGTH_SHORT).show();
            } else if (path.endsWith(".pdf")) {

                File file = new File(path);
                RequestBody reqFile = RequestBody.create(file, MediaType.parse("*/*"));
                requestFile = MultipartBody.Part.createFormData("attachment", file.getName(), reqFile);
                filename.setText(file.getName());
                filename.setVisibility(View.VISIBLE);
                filename.setError(null);
                Toast.makeText(this, "PDF File added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid file format", Toast.LENGTH_SHORT).show();
                filename.setError("Invalid File Format");
                filename.setText("Please select images(jpg/jpeg/png) or pdf file");
                filename.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void standardspinner() {
        String[] items = new String[standardlist.size()];
        for (int i = 0; i < standardlist.size(); i++) {
            items[i] = standardlist.get(i).getStandardSection();
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter(AddHomeworkActivity.this, android.R.layout.simple_list_item_1, items);
            standardspinner.setAdapter(adapter);
        }
    }

    private void subjectspinner() {
        String[] items = new String[subjectlist.size()];
        for (int i = 0; i < subjectlist.size(); i++) {
            items[i] = subjectlist.get(i).getSubjectName();
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter(AddHomeworkActivity.this, android.R.layout.simple_list_item_1, items);
            subjectspinner.setAdapter(adapter);
        }
    }

    private boolean checkPermissionExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            int result = ContextCompat.checkSelfPermission(AddHomeworkActivity.this, permission);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissionForExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                manageAllFilesAccessLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                manageAllFilesAccessLauncher.launch(intent);
            }
        } else {
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissionLauncher.launch(permissions);
        }
    }

    private boolean validate() {
        if (enterdescription.getText().toString().isEmpty()) {
            enterdescription.requestFocus();
            enterdescription.setError("enter the description");
            return false;
        } else if (assigneddatetext.getText().toString().isEmpty()) {
            assigneddatetext.requestFocus();
            assigneddatetext.setError("select the date");
            return false;
        }
        else if (submissiondatetext.getText().toString().isEmpty()) {
            submissiondatetext.requestFocus();
            submissiondatetext.setError("select the date");
            return false;
        }
        return true;
    }
}
