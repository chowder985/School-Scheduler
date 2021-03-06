package com.michael.android.schoolscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    SQLiteDatabase db;
    ListView subjectList;
    ArrayList<String> subjects = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        DBHelper helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        subjectList = (ListView)findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, subjects);
        subjectList.setAdapter(adapter);

        Cursor c1 = db.rawQuery("select subject from tb_data", null);
        subjects.clear();
        for(int k=0; k<c1.getCount(); k++){
            c1.moveToNext();
            String subject = c1.getString(0);
            subjects.add(subject);
        }
        adapter.notifyDataSetChanged();

        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                intent.putExtra("subject_name", subjects.get(position));
                startActivity(intent);
            }
        });

        subjectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("과목 삭제");
                builder.setMessage(subjects.get(position)+" 과목을 삭제하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] selectionArgs = {subjects.get(position)};
                        subjects.remove(position);
                        db.delete("tb_data", "subject LIKE ?", selectionArgs);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("아니요", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.button_items, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.set_schedule:

                return true;
            case R.id.donate:
                Toast.makeText(this, "1만원 입금 되셨습니다. 감사합니다.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.add_subject:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_subject, null);

                builder.setView(view)
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText subjectName = (EditText)view.findViewById(R.id.subject_name);
                                EditText teacherName = (EditText)view.findViewById(R.id.subject_t);
                                EditText teacherLocation = (EditText)view.findViewById(R.id.t_location);
                                EditText teacherEmail = (EditText)view.findViewById(R.id.t_email);
                                EditText teacherPhone = (EditText)view.findViewById(R.id.t_phone);

                                db.execSQL("insert into tb_data (subject, name, location, email, number) values ('"+
                                        subjectName.getText().toString() +"', '"+
                                        teacherName.getText().toString() +"', '"+
                                        teacherLocation.getText().toString()+"', '"+
                                        teacherEmail.getText().toString()+"', '"+
                                        teacherPhone.getText().toString()+"');");

                                Cursor c1 = db.rawQuery("select subject from tb_data", null);
                                subjects.clear();
                                for(int k=0; k<c1.getCount(); k++){
                                    c1.moveToNext();
                                    String subject = c1.getString(0);
                                    subjects.add(subject);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("취소", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.add_picture:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor c1 = db.rawQuery("select subject from tb_data", null);
        subjects.clear();
        for(int k=0; k<c1.getCount(); k++){
            c1.moveToNext();
            String subject = c1.getString(0);
            subjects.add(subject);
        }
        adapter.notifyDataSetChanged();
    }
}
