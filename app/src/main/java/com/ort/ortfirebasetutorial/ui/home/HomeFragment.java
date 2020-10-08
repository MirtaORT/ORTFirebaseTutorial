package com.ort.ortfirebasetutorial.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ort.ortfirebasetutorial.Entities.Notas;
import com.ort.ortfirebasetutorial.R;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView name_editText;
    private TextView subject_editText;
    private TextView mark_editText;
    private TextView list_textView;
    private Button save_button;
    private static final String TAG = "HomeFragment";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        name_editText = root.findViewById(R.id.name_editText);
        subject_editText = root.findViewById(R.id.subject_editText);
        mark_editText = root.findViewById(R.id.mark_editText);
        save_button = root.findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarNotas();
            }
        });

        root.findViewById(R.id.constraintFragmentLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        return root;
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
            Toast.makeText(getContext(),"Se guardo en la base de firebase",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"Debe completar todos los campos",Toast.LENGTH_LONG).show();
        }
    }
}