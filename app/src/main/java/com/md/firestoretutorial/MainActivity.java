package com.md.firestoretutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    // Initialise the instance of firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    //Create document in the collection and document
    private DocumentReference noteRef=db.document("Notebook/ My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);

    }


//     Todo Step 3:* Starts listening to the document referenced by this {@code DocumentReference} using an
//   * Activity-scoped listener.
    @Override
    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                //Check if there is a exception
                if (error!=null){
                    Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    return;
                }

                //If document available then get data and set to textview
                if (documentSnapshot.exists()) {
//                    String title = documentSnapshot.getString(KEY_TITLE);
//                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
//                    textViewData.setText("Title: " + title + "\n" + "Description: " + description);


                    //Todo step 8:Access from Note.java
                    Note note= documentSnapshot.toObject(Note.class);

                    String title=note.getTitle();
                    String description=note.getDescription();
                    textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                } else {
                    textViewData.setText("");
                }
            }
        });
    }

    //Todo Step 1:Onclick on save , data will be saved in the firestore
    public void saveNote(View view) {

        //Get entered data and store it
        String title= editTextTitle.getText().toString();
        String desc=editTextDescription.getText().toString();


        //Create Hashmap to store Key-Value pair data
//        Map<String,Object> note=new HashMap<>();
//        note.put(KEY_TITLE,title);
//        note.put(KEY_DESCRIPTION,desc);

        //Todo step 8: access from Note class
        Note note=new Note(title,desc);

        //The data to write to the document
        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    //Todo Step 2: Method to get the data from firestore
    public void loadNote(View view) {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        //If document available then get data and set to textview
                        if (documentSnapshot.exists()) {
//                            String title = documentSnapshot.getString(KEY_TITLE);
//                            String description = documentSnapshot.getString(KEY_DESCRIPTION);
//                            textViewData.setText("Title: " + title + "\n" + "Description: " + description);

                            //Todo step 8:Access from Note.java
                            Note note= documentSnapshot.toObject(Note.class);

                            String title=note.getTitle();
                            String description=note.getDescription();
                            textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                        } else {
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Todo Step 4: Update the description
    public void updateDescription(View view) {
        String description = editTextDescription.getText().toString();
        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, description);

        //Instead of map we can pass key value pair directly
        noteRef.update(KEY_DESCRIPTION,description);
    }

    //Todo Step 5: Delete the desciption
    public void deleteDescription(View view) {
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    //Todo Step 6: Delete the entire note
    public void deleteNote(View view) {
        noteRef.delete();
    }
}