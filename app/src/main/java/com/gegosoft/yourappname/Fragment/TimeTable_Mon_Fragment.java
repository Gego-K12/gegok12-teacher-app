package com.gegosoft.yourappname.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.yourappname.Models.TImeTable_Model;
import com.gegosoft.yourappname.R;

import java.util.List;

public class TimeTable_Mon_Fragment extends Fragment {
    RecyclerView recyclerView;
    MondayAdapter mondayAdapter;
    TextView invisibletext;
    String dayname;

    List<TImeTable_Model.Datum.subDatum> periodsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mon_recyclerview_layout, null);
        dayname = getArguments().getString("dayname");
        periodsList = getArguments().getParcelableArrayList(dayname);
        recyclerView = view.findViewById(R.id.monrecyclerview);
        invisibletext = view.findViewById(R.id.monnodatafoundtxt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mondayAdapter = new MondayAdapter();
        recyclerView.setAdapter(mondayAdapter);
        return view;

    }

    public class MondayAdapter extends RecyclerView.Adapter<MondayAdapter.MondayViewHolder> {

        @NonNull
        @Override
        public MondayAdapter.MondayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_mon_layout, parent, false);
            return new MondayAdapter.MondayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MondayAdapter.MondayViewHolder holder, int position) {
            holder.sno.setText(String.valueOf(position + 1));
            holder.class1.setText(periodsList.get(position).getStandardName());
            holder.sub.setText(periodsList.get(position).getSubjectName());
        }

        @Override
        public int getItemCount() {
            return periodsList.size();
        }

        public class MondayViewHolder extends RecyclerView.ViewHolder {
            TextView sub, period, class1,sno;

            public MondayViewHolder(@NonNull View itemView) {
                super(itemView);
                sub = itemView.findViewById(R.id.montimetablesubject);
                period = itemView.findViewById(R.id.montimetableperiod);
                class1 = itemView.findViewById(R.id.montimetableclass);
                sno = itemView.findViewById(R.id.txtSerialNum);
            }
        }
    }
}
