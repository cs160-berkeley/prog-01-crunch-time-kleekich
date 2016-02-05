package com.example.kangsik.crunchtime;

/**
 * Created by Kangsik on 2/4/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class CustomAdapter extends ArrayAdapter<String> {

    CustomAdapter(Context context, String[] rows) {
        super(context, R.layout.custom_row, rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater someInflater = LayoutInflater.from(getContext());
        View customView = someInflater.inflate(R.layout.custom_row, parent, false);

        String singleExerciseItem = getItem(position);
        TextView exeText = (TextView) customView.findViewById(R.id.exerciseName);

        exeText.setText(singleExerciseItem);


        return customView;
    }
}
