package com.example.smartbus.driver;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartbus.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewHolder> {
    private static Context mContext;
    private String[] students;
    private ListAdapter mAdapterAzkar;
    private ListAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public ListAdapter(Context context,String[] students) {
        this.mContext = context;
        this.students = students;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.driver_item_list, parent, false);
        return new ListAdapter.viewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.studentName.setText(students[position]);
        Glide.with(mContext).load(R.drawable.scan).into(holder.studentImage);
    }

    @Override
    public int getItemCount() {
        return students.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView studentImage;
        private TextView studentName;

        private TextView scan, rate, profile;
        private LinearLayout utilities;

        public viewHolder(@NonNull final View itemView, final ListAdapter.OnItemClickListener listener) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            studentImage = itemView.findViewById(R.id.student_image);
            utilities = itemView.findViewById(R.id.utilities_layout);
            scan = itemView.findViewById(R.id.scan);
            rate = itemView.findViewById(R.id.rate_student);
            profile = itemView.findViewById(R.id.profile);


            scan.setOnClickListener(this);
            rate.setOnClickListener(this);
            profile.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        utilities.setVisibility(View.VISIBLE);
                    /*              if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }*/

                }
            });

        }


        @Override
        public void onClick(View v) {
            if (utilities.getVisibility() == View.VISIBLE) {
                Intent intent = null;
                switch (v.getId()) {
                    case R.id.scan:
                        //   intent = new Intent(mContext, ScanActivity.class);
                        Toast.makeText(mContext, "Scan", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.rate_student:
                        intent = new Intent(mContext, RateStudent.class);
                        break;

                    case R.id.profile:
                        intent = new Intent(mContext, StudentProfile.class);
                        break;

                }
                mContext.startActivity(intent);
            }
        }
    }
}
