package com.example.rustybucket.pickup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    EditText activityName;
    EditText description;
    EditText location;
    EditText dateEditText;
    EditText timeEditText;

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_createpost);

        activityName = findViewById(R.id.activityName);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        final Calendar myCalendar = Calendar.getInstance();

        //Date picker
        dateEditText = (EditText) findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener datePick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateEditText.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreatePostActivity.this, datePick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Time picker
        timeEditText = (EditText) findViewById(R.id.time);
        final TimePickerDialog.OnTimeSetListener timePick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                final String timePostfix = selectedHour < 12 ? "AM" : "PM";
                String hr = selectedHour % 12 == 0 ? "12" : Integer.toString(selectedHour % 12);

                String min = selectedMinute < 10 ? "0" + selectedMinute : Integer.toString(selectedMinute);
                timeEditText.setText(hr + ":" + min + " " + timePostfix);
            }
        };

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(CreatePostActivity.this, timePick, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),
                        false).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.createpost_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cancelNewPost) {
            this.finish();
            return true;
        }
        if (id == R.id.confirmNewPost) {
            boolean descriptionEmptyFlag = description.getText().toString().isEmpty();
            boolean activityNameEmptyFlag = activityName.getText().toString().isEmpty();
            boolean dateEmptyFlag = dateEditText.getText().toString().isEmpty();
            boolean timeEmptyFlag = timeEditText.getText().toString().isEmpty();
            if (descriptionEmptyFlag || activityNameEmptyFlag || dateEmptyFlag || timeEmptyFlag) {
                Toast toast=Toast.makeText(getApplicationContext(),"All fields besides location are required",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                this.finish();

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DataManager dataManager = new DataManager();
                DateFormat df = new SimpleDateFormat("M/d/yy h:m a");
                final Date notificationDate;
                String dateAndTime = dateEditText.getText().toString() + " " + timeEditText.getText().toString();
                try {
                    notificationDate = df.parse(dateAndTime);
                    dataManager.getUser(firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            LinkedList<String> tags = new LinkedList<>(Arrays.asList("Sport", "Fun"));

                            List<String> zipCodes = user.getZipCodes();

                            Notification notification = new Notification(user, description.getText().toString(),
                                    notificationDate, tags, activityName.getText().toString(), zipCodes);
                            for (String zip : zipCodes) {
                                dataManager.addNotification(zip, notification);
                            }
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }
}
