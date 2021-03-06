package com.planetsystems.tela.activities.staff.administration.taskAttendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.tela.R;
import com.planetsystems.tela.activities.staff.administration.serviceRequests.PendingHelpRequest;
import com.planetsystems.tela.activities.staff.administration.serviceRequests.RequestsMade;
import com.planetsystems.tela.activities.staff.administration.timeAttendance.ConfirmTimeAttendance;
import com.planetsystems.tela.activities.staff.administration.timeAttendance.TimeAttendanceList;
import com.planetsystems.tela.activities.staff.regularStaff.home.Tasks;
import com.planetsystems.tela.activities.staff.regularStaff.home.TasksAdapter;
import com.planetsystems.tela.activities.staff.regularStaff.home.TeacherHomeActivity;
import com.planetsystems.tela.activities.staff.regularStaff.home.TeacherHomeActivityViewModel;
import com.planetsystems.tela.data.confirmTimeOnTaskAttendance.SyncConfirmTimeOnTaskAttendance;
import com.planetsystems.tela.data.timeOnTask.SynTimeOnTask;
import com.planetsystems.tela.data.timetable.SyncTimeTable;
import com.planetsystems.tela.workers.WorkManagerTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SupervisorObservations extends AppCompatActivity {

    TeacherHomeActivityViewModel teacherHomeActivityViewModel;
    SupervisorObservationsViewModel supervisorObservationsViewModel;
    List<Tasks> tasksModel;
    TasksConfirmedAdapter adapter;
    RecyclerView tasks;
    TextView datetoday;
    TextView emp_Name;
    TextView emp_Id;
    TextView tvname;
    //TextView datetoday;
    Button submit, selfmenu;
    public String dateString, timeString;
    String emp_id_extra, emp_name_extra, admin_id_extra;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_observations);
        setTitle("Supervisor Observations");

        tasks = findViewById(R.id.task_list);
        tasks = findViewById(R.id.task_list);
        emp_Name = findViewById(R.id.namexx);
        emp_Id = findViewById(R.id.staffId);
        datetoday = findViewById(R.id.datetoday);
        submit = findViewById(R.id.submit);
        selfmenu = findViewById(R.id.menuBtn);

        Date currentTime = Calendar.getInstance().getTime();
        datetoday.setText(""+currentTime.toString());

        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd /MM/ yyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        dateString = dateFormat.format(date);
        timeString = time.format(date);

        /////////////////////////////Sync data/////////////////////////////////////////////////////////
        WorkManagerTrigger.startFetchWorkers(getApplicationContext());
        WorkManagerTrigger.startUploadWorkers(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        admin_id_extra = bundle.getString("admin");
        emp_id_extra = bundle.getString("employee_No");
        emp_name_extra = bundle.getString("employee_Name");

        emp_Name.append(emp_name_extra);
        emp_Id.append(emp_id_extra);

        //Listing individual task in recyclerview
        tasksModel = new ArrayList<>();
        adapter = new TasksConfirmedAdapter(this, tasksModel);
        tasks.setAdapter(adapter);
        tasks.setLayoutManager(new LinearLayoutManager(this));

        supervisorObservationsViewModel = new ViewModelProvider(this).get(SupervisorObservationsViewModel.class);
        teacherHomeActivityViewModel = new ViewModelProvider(this).get(TeacherHomeActivityViewModel.class);

        teacherHomeActivityViewModel.tasksWithPresentActionStatus(emp_id_extra, dateString, "Present").observe(this, new Observer<List<SynTimeOnTask>>() {
            @Override
            public void onChanged(List<SynTimeOnTask> synTimeOnTasks) {
                if(synTimeOnTasks.size() != count){
                    for (int i = 0; i < synTimeOnTasks.size(); i++){
                        Tasks taskList = new Tasks();
                        taskList.setTaskId(synTimeOnTasks.get(i).getTaskId());
                        taskList.setTaskName(synTimeOnTasks.get(i).getTaskName());
                        taskList.setStartTime(synTimeOnTasks.get(i).getStartTime());
                        taskList.setEndTime(synTimeOnTasks.get(i).getEndTime());
                        taskList.setStatus("Taught");
                        taskList.setInTime("In Time");

                        tasksModel.add(taskList);
                    }
                    adapter.setTaskList(tasksModel);
                }else {
                    new AlertDialog.Builder(SupervisorObservations.this)
                            .setTitle("Confirmation")
                            .setMessage("Dear Supervisor, "+ emp_name_extra +" has not committed to any task today. \n So there is no task to show for supervision.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Alright", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(SupervisorObservations.this, TaskAttendance.class);
                                    intent.putExtra("admin", admin_id_extra);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    SupervisorObservations.this.finish();
                                }}).show();
                }
            }
        });

        supervisorObservationsViewModel.getAllTimeOnTask().observe(this, new Observer<List<SyncConfirmTimeOnTaskAttendance>>() {
            @Override
            public void onChanged(List<SyncConfirmTimeOnTaskAttendance> syncConfirmTimeOnTaskAttendances) {
               // Toast.makeText(getApplicationContext(), "size is: " + String.valueOf(syncConfirmTimeOnTaskAttendances.size()), Toast.LENGTH_LONG).show();
            }
        });

        supervisorObservationsViewModel.getEmployeeNoAndDate(emp_id_extra,dateString).observe(this, new Observer<List<SyncConfirmTimeOnTaskAttendance>>() {
            @Override
            public void onChanged(List<SyncConfirmTimeOnTaskAttendance> syncConfirmTimeOnTaskAttendances) {
                if(syncConfirmTimeOnTaskAttendances.size() != count ){
                    new AlertDialog.Builder(SupervisorObservations.this)
                            .setTitle("Confirmation")
                            .setMessage("Dear Supervisor, "+ emp_name_extra +"'s tasks have been already supervised and submitted successfully.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Alright", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(SupervisorObservations.this, TaskAttendance.class);
                                    intent.putExtra("admin", admin_id_extra);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    SupervisorObservations.this.finish();
                                }}).show();
                }else {
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(SupervisorObservations.this)
                                    .setTitle("Confirmation")
                                    .setMessage("Do you really want to submit these observations?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            PostToSyncConfirmTimeOnTaskAttendance();
                                        }})
                                    .setNegativeButton(android.R.string.no, null).show();
                        }
                    });
                }
            }
        });
    }

    private void PostToSyncConfirmTimeOnTaskAttendance() {
        for(Tasks task : tasksModel){
            SyncConfirmTimeOnTaskAttendance syncConfirmTimeOnTaskAttendance = new SyncConfirmTimeOnTaskAttendance(
                    task.getStatus(),
                    task.getInTime(),
                    emp_id_extra,
                    emp_id_extra,
                    task.getStatus(),
                    admin_id_extra,
                    task.getTaskId(),
                    dateString,
                    false
            );

            supervisorObservationsViewModel.insertNewConfirmations(syncConfirmTimeOnTaskAttendance);
            Toast.makeText(getApplicationContext(),"Submitted Successfully " ,Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SupervisorObservations.this, TaskAttendance.class);
            intent.putExtra("admin", admin_id_extra);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            SupervisorObservations.this.finish();
        }
    }
}
