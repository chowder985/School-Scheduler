package com.michael.android.schoolscheduler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class SubjectDetailActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText subjectEditName, tName, tLocation, tEmail, tPhone;
    String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        subjectEditName = (EditText)findViewById(R.id.detail_subject_edit);
        tName = (EditText)findViewById(R.id.detail_t_edit);
        tLocation = (EditText)findViewById(R.id.detail_location_edit);
        tEmail = (EditText)findViewById(R.id.detail_email_edit);
        tPhone = (EditText)findViewById(R.id.detail_phone_edit);

        Intent intent = getIntent();
        subjectName = intent.getStringExtra("subject_name");

        getSupportActionBar().setTitle(subjectName+" 과목의 세부사항");

        DBHelper helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        Cursor c1 = db.rawQuery("select subject, name, location, email, number from tb_data where subject = '"+subjectName+"'", null);
        c1.moveToNext();
        String subject = c1.getString(0);
        String name = c1.getString(1);
        String location = c1.getString(2);
        String email = c1.getString(3);
        String number = c1.getString(4);

        subjectEditName.setText(subject);
        tName.setText(name);
        tLocation.setText(location);
        tEmail.setText(email);
        tPhone.setText(number);
    }

    public void saveToDB(View view){
        db.execSQL("update tb_data set subject = '"+subjectEditName.getText().toString()+"', name = '"+tName.getText().toString()+"', location = '"+tLocation.getText().toString()+"', email = '"+tEmail.getText().toString()+"', number = '"+tPhone.getText().toString()+"' where subject = '"+subjectName+"';");
        Intent intent = new Intent();
        intent.putExtra("changed_subject", subjectEditName.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
