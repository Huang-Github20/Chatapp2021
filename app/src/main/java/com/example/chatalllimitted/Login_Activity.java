package com.example.chatalllimitted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

public class Login_Activity extends AppCompatActivity {

    private TextInputLayout inputEmail,inputPassword;
    Button btnLogin;
    TextView txtquenmk,txtdangkitk;
    ProgressDialog mLoadingBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        inputEmail = findViewById(R.id.input_Email_login);
        inputPassword = findViewById(R.id.input_Password);
        btnLogin = findViewById(R.id.btn_login);
        txtquenmk = findViewById(R.id.txt_quenmk);
        txtdangkitk = findViewById(R.id.txt_dangkitaikhoan);
        mLoadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        txtdangkitk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,registerActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Atamplogin();
            }
        });


    }

    private void Atamplogin() {
        String email = inputEmail.getEditText().getText().toString();
        String pass = inputPassword.getEditText().getText().toString();

        if(email.isEmpty() || !email.contains("@gmail")){
            showError(inputEmail,"Tại sao em nhập Email không đúng !");
        }
        else if(pass.isEmpty() || pass.length()<6){
            showError(inputPassword,"Password em cần phải nhập hơn 6 kí tự !");
        }

        else{
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Vui lòng đợi nhé em yêu <3 ");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       mLoadingBar.dismiss();
                       Toast.makeText(Login_Activity.this, "Ô zia ! em đã đăng nhập thành công", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(Login_Activity.this,setupActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                       finish();
                   }
                   else {
                       mLoadingBar.dismiss();
                       Toast.makeText(Login_Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }

    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();


    }

}