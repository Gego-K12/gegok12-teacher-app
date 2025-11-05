package com.gegosoft.schoolteacherapp.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Models.ApiSuccessModel;
import com.gegosoft.schoolteacherapp.Models.MyActiveTaskListModel;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCompletedTask_Fragment extends Fragment {
    RecyclerView recyclerview;
    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String,String> headermap;
    List<MyCompletedTask> myCompletedTasks;
    MyCompletedTaskAdapter activeTaskAdapter;
    String type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recyclerview,null);
        recyclerview=view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(getActivity());
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization","Bearer "+userDetailsSharedPref.getString("token"));
        headermap.put("Accept","application/json");

        type = getArguments().getString("type");
        if (type.equalsIgnoreCase("Self")){
            getOtherTaskCompleted();
        }
        else{
            getMyCompletedTask();
        }

        return view;
    }

    private void getOtherTaskCompleted() {
        Call<ResponseBody> activeTaskListModelCall = api.getOtherCompletedTask(headermap);
        activeTaskListModelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){



                    try {
                        myCompletedTasks = new ArrayList<>();
                        List<MyActiveTaskListModel.Datum> data = new ArrayList<>();
                        JSONObject jsonObject =  new JSONObject(response.body().string());
                        Iterator keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = jsonObject.getJSONArray((String) key);
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<MyActiveTaskListModel.Datum>>() {
                            }.getType();
                            data = gson.fromJson(String.valueOf(value), listType);
                            myCompletedTasks.add(new MyCompletedTask((String) key,data));
                        }

                        MyCompletedTaskAdapter activeTaskAdapter = new MyCompletedTaskAdapter(myCompletedTasks);
                        recyclerview.setAdapter(activeTaskAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void getMyCompletedTask(){

        Call<ResponseBody> activeTaskListModelCall = api.getMyCompletedTask(headermap);
        activeTaskListModelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){



                    try {
                        myCompletedTasks = new ArrayList<>();
                        List<MyActiveTaskListModel.Datum> data = new ArrayList<>();
                        JSONObject jsonObject =  new JSONObject(response.body().string());
                        Iterator keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = jsonObject.getJSONArray((String) key);
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<MyActiveTaskListModel.Datum>>() {
                            }.getType();
                            data = gson.fromJson(String.valueOf(value), listType);
                            myCompletedTasks.add(new MyCompletedTask((String) key,data));

                        }

                        activeTaskAdapter = new MyCompletedTaskAdapter(myCompletedTasks);
                        recyclerview.setAdapter(activeTaskAdapter);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    public class MyCompletedTask extends ExpandableGroup<MyActiveTaskListModel.Datum> {

        public MyCompletedTask(String title, List<MyActiveTaskListModel.Datum> items) {
            super(title, items);
        }
    }
    private class MyCompletedTaskAdapter extends ExpandableRecyclerViewAdapter<MyCompletedTaskAdapter.TaskTypeHolder,MyCompletedTaskAdapter.ChildHolder> {
        List<? extends ExpandableGroup> groups;
        int total,average;

        public MyCompletedTaskAdapter(List<? extends ExpandableGroup> groups) {
            super(groups);
            this.groups = groups;
        }

        @Override
        public MyCompletedTaskAdapter.TaskTypeHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_parent_task_layout, parent, false);
            return new MyCompletedTaskAdapter.TaskTypeHolder(view);
        }

        @Override
        public MyCompletedTaskAdapter.ChildHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_child_task_layout, parent, false);
            return new MyCompletedTaskAdapter.ChildHolder(view);
        }

        @Override
        public void onBindChildViewHolder(MyCompletedTaskAdapter.ChildHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            MyActiveTaskListModel.Datum datum = (MyActiveTaskListModel.Datum) group.getItems().get(childIndex);
            if (datum!=null){
                holder.date.setText(datum.getTaskDate());
                holder.title.setText(datum.getTitle());
                holder.delete_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Do you want to delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteTask(datum);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog  = builder.create();
                        dialog.show();
                    }
                });
            }
        }

        @Override
        public void onBindGroupViewHolder(MyCompletedTaskAdapter.TaskTypeHolder holder, int flatPosition, ExpandableGroup group) {

            holder.parent_title.setText(group.getTitle()+" ("+group.getItems().size()+")");

        }

        public class TaskTypeHolder extends GroupViewHolder {
            TextView parent_title;
            ImageView arrow;
            public TaskTypeHolder(View itemView) {
                super(itemView);
                parent_title = itemView.findViewById(R.id.parent_title);
                arrow = itemView.findViewById(R.id.arrow);
            }
        }

        public class ChildHolder extends ChildViewHolder {
            TextView title,date;
            ImageView delete_image;

            public ChildHolder(View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                date=itemView.findViewById(R.id.date);
                delete_image=itemView.findViewById(R.id.delete_image);
            }
        }
        private void DeleteTask(MyActiveTaskListModel.Datum datum){
            Call<ApiSuccessModel>modelCall=api.getdeletetask(headermap, String.valueOf(datum.getTaskId()));
            modelCall.enqueue(new Callback<ApiSuccessModel>() {
                @Override
                public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                    if(response.isSuccessful()){
                        myCompletedTasks.remove(datum);
                        activeTaskAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(),"Task deleted Successfully",Toast.LENGTH_SHORT).show();
                        if (type.equalsIgnoreCase("others")){
                            getOtherTaskCompleted();
                        }
                        else{
                            getMyCompletedTask();
                        }
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
    }

}
