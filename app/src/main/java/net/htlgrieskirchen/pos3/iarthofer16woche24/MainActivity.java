package net.htlgrieskirchen.pos3.iarthofer16woche24;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Message> messages = new ArrayList<>();

    private FirebaseFirestore db;

    private final String collectionName = "Messages";
    private final String TAG = "BRWTalk";
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();


    }

    private void addMessageToFirebase(Message message){
        Log.d(TAG, "addMessageToFirebase");
        db.collection(collectionName)
                .document(String.valueOf(message.getId()))
                .set(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        counter++;
    }

    private void readMessagesFromFirebase(){
        messages = new ArrayList<>();

        db.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Message> newMessages = queryDocumentSnapshots.toObjects(Message.class);

                        Collections.sort(newMessages);

                        messages.addAll(newMessages);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failure");
                    }
                });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!messages.isEmpty()){
            Collections.sort(messages);
            counter = messages.size()+1;
        }
    }

    //TODO firebase authentication

    //TODO save messages in firebase

    //TODO load messages from db after login

    //TODO always load new notes from db

    //TODO show messages in LV

    //TODO Logs
}
