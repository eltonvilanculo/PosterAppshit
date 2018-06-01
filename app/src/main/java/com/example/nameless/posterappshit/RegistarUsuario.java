package com.example.nameless.posterappshit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistarUsuario extends AppCompatActivity {

    private static final String TAG = RegistarUsuario.class.getSimpleName();
    EditText editNomeUsuario,editEmail,editPassword, editConfirmPassword;
    TextInputLayout inputNomeUsuario,inputMail,inputPassword,inputConfirPassword;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference contactReference;
    private FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_usuario);

        editNomeUsuario = (EditText) findViewById(R.id.editNomeUsuario);
        editEmail = (EditText) findViewById(R.id.email_register_input);
        editPassword = (EditText) findViewById(R.id.password_register_input);

        editConfirmPassword = (EditText) findViewById(R.id.password2_register_input);

        // inputs

        inputNomeUsuario = (TextInputLayout) findViewById(R.id.inputNomeUsuario);
        inputMail = (TextInputLayout) findViewById(R.id.inputEmail);
        inputPassword = (TextInputLayout) findViewById(R.id.inputSenha);
        inputConfirPassword = (TextInputLayout) findViewById(R.id.inputConfirSenha);

        contactReference = firebaseDatabase.getReference(BDCaminhos.USER);

    }

    public void signUp(View view) {
// Continuar daqui , algo nao ta nice no metodo
      if (validacaoLogin()==true){
         //Toast.makeText(RegistarUsuario.this, "Autenticado", Toast.LENGTH_SHORT).show();
          createUser(editEmail.getText().toString(), editPassword.getText().toString(), editNomeUsuario.getText().toString());
      }


               // gestorPreference.addPreference(editEmail.getText().toString(), editPassword.getText().toString());
         //

        }






    private void createUser(final String email , final String password, final String username) {
        System.out.println("email = [" + email + "], username = [" + username + "], password = [" + password + "]");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistarUsuario.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in firebaseUser's information
                            Log.d(TAG, "createUserWithEmail:success");





                            loginUser(email,password);
                            User user = new User(username, email, firebaseUser.getUid());
                            addUser(user);

                            if (firebaseUser !=null) {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                firebaseUser.updateProfile(userProfileChangeRequest);
                            }

                        } else {
                            // If sign in fails, display a message to the firebaseUser.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistarUsuario.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistarUsuario.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in firebaseUser's information
                            Log.d(TAG, "signInWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();
                            Toast.makeText(RegistarUsuario.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            goToCOntactList();
                        } else {
                            // If sign in fails, display a message to the firebaseUser.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegistarUsuario.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void goToCOntactList() {
        Intent intent = new Intent(RegistarUsuario.this, HomeActivity.class);
        intent.putExtra("NAME", firebaseUser.getDisplayName());
        intent.putExtra("EMAIL", firebaseUser.getEmail());
        startActivity(intent);
    }

    //Coloca o usuario na base de dados
    public void addUser(User user) {
        System.out.println(user);
        Task<Void> task = contactReference.child(user.getUsername()).setValue(user);
        if (!task.isSuccessful())
            Toast.makeText(RegistarUsuario.this, "Not Possible to create the User. try again later", Toast.LENGTH_SHORT).show();

    }

    public void voltarLogin(View view) {

        Intent chamarLogin = new Intent(RegistarUsuario.this,LoginActivity.class);
        startActivity(chamarLogin);

    }

    public boolean validacaoLogin () {

        boolean isValid = true;

        if (editNomeUsuario.getText().toString().isEmpty()) {
            inputNomeUsuario.setError(getString(R.string.validacaoNome));
            isValid = false;
        } else {
            // o erro nao sera activado se preencher o editNome
            inputNomeUsuario.setErrorEnabled(false);
        }
        if (editEmail.getText().toString().isEmpty()) {
            inputMail.setError(getString(R.string.validacaoEmail));
            isValid = false;
        } else {
            // o erro nao sera activado se preencher o editEmail
            inputMail.setErrorEnabled(false);
        }
        if (editPassword.getText().toString().trim().length() < 8) {
            inputPassword.setError(getString(R.string.validacaoPassword));
            isValid = false;
        } else {
            // o erro nao sera activado se preencher o editEmail
            inputPassword.setErrorEnabled(false);
        }
        if (!editConfirmPassword.getText().toString().equalsIgnoreCase(editPassword.getText().toString())) {
            inputConfirPassword.setError(getString(R.string.validacaoConfirmar));
            isValid = false;
        } else {
            // o erro nao sera activado se preencher o editEmail
            inputConfirPassword.setErrorEnabled(false);
        }




        /**if (isValid == true) {

            return true;
        }*/

        return isValid;
    }
}
