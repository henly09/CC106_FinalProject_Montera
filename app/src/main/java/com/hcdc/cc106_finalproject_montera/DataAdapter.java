package com.hcdc.cc106_finalproject_montera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataView>{

    private ArrayList<UserStats_Model> modelArrayList;
    private Context context;
    SQLiteDatabase myDB;

    public DataAdapter(ArrayList<UserStats_Model> modelArrayList,Context context){
        this.modelArrayList = modelArrayList;
        this.context = context;
        myDB = context.openOrCreateDatabase("cc106_pedometer.db", 0, null);
    }

    @NonNull
    @Override
    public DataAdapter.DataView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.DataView holder, int position) {
        UserStats_Model model = modelArrayList.get(position);
        holder.session.setText(model.getSession_id());
        holder.cal.setText(model.getCalories_burned());
        holder.steps.setText(model.getSteps_count());
        holder.distance.setText(model.getDistance());
        holder.date.setText(model.getCreated_at());
        holder.duration.setText(model.getDuration());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class DataView extends RecyclerView.ViewHolder {

        TextView session,cal,steps,distance,date,duration;

        public DataView(@NonNull View itemView) {
            super(itemView);

            session = itemView.findViewById(R.id.sessioniddisplay);
            cal = itemView.findViewById(R.id.calburndisplay);
            steps = itemView.findViewById(R.id.stepcountdisplay);
            distance = itemView.findViewById(R.id.distancedisplay);
            date = itemView.findViewById(R.id.datedisplay);
            duration = itemView.findViewById(R.id.durationdisplay);
        }
    }
}
