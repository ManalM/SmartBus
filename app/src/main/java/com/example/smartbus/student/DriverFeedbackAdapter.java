package com.example.smartbus.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbus.R;

import java.util.ArrayList;

public class DriverFeedbackAdapter extends RecyclerView.Adapter<DriverFeedbackAdapter.viewHolder> {
    private static Context mContext;
    ArrayList<String> comments , names, stars, dates;

public  DriverFeedbackAdapter (Context context , ArrayList<String> c,ArrayList<String>  n,ArrayList<String> s,ArrayList<String> d){
    mContext =context;
    comments =c;
    names=n;
    stars=s;
    dates=d;
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

        holder.name.setText(names.get(position));
        holder.ratingBar.setRating(Integer.valueOf(stars.get(position)));
        holder.comment.setText(comments.get(position));
        holder.time.setText(dates.get(position));

    }

    @Override
    public int getItemCount() {

return names.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView name, comment, time;
        RatingBar ratingBar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.student_name_feedback);
            comment = itemView.findViewById(R.id.student_feedback);
            ratingBar = itemView.findViewById(R.id.ratingBar2);
            time = itemView.findViewById(R.id.date);
        }
    }
}
