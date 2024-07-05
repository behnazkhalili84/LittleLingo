package com.example.littlelingo.ui.user;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.littlelingo.R;
import com.example.littlelingo.SignInActivity;
import com.example.littlelingo.databinding.FragmentProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import java.io.ByteArrayOutputStream;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    private ImageView profileImageView;
    private FloatingActionButton editImageButton;
    private Uri imageUri;

    // Register the activity result launcher for camera and gallery intents
    private final ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri != null) {
                imageUri = uri;
                profileImageView.setImageURI(uri);
            }
        }
    });

    private final ActivityResultLauncher<Void> takePicture = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap bitmap) {
            if (bitmap != null) {
                profileImageView.setImageBitmap(bitmap);
                imageUri = getImageUriFromBitmap(bitmap);
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize AuthViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        Log.d(TAG, "onCreateView: "+ authViewModel);

        TextInputEditText usernameEditText = view.findViewById(R.id.usernameProfile);
        TextInputEditText emailEditText = view.findViewById(R.id.emailProfile);
        TextInputEditText ageEditText = view.findViewById(R.id.ageProfile);
        TextInputEditText nativeLangEditText = view.findViewById(R.id.nativeLanguage);
        Button editButton = view.findViewById(R.id.editButton);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        profileImageView = view.findViewById(R.id.profileImageView);
        editImageButton = view.findViewById(R.id.editImageButton); // image edit button

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choosing options to either take a photo or select from gallery
                String[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Edit Profile Picture");
                usernameEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                ageEditText.setEnabled(true);
                nativeLangEditText.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
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

        // Observe user data from ViewModel
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                usernameEditText.setText(user.getName());
                emailEditText.setText(user.getEmail());
                Log.d(TAG, "onClick: "+ user.toString());
                ageEditText.setText(String.valueOf(user.getAge()));
                nativeLangEditText.setText(user.getNativeLanguage());
                if (user.getImageLink() != null && !user.getImageLink().isEmpty()) {
                    Glide.with(this).load(user.getImageLink()).into(profileImageView);
                }

            }
        });

        // Show the Save button when Edit button is clicked
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                ageEditText.setEnabled(true);
                nativeLangEditText.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
            }
        });

        // Handle Save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedUsername = usernameEditText.getText().toString();
                String updatedEmail = emailEditText.getText().toString();
                String updatedAge = ageEditText.getText().toString();
                String updatedNativeLang = nativeLangEditText.getText().toString();

                Users updatedUser = new Users(authViewModel.getUserLiveData().getValue().getUserId(), updatedUsername, updatedEmail, Integer.parseInt(updatedAge), updatedNativeLang, authViewModel.getUserLiveData().getValue().getDateOfBirth());
                authViewModel.updateUser(updatedUser);

                // Save the image to Firebase Storage and update the database with the image URL
                if (imageUri != null) {
                    saveImageToFirebaseStorage(imageUri, updatedUser);
                }

                usernameEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                ageEditText.setEnabled(false);
                nativeLangEditText.setEnabled(false);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancel button click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset fields to original values
                authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        usernameEditText.setText(user.getName());
                        emailEditText.setText(user.getEmail());

                        ageEditText.setText(user.getAge());
                        nativeLangEditText.setText(user.getNativeLanguage());
                    }
                });
                usernameEditText.setEnabled(false);
                emailEditText.setEnabled(false);

                ageEditText.setEnabled(false);
                nativeLangEditText.setEnabled(false);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
            }
        });

        // Delete account button
        Button deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // delete logic here

                // Navigate to sign-in screen or close the app
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });


        return view;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "ProfileImage", null);
        return Uri.parse(path);
    }

    private void saveImageToFirebaseStorage(Uri imageUri, Users user) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImageRef = storageRef.child("usersImages/" + user.getUserId() + ".jpg");

        profileImageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                user.setImageLink(uri.toString());
                authViewModel.updateUser(user);
            });
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to upload image to Firebase Storage", e);
        });
    }
}