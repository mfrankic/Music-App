package com.example.musicapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadSongFragment extends Fragment {

    private String songPath;
    private TextView fileToUpload;
    private EditText songName;
    private Spinner genre;
    private Uri uri;
    private File songFile;
    protected FirebaseAuth auth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UploadSongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_song, container, false);
    }


    // File picker callback
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Log.d("DATA", data.toString());
                        uri = data.getData();

                        fileToUpload.setText(uri.getPath());
                    }
                }
            });

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;



        /*
        MaterialToolbar toolbar = view.findViewById(R.id.top_bar);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, activity.homeFragment)
                .commit());
         */

        // Current user firebase storage reference
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());

        // Firebase storage instance and root reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        fileToUpload = view.findViewById(R.id.fragment_upload_upload_file);
        songName = view.findViewById(R.id.fragment_upload_song_name);
        genre = view.findViewById(R.id.fragment_upload_song_genre_spinner);
        Spinner album = view.findViewById(R.id.fragment_upload_song_album_spinner);


        // Browse button click listener
        Button browseBtn = view.findViewById(R.id.fragment_upload_song_browse_btn);
        browseBtn.setOnClickListener(v -> {
            // Define the intent to open the file picker
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*"); // Set the MIME type of the files to select

            someActivityResultLauncher.launch(intent);

        });

        // Upload btn listener and upload of song
        Button uploadBtn = view.findViewById(R.id.fragment_upload_upload_song_btn);
        uploadBtn.setOnClickListener(v -> {

            // Create UUID
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            //Make storage reference with uuid as the file name on the cloud
            StorageReference song = storageRef.child("songs/" + uuidString + ".mp3");
            //Uri file = Uri.fromFile(songFile);
            UploadTask uploadTask = song.putFile(uri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Log.d("TAG", "Upload failed");
                new Toast(getContext());
                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_LONG).show();
            }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d("TAG", "Upload successful");
                new Toast(getContext());
                Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
            });

            // Update user document to reference the uploaded song
            CollectionReference subCollectionRef = docRef.collection("songs");
            Map<String, String> data = new HashMap<>();
            data.put("songUUID", uuidString);
            data.put("songName", songName.getText().toString());
            data.put("genre", genre.getSelectedItem().toString());

            subCollectionRef.add(data)
                    .addOnSuccessListener(documentReference ->
                            Log.d("TAG", "Document added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));

        });


    }
}