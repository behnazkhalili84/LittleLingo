package com.example.littlelingo.ui.admin;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.example.littlelingo.R;
import com.example.littlelingo.ui.learningvocabulary.Word;
import com.example.littlelingo.ui.user.AuthViewModel;
import com.example.littlelingo.ui.user.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import androidx.core.view.WindowInsetsCompat;


public class adminAddWord extends AppCompatActivity {

    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_AUDIO = 2;
    private EditText editTextWordName,editTextExampleSentence,editTextWordType;
    private ImageView imageViewPhoto;
   private TextView textViewUploadAudio, tvAddImage;;
    private Button buttonAddWord;

  //  private Uri photoUri;
    private  Uri audioUri;
    //DataSnapshot dataSnapshot;


    private DatabaseReference mDatabase;
    private AuthViewModel authViewModel;
    private StorageReference storageReference;

    private Uri imageUri;

    private final ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri != null) {
                imageUri = uri;
                imageViewPhoto.setImageURI(uri);
                tvAddImage.setVisibility(View.GONE); // Hide the "Add Image" text
            }
        }
    });

    private final ActivityResultLauncher<Void> takePicture = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap bitmap) {
            if (bitmap != null) {
                imageViewPhoto.setImageBitmap(bitmap);
                imageUri = getImageUriFromBitmap(bitmap);
                tvAddImage.setVisibility(View.GONE); // Hide the "Add Image" text
            }
        }
    });

    private final ActivityResultLauncher<String> getAudioContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri != null) {
                audioUri = uri;
                textViewUploadAudio.setText(uri.getPath());
            }
        }
    });



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
        tvAddImage = findViewById(R.id.tv_add_image);
        buttonAddWord = findViewById(R.id.btn_addWord);

        mDatabase = FirebaseDatabase.getInstance().getReference("word");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choosing options to either take a photo or select from gallery
                String[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(adminAddWord.this);
                builder.setTitle("Edit Profile Picture");

                builder.setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            takePicture.launch(null); // Take photo
                            break;
                        case 1:
                            getContent.launch("image/*"); // Choose from gallery
                            break;
                    }
                });
                builder.show();
            }
        });

        textViewUploadAudio.setOnClickListener(v -> getAudioContent.launch("audio/*"));

        buttonAddWord.setOnClickListener(view -> {
            // Check if the user has entered all required fields
            String wordName = editTextWordName.getText().toString().trim();
            String exampleSentence = editTextExampleSentence.getText().toString().trim();
            String wordType = editTextWordType.getText().toString().trim();

            Log.d(TAG, "Word Name: " + wordName);
            Log.d(TAG, "Example Sentence: " + exampleSentence);
            Log.d(TAG, "Word Type: " + wordType);
            Log.d(TAG, "Image URI: " + imageUri);
            Log.d(TAG, "Audio URI: " + audioUri);

            if (wordName.isEmpty() || exampleSentence.isEmpty() || wordType.isEmpty() || imageUri == null || audioUri == null) {
                Toast.makeText(this, "Please fill all fields and add both image and audio.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveWordToFirebase(wordName, exampleSentence, wordType);
        });


//        buttonAddWord.setOnClickListener(view -> {
//            // First, let the user pick a photo
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, REQUEST_CODE_PHOTO);
//        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        EditText etExampleSentence = findViewById(R.id.et_exampleSentence);

        ViewCompat.setOnApplyWindowInsetsListener(scrollView, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            int bottomInset = Math.max(imeInsets.bottom, systemBarsInsets.bottom);
            v.setPadding(0, 0, 0, bottomInset);
            return WindowInsetsCompat.CONSUMED;
        });

    }

    private void saveWordToFirebase(String wordName, String exampleSentence, String wordType) {
        Random random = new Random();
        int randomNumber = random.nextInt(100000); // Generates a random number between 0 and 99999

        StorageReference photoRef = storageReference.child("wordPhotos/" + wordName + randomNumber + ".jpg");
        StorageReference audioRef = storageReference.child("wordAudio/" + wordName + randomNumber + ".mp3");

        photoRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(photoDownloadUrl -> {
            audioRef.putFile(audioUri).addOnSuccessListener(taskSnapshot1 -> audioRef.getDownloadUrl().addOnSuccessListener(audioDownloadUrl -> {
                String wordId = mDatabase.push().getKey();
                Word word = new Word(wordId, wordName, wordType, exampleSentence, photoDownloadUrl.toString(), audioDownloadUrl.toString(), "", "", "");
                mDatabase.child(wordId).setValue(word).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(adminAddWord.this, "Word added successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(adminAddWord.this, "Failed to add word", Toast.LENGTH_SHORT).show();
                    }
                });
            })).addOnFailureListener(e -> {
                Toast.makeText(adminAddWord.this, "Failed to upload audio", Toast.LENGTH_SHORT).show();
            });
        })).addOnFailureListener(e -> {
            Toast.makeText(adminAddWord.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }

    private void clearFields() {
        editTextWordName.setText("");
        editTextExampleSentence.setText("");
        editTextWordType.setText("");
        imageViewPhoto.setImageURI(null);
        tvAddImage.setVisibility(View.VISIBLE); // Show the "Add Image" text again
        textViewUploadAudio.setText("");
        imageUri = null;
        audioUri = null;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "ProfileImage", null);
        return Uri.parse(path);
    }
//    private void saveImageToFirebaseStorage(Uri imageUri, Users user) {
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference profileImageRef = storageRef.child("wordsImages/" + user.getUserId() + ".jpg");
//
//        profileImageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                user.setImageLink(uri.toString());
//                authViewModel.updateUser(user);
//            });
//        }).addOnFailureListener(e -> {
//            Log.e(TAG, "Failed to upload image to Firebase Storage", e);
//        });
//    }

//    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//
//    if (resultCode == RESULT_OK && data != null) {
//        Uri selectedUri = data.getData();
//        if (requestCode == REQUEST_CODE_PHOTO) {
//            photoUri = selectedUri;
//
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
//                imageViewPhoto.setImageBitmap(bitmap);
//
//                // Now let the user pick an audio file
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, REQUEST_CODE_AUDIO);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else if (requestCode == REQUEST_CODE_AUDIO) {
//            audioUri = selectedUri;
//            textViewUploadAudio.setText(selectedUri.getPath());
//
//            uploadWordToFirebase();
//        }
//    }
//}
//
//    private void uploadWordToFirebase() {
//        String wordName = editTextWordName.getText().toString().trim();
//        String exampleSentence = editTextExampleSentence.getText().toString().trim();
//        String wordType = editTextWordType.getText().toString().trim();
//        String image = photoUri.toString();
//        String audio = audioUri.toString();
//
//
//        // Check if the user has entered all required fields
//        if (wordName.isEmpty() || exampleSentence.isEmpty() || wordType.isEmpty() || image.isEmpty() || audio.isEmpty()) {
//            // Show an error message to the user
//            return;
//        }
//
//        if (wordName.isEmpty() || photoUri == null || audioUri == null) {
//            // Show an error message to the user
//            return;
//        }
//
//        StorageReference photoRef = storageReference.child("photos/" + wordName + ".jpg");
//        StorageReference audioRef = storageReference.child("audio/" + wordName + ".mp3");
//
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] photoData = baos.toByteArray();
//
//            long maxWordId = 0;
//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                String wordIdStr = snapshot.child("wordId").getValue(String.class);
//                if (wordIdStr != null) {
//                    long wordId = Long.parseLong(wordIdStr);
//                    if (wordId > maxWordId) {
//                        maxWordId = wordId;
//                    }
//                }
//            }
//            String newWordId = (maxWordId + 1) + "";
//            UploadTask photoUploadTask = photoRef.putBytes(photoData);
//            photoUploadTask.addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(photoDownloadUrl -> {
//                UploadTask audioUploadTask = audioRef.putFile(audioUri);
//                audioUploadTask.addOnSuccessListener(taskSnapshot1 -> audioRef.getDownloadUrl().addOnSuccessListener(audioDownloadUrl -> {
//                    Word word = new Word(newWordId,wordName, exampleSentence,wordType,image.toString(), audio.toString(),"","","");
//                    mDatabase.child(wordName).setValue(word);
//                }));
//            }));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



}



