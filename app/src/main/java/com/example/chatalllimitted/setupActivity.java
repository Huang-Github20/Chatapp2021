package com.example.chatalllimitted;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class setupActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    CircleImageView profileimage;
    EditText edtusername,edtusercity,edtusercountry,edtuserprofession;
    Button btnsave;
    Uri imageuri;



    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    StorageReference storageRef;


    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        profileimage = findViewById(R.id.profile_image);
        edtusername = findViewById(R.id.edt_username);
        edtusercity = findViewById(R.id.edt_userCity);
        edtusercountry = findViewById(R.id.edt_userCountry);
        edtuserprofession = findViewById(R.id.edt_userProfession);
        btnsave = findViewById(R.id.btn_save);
        mLoadingBar=new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
    }

    private void SaveData() {
        String username = edtusername.getText().toString();
        String usercity = edtusercity.getText().toString();
        String usercountry = edtusercountry.getText().toString();
        String userprofession = edtuserprofession.getText().toString();


        if(username.isEmpty() || username.length()<3){
            showError(edtusername,"Bé nhập tên không hợp lệ rồi !");
        }else if(usercity.isEmpty() || usercity.length()<3){
            showError(edtusercity,"Bé nhập thành phố không hợp lệ rồi !");
        }else if(usercountry.isEmpty() || usercountry.length()<3){
            showError(edtusercountry,"Bé nhập quê hương không hợp lệ rồi !");
        }else if(userprofession.isEmpty() || userprofession.length()<3){
            showError(edtuserprofession,"Bé nhập công việc không hợp lệ rồi !");
        }
        else if(imageuri == null){
            Toast.makeText(this, "Xin bé hãy chọn ảnh ngay !", Toast.LENGTH_SHORT).show();
        }
        else {

            mLoadingBar.setTitle("adding setup Profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            storageRef.child(mUser.getUid()).putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("username",username);
                                hashMap.put("usercity",usercity);
                                hashMap.put("usercountry",usercountry);
                                hashMap.put("userprofession",userprofession);
                                hashMap.put("profileImage",uri.toString());
                                hashMap.put("status","offline");


                                mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Intent intent = new Intent(setupActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        mLoadingBar.dismiss();
                                        Toast.makeText(setupActivity.this, "Cập nhật thông tin thành công rồi em nhé !", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        mLoadingBar.dismiss();
                                        Toast.makeText(setupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }

    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null)
        {
            imageuri = data.getData();
            profileimage.setImageURI(imageuri);
        }
    }
}