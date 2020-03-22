package com.example.smartbus.driver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartbus.R;
import com.example.smartbus.student.StudentListAdapter;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewHolder> {
    private static Context mContext;
    private ArrayList<String> students;

    private ListAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    // constructor
    public ListAdapter(Context context, ArrayList<String> students) {
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
        // put element of array here
        holder.studentName.setText(students.get(position));
     Glide.with(mContext).load(R.drawable.profile).into(holder.studentImage);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView studentImage;
        private TextView studentName;

        private TextView scan, rate, profile;
        private CardView utilities;

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

// if press on each item show menu
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    clickItem();
                                    Log.i("tag", "In adapter");

                                    listener.onItemClick(position);
                                }
                            }

                        }
                    });

                }
            });

        }

        protected void clickItem() {
            utilities.setVisibility(View.VISIBLE);

        }
// click for each menu item
@Override
public void onClick(View v) {
    if (utilities.getVisibility() == View.VISIBLE) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.scan:
                //intent = new Intent(mContext, ScanActivity.class);
                //todo:scan and send notification to student
                intent = new Intent(mContext, Scan.class).putExtra("nameOfStudent", students.get(getAdapterPosition()));
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Scan", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rate_student:
                intent = new Intent(mContext, RateStudent.class).putExtra("nameOfStudent", students.get(getAdapterPosition()));
                mContext.startActivity(intent);
                break;

            case R.id.profile:
                intent = new Intent(mContext, StudentProfile.class).putExtra("nameOfStudent", students.get(getAdapterPosition()));
                mContext.startActivity(intent);
                break;

        }

    }
}

    }
}
