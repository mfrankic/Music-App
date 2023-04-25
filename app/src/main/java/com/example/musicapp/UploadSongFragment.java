package com.example.musicapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UploadSongFragment extends Fragment {

    private Button browseBtn, uploadBtn;
    private String songPath;
    private TextView fileToUpload;
    private EditText songName;
    private Spinner genre, album;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            uri = data.getData();

            fileToUpload.setText(uri.getPath().toString());
        }
    }

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

        // Current user firebase storage refernece
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());

        // Firebase sotrage instance and root refernece
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        fileToUpload = view.findViewById(R.id.fragment_upload_upload_file);
        songName = view.findViewById(R.id.fragment_upload_song_name);
        genre = view.findViewById(R.id.fragment_upload_song_genre_spinner);
        album = view.findViewById(R.id.fragment_upload_song_album_spinner);


        // Browse button click listener
        browseBtn = view.findViewById(R.id.fragment_upload_song_browse_btn);
        browseBtn.setOnClickListener(v -> {
            // Define the intent to open the file picker
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*"); // Set the MIME type of the files to select

            startActivityForResult(intent, 10);

        });

        // Upload btn listener and upload of song
        uploadBtn = view.findViewById(R.id.fragment_upload_upload_song_btn);
        uploadBtn.setOnClickListener(v ->{

            // Create UUID
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            //Make storage refernece with uuid as the file name on the cloud
            StorageReference song = storageRef.child("songs/" + uuidString + ".mp3");
            //Uri file = Uri.fromFile(songFile);
            UploadTask uploadTask = song.putFile(uri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });

            // Update user document to reference the uplodaed song
            CollectionReference subCollectionRef = docRef.collection("songs");
            Map<String, String> data = new HashMap<>();
            data.put("songUUID", uuidString);
            data.put("songName", songName.getText().toString());
            data.put("genre", genre.getSelectedItem().toString());

            subCollectionRef.add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "Document added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });

        });


    }
}