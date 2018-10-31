package com.androidboiz.whatsfordinner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MealPlan extends AppCompatActivity {

    private Toolbar toolbar;
    private String[][] daysOfWeek;
    private Map<String, Integer> mapDay;
    private int currentDay;
    private Spinner breakfast;
    private Spinner lunch;
    private Spinner dinner;
    private ArrayList<Spinner> mealsSpinnerList = new ArrayList<>();
    private ArrayAdapter<String> mealNamesSpinnerAdapter;
    public TreeMap<String, Integer> meals;
    public static List<String> mealNames = new ArrayList<String>();
    int check = 0;
    boolean initialized = false;
    boolean repeated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        setTitle("Meal Plan");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentDay = 0;
        daysOfWeek = new String[7][3];
        mapDay = new HashMap<>();
        mapDay.put("Monday", 0);
        mapDay.put("Tuesday", 1);
        mapDay.put("Wednesday", 2);
        mapDay.put("Thursday", 3);
        mapDay.put("Friday", 4);
        mapDay.put("Saturday", 5);
        mapDay.put("Sunday", 6);
        initialized = getMeals();
        if (initialized) {
            mealNamesSpinnerAdapter = new ArrayAdapter<String>(MealPlan.this, android.R.layout.simple_spinner_item, mealNames);
            mealNamesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            breakfast = (Spinner) findViewById(R.id.mealName0);
            lunch = (Spinner) findViewById(R.id.mealName1);
            dinner = (Spinner) findViewById(R.id.mealName2);
            breakfast.setAdapter(mealNamesSpinnerAdapter);
            lunch.setAdapter(mealNamesSpinnerAdapter);
            dinner.setAdapter(mealNamesSpinnerAdapter);
            mealsSpinnerList.add(breakfast);
            mealsSpinnerList.add(lunch);
            mealsSpinnerList.add(dinner);
            addListener();
        }
    }

    public void changeDay(View view) {
        TextView day = findViewById(R.id.daysOfWeek);
        if (view.getId() == R.id.leftButton) {
            currentDay--;
            if (currentDay == -1) {
                currentDay = 6;
            }
        }
        else {
            currentDay++;
            if (currentDay == 7) {
                currentDay = 0;
            }
        }
        day.setText(getKey(mapDay, currentDay));
        if (initialized) {
            loadData();
        }
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (K key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }
    @Override
    protected void onStop() {
        super.onStop(); //required
        saveMealPlan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMealPlan();
        TextView day = findViewById(R.id.daysOfWeek);
        day.setText(getKey(mapDay, currentDay));
        loadData();
    }
    protected void onPause() {
        super.onPause();
        saveMealPlan();
    }
    private void loadMealPlan() {
        File file = getBaseContext().getFileStreamPath("mealPlans");
        if(file.exists()) {
            try {
                FileInputStream inputStream = openFileInput("mealPlans");
                ObjectInputStream in = new ObjectInputStream(inputStream);
                daysOfWeek = (String[][]) in.readObject();
                currentDay = (int) in.readObject();
                meals = (TreeMap<String, Integer>) in.readObject();
                in.close();
                inputStream.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    private void saveMealPlan() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("mealPlans", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(daysOfWeek);
            out.writeObject(currentDay);
            out.writeObject(meals);
            out.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        String[] mealList = daysOfWeek[currentDay];
        for (int i = 0; i < mealList.length; i++) {
            String eachMeal = mealList[i];
            Spinner thisSpinner = mealsSpinnerList.get(i);
            if (eachMeal == null) {
                eachMeal = "Eating out";
                daysOfWeek[currentDay][i] = eachMeal;
            }
            setSelectionSpinner(thisSpinner, eachMeal);
        }
    }


    private boolean getMeals() {
        try {
            FileInputStream inputStream = openFileInput("meals");
            ObjectInputStream in = new ObjectInputStream(inputStream);
            meals = (TreeMap<String, Integer>) in.readObject();
            in.close();
            inputStream.close();
        } catch (Exception e) {
            return false;
        }
        mealNames.add("Eating out");
        int counter = 1;
        for (Map.Entry<String, Integer> entry : meals.entrySet()) {
            mealNames.add(entry.getKey());
            counter++;
        }
        return true;
    }

    public void addListener() {
        for (Spinner i : mealsSpinnerList) {
            i.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (++check> 3) {
                        int index = mealsSpinnerList.indexOf(parent);
                        String text = parent.getSelectedItem().toString();
                        String temp = daysOfWeek[currentDay][index];        //get the name from the array of array
                        if (!repeated) {
                            if (temp != "Eating out" && temp != null) {          //if this has a value in the array
                                if (text != "Eating out" && text != null) {
                                    if (!checkCount(text, index, temp)) {
                                        meals.put(temp, meals.get(temp) + 1);           //add 1 to the meal count previously held
                                        daysOfWeek[currentDay][index] = text;           //set the new value in the array
                                        meals.put(text, meals.get(text) - 1);           //subtract 1 to the current meal count
                                        Toast.makeText(MealPlan.this, "There are " + meals.get(text) + " " + text + " left\nThere are " + meals.get(temp) + " " + text + " left", Toast.LENGTH_LONG).show();

                                    } else {
                                        repeated = true;
                                        Toast.makeText(MealPlan.this, "There is no more count, please add more from recipe list", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    daysOfWeek[currentDay][index] = text;
                                    meals.put(temp, meals.get(temp) + 1);
                                    Toast.makeText(MealPlan.this, "There are " + meals.get(temp) + " " + temp + " left", Toast.LENGTH_LONG).show();
                                }
                            } else {                                              //then this current value in the array is null
                                if (text != "Eating out" && text != null) {      //we have to check if the new value is not eating out or null
                                    if (!checkCount(text, index, temp)) {
                                        daysOfWeek[currentDay][index] = text;
                                        meals.put(text, meals.get(text) - 1);
                                        Toast.makeText(MealPlan.this, "There are " + meals.get(text) + " " + text + " left", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(MealPlan.this, "There is no more count, please add more from recipe list", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    daysOfWeek[currentDay][index] = text;
                                }
                            }
                        }
                        else {
                            repeated = false;
                        }
                        Log.d("this is my array", "arr: " + Arrays.toString(daysOfWeek[currentDay]));
                        Log.d("this is my array", "count: " + check);
                        for (Map.Entry<String,Integer> entry : meals.entrySet()) {
                            String key = entry.getKey();
                            int value = entry.getValue();
                            Log.d("This is my map", "key: " + key);
                            Log.d("This is my map", "value: " + value);
                            // do stuff
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }
    public boolean checkCount(String text, int index, String previous) {
        if (meals.get(text)-1 < 0) {
            if (previous == null) {
                previous = "Eating out";
            }
            setSelectionSpinner(mealsSpinnerList.get(index), previous);
            return true;
        }
        return false;
    }

    public void setSelectionSpinner(Spinner thisSpinner, String selected) {
        for (int j= 0; j < thisSpinner.getAdapter().getCount(); j++) {
            if (thisSpinner.getAdapter().getItem(j).toString().contains(selected)) {
                thisSpinner.setSelection(j);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}