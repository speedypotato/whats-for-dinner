package com.androidboiz.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

public class RecipesListFragment extends Fragment {
    public ArrayList<String> recipes;
    public String currentDishName;
    public String currentDish;
    public ArrayList<String> recipeNames;
    public TreeMap<String, Integer> meals;

    public RecipesListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();

        recipes = new ArrayList<>();
        currentDish = "";
        recipeNames = new ArrayList<>();
        meals = new TreeMap<>();

        if (b != null) {
            recipes = (ArrayList<String>)b.getSerializable("recipes");
            for (String s : recipes) {
                recipeNames.add(s.substring(0, s.indexOf('%')));
            }
        }

        View v = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ArrayAdapter adapter = new ArrayAdapter(RecipesListFragment.this.getActivity(), android.R.layout.simple_expandable_list_item_1, recipeNames);
        ListView lv = (ListView) v.findViewById(R.id.recipesListView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int dishAmt = 1;
                meals = getMeals();
                currentDishName = (String)adapterView.getItemAtPosition(i);
                if (meals.get(currentDishName) != null) {
                    dishAmt = meals.get(currentDishName) + 1;
                }
                meals.put(currentDishName, dishAmt);
                setMeals(meals);
                Toast.makeText(getActivity(), dishAmt + " " + currentDishName + " available.", Toast.LENGTH_LONG).show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                for (String s : recipes) {
                    String name = s.substring(0, s.indexOf('%'));
                    if (selected.equals(name)) {
                        currentDish = s;
                        break;
                    }
                }
                if (!currentDish.equals("")) {      //if found, edit dish
                    Intent intent = new Intent(getActivity(), EditDish.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe", currentDish);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    private TreeMap<String, Integer> getMeals() {
        TreeMap<String, Integer> returnMe = new TreeMap<>();
        try {
            FileInputStream inputStream = getActivity().openFileInput("meals");
            ObjectInputStream in = new ObjectInputStream(inputStream);
            returnMe = (TreeMap<String, Integer>) in.readObject();
            in.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMe;
    }

    private void setMeals(TreeMap<String, Integer> meals) {
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput("meals", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(meals);
            out.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}