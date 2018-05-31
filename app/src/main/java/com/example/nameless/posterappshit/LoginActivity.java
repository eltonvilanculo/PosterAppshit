package com.example.nameless.posterappshit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText  email, password;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference contactReference;
    private FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    TextInputLayout inputEmail,inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        contactReference = firebaseDatabase.getReference(BDCaminhos.USER);

// input

        inputEmail = (TextInputLayout) findViewById(R.id.inputMail);
        inputPassword =(TextInputLayout) findViewById(R.id.inputPassword);


        password = findViewById(R.id.password_login_input);

        email = findViewById(R.id.email_login_input);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if firebaseUser is signed in (non-null) and update UI accordingly.
        firebaseUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void login(View view) {

        if (validacaoLogin()==true) {

            loginUser(email.getEditableText().toString(), password.getEditableText().toString());
        }
    }

 /*   public void signUp(View view) {

        if (password.getEditableText().toString().trim().equals(password.getEditableText().toString())) {
            createUser(email.getEditableText().toString().trim(),password.getEditableText().toString().trim(), username.getEditableText().toString().trim());
        }


    }*/

    public void showSignUp(View view) {
      Intent chamarRegistar = new Intent(LoginActivity.this,RegistarUsuario.class);
        startActivity(chamarRegistar);
    }



    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in firebaseUser's information
                            Log.d(TAG, "signInWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                           goToCOntactList();
                        } else {
                            // If sign in fails, display a message to the firebaseUser.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            System.out.println("email = " + email + "password = " + password);
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void goToCOntactList() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("NAME", firebaseUser.getDisplayName());
        intent.putExtra("EMAIL", firebaseUser.getEmail());
        startActivity(intent);
    }


   public boolean validacaoLogin () {

        boolean isValid = true;

/*        if (email.getText().toString().isEmpty()) {
            inputEmail.setError("O campo do email nao pode estar vazio ");
            isValid = false;
        } else {
            // o erro nao sera activado se preencher o editNome
            inputEmail.setErrorEnabled(false);
        }
        if (password.getText().toString().trim().length() < 8) {
            inputEmail.setError("Passoword deve ter no minimo 8 caracteres  ");
            isValid = false;
        } else {
            // o erro nao sera activado se preencher o editEmail
            inputEmail.setErrorEnabled(false);
        }


        if (isValid == true) {

            return true;
        }else{
       return false;
        }*/
return true;
   }

    public void loginAnonimo(View view) {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                firebaseUser = mAuth.getCurrentUser();
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
