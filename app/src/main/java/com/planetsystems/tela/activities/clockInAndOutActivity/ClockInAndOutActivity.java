package com.planetsystems.tela.activities.clockInAndOutActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planetsystems.tela.R;
import com.planetsystems.tela.activities.clockInWithEmployeeNumber.ClockInWithEmployeeNumberActivity;
import com.planetsystems.tela.activities.enrollActivity.EnrollmentActivity;
import com.planetsystems.tela.activities.fingerprint.FingerPrintActivity;
import com.planetsystems.tela.activities.staff.smc.SmcActivity;
import com.planetsystems.tela.activities.test.TestActivity;
import com.planetsystems.tela.constants.Role;
import com.planetsystems.tela.data.Teacher.SyncTeacher;
import com.planetsystems.tela.activities.staff.administration.AdminSideActivity;
import com.planetsystems.tela.activities.staff.regularStaff.home.TeacherHomeActivity;
import com.planetsystems.tela.data.clockOut.SyncClockOut;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class ClockInAndOutActivity extends AppCompatActivity {

    private final int START_CLOCK_IN_WITH_STAFF_ID_ACTIVITY_FOR_RESULT = 123;
    private ClockInAndOutActivityViewModel clockInAndOutActivityViewModel;
    public static final int CLOCK_IN_FINGER_PRINT_ACTIVITY_REQUEST_CODE = 645;
    public static final int CLOCK_OUT_FINGER_PRINT_ACTIVITY_REQUEST_CODE = 445;

    TextView dateDisplay, schoolName;
    TextView close_clockIn, close_clockOut;
    Button btnFingerprint_In, btnStaffId_In, btnFingerprint_Out, btnStaffId_Out;
    CardView checkin, checkout, datacenter;
    Dialog checkInDialog, checkOutDialog, checkOutPopup;

    ClockInAndOutActivityViewModel viewModel;
    String deviceIMEI_extra, schoolName_extra;
    String dateString, timeString;

    ////checkout//
    TextView close;
    Button btnClockOut;
    EditText staff_Id, staff_comment;
    CheckBox norm,oth;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockin__clock_out);
        viewModel = new ViewModelProvider(this).get(ClockInAndOutActivityViewModel.class);


        dateDisplay = findViewById(R.id.calendarView4);
        schoolName = findViewById(R.id.schoolName);
        datacenter = findViewById(R.id.cardview2);
        checkin = findViewById(R.id.cardview3);
        checkout = findViewById(R.id.cardview4);

        Bundle bundle = getIntent().getExtras();
        deviceIMEI_extra = bundle.getString("device_imei");
        schoolName_extra = bundle.getString("schoolName");

        schoolName.setText(schoolName_extra);

        //SCHOOL_ID = deviceIMEI_extra;


        checkInDialog = new Dialog(this);
        checkOutDialog = new Dialog(this);
        checkOutPopup = new Dialog(this);

        //Display the date to ui
        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd /MM/ yyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        dateString = dateFormat.format(date);
        timeString = time.format(date);
        dateDisplay.setText(dateString);

        //Clock-in action
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockIn();
            }
        });

        //Clock-out action
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockOut();
            }
        });

