package net.htlgrieskirchen.pos3.iarthofer16woche24;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView message_TV;
    Button send_button;

    private ArrayList<Message> messages = new ArrayList<>();

    MessageAdapter messageAdapter;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private final String collectionName = "Messages";
    private final String TAG = "BRWTalk";
    private int counter = 0;
    private String username = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        username = user.getEmail();

        listView = findViewById(R.id.listView);
        message_TV = findViewById(R.id.main_message);
        send_button = findViewById(R.id.send_button);

        readMessagesFromFirebase();

        messageAdapter = new MessageAdapter(this, messages);

        listView.setAdapter(messageAdapter);

        db.collection(collectionName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Message m = dc.getDocument().toObject(Message.class);
                                    m.setId(counter);
                                    messages.add(m);
                                    Collections.sort(messages);
                                    counter++;
                                    break;
                                default:
                                    break;
                            }
                        }

                        messageAdapter.notifyDataSetChanged();
                    }
                });


    }

    public void onClick(View view) {
        Log.d(TAG, "buttonSendClicked");
                String message = message_TV.getText().toString();
                Message m = new Message(message, username, counter++);
                addMessageToFirebase(m);
                messageAdapter.notifyDataSetChanged();
                message_TV.setText("");
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
    }

    private void readMessagesFromFirebase(){
        Log.d(TAG, "readMEssagesFromFirebase");
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
            counter = messages.size()+2;
        }
    }

    //TODO Logs
}
