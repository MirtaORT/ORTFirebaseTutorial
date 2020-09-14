package com.ort.ortfirebasetutorial.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ort.ortfirebasetutorial.R;

public class SignActivity extends AppCompatActivity {
    private static final String TAG = "AndroidClarified";
    private static final int RC_SIGN_IN = 101 ;
    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        mAuth = FirebaseAuth.getInstance();
       // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validar si tengo internet aca tener un try por si no hay...
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                       // Log.d(TAG, "SignInWithGoogle:" + account.getId());
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                        //onLoggedIn(account);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(MenuActivity.GOOGLE_ACCOUNT, googleSignInAccount);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI();
    }

    private void updateUI() {
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }


    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           onLoggedIn(account);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }


}

