package com.mandaditos.administrador;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity
{
	private static final String TAG = "LoginActivity";
	FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    EditText emailText;
	EditText passwordText;
	Button loginButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		mFirebaseAuth = FirebaseAuth.getInstance();

		loginButton = findViewById(R.id.btn_login);
		passwordText = findViewById(R.id.input_password);
		emailText = findViewById(R.id.input_email);




		mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
					String email = mFirebaseUser.getEmail().toString();
					int index = email.indexOf('@');
					email = email.substring(0, index);
					Log.wtf("User is",email.toString());
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, Home.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };


        loginButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					String email = emailText.getText().toString();
					String pwd = passwordText.getText().toString();
					if(email.isEmpty()){
						emailText.setError("Introduce tu usuario");
						emailText.requestFocus();
					}
					else  if(pwd.isEmpty()){
						passwordText.setError("Ingresa tu password");
						passwordText.requestFocus();
					}
					else  if(email.isEmpty() && pwd.isEmpty()){
						Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
					}
					else  if(!(email.isEmpty() && pwd.isEmpty())){
						mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if(!task.isSuccessful()){
										Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
									}
									else{
										Intent intToHome = new Intent(LoginActivity.this,Home.class);
										startActivity(intToHome);
									}
								}
							});
					}
					else{
						Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

					}

				}
			});

    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

	@Override
	protected void onStart()
	{
		super.onStart();
		mFirebaseAuth.addAuthStateListener(mAuthStateListener);
	}



}
