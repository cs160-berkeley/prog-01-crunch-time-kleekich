package com.example.kangsik.crunchtime;

import java.util.*;
import static java.lang.System.out;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.view.MotionEvent;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity{

  

    private GestureDetectorCompat gestureDetector;

    //exercise list
    public static String[] exerciseList={"Pushup","Situp","Squats","Leg-lift","Plank","Jumping Jacks","Pullup","Cycling","Walking", "Jogging", "Swimming", "Stair-Climbing"};
    //conversion rate(reps and min) per calorie

    public static double[] conversionRate={0.2857142857, 0.5, 0.4444444444, 4, 4, 10, 1, 8.3333333333, 5, 8.3333333333, 7.6923076923, 6.6666666667};

    public static boolean[] unitInMin = {false,false,false,true,true,true,false,true,true,true,true,true};

    public static Hashtable<String, Double> conversionTable = new Hashtable<String, Double>();

    public static Hashtable<String, Boolean> unitsTable = new Hashtable<String, Boolean>();

    /*radio group*/
    private static RadioGroup radioG;
    private static RadioButton radioRm,radioCal;





    public static boolean inMinutes;
    private EditText inputCalTxt;
    public static int inputInt =0;
    public static double convertedCal = 0;
    public static String selectedExType = "";
    public static String[] rows = new String[12];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //HashTable for exercise and conversionRate
        for(int i= 0; i<exerciseList.length; i++){
            conversionTable.put(exerciseList[i], conversionRate[i]);
        }
        for(int i= 0; i<exerciseList.length; i++){
            unitsTable.put(exerciseList[i], unitInMin[i]);
        }


        /*spinner*/
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.excercise_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+ " selected", Toast.LENGTH_LONG).show();
                TextView unitText = (TextView) findViewById(R.id.unitText);
                unitText.setText(unitsTable.get(spinner.getSelectedItem().toString())? "mins": "reps");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /*radio Button
        radioG = (RadioGroup) findViewById(R.id.radioG);

        radioRm.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        TextView unitText = (TextView) findViewById(R.id.unitText);
                        unitText.setText("cal");

                    }
                }

        );
        int selected_id = radioG.getCheckedRadioButtonId();


*/







        /*Convert Button*/
        Button convertBtn = (Button) findViewById(R.id.convertBtn);

        convertBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView caloriesText = (TextView) findViewById(R.id.caloriesText);

                        selectedExType = spinner.getSelectedItem().toString();
                        inputCalTxt = (EditText) findViewById(R.id.userInput);
                        inputInt = Integer.parseInt(inputCalTxt.getText().toString());

                        convertedCal = convertToCal(selectedExType, inputInt);
                        caloriesText.setText(' ' + Integer.toString((int) convertedCal));

                        //Creating list for other excercises

                        StringBuffer rowBuffer = new StringBuffer();
                        for (int j = 0; j < exerciseList.length; j++) {
                            String type = exerciseList[j];
                            int work = (int) convertToWork(type, convertedCal);
                            //if conversionRate is less than 1, the unit must be in minutes
                            rowBuffer.append(exerciseList[j]);
                            rowBuffer.append(' ');
                            rowBuffer.append(work);
                            if (unitsTable.get(type)) {
                                rowBuffer.append(" mins");
                            } else {
                                rowBuffer.append(" reps");
                            }
                            rows[j] = rowBuffer.toString();
                            rowBuffer.setLength(0);
                        }
                    }
                }

        );
        /*listView*/

        ListAdapter listAdapter = new CustomAdapter(this, rows);
        ListView exerciseListView = (ListView) findViewById(R.id.exerciseListView);
        exerciseListView.setAdapter(listAdapter);

        exerciseListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String equival = String.valueOf(parent.getItemAtPosition(position));

                        Toast.makeText(MainActivity.this, equival, Toast.LENGTH_LONG).show();
                    }
                }
        );




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public double convertToCal(String type, int amountOfWork){
        double rate = conversionTable.get(type);
        double cal = amountOfWork*rate;
        return cal;


    };

    public double convertToWork(String type, double cal){
        double rate = conversionTable.get(type);

        return cal/rate;
        };


    public void onClickListenerButton() {
        radioG = (RadioGroup) findViewById(R.id.radioG);




    }


}
