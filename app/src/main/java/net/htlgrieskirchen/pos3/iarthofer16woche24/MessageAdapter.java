package net.htlgrieskirchen.pos3.iarthofer16woche24;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private final String TAG = "BRWTalk";
    private static final String collectionName = "Notes";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public MessageAdapter(Context context, ArrayList<Message> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Message m = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message, parent, false);
        }

        TextView name_TV = convertView.findViewById(R.id.name_TV);
        TextView date_TV = convertView.findViewById(R.id.date_TV);
        TextView message_TV = convertView.findViewById(R.id.message_TV);

        name_TV.setText(m.getUser());
        date_TV.setText(sdf.format(m.getDateTime()));
        message_TV.setText(m.getMessage());

        return convertView;

    }
}
