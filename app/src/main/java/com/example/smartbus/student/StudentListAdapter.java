package com.example.smartbus.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbus.R;
import com.example.smartbus.driver.ListAdapter;

import java.util.ArrayList;
class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.viewHolder> {
    private static Context mContext;
    private ArrayList<String> students;
    private StudentListAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(StudentListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public StudentListAdapter(Context context , ArrayList<String> arrayList){
        mContext = context;
        students = arrayList;
    }
    @NonNull
    @Override
    public StudentListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        View view = mInflater.inflate(R.layout.student_item_list, parent, false);
        return new StudentListAdapter.viewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.viewHolder holder, int position) {

        holder.name.setText(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView name;
        public viewHolder(@NonNull View itemView,final StudentListAdapter.OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.student_list_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }
    }
}