package com.example.rustybucket.pickup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private EditText description;
    private String uuid;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");

        description = findViewById(R.id.descEntry);

        toolbar = findViewById(R.id.ab);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cancelEdit) {
            this.finish();
            return true;
        }
        if (id == R.id.confirmEdit) {
            boolean descriptionEmptyFlag = description.getText().toString().isEmpty();
            if (descriptionEmptyFlag) {
                Toast toast=Toast.makeText(getApplicationContext(),"Empty description",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {

                //set values
                this.finish();

                editProfile();

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                return true;
            }
        }
        return true;
    }

    public void editProfile(){
        DataManager dataManager = new DataManager();

        dataManager.editProfile(uuid, description.getText().toString());
    }
}
