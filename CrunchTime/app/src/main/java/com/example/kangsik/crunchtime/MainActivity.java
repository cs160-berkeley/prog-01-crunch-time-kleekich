package com.example.kangsik.crunchtime;

import java.util.*;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.support.v4.view.GestureDetectorCompat;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.RadioButton;

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
    private EditText inputTxt;
    public static int inputInt =0;
    public static double convertedOutput = 0;
    public static String selectedExType = "";
    public static String[] rows = new String[12];
    public static boolean calSelected = false;
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
                if(!calSelected){
                    unitText.setText(unitsTable.get(spinner.getSelectedItem().toString()) ? "mins" : "reps");
                }else{
                    unitText.setText("cal");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /*radio Button*/
        radioG = (RadioGroup) findViewById(R.id.radioG);
        radioRm = (RadioButton) findViewById(R.id.radioRm);
        final TextView unitText = (TextView) findViewById(R.id.unitText);
        final TextView unitText2 = (TextView) findViewById(R.id.unitText2);

        radioRm.setOnClickListener(

                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        unitText.setText(unitsTable.get(spinner.getSelectedItem().toString())? "mins": "reps");
                        unitText2.setText("cal");
                        calSelected = false;
                    }
                }
        );
        radioCal = (RadioButton) findViewById(R.id.radioCal);
        radioCal.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                        unitText.setText("cal");
                        unitText2.setText(unitsTable.get(spinner.getSelectedItem().toString())? "mins": "reps");
                        calSelected = true;

                                        }
                                    }
        );
        int selected_id = radioG.getCheckedRadioButtonId();









        /*Convert Button*/
        Button convertBtn = (Button) findViewById(R.id.convertBtn);

        convertBtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView outputText = (TextView) findViewById(R.id.outputText);

                        selectedExType = spinner.getSelectedItem().toString();
                        inputTxt = (EditText) findViewById(R.id.userInput);
                        inputInt = Integer.parseInt(inputTxt.getText().toString());
                        int measurement =0;

                        if(!calSelected) {
                            convertedOutput = convertToCal(selectedExType, inputInt);
                            Toast.makeText(getBaseContext(), "Converted To cal", Toast.LENGTH_LONG).show();
                            unitText2.setText("cal");
                        }else{
                            convertedOutput = convertToWork(selectedExType, inputInt);
                            String unit = unitsTable.get(spinner.getSelectedItem().toString())? "mins": "reps";
                            Toast.makeText(getBaseContext(), "Converted To "+ unit, Toast.LENGTH_LONG).show();
                            unitText2.setText(unit);
                        }
                        outputText.setText(' ' + Integer.toString((int) convertedOutput));
                            //Creating list for other excercises

                            StringBuffer rowBuffer = new StringBuffer();
                            for (int j = 0; j < exerciseList.length; j++) {
                                //if conversionRate is less than 1, the unit must be in minutes
                                rowBuffer.append(exerciseList[j]);
                                rowBuffer.append(' ');
                                if(!calSelected){
                                    measurement = (int) convertToWork(exerciseList[j], convertedOutput);
                                }else{
                                    measurement = (int) convertToWork(exerciseList[j], inputInt);
                                }
                                rowBuffer.append(measurement);

                                if (unitsTable.get(exerciseList[j])) {
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
    }//onCreate

    public double convertToCal(String type, double amountOfWork){
        double rate = conversionTable.get(type);
        double cal = amountOfWork*rate;
        return cal;


    }

    public double convertToWork(String type, double cal){
        double rate = conversionTable.get(type);

        return cal/rate;

    }


    public void onClickListenerButton() {
        radioG = (RadioGroup) findViewById(R.id.radioG);




    }


}