//        clockInAndOutActivityViewModel = new ViewModelProvider(this).get(ClockInAndOutActivityViewModel.class);
//        clockInAndOutActivityViewModel.allClockOuts().observe(this, new Observer<List<SyncClockOut>>() {
//            @Override
//            public void onChanged(List<SyncClockOut> syncClockOuts) {
//                Toast.makeText(getApplicationContext(), "size is: " + String.valueOf(syncClockOuts.size()), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.enroll:
                Intent home = new Intent(ClockInAndOutActivity.this, EnrollmentActivity.class);
                startActivity(home);
                return true;
            case R.id.settings:
                //showHelp();
                return true;

            case R.id.testing:
                startActivity(new Intent(this, TestActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ClockIn(){
        checkInDialog.setContentView(R.layout.check_in_popup);
        close_clockIn = checkInDialog.findViewById(R.id.txclose);
        btnFingerprint_In = checkInDialog.findViewById(R.id.finger_in);
        btnStaffId_In = checkInDialog.findViewById(R.id.staffId_in);

        //Clock in with fingerprint
        btnFingerprint_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClockInAndOutActivity.this, FingerPrintActivity.class);
                intent.setAction(FingerPrintActivity.ACTION_CLOCK_IN);
                startActivityForResult(intent, CLOCK_IN_FINGER_PRINT_ACTIVITY_REQUEST_CODE);
                checkInDialog.dismiss();

            }
        });

        //Clock in with staff ID
        btnStaffId_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClockInAndOutActivity.this, ClockInWithEmployeeNumberActivity.class);
                startActivityForResult(i, START_CLOCK_IN_WITH_STAFF_ID_ACTIVITY_FOR_RESULT);
                checkInDialog.dismiss();
            }
        });

        //Close clock in dialog
        close_clockIn.setText("X");
        close_clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInDialog.dismiss();
            }
        });

        checkInDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkInDialog.show();
    }

    public void ClockOut(){
        checkOutDialog.setContentView(R.layout.check_out_popup);
        close_clockOut = checkOutDialog.findViewById(R.id.txclose);
        btnFingerprint_Out = checkOutDialog.findViewById(R.id.finger_out);
        btnStaffId_Out = checkOutDialog.findViewById(R.id.staffId_out);

        //Clock out with fingerprint
        btnFingerprint_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clock Out", "clocking out");
                Intent intent = new Intent(ClockInAndOutActivity.this, FingerPrintActivity.class);
                intent.setAction(FingerPrintActivity.ACTION_CLOCK_OUT);
                startActivityForResult(intent, CLOCK_OUT_FINGER_PRINT_ACTIVITY_REQUEST_CODE);
                checkOutDialog.dismiss();



            }
        });

        //Clock out with staff ID
        btnStaffId_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCheckOutPopup();
            }
        });

        //Close clock out dialog
        close_clockOut.setText("X");
        close_clockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutDialog.dismiss();
            }
        });

        checkOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOutDialog.show();
    }

    public void ShowCheckOutPopup() {

        checkOutPopup.setContentView(R.layout.checkoutpopup);

        close = checkOutPopup.findViewById(R.id.txclose);
        btnClockOut = checkOutPopup.findViewById(R.id.out);
        staff_Id = checkOutPopup.findViewById(R.id.staff_id);
        staff_comment = checkOutPopup.findViewById(R.id.comment);
        norm = checkOutPopup.findViewById(R.id.normal) ;
        oth = checkOutPopup.findViewById(R.id.other);

        norm.setChecked(true);
        staff_comment.setText("Normal end of day");
        staff_comment.setFocusableInTouchMode(false);

        oth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    staff_comment.setText("Specify reason");
                    staff_comment.setFocusableInTouchMode(true);
                    norm.setChecked(false);
                }

            }
        });
        norm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    staff_comment.setText("Normal end of day");
                    staff_comment.setFocusableInTouchMode(false);
                    oth.setChecked(false);
                }

            }
        });
        btnClockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staff_Id.getText().toString().equalsIgnoreCase("")){
                    staff_Id.setError("Id Missing!");
                }else{
                    new AlertDialog.Builder(ClockInAndOutActivity.this)
                            .setTitle("Confirmation")
                            .setMessage("Do you really want to clock out?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    clockOutWithStaffID();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        close.setText("X");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutPopup.dismiss();
            }
        });

        checkOutPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOutPopup.show();

    }

    //clock out functionality
    private void clockOutWithStaffID() {
        String staffID = staff_Id.getText().toString();
        String staffComment = staff_comment.getText().toString();
        SyncTeacher teacher = viewModel.clockOutTeacherWithEmployeeID(staffID, staffComment);
        //loadTeacherHomePage(teacher);
        if(teacher == null){
            new AlertDialog.Builder(ClockInAndOutActivity.this)
                    .setTitle("Confirmation")
                    .setMessage("The employee number is incorrect \n Try again... ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Alright", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            checkOutPopup.dismiss();
                        }})
                    .setNegativeButton("", null).show();
        }else {
            new AlertDialog.Builder(ClockInAndOutActivity.this)
                    .setTitle("Successfully clocked out")
                    .setMessage("=============================\n"+ "Name : " + teacher.getFirstName() + " " + teacher.getLastName()
                            + "\n\n" + "Date Out : " + dateString + "\n\n" + "Time Out : " + timeString)
                    .setIcon(R.drawable.success)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            checkOutPopup.dismiss();
                        }}).show();
                    //.setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null ) {
            if (requestCode == START_CLOCK_IN_WITH_STAFF_ID_ACTIVITY_FOR_RESULT ) {
                // we have the code
                if (resultCode == RESULT_OK ) {
                    String employeeNumber = data.getStringExtra(ClockInWithEmployeeNumberActivity.EMPLOYEE_NUMBER);
                    SyncTeacher syncTeacher = viewModel.clockInTeacherEmployeeNumber(Objects.requireNonNull(employeeNumber));
                    loadTeacherHomePage(syncTeacher);
                }
            } else if (requestCode == CLOCK_OUT_FINGER_PRINT_ACTIVITY_REQUEST_CODE) {
                // teacher clocking out
                if (resultCode == RESULT_OK ) {
                    String stringEncodedFingerPrint = data.getStringExtra(FingerPrintActivity.FINGER_PRINT_DATA);
                    String base64EncodedBitmapImage = data.getStringExtra(FingerPrintActivity.FINGER_PRINT_IMAGE);
                    SyncTeacher syncTeacher = viewModel.clockOutTeacherWithFingerPrint(stringEncodedFingerPrint, "No Comment");
                    loadTeacherHomePage(syncTeacher);
                }
            } else if (requestCode == CLOCK_IN_FINGER_PRINT_ACTIVITY_REQUEST_CODE) {
                // teacher clocking out
                if (resultCode == RESULT_OK ) {
                    String stringEncodedFingerPrint = data.getStringExtra(FingerPrintActivity.FINGER_PRINT_DATA);
                    String base64EncodedBitmapImage = data.getStringExtra(FingerPrintActivity.FINGER_PRINT_IMAGE);
                    SyncTeacher syncTeacher = viewModel.clockInTeacherWithFingerPrint(stringEncodedFingerPrint);
                    loadTeacherHomePage(syncTeacher);
                }
            }  // I don't know what has happened
        }
    }

    private void loadTeacherHomePage(SyncTeacher syncTeacher) {
        if (syncTeacher != null ) {
            /*
            * The teacher have successfully signed in, new we take him to the
            * teachers home page basing on the role*/
            if (syncTeacher.getRole().equals(Role.TEACHER_ROLE)) {
                Intent teacherHome = new Intent(this, TeacherHomeActivity.class);
                teacherHome.putExtra("employee_No", syncTeacher.getEmployeeNumber());
                teacherHome.putExtra("employee_Name", syncTeacher.getFirstName() + " " + syncTeacher.getLastName());
                startActivity(teacherHome);

            } else if (syncTeacher.getRole().equals(Role.HEAD_TEACHER_ROLE)) {
                Intent headTeacherHome = new Intent(this, AdminSideActivity.class);
                headTeacherHome.putExtra("employee_No", syncTeacher.getEmployeeNumber());
                headTeacherHome.putExtra("employee_Name",syncTeacher.getFirstName() + " " + syncTeacher.getLastName());
                // TODO: SCHOOL NUMBER MUST BE CHANGED below
                //headTeacherHome.putExtra(AdminSideActivity.SCHOOL_NUMBER, "354633111523205");
                startActivity(headTeacherHome);

            }else if (syncTeacher.getRole().equals(Role.SMC)) {
                Intent smcHome = new Intent(this, SmcActivity.class);
                smcHome.putExtra("employee_No", syncTeacher.getEmployeeNumber());
                smcHome.putExtra("employee_Name",syncTeacher.getFirstName() + " " + syncTeacher.getLastName());
                // TODO: SCHOOL NUMBER MUST BE CHANGED below

                startActivity(smcHome);
            }
        } else {
            Toast.makeText(this, "Invalid Employee Number or Finger Print", Toast.LENGTH_LONG).show();
        }
    }
}
