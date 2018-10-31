package com.androidboiz.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class RecipesListDetailFragment extends Fragment {
    public ArrayList<String> recipes;
    public ArrayList<String> recipeNames;
    public String currentDishName;
    public String currentDish;

    private ListView list;
    private TextView name;
    private TextView ingredients;
    private TextView description;
    private ImageView image;

    public RecipesListDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();

        recipes = new ArrayList<>();
        recipeNames = new ArrayList<>();
        currentDishName = "";
        currentDish = "";

        View v = inflater.inflate(R.layout.fragment_recipes, container, false);
        if (b != null) {
            recipes = (ArrayList<String>)b.getSerializable("recipes");
            for (String s : recipes) {
                recipeNames.add(s.substring(0, s.indexOf('%')));
            }
        }

        list = (ListView)v.findViewById(R.id.recipesListViewLandscape);
        name = (TextView)v.findViewById(R.id.recipeDetailName);
        ingredients = (TextView)v.findViewById(R.id.recipeDetailIngredients);
        description = (TextView)v.findViewById(R.id.recipeDetailDirections);
        image = (ImageView)v.findViewById(R.id.recipeDetailPic);

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, recipeNames);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                for(String s : recipes) {
                    String name = s.substring(0, s.indexOf('%'));
                    if (selected.equals(name)) {
                        currentDish = s;
                        break;
                    }
                }
                String[] split = currentDish.split("%");
                name.setText(split[0]);
                description.setText("Directions:\n" + split[1]);
                image.setImageBitmap(decodeToBase64(split[2]));
                String ingBuilder = "Ingredients:\n";
                for (int j = 3; j < split.length; j+=3) {
                    ingBuilder += split[j];
                    ingBuilder += " (";
                    ingBuilder += split[j+1];
                    ingBuilder += " ";
                    ingBuilder += split[j+2];
                    ingBuilder += ")\n";
                }
                ingredients.setText(ingBuilder);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
    }

    public Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}