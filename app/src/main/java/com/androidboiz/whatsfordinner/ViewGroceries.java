package com.androidboiz.whatsfordinner;

import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

//TODO:Add left swipe gesture and handle addition and subtraction
//TODO:Handle entry strikethrough
public class ViewGroceries extends AppCompatActivity{

    private Toolbar toolbar;
    ArrayList<String> arrayOfGroceries;
    TreeMap<String, Double> countMap;
    TreeMap<String, String> unitMap;
    Storage storage;
    SwipeMenuListView groceryList;
    SwipeRefreshLayout swipeView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groceries);
        setTitle("Grocery Shopping");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storage = new Storage(this);
        arrayOfGroceries = new ArrayList<String>();
        countMap = new TreeMap<>();
        unitMap = new TreeMap<>();
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeView);

        adapter = new ArrayAdapter<String>(this, R.layout.grocery_list, arrayOfGroceries);
        attachAdapter();

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeView.setRefreshing(false);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem increaseCount = new SwipeMenuItem(
                        getApplicationContext());

                increaseCount.setIcon(R.drawable.plus_circle_outline);
                increaseCount.setWidth(convertDpToPx(75));
                menu.addMenuItem(increaseCount);

                SwipeMenuItem decreaseCount = new SwipeMenuItem(
                        getApplicationContext());

                decreaseCount.setWidth(convertDpToPx(75));
                decreaseCount.setIcon(R.drawable.minus_circle_outline);
                menu.addMenuItem(decreaseCount);
            }
        };

        groceryList.setMenuCreator(creator);

        groceryList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String grocery = arrayOfGroceries.toArray()[position].toString();
                String groceryArray[] = grocery.split(" ");
                double count = Double.parseDouble(groceryArray[1]);
                switch (index) {
                    case 0:
                        double newCount1 = count + 1;

                        if (newCount1 > 0) {
                            String newGrocery1 = grocery.replace(Double.toString(count), Double.toString(newCount1));
                            arrayOfGroceries.set(position, newGrocery1);

                            TextView groceryRow = groceryList.getChildAt(position).findViewById(R.id.groceryTextview);
                            groceryRow.setPaintFlags(groceryRow.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        } else {
                            String newGrocery1 = grocery.replace(Double.toString(count), Double.toString(newCount1));
                            arrayOfGroceries.set(position, newGrocery1);
                        }

                        break;
                    case 1:
                        double newCount2 = count - 1;

                        if (newCount2 <= 0.0) {
                            String newGrocery2 = grocery.replace(Double.toString(count), Double.toString(0.0));
                            arrayOfGroceries.set(position, newGrocery2);

                            TextView groceryRow = groceryList.getChildAt(position).findViewById(R.id.groceryTextview);
                            groceryRow.setPaintFlags(groceryRow.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        } else {
                            String newGrocery2 = grocery.replace(Double.toString(count), Double.toString(newCount2));
                            arrayOfGroceries.set(position, newGrocery2);
                        }

                        break;
                }
                adapter.notifyDataSetChanged();
                refreshView();

                return false;
            }
        });
    }

    /**
     * This is where we initialize our arrayadapter and loads the shared preference onto the adapter
     */
    protected void attachAdapter() {
        groceryList = findViewById(R.id.groceryListView);
        populateList();
        groceryList.setAdapter(adapter);
    }

    /**
     * This does the initial iteration of the shared preference by loading each key's values and then updating it if it finds duplicates
     */
    private void populateList() {
        String[] keys = storage.getAllKey();
        TreeMap<String, Integer> mealCount = getMeals();
        for (String key : mealCount.keySet()) { //for each type of dish
            for (int count = 0; count < mealCount.get(key); count++) { //for each quantity of that dish
                String[] values = storage.getValueArray(key); //getting the key's value
                for (int j = 3; j < values.length; j += 3) {
                    if (!countMap.containsKey(values[j])) {
                        countMap.put(values[j], Double.parseDouble(values[j + 1]));
                        unitMap.put(values[j], values[j + 2]);
                    } else {
                        double temp = countMap.get(values[j]);
                        temp += Double.parseDouble(values[j + 1]);
                        countMap.put(values[j], temp);
                    }
                }
            }
        }

        /* Saving from Recipes
        for (int i = 0; i < storage.getKeySize(); i++) {                             //This is looping the keys
            String[] values = storage.getValueArray(keys[i]);                       //getting the key's value
            for (int j = 3; j < values.length; j += 3) {
                if (!countMap.containsKey(values[j])) {
                    countMap.put(values[j], Double.parseDouble(values[j + 1]));
                    unitMap.put(values[j], values[j + 2]);
                } else {
                    double temp = countMap.get(values[j]);
                    temp += Double.parseDouble(values[j + 1]);
                    countMap.put(values[j], temp);
                }
            }
        } */

        for (String s : countMap.keySet()) {
            String temp = s + " " + countMap.get(s) + " " + unitMap.get(s);
            arrayOfGroceries.add(temp);
        }
        adapter.notifyDataSetChanged();
        refreshView();
    }

    /**
     * This method builds the new string with updated quantity
     * @param original the array that got split from the original arraylist
     * @param value The duplicate's value
     * @return the exact same string as the original except with updated value
     */
    public String editString(String[] original, int value) {
        String builder = original[0];
        String[] quantity = original[1].split(" ");
        int val = Integer.parseInt(quantity[0]) + value;
        return builder + "(" + val + " " + quantity[1] + ")";
    }

    /**
     * This method loops the arraylist and checks each element iteratively against the checkStr
     * @param list the arraylist used in the adapter
     * @param checkStr string to compare against
     * @return the index in the arraylist where it matches, it returns -1 when it cannot be found
     */
    public int listContainsString(List<String> list, String checkStr) {
        Iterator<String> iter = list.iterator();
        int count = 0;
        while (iter.hasNext()) {
            String s = iter.next();
            if (s.contains(checkStr)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }

    private void refreshView(){
        groceryList.setVisibility(View.GONE);
        groceryList.setVisibility(View.VISIBLE);
    }

    private TreeMap<String, Integer> getMeals() {
        TreeMap<String, Integer> returnMe = new TreeMap<>();
        try {
            FileInputStream inputStream = openFileInput("meals");
            ObjectInputStream in = new ObjectInputStream(inputStream);
            returnMe = (TreeMap<String, Integer>) in.readObject();
            in.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMe;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}