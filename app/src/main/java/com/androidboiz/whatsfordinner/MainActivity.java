package com.androidboiz.whatsfordinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void addNewDish(View view) {
        Intent i = new Intent(this, NewDish.class);
        startActivity(i);
    }

    protected void viewRecipesList(View view) {
        Intent i = new Intent(this, RecipesList.class);
        startActivity(i);
    }

    protected void viewGroceries(View view) {
        Intent i = new Intent(this, ViewGroceries.class);
        startActivity(i);
    }

    protected void viewMealPlan(View view) {
        Intent i = new Intent(this, MealPlan.class);
        startActivity(i);
    }

    protected void applicationInformation(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Authors: " +
                "\n" +
                    "   Chirag Chaudhari" +
                "\n" +
                    "   Nicholas Gadjali" +
                "\n" +
                    "   Brian Tan" +
                "\n" +
                    "Version: 1" +
                "\n" +
                    "Contact Information: " +
                "\n" +
                    "   chirag.chaudhari@sjsu.edu" +
                "\n" +
                    "   nicholas.gadjali@sjsu.edu" +
                "\n" +
                    "   brian.tan@sjsu.edu");
                alertDialogBuilder.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}