package com.example.littlelingo.ui.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.littlelingo.R;
import com.example.littlelingo.ui.learningvocabulary.Word;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class adminAddWord extends AppCompatActivity {

    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_AUDIO = 2;
    private EditText editTextWordName,editTextExampleSentence,editTextWordType;
    private ImageView imageViewPhoto;
   private TextView textViewUploadAudio;
    private Button buttonAddWord;

    private Uri photoUri;
    private  Uri audioUri;
    DataSnapshot dataSnapshot;


    private DatabaseReference mDatabase;
    private StorageReference storageReference;


    @SuppressLint("MissingInflatedId")

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_word);

        editTextWordName = findViewById(R.id.et_wordname);
        editTextWordType = findViewById(R.id.et_wordType);
        editTextExampleSentence = findViewById(R.id.et_exampleSentence);
        imageViewPhoto = findViewById(R.id.iv_image);
        textViewUploadAudio = findViewById(R.id.tv_uploadAudio);
        buttonAddWord = findViewById(R.id.btn_addWord);

        mDatabase = FirebaseDatabase.getInstance().getReference("words");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        buttonAddWord.setOnClickListener(view -> {
            // First, let the user pick a photo
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PHOTO);
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK && data != null) {
        Uri selectedUri = data.getData();
        if (requestCode == REQUEST_CODE_PHOTO) {
            photoUri = selectedUri;


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                imageViewPhoto.setImageBitmap(bitmap);

                // Now let the user pick an audio file
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_AUDIO);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == REQUEST_CODE_AUDIO) {
            audioUri = selectedUri;
            textViewUploadAudio.setText(selectedUri.getPath());
            
            uploadWordToFirebase();
        }
    }
}

    private void uploadWordToFirebase() {
        String wordName = editTextWordName.getText().toString().trim();
        String exampleSentence = editTextExampleSentence.getText().toString().trim();
        String wordType = editTextWordType.getText().toString().trim();
        String image = photoUri.toString();
        String audio = audioUri.toString();


        // Check if the user has entered all required fields
        if (wordName.isEmpty() || exampleSentence.isEmpty() || wordType.isEmpty() || image.isEmpty() || audio.isEmpty()) {
            // Show an error message to the user
            return;
        }

        if (wordName.isEmpty() || photoUri == null || audioUri == null) {
            // Show an error message to the user
            return;
        }

        StorageReference photoRef = storageReference.child("photos/" + wordName + ".jpg");
        StorageReference audioRef = storageReference.child("audio/" + wordName + ".mp3");

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoData = baos.toByteArray();

            long maxWordId = 0;
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String wordIdStr = snapshot.child("wordId").getValue(String.class);
                if (wordIdStr != null) {
                    long wordId = Long.parseLong(wordIdStr);
                    if (wordId > maxWordId) {
                        maxWordId = wordId;
                    }
                }
            }
            String newWordId = (maxWordId + 1) + "";
            UploadTask photoUploadTask = photoRef.putBytes(photoData);
            photoUploadTask.addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(photoDownloadUrl -> {
                UploadTask audioUploadTask = audioRef.putFile(audioUri);
                audioUploadTask.addOnSuccessListener(taskSnapshot1 -> audioRef.getDownloadUrl().addOnSuccessListener(audioDownloadUrl -> {
                    Word word = new Word(newWordId,wordName, exampleSentence,wordType,image.toString(), audio.toString(),"","","");
                    mDatabase.child(wordName).setValue(word);
                }));
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}



