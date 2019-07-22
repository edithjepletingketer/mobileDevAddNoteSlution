package com.example.solution;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.solution.database.DatabaseHelper;
import com.example.solution.database.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AddNoteActivitty extends AppCompatActivity {
    private EditText etTitle;
    private EditText etNote;
    private Button btnAddPhoto;
    private Button btnAddVoiceNote;
    private Button btnSave;
    private String noteText;
    private String title;
    private ImageView imgView;
    private String NOTES_API_URL ="https://akirachixnotesapi.herokuapp.com/api/v1/notes";
    private String TAG="NOTES_API_RESPONSE";

    private static final int CAPTURE_IMAGE_REQUEST_CODE=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_activitty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNote);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnAddVoiceNote = findViewById(R.id.btnAddVoiceNote);
        btnSave = findViewById(R.id.btnSave);
        imgView =findViewById(R.id.imgView);


        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAPTURE_IMAGE_REQUEST_CODE);
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = etTitle.getText().toString();
                noteText = etNote.getText().toString();
//                Note note = new Note(title,noteText);
//                DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext(),"notes",null,1);
//                long rows = databaseHelper.addNote(note);
//                Log.d("AddNote","the number of notes is "+rows);
//                finish();
                postNote(title,noteText);
            }
        });





    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==CAPTURE_IMAGE_REQUEST_CODE&& requestCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            Bitmap bitmap=(Bitmap)bundle.get("data");
            imgView.setImageBitmap(bitmap);
            Log.d("name",bitmap.toString());
        }
    }
 private void postNote(final String title, final String noteText){
     StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTES_API_URL, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
          Log.d(TAG,response);
             try {
                 JSONObject jsonObject = new JSONObject(response);
                 int id = jsonObject.getInt("id");
                 String title = jsonObject.getString("title");
                 String noteText= jsonObject.getString("noteText");
             } catch (JSONException e) {
                 e.printStackTrace();

             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             Log.e(TAG,error.getMessage());
         }
     }){
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String,String>params=new HashMap<String,String>();
             params.put("title",title);
             params.put("noteText",noteText);
             return params;
         }
     };
     RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
     requestQueue.add(stringRequest);
 }
     private void getNote(final String title, final String noteText){


     }


}
