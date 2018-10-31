package com.androidboiz.whatsfordinner;

import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class RecipesList extends AppCompatActivity {
    public ArrayList<String> recipes;
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipes_list);
        setTitle("Recipes");

        recipes = new ArrayList<>();
        storage = new Storage(this);
        getRecipes();

        Bundle bundle = new Bundle();
        bundle.putSerializable("recipes", recipes);
        Configuration config = getResources().getConfiguration();

        FragmentManager fm = getSupportFragmentManager(); //Get Fragment Manager
        FragmentTransaction ft = fm.beginTransaction(); //Get Fragment Transaction

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecipesListFragment rlFrag = new RecipesListFragment();
            rlFrag.setArguments(bundle);
            ft.replace(android.R.id.content, rlFrag);
            ft.commit();
        } else {
            RecipesListDetailFragment rldFrag = new RecipesListDetailFragment();
            rldFrag.setArguments(bundle);
            ft.replace(android.R.id.content, rldFrag);
            ft.commit();
        }
    }

    private void getRecipes() {
        String[] keys = storage.getAllKey();
        for (String k : keys) {                             //This is looping the keys
            recipes.add(storage.getValue(k));
        }
    }
}