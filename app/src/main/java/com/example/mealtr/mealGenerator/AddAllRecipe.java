package com.example.mealtr.mealGenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mealtr.R;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.database.RecipeDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AddAllRecipe extends AsyncTask<String, Void, String> {

    Context context;
    MenuViewModel menuViewModel;

    int id;
    int i;
    boolean end;
    public AddAllRecipe(Context context, int id, MenuViewModel menuViewModel) {
        this.context = context;
        this.menuViewModel = menuViewModel;
        this.id = id;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            InputStream inputStream = context.getResources().openRawResource(id);

            // Read the content of the file into a string
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Close the input stream and reader
            inputStream.close();
            bufferedReader.close();

            String response = stringBuilder.toString();

            JSONArray returnedResponse = new JSONArray(response);

            List<RecipeDB> recipes = new ArrayList<>();
            for(int i = 0; i < returnedResponse.length(); i++){
                JSONObject r = returnedResponse.getJSONObject(i);
                RecipeDB recipe = new RecipeDB(
                        r.getLong("recipeId"),
                        r.getString("type"),
                        r.getString("image"),
                        r.getString("title"),
                        r.getString("sourceUrl"),
                        r.getDouble("calories"),
                        r.getDouble("protein"),
                        r.getDouble("fat")
                );
                recipes.add(recipe);
            }
            menuViewModel.insertRecipe(recipes);

            return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
