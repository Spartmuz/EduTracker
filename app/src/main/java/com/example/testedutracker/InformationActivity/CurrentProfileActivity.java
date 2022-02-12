package com.example.testedutracker.InformationActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.testedutracker.Model.User;
import com.example.testedutracker.R;
import com.example.testedutracker.databinding.CurrentProfileActivityBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CurrentProfileActivity extends AppCompatActivity {
    CurrentProfileActivityBinding binding;
    DatabaseReference reference;
    FirebaseUser fuser;
    StorageReference storageReference;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private String state;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CurrentProfileActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {

            storageReference = FirebaseStorage.getInstance().getReference("uploads");
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        user = dataSnapshot.getValue(User.class);

                        binding.nameProfile.setText(user.getName());
                        binding.phoneProfile.setText(user.getPhone());
                        binding.mailProfile.setText(user.getUsername());
                        state = user.getState();

                        if (user.getImageURL().equals("default")) {
                            binding.profileImage.setImageResource(R.drawable.ic_default);
                        }
                        else {
                            Glide.with(getApplicationContext()).load(user.getImageURL()).into(binding.profileImage);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            binding.profileImage.setOnClickListener(view -> openImage());

        }
    }

    private void openImage() {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){

        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                if (!task.isSuccessful()){
                    throw  task.getException();
                }

                return  fileReference.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("imageURL", "" + mUri);
                    reference.updateChildren(map);

                    if (state.equals("parent")) {
                        reference = FirebaseDatabase.getInstance().getReference("Students").child(user.getGrade()).child(fuser.getUid());
                        reference.updateChildren(map);
                    } else if (state.equals("teacher")) {
                        reference = FirebaseDatabase.getInstance().getReference("Teachers").child(user.getSubject()).child(fuser.getUid());
                        reference.updateChildren(map);
                    }
                } else {
                    Toast.makeText(CurrentProfileActivity.this.getApplicationContext(), "Ձախողվեց!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(CurrentProfileActivity.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getApplicationContext(), "Պատկեր ընտրված չէ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(), "Վերբեռնումն ընթացքի մեջ է", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

}