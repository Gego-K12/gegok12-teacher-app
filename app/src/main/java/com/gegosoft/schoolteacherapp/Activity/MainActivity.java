package com.gegosoft.schoolteacherapp.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Helper.AppUtils;
import com.gegosoft.schoolteacherapp.Helper.ProgressDialogFragment;
import com.gegosoft.schoolteacherapp.Interface.RecycleronItemclick;
import com.gegosoft.schoolteacherapp.Models.ClassNoticeBoardModel;
import com.gegosoft.schoolteacherapp.Models.DashboardModel;
import com.gegosoft.schoolteacherapp.Models.LogOutModel;
import com.gegosoft.schoolteacherapp.Models.MyProfileModel;
import com.gegosoft.schoolteacherapp.Models.Teacher_School_NoticeboardModel;
import com.gegosoft.schoolteacherapp.Models.UpdateFCMTokenModel;
import com.gegosoft.schoolteacherapp.Receiver.NotificationReceiver;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.gegosoft.schoolteacherapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView leaveactivity, navusername, navposition;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button myprofile;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    List<MyProfileModel.Data> myprofilelist;
    Map<String, String> headermap;
    Toolbar toolbar;
    List<MyProfileModel> myProfileModelList;
    List<String> permissionList;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView allnotificationimg, navprofileimage;
    TextView[] dots;
    LinearLayout dotslayout, logout;
    SchoolNoticeAdapter schoolNoticeAdapter;
    ClassNoticeAdapter classNoticeAdapter;
    ConcatAdapter concatAdapter;
    RecyclerView recyclerView;
    CircleImageView profilepic;
    TextView doj, profilename;
    ArrayList<DashboardModel> dashboardModels = new ArrayList<>();
    GridAdapter gridAdapter;
    RecyclerView noticeboardlist;
    LinearLayoutManager linearLayoutManager;
    List<Teacher_School_NoticeboardModel.Datum> teacherschoolnoticelist;
    List<ClassNoticeBoardModel.Datum> classchoolnoticelist;
    CardView nodatacardview;
    Menu nav_Menu;
    String classteacher;

    private NotificationReceiver notificationReceiver = new NotificationReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            RemoteMessage message = intent.getParcelableExtra("MessageReceived");
            JSONObject jsonObject = new JSONObject(message.getData());
            String type = null;
            try {
                type = jsonObject.getString("type");
                if (type.equalsIgnoreCase("private message")) {
                    userDetailsSharedPref.saveBoolean("isPrivateMessage", true);
                }
                if (type.equalsIgnoreCase("homework")) {
                    userDetailsSharedPref.saveBoolean("isHomeWork", true);
                }
                if (type.equalsIgnoreCase("assignment")) {
                    userDetailsSharedPref.saveBoolean("isAssignment", true);
                }
                if (type.equalsIgnoreCase("video_room")) {
                    userDetailsSharedPref.saveBoolean("isVideoroom", true);
                }
                if (type.equalsIgnoreCase("discipline")) {
                    userDetailsSharedPref.saveBoolean("isDiscipline", true);
                }
                if (type.equalsIgnoreCase("bulletin")) {
                    userDetailsSharedPref.saveBoolean("isMagazine", true);
                }
                if (type.equalsIgnoreCase("event")) {
                    userDetailsSharedPref.saveBoolean("isEvent", true);
                }
                if (type.equalsIgnoreCase("conversation")) {
                    userDetailsSharedPref.saveBoolean("isFeedback", true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (exit) {
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        leaveactivity = findViewById(R.id.leaveactivitytext);
        navigationView = findViewById(R.id.navigationview);
        nav_Menu = navigationView.getMenu();
        View hView = navigationView.inflateHeaderView(R.layout.header_layout);
        navprofileimage = hView.findViewById(R.id.navprofileimage);
        navusername = hView.findViewById(R.id.navusername);
        navposition = hView.findViewById(R.id.navposition);
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar);
        myprofile = findViewById(R.id.myprofilebtn);
        doj = findViewById(R.id.ddoj);
        profilepic = findViewById(R.id.dprofilepic);
        profilename = findViewById(R.id.dprofilename);
        nodatacardview = findViewById(R.id.nodatacardview);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        recyclerView = findViewById(R.id.maingridview);
        noticeboardlist = findViewById(R.id.noticeboardlist);
        allnotificationimg = findViewById(R.id.allnotificationimg);

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);

        if (userDetailsSharedPref.getString("firebasetoken") != null) {
            FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            messaging.getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String firebasetoken) {
                    if (!userDetailsSharedPref.getString("firebasetoken").equalsIgnoreCase(firebasetoken)) {
                        sendRegistrationToServer(firebasetoken);
                    }
                }
            });

        } else {
            FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            messaging.getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String firebasetoken) {

                    sendRegistrationToServer(firebasetoken);

                }
            });
        }

        getdprofiledetails();
        logout = hView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        dotslayout = findViewById(R.id.dotslayout);
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        noticeboardlist.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(noticeboardlist);
        noticeboardlist.setHasFixedSize(true);
        dashboardModels = new ArrayList<>();
        dashboardModels.add(new DashboardModel("Home Work", R.drawable.ic_homework_svg));
        dashboardModels.add(new DashboardModel("Lesson Plan", R.drawable.ic_lessonplan_svg));
        dashboardModels.add(new DashboardModel("Attendance", R.drawable.ic_attendance_svg));
        dashboardModels.add(new DashboardModel("Leave", R.drawable.ic_applyleave_svg));
        dashboardModels.add(new DashboardModel("Messages", R.drawable.ic_messages_svg));
        dashboardModels.add(new DashboardModel("Profile", R.drawable.ic_profile_svg));
        dashboardModels.add(new DashboardModel("Assignment", R.drawable.ic_assignment_svg));
        dashboardModels.add(new DashboardModel("Online Class", R.drawable.ic_onlineclass_svg));
        dashboardModels.add(new DashboardModel("Time Table", R.drawable.ic_timetable_svg));

        gridAdapter = new GridAdapter(new RecycleronItemclick() {
            @Override
            public void mainmenuclick(View view, int position) {
                if (position == 0) {
                    startActivity(new Intent(MainActivity.this, HomeWorkActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(MainActivity.this, LessonPlanActivity.class));
                } else if (position == 2) {
                    startActivity(new Intent(MainActivity.this, AttendanceActivity.class).putExtra("classteacher", classteacher));
                } else if (position == 3) {
                    startActivity(new Intent(MainActivity.this, LeaveActivity.class));
                } else if (position == 4) {
                    startActivity(new Intent(MainActivity.this, MessageActivity.class));
                } else if (position == 5) {
                    startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
                } else if (position == 6) {
                    startActivity(new Intent(MainActivity.this, AssignmentList.class));
                } else if (position == 7) {
                    startActivity(new Intent(MainActivity.this, RoomActivity.class));
                } else if (position == 8) {
                    startActivity(new Intent(MainActivity.this, TimeTableActivity.class));
                }
            }
        }, dashboardModels) {
        };
        noticeboardlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {


                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int review_position = linearLayoutManager.findFirstVisibleItemPosition();

                    addDots(review_position);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        final Menu menu = navigationView.getMenu();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {

                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }

            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }

        };
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();

                if (id == R.id.teacherleavelist) {
                    startActivity(new Intent(MainActivity.this, MyLeaveActivity.class));
                    return true;
                }  else if (id == R.id.exams) {
                    startActivity(new Intent(MainActivity.this, ExamMarkActivity.class));
                    return true;
                } else if (id == R.id.todolist) {
                    startActivity(new Intent(MainActivity.this, To_Do_List_Activity.class));
                    return true;
                } else if (id == R.id.leavchecker_by_principal) {
                    startActivity(new Intent(MainActivity.this, MyLeaveActivityWithPermission.class));
                    return true;
                } else if (id == R.id.videocall) {
                    startActivity(new Intent(MainActivity.this, RoomActivity.class));
                    return true;
                } else if (id == R.id.lessonplanactivity) {
                    startActivity(new Intent(MainActivity.this, LessonPlanActivity.class));
                    return true;
                } else if (id == R.id.holidaylist) {
                    startActivity(new Intent(MainActivity.this, HolidayListActivity.class));
                    return true;
                } else if (id == R.id.studentleave) {
                    startActivity(new Intent(MainActivity.this, StudentLeaveList.class));
                    return true;
                } else if (id == R.id.addhomework) {
                    startActivity(new Intent(MainActivity.this, AddHomeworkActivity.class));
                    return true;
                } else if (id == R.id.addassignement) {
                    startActivity(new Intent(MainActivity.this, Add_AssigmentActivity.class));
                    return true;
                } else if (id == R.id.changepassword) {
                    startActivity(new Intent(MainActivity.this, ChangePasswordWithLogin.class));
                    return true;
                } else if (id == R.id.absentlist) {
                    startActivity(new Intent(MainActivity.this, AbsentListActivity.class));
                    return true;
                } else {
                    return false;
                }
            }


        });
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.whitecolor));
        allnotificationimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllNotificationActivity.class);
                startActivity(intent);
            }
        });
        getprofiledetails();
        getSchoolNoticeBoard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(notificationReceiver, new IntentFilter(NotificationReceiver.ACTION_NOTIFICATION),Context.RECEIVER_NOT_EXPORTED);
        }else {
            registerReceiver(notificationReceiver, new IntentFilter(NotificationReceiver.ACTION_NOTIFICATION));
        }
    }


    private void getdprofiledetails() {

        Call<MyProfileModel> myProfileModelCall = api.getprofilemodel(headermap);
        myProfileModelCall.enqueue(new Callback<MyProfileModel>() {
            @Override
            public void onResponse(Call<MyProfileModel> call, Response<MyProfileModel> response) {
                if (response.isSuccessful()) {
                    MyProfileModel.Data data = response.body().getData();
                    myprofilelist = new ArrayList<>();
                    String mydoj = data.getJoiningDate();
                    classteacher = data.getClassTeacher();
                    userDetailsSharedPref.saveString("classteacher", classteacher);
                    mydoj = mydoj.substring(0, 6) + "," + mydoj.substring(6, mydoj.length());
                    doj.setText(mydoj);
                    String myprofilename = data.getFullname();
                    userDetailsSharedPref.saveString("name", myprofilename);
                    profilename.setText(myprofilename);
                    Glide.with(MainActivity.this).load(data.getAvatar()).into(profilepic);

                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyProfileModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getClassNoticeBoard() {

        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(), "loading screen");
        Call<ClassNoticeBoardModel> boardModelCall = api.getClassNotice(headermap, userDetailsSharedPref.getString("userId"));
        boardModelCall.enqueue(new Callback<ClassNoticeBoardModel>() {
            @Override
            public void onResponse(Call<ClassNoticeBoardModel> call, Response<ClassNoticeBoardModel> response) {
                if (response.isSuccessful()) {
                    classchoolnoticelist = response.body().getData();
                    if (classchoolnoticelist.size() != 0) {

                        noticeboardlist.setVisibility(View.VISIBLE);

                        classNoticeAdapter = new ClassNoticeAdapter(classchoolnoticelist);
                        nodatacardview.setVisibility(View.INVISIBLE);
                        if (concatAdapter == null) {
                            concatAdapter = new ConcatAdapter(classNoticeAdapter);
                            noticeboardlist.setAdapter(classNoticeAdapter);
                        } else {
                            concatAdapter.addAdapter(classNoticeAdapter);
                            concatAdapter.notifyDataSetChanged();
                        }
                        addDots(0);

                    } else {
                        if (teacherschoolnoticelist.size() != 0) {
                            nodatacardview.setVisibility(View.INVISIBLE);
                            noticeboardlist.setVisibility(View.VISIBLE);
                        } else {
                            nodatacardview.setVisibility(View.VISIBLE);
                            noticeboardlist.setVisibility(View.INVISIBLE);
                        }
                    }


                } else {
                    if (response.code() == 401) {
                        AppUtils.SessionExpired(MainActivity.this);
                    }
                }
                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<ClassNoticeBoardModel> call, Throwable t) {

                AppUtils.APIFails(MainActivity.this, t);
                progressDialogFragment.dismiss();
            }
        });

    }

    private void getSchoolNoticeBoard() {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", "Loading");
        progressDialogFragment.setArguments(bundle);
        progressDialogFragment.show(getSupportFragmentManager(), "loading screen");
        Call<Teacher_School_NoticeboardModel> boardModelCall = api.getSchoolNotice(headermap);
        boardModelCall.enqueue(new Callback<Teacher_School_NoticeboardModel>() {
            @Override
            public void onResponse(Call<Teacher_School_NoticeboardModel> call, Response<Teacher_School_NoticeboardModel> response) {
                if (response.isSuccessful()) {
                    teacherschoolnoticelist = response.body().getData();

                    if (teacherschoolnoticelist.size() != 0) {

                        noticeboardlist.setVisibility(View.VISIBLE);
                        schoolNoticeAdapter = new SchoolNoticeAdapter(teacherschoolnoticelist);
                        nodatacardview.setVisibility(View.INVISIBLE);
                        if (concatAdapter == null) {
                            concatAdapter = new ConcatAdapter(schoolNoticeAdapter);
                            noticeboardlist.setAdapter(concatAdapter);
                        } else {
                            concatAdapter.addAdapter(schoolNoticeAdapter);
                            concatAdapter.notifyDataSetChanged();
                        }
                        addDots(0);

                    } else {
                        nodatacardview.setVisibility(View.VISIBLE);
                        noticeboardlist.setVisibility(View.INVISIBLE);
                    }

                    getClassNoticeBoard();
                } else {
                    if (response.code() == 401) {
                        AppUtils.SessionExpired(MainActivity.this);
                    }
                }

                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<Teacher_School_NoticeboardModel> call, Throwable t) {
                AppUtils.APIFails(MainActivity.this, t);
                progressDialogFragment.dismiss();
            }
        });

    }

    private void getprofiledetails() {

        Call<MyProfileModel> myProfileModelCall = api.getprofilemodel(headermap);
        myProfileModelCall.enqueue(new Callback<MyProfileModel>() {
            @Override
            public void onResponse(Call<MyProfileModel> call, Response<MyProfileModel> response) {
                if (response.isSuccessful()) {
                    MyProfileModel.Data data = response.body().getData();
                    myProfileModelList = new ArrayList<>();
                    permissionList = new ArrayList<>();

                    permissionList = data.getPermissions();
                    recyclerView.setAdapter(gridAdapter);
                    navusername.setText(data.getFullname());
                    navposition.setText(data.getDesignation());
                    Glide.with(MainActivity.this).load(data.getAvatar()).into(navprofileimage);
                    nav_Menu.findItem(R.id.leavchecker_by_principal).setVisible(false);
                    nav_Menu.findItem(R.id.studentleave).setVisible(false);
                    for (String permission : permissionList) {
                        if (permission.equalsIgnoreCase("leave_checker")) {
                            nav_Menu.findItem(R.id.leavchecker_by_principal).setVisible(true);
                        }
                        if (permission.equalsIgnoreCase("student_leave_checker")) {
                            nav_Menu.findItem(R.id.studentleave).setVisible(true);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyProfileModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addDots(int position) {
        int count = 0;
        if (schoolNoticeAdapter != null) {
            count = schoolNoticeAdapter.getItemCount();
        }
        if (classNoticeAdapter != null) {
            count = count + classNoticeAdapter.getItemCount();
        }

        dots = new TextView[count];
        dotslayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextSize(35);

            dotslayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.black));
        }

    }

    private void logout() {
        userDetailsSharedPref = userDetailsSharedPref.getInstance(MainActivity.this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        userDetailsSharedPref.logout();
        Call<LogOutModel> logOutModelCall = api.getlogout(headermap);
        logOutModelCall.enqueue(new Callback<LogOutModel>() {
            @Override
            public void onResponse(Call<LogOutModel> call, Response<LogOutModel> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Loggedout Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.code() == 401) {
                        AppUtils.SessionExpired(MainActivity.this);
                    }
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogOutModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class ClassNoticeAdapter extends RecyclerView.Adapter<ClassNoticeAdapter.MyViewHolder> {
        List<ClassNoticeBoardModel.Datum> data;

        public ClassNoticeAdapter(List<ClassNoticeBoardModel.Datum> data) {
            this.data = data;

        }

        @NonNull
        @Override
        public ClassNoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classnoticelist, parent, false);
            return new ClassNoticeAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ClassNoticeAdapter.MyViewHolder holder, int position) {
            holder.classtitle.setText(data.get(position).getTitle());
            holder.classdescription.setText(Html.fromHtml(data.get(position).getDescription()));
            if (data.get(position).getBackgroundimage().equals("")) {
                Glide.with(MainActivity.this).load(R.drawable.noticedemo).into(holder.backgroundimg);

            } else {
                Glide.with(MainActivity.this).load(data.get(position).getBackgroundimage()).into(holder.backgroundimg);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView classtitle, classdescription;
            ImageView backgroundimg;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                classtitle = itemView.findViewById(R.id.classtitle);
                classdescription = itemView.findViewById(R.id.classdescription);
                backgroundimg = itemView.findViewById(R.id.noticebackground);
            }
        }
    }

    private class SchoolNoticeAdapter extends RecyclerView.Adapter<SchoolNoticeAdapter.MyViewHolder> {
        List<Teacher_School_NoticeboardModel.Datum> data;

        public SchoolNoticeAdapter(List<Teacher_School_NoticeboardModel.Datum> data) {
            this.data = data;

        }

        @NonNull
        @Override
        public SchoolNoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacherschoolnoticelist, parent, false);
            return new SchoolNoticeAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SchoolNoticeAdapter.MyViewHolder holder, int position) {
            holder.title.setText(data.get(position).getTitle());
            holder.description.setText(data.get(position).getDescription());
            holder.description.setText(Html.fromHtml(data.get(position).getDescription()));
            if (data.get(position).getBackgroundimage().equals("")) {
                Glide.with(MainActivity.this).load(R.drawable.noticedemo).into(holder.backgroundimg);

            } else {
                Glide.with(MainActivity.this).load(data.get(position).getBackgroundimage()).into(holder.backgroundimg);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, description;
            ImageView backgroundimg;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                description = itemView.findViewById(R.id.description);
                backgroundimg = itemView.findViewById(R.id.noticebackground);

            }
        }
    }

    public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
        RecycleronItemclick recycleronItemclick;
        List<DashboardModel> dashboardModels;

        public GridAdapter(RecycleronItemclick recycleronItemclick, List<DashboardModel> dashboardModels) {
            this.recycleronItemclick = recycleronItemclick;
            this.dashboardModels = dashboardModels;
        }

        @NonNull
        @Override
        public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_gridlayout, parent, false);
            return new GridViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.imageView.setImageResource(dashboardModels.get(position).getGridItemImage());
            holder.textView.setText(dashboardModels.get(position).getGridItemName());
            holder.gridcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleronItemclick != null) {
                        recycleronItemclick.mainmenuclick(v, position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dashboardModels.size();
        }

        public class GridViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            CardView gridcard;

            public GridViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.gridimg);
                textView = itemView.findViewById(R.id.gridtext);
                gridcard = itemView.findViewById(R.id.gridcard);
            }
        }
    }

    private Boolean exit = false;

    private void sendRegistrationToServer(final String refreshedToken) {
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        api = ApiClient.getClient().create(Api.class);
        Call<UpdateFCMTokenModel> tokenModelCall = api.updatefcmtoken(headermap, refreshedToken);
        tokenModelCall.enqueue(new Callback<UpdateFCMTokenModel>() {
            @Override
            public void onResponse(Call<UpdateFCMTokenModel> call, Response<UpdateFCMTokenModel> response) {
                if (response.isSuccessful()) {
                    userDetailsSharedPref.saveString("firebasetoken", refreshedToken);
                    Toast.makeText(getBaseContext(), refreshedToken, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<UpdateFCMTokenModel> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
