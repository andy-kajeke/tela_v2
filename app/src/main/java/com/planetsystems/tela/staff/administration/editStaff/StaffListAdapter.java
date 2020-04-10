package com.planetsystems.tela.staff.administration.editStaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.planetsystems.tela.R;
import com.planetsystems.tela.data.Teacher.SyncTeacher;
import com.planetsystems.tela.data.Teacher.SyncTeacherModel;

import java.util.ArrayList;
import java.util.List;


public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffViewHolder> {

    private LayoutInflater layoutInflater;
    private  Context mContext;
    private List<SyncTeacher> mSyncTeacherModels;

    public StaffListAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.get_staff_list, parent, false);
        StaffViewHolder viewHolder = new StaffViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        if (mSyncTeacherModels != null){
            SyncTeacher syncTeacher = mSyncTeacherModels.get(position);
            holder.setData(syncTeacher.getFirstName(), syncTeacher.getLastName(), syncTeacher.getEmployeeNumber(), syncTeacher.getRole(), position);

            String L= ""+mSyncTeacherModels.get(position).getFirstName();
            char k = mSyncTeacherModels.get(position).getFirstName().charAt(0);
            String Cap= ""+k+"".toString().toUpperCase();
            String s = Character.toString(k);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use

            int color1 = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder().buildRoundRect(""+s.toUpperCase(),color1,60); //radius in px

            holder.imageView.setImageDrawable(drawable);

        }else {
            holder.It_role.setText("No record");
        }
    }

    @Override
    public int getItemCount() {
        if (mSyncTeacherModels != null){
            return  mSyncTeacherModels.size();
        }else {
            return 0;
        }
    }

    public void setTeacherDetails(List<SyncTeacher> teacher){
        mSyncTeacherModels = teacher;
        notifyDataSetChanged();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder {
        public TextView It_name;
        public TextView It_code;
        public TextView It_role;
        public ImageView imageView;
        public int mPosition;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            It_name = itemView.findViewById(R.id.category);
            It_code = itemView.findViewById(R.id.name);
            It_role = itemView.findViewById(R.id.empRole);
            imageView = itemView.findViewById(R.id.personphoto);
        }

        public void setData(String employeeNumber, String firstName, String lastName, String role, int position) {
            It_name.setText(firstName + " " + lastName);
            It_code.setText(employeeNumber);
            It_role.setText(role);
            mPosition = position;
        }
    }
}

