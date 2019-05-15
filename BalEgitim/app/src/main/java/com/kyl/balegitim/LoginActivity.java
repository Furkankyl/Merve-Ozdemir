package com.kyl.balegitim;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;

    private TextInputEditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            //
        }

        editTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);


        //Kayıt ol ekranından gelen email
        String email = getIntent().getStringExtra("email");
        if(email != null && !email.isEmpty())
            editTextEmail.setText(email);


        //Email girerken her karakterde mail doğrulaması işlemi  afterTextChanged metodu içerisinde
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = editTextEmail.getText().toString();

                TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);

                if (email.isEmpty()) {
                    textInputLayout.setError("Boş bırakılamaz!");
                    buttonSignIn.setEnabled(false);
                    buttonSignIn.getBackground().setAlpha(50);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    buttonSignIn.setEnabled(false);
                    buttonSignIn.getBackground().setAlpha(50);
                    textInputLayout.setError("Geçerli bir mail adresi girin!");
                } else {

                    buttonSignIn.setEnabled(true);
                    buttonSignIn.getBackground().setAlpha(255);

                    textInputLayout.setError("");
                }
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                signIn(email, password);
            }
        });
        TextView textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }
    private void goToMainActivity(){
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }
    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Hoş geldin: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
