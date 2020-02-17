package com.example.smartbus.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbus.R;

import java.util.ArrayList;

public class DriverFeedbackAdapter extends RecyclerView.Adapter<DriverFeedbackAdapter.viewHolder> {
    private static Context mContext;
    private ArrayList<String> studentsName;
    private ArrayList<String> driverComment;

    public DriverFeedbackAdapter(Context mContext,ArrayList<String> studentsName, ArrayList<String> driverComment) {

        this.mContext = mContext;
        this.studentsName = studentsName;
        this.driverComment = driverComment;
    }

    @NonNull
    @Override
    public DriverFeedbackAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        View view = mInflater.inflate(R.layout.feedback_item, parent, false);
        return new DriverFeedbackAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverFeedbackAdapter.viewHolder holder, int position) {
        holder.comment.setText(driverComment.get(position));
        holder.name.setText(studentsName.get(position));
    }

    @Override
    public int getItemCount() {
        return studentsName.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView name,comment;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

           name = itemView.findViewById(R.id.student_name_feedback);
           comment = itemView.findViewById(R.id.student_feedback);

        }
    }
}
