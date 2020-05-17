package com.mandaditos.administrador;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.mandaditos.administrador.models.*;

public class CreateAccount extends Activity
{

	EditText nameEd;
	EditText emailEd;
	EditText passwordEd;
	Button signupButt;
	FirebaseAuth mFirebaseAuth;
	DatabaseReference mDataBase;

	private EditText direccionEd;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_account);
		
		mFirebaseAuth = FirebaseAuth.getInstance();
		mDataBase = FirebaseDatabase.getInstance().getReference();


		nameEd = findViewById(R.id.input_name);
		emailEd = findViewById(R.id.input_email);
		direccionEd = findViewById(R.id.DireccioncreateaccountEditText1);
		passwordEd = findViewById(R.id.input_password);
		signupButt = findViewById(R.id.btn_signup);

        signupButt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final String email = emailEd.getText().toString();
					String pwd = passwordEd.getText().toString();
					if(email.isEmpty()){
						emailEd.setError("Please enter email id");
						emailEd.requestFocus();
					}
					String direccion = direccionEd.getText().toString();
					if(direccion.isEmpty()){
						emailEd.setError("Please enter address");
						emailEd.requestFocus();
					}
					else  if(pwd.isEmpty()){
						passwordEd.setError("Please enter your password");
						passwordEd.requestFocus();
					}
					else  if(email.isEmpty() && pwd.isEmpty()){
						Toast.makeText(CreateAccount.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
					}
					else  if(!(email.isEmpty() && pwd.isEmpty())){
						mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if(!task.isSuccessful()){
										Toast.makeText(CreateAccount.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
									}
									else {



										FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
										String userId = mFirebaseUser.getUid().toString();
										mUser user = new mUser();
										user.setNombre(nameEd.getText().toString());
										user.setmUserId(mFirebaseUser.getUid().toString());
										user.setAddress(direccionEd.getText().toString());
										mDataBase.child("Usuarios/" + userId + "/Perfil").setValue(user);
									}
								}
							});
					}
					else{
						Toast.makeText(CreateAccount.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

					}
				}
			});

    }
	
}
