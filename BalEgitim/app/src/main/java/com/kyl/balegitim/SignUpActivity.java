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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;

    private TextInputEditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            //
        }

        editTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

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
                    buttonSignUp.setEnabled(false);
                    buttonSignUp.getBackground().setAlpha(50);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    buttonSignUp.setEnabled(false);
                    buttonSignUp.getBackground().setAlpha(50);
                    textInputLayout.setError("Geçerli bir mail adresi girin!");
                } else {

                    buttonSignUp.setEnabled(true);
                    buttonSignUp.getBackground().setAlpha(255);

                    textInputLayout.setError("");
                }
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                signUp(email, password);
            }
        });
        TextView textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn(String... email) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        if (email != null)
            intent.putExtra("email", email);
        startActivity(intent);
    }

    private void signUp(final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    
                    intent.putExtra("email", email);
                    startActivity(intent);
                    Toast.makeText(SignUpActivity.this, "Başarıyla kayıt oldun " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
