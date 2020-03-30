package com.planetsystems.tela.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.planetsystems.tela.R;

import java.util.List;
import java.util.Locale;

public class ClockIn_with_StaffId extends Activity {

    ProgressDialog dialog;
    CardView btnFollow;
    EditText staffid;
    RadioButton staff,admin,Smc;

    LocationManager locationManager;
    double longitude;
    double latitude;

    int Employee_ID;
    String Name;
    String Role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockin_with__staff_id);

        btnFollow = findViewById(R.id.google);
        staffid = findViewById(R.id.staffID);

        //Button action
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(staffid.getText().toString().equalsIgnoreCase("")){
                    staffid.setError("Id Missing!");
                }else{

                    String employeeNumber = staffid.getText().toString();

                    //String API = Constants.ServiceType.STAFF_CLOCK_IN;

                    if (!isConnected()) {
                        Toast.makeText(ClockIn_with_StaffId.this, "No internet connection",Toast.LENGTH_LONG).show();

                    } else {
                        //new GET_EMPLOYEE_INFO().execute(API + employeeNumber + "/" + latitude + "/" + longitude);
                        if (staffid.getText().equals("2001")){

                        }else if (staffid.getText().equals("3001")){

                        }else if (staffid.getText().equals("5001")){

                        }
                    }

                }
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        // getLocation();
    }

    ///Check Internet connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}

