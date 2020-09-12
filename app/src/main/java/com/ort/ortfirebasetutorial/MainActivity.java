package com.ort.ortfirebasetutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ort.ortfirebasetutorial.Entities.Notas;

public class MainActivity extends AppCompatActivity {
TextView name_editText;
TextView subject_editText;
TextView mark_editText;
TextView list_textView;
Button save_button;
private static final String TAG = "MainActivity";
private FirebaseDatabase mDatabase;
private DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setting the AppTheme to display the activity
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_editText = findViewById(R.id.name_editText);
        subject_editText = findViewById(R.id.subject_editText);
        mark_editText = findViewById(R.id.mark_editText);
        save_button = findViewById(R.id.save_button);
        list_textView = findViewById(R.id.list_textView);

        findViewById(R.id.constraintLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarNotas();
            }
        });
    }

    public void registrarNotas(){
        String nombre = name_editText.getText().toString();
        String subject = subject_editText.getText().toString();
        String mark = mark_editText.getText().toString();
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference().child("Notas");

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(subject) && !TextUtils.isEmpty(mark) ){
            Notas nota = new Notas (nombre,subject,mark);
            mDbRef.push().setValue(nota);
            Toast.makeText(getApplicationContext(),"Se guardo en la base de firebase",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Debe completar todos los campos",Toast.LENGTH_LONG).show();
        }
    }
}