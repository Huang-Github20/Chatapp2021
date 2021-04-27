package com.example.chatalllimitted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends AppCompatActivity {

    private TextInputLayout inputEmail,inputpassword1,inputpassword2;
    Button btntaotk;
    TextView txtdacotaikhoan;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail=findViewById(R.id.input_Email);
        inputpassword1=findViewById(R.id.input_Password_1);
        inputpassword2=findViewById(R.id.input_Password_2);
        btntaotk=findViewById(R.id.btn_dangkitk);
        txtdacotaikhoan=findViewById(R.id.txt_dacotaikhoan);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar= new ProgressDialog(this);

        btntaotk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtempRegistartion();
            }
        });

        txtdacotaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerActivity.this,Login_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void AtempRegistartion() {
        String email = inputEmail.getEditText().getText().toString();
        String pass1 = inputpassword1.getEditText().getText().toString();
        String pass2 = inputpassword2.getEditText().getText().toString();

        if(email.isEmpty() || !email.contains("@gmail")){
            showError(inputEmail,"Tại sao em nhập Email không đúng !");
        }
        else if(pass1.isEmpty() || pass1.length()<6){
            showError(inputpassword1,"Password em cần phải nhập hơn 6 kí tự !");
        }
        else if(!pass2.equals(pass1)){
            showError(inputpassword2,"Mật khẩu chưa khớp đâu em ơi ! nhập lại hộ anh :)");
        }
        else{
            mLoadingBar.setTitle("Đăng kí");
            mLoadingBar.setMessage("Vui lòng đợi nhé em yêu <3 ");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        mLoadingBar.dismiss();
                        Toast.makeText(registerActivity.this, "Chúc mừng em đã đăng kí thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(registerActivity.this,setupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(registerActivity.this, "Xin lỗi em ! em đã đăng kí thất bại @_@ ", Toast.LENGTH_SHORT).show();
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