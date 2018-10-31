package com.androidboiz.whatsfordinner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Spinner
 * TODO: ADD IMAGE
 */
public class NewDish extends AppCompatActivity {

    private Toolbar toolbar;
    static final int RESULT_LOAD_IMG = 1;
    private static final String TAG = "NewDish";    //Testing purposes, used for logging data
    static boolean taken = true;
    Storage storage;
    private String imageUrl = "";
    public static List<String> ingredientNames = new ArrayList<String>();
    private ArrayList<Spinner> ingredientNamesSpinnerList = new ArrayList<>();
    private ArrayList<Spinner> ingredientUnitsSpinnerList = new ArrayList<>();
    private ArrayList<EditText> ingredientCountsSpinnerList = new ArrayList<>();
    ArrayAdapter<String> ingredientNamesSpinnerAdapter;
    ArrayAdapter<String> ingredientUnitsSpinnerAdapter;
    private Spinner ingredient1Name;
    private Spinner ingredient2Name;
    private Spinner ingredient3Name;
    private Spinner ingredient4Name;
    private Spinner ingredient5Name;
    private Spinner ingredient6Name;
    private Spinner ingredient7Name;
    private Spinner ingredient8Name;
    private Spinner ingredient9Name;
    private Spinner ingredient10Name;
    private Spinner ingredient1Unit;
    private Spinner ingredient2Unit;
    private Spinner ingredient3Unit;
    private Spinner ingredient4Unit;
    private Spinner ingredient5Unit;
    private Spinner ingredient6Unit;
    private Spinner ingredient7Unit;
    private Spinner ingredient8Unit;
    private Spinner ingredient9Unit;
    private Spinner ingredient10Unit;
    private EditText ingredient1Count;
    private EditText ingredient2Count;
    private EditText ingredient3Count;
    private EditText ingredient4Count;
    private EditText ingredient5Count;
    private EditText ingredient6Count;
    private EditText ingredient7Count;
    private EditText ingredient8Count;
    private EditText ingredient9Count;
    private EditText ingredient10Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);
        setTitle("New Dish");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = new Storage(this);
        final TextView editTextTitle = (TextView)findViewById(R.id.recipeName);
        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkNameRepeat(editTextTitle);
                    if(taken) {
                        Toast.makeText(NewDish.this, "The recipe name is taken",Toast.LENGTH_LONG).show();
                    }
                    else if (editTextTitle.getText().toString().isEmpty()) {
                        Toast.makeText(NewDish.this, "The recipe name is empty",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        String[] ingredientUnitsArray = {"Unit","pcs","tsp", "tbsp", "fl oz", "cup", "pt", "qt", "gal", "ml",
                                        "l", "oz", "mg", "g", "lb", "kg"};
        ingredientUnitsSpinnerAdapter = new ArrayAdapter<String>(NewDish.this, android.R.layout.simple_spinner_item, ingredientUnitsArray);
        ingredientUnitsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredient1Count = (EditText) findViewById(R.id.ingredient1Count);
        ingredient2Count = (EditText) findViewById(R.id.ingredient2Count);
        ingredient3Count = (EditText) findViewById(R.id.ingredient3Count);
        ingredient4Count = (EditText) findViewById(R.id.ingredient4Count);
        ingredient5Count = (EditText) findViewById(R.id.ingredient5Count);
        ingredient6Count = (EditText) findViewById(R.id.ingredient6Count);
        ingredient7Count = (EditText) findViewById(R.id.ingredient7Count);
        ingredient8Count = (EditText) findViewById(R.id.ingredient8Count);
        ingredient9Count = (EditText) findViewById(R.id.ingredient9Count);
        ingredient10Count = (EditText) findViewById(R.id.ingredient10Count);

        ingredient1Unit = (Spinner) findViewById(R.id.ingredient1Unit);
        ingredient2Unit = (Spinner) findViewById(R.id.ingredient2Unit);
        ingredient3Unit = (Spinner) findViewById(R.id.ingredient3Unit);
        ingredient4Unit = (Spinner) findViewById(R.id.ingredient4Unit);
        ingredient5Unit = (Spinner) findViewById(R.id.ingredient5Unit);
        ingredient6Unit = (Spinner) findViewById(R.id.ingredient6Unit);
        ingredient7Unit = (Spinner) findViewById(R.id.ingredient7Unit);
        ingredient8Unit = (Spinner) findViewById(R.id.ingredient8Unit);
        ingredient9Unit = (Spinner) findViewById(R.id.ingredient9Unit);
        ingredient10Unit = (Spinner) findViewById(R.id.ingredient10Unit);

        ingredient1Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient2Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient3Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient4Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient5Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient6Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient7Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient8Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient9Unit.setAdapter(ingredientUnitsSpinnerAdapter);
        ingredient10Unit.setAdapter(ingredientUnitsSpinnerAdapter);

        if (ingredientNames.size() < 1) {
            ingredientNames.add(getString(R.string.pick_ingredient));
        }
        ingredientNames = getIngredientsList();

        String [] ingredientNamesArray = ingredientNames.toArray(new String[ingredientNames.size()]);
        ingredientNamesSpinnerAdapter = new ArrayAdapter<String>(NewDish.this, android.R.layout.simple_spinner_item, ingredientNamesArray);
        ingredientNamesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredient1Name = (Spinner) findViewById(R.id.ingredient1Name);
        ingredient2Name = (Spinner) findViewById(R.id.ingredient2Name);
        ingredient3Name = (Spinner) findViewById(R.id.ingredient3Name);
        ingredient4Name = (Spinner) findViewById(R.id.ingredient4Name);
        ingredient5Name = (Spinner) findViewById(R.id.ingredient5Name);
        ingredient6Name = (Spinner) findViewById(R.id.ingredient6Name);
        ingredient7Name = (Spinner) findViewById(R.id.ingredient7Name);
        ingredient8Name = (Spinner) findViewById(R.id.ingredient8Name);
        ingredient9Name = (Spinner) findViewById(R.id.ingredient9Name);
        ingredient10Name = (Spinner) findViewById(R.id.ingredient10Name);

        ingredient1Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient2Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient3Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient4Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient5Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient6Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient7Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient8Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient9Name.setAdapter(ingredientNamesSpinnerAdapter);
        ingredient10Name.setAdapter(ingredientNamesSpinnerAdapter);

        ingredientNamesSpinnerList.add(ingredient1Name); //reference in array for easier processing
        ingredientNamesSpinnerList.add(ingredient2Name);
        ingredientNamesSpinnerList.add(ingredient3Name);
        ingredientNamesSpinnerList.add(ingredient4Name);
        ingredientNamesSpinnerList.add(ingredient5Name);
        ingredientNamesSpinnerList.add(ingredient6Name);
        ingredientNamesSpinnerList.add(ingredient7Name);
        ingredientNamesSpinnerList.add(ingredient8Name);
        ingredientNamesSpinnerList.add(ingredient9Name);
        ingredientNamesSpinnerList.add(ingredient10Name);

        ingredientUnitsSpinnerList.add(ingredient1Unit); //reference in array for easier processing
        ingredientUnitsSpinnerList.add(ingredient2Unit);
        ingredientUnitsSpinnerList.add(ingredient3Unit);
        ingredientUnitsSpinnerList.add(ingredient4Unit);
        ingredientUnitsSpinnerList.add(ingredient5Unit);
        ingredientUnitsSpinnerList.add(ingredient6Unit);
        ingredientUnitsSpinnerList.add(ingredient7Unit);
        ingredientUnitsSpinnerList.add(ingredient8Unit);
        ingredientUnitsSpinnerList.add(ingredient9Unit);
        ingredientUnitsSpinnerList.add(ingredient10Unit);

        ingredientCountsSpinnerList.add(ingredient1Count); //reference in array for easier processing
        ingredientCountsSpinnerList.add(ingredient2Count);
        ingredientCountsSpinnerList.add(ingredient3Count);
        ingredientCountsSpinnerList.add(ingredient4Count);
        ingredientCountsSpinnerList.add(ingredient5Count);
        ingredientCountsSpinnerList.add(ingredient6Count);
        ingredientCountsSpinnerList.add(ingredient7Count);
        ingredientCountsSpinnerList.add(ingredient8Count);
        ingredientCountsSpinnerList.add(ingredient9Count);
        ingredientCountsSpinnerList.add(ingredient10Count);
    }

    protected void checkNameRepeat(TextView v) {
        String in = v.getText().toString();                     //get the recipe name
        if (storage.getKeySize() == 0) {                         //if this is the first entry;
            taken = false;                                      //then set the taken flag to false
        } else {
            if (storage.getValue(in) != null) {                 //if there is a match
                taken = true;                                   //set flag to true
            } else {
                taken = false;
            }
        }
    }

    public void changeImageViaDevice(View view) {
        Intent getPicture = new Intent(Intent.ACTION_PICK);
        getPicture.setType("image/*");
        startActivityForResult(getPicture, RESULT_LOAD_IMG);
    }

    public void changeImageViaWeb(View view) {
        final ImageView recipeImage = (ImageView) findViewById(R.id.recipeImage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input image web URL");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    URL url = new URL(input.getText().toString());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    recipeImage.setImageBitmap(bmp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**Handles the intent results and changes the ImageView**/
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                ImageView recipeImage = findViewById(R.id.recipeImage);
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                recipeImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(NewDish.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(NewDish.this, "You haven't picked image",Toast.LENGTH_LONG).show();
        }
    }


    /** method that saves the information when save button is clicked **/
    protected void saveNewRecipe(View view) {
        if (!taken) {
            setIngredientsList(new ArrayList<String>(ingredientNames));
            final ImageView recipeImage = (ImageView) findViewById(R.id.recipeImage);
            Bitmap b = ((BitmapDrawable)recipeImage.getDrawable()).getBitmap();

            TextView recipeTitle = (TextView) findViewById(R.id.recipeName);
            String recipeName = recipeTitle.getText().toString();                                   //creates a string with the title;
            TextView dir = findViewById(R.id.direction);
            int count = 0;
            boolean flag = false;
            String builder = recipeName + "%" + dir.getText().toString() + "%" + encodeToBase64(b);                                              //first add description to the list
            if (!builder.isEmpty()) {
                builder += "%";
                flag = true;
            }

            for (Spinner s : ingredientNamesSpinnerList) {
                String ingr = s.getSelectedItem().toString();
                String ingrC = ingredientCountsSpinnerList.get(count).getText().toString();
                String ingrU = ingredientUnitsSpinnerList.get(count).getSelectedItem().toString();
                if (!ingr.equals(getString(R.string.pick_ingredient)) && !ingrC.trim().equals("") && !ingrU.equals("Unit")) {//if not default value, add it
                    builder += ingr + "%";
                    builder += ingrC + "%";
                    builder += ingrU + "%";
                    count++;
                }
            }
            if (count == 0 && !flag) {
                Toast.makeText(NewDish.this, "items and direction are both empty",Toast.LENGTH_LONG).show();
            } else if (count == 0) {
                Toast.makeText(NewDish.this, "items are empty",Toast.LENGTH_LONG).show();
            } else if (!flag) {
                Toast.makeText(NewDish.this, "direction is empty",Toast.LENGTH_LONG).show();
            } else {
                storage.setValue(recipeName,builder);
                Toast.makeText(NewDish.this, "Added new recipe",Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(NewDish.this, "The recipe name is taken or empty",Toast.LENGTH_LONG).show();
        }
    }

    public void addIngredient(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Ingredient");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Integer> temp = new ArrayList<>(); //remember currently set values
                for (Spinner s : ingredientNamesSpinnerList) {
                    temp.add(s.getSelectedItemPosition());
                }

                String ingredientName = input.getText().toString(); //new ingredient to add
                if (ingredientNames.contains(input.getText().toString())){
                    Toast.makeText(NewDish.this, "The ingredient has already been added", Toast.LENGTH_LONG).show();
                } else {
                    ingredientNames.add(ingredientName);
                }

                ingredientNamesSpinnerAdapter = new ArrayAdapter<String>(NewDish.this, android.R.layout.simple_spinner_item, ingredientNames.toArray(new String[ingredientNames.size()])); //update arrayadapter with new ingredient
                ingredientNamesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                int index = 0;
                for (Spinner s : ingredientNamesSpinnerList) { //update all spinner arrayAdapters and reapply preset values
                    s.setAdapter(ingredientNamesSpinnerAdapter);
                    s.setSelection(temp.get(index));
                    index++;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    private ArrayList<String> getIngredientsList() {
        ArrayList<String> returnMe = new ArrayList<>();
        try {
            FileInputStream inputStream = openFileInput("ingredients");
            ObjectInputStream in = new ObjectInputStream(inputStream);
            returnMe = (ArrayList<String>) in.readObject();
            in.close();
            inputStream.close();
        } catch (Exception e) {
            setIngredientsList(new ArrayList<>(ingredientNames));
            return new ArrayList<>(ingredientNames);
        }
        return returnMe;
    }

    private void setIngredientsList(ArrayList<String> ingredients) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("ingredients", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(ingredients);
            out.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
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