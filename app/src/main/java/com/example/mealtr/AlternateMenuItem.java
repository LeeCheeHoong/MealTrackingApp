package com.example.mealtr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mealtr.database.MealDB;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.database.RecipeDB;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlternateMenuItem extends Fragment {
    RecipeDB recipe;
    long mealId;
    public AlternateMenuItem(RecipeDB recipe, long mealId) {
        this.recipe = recipe;
        this.mealId = mealId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alternate_menu_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        TextView title = view.findViewById(R.id.alt_mealTitle);
        title.setText(this.recipe.title);
        ImageView image = view.findViewById(R.id.alt_meal_image);
        new LoadImageTask(image).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, this.recipe.image);

        Button btnViewRecipe = view.findViewById(R.id.alt_btnViewRecipe);
        btnViewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(recipe.sourceUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnChangeRecipe = view.findViewById(R.id.alt_btnChangeRecipe);
        btnChangeRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateMealAsync(menuViewModel).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView img;
        public LoadImageTask(ImageView img) {
            this.img = img;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                // Create a URL object from the given URL string
                URL url = new URL(params[0]);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Get the input stream from the connection
                InputStream input = connection.getInputStream();

                // Decode the input stream into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Close the input stream and disconnect the connection
                input.close();
                connection.disconnect();

                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Set the Bitmap to the ImageView
                img.setImageBitmap(result);
            }
        }
    }
    private class UpdateMealAsync extends AsyncTask<Long, Void, Void> {
        MenuViewModel menuViewModel;
        public UpdateMealAsync(MenuViewModel menuViewModel) {
            this.menuViewModel = menuViewModel;
        }
        @Override
        protected Void doInBackground(Long... longs) {
            MealDB oldRecipe = menuViewModel.getMealById(mealId);
            oldRecipe.recipeId = recipe.recipeId;
            oldRecipe.image = recipe.image;
            oldRecipe.title = recipe.title;
            oldRecipe.sourceUrl = recipe.sourceUrl;
            oldRecipe.calories = recipe.calories;
            oldRecipe.protein = recipe.protein;
            oldRecipe.fat = recipe.fat;
            menuViewModel.updateMeal(oldRecipe);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            getActivity().onBackPressed();
        }
    }

}