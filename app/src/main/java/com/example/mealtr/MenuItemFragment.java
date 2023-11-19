package com.example.mealtr;

import android.app.Activity;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mealtr.mealGenerator.ChildCallback;
import com.example.mealtr.mealGenerator.Meal;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuItemFragment extends Fragment {
    Meal meal;
    Activity context;
    private ChildCallback callback;

    public MenuItemFragment(Meal meal) {
        this.meal = meal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        return inflater.inflate(R.layout.fragment_menu_item, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView type = view.findViewById(R.id.mealType);
        type.setText(this.meal.type);
        TextView title = view.findViewById(R.id.mealTitle);
        title.setText(this.meal.title);
        ImageView image = view.findViewById(R.id.meal_image);
        new LoadImageTask(image).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, this.meal.image);

        Button btnViewRecipe = view.findViewById(R.id.btnViewRecipe);
        btnViewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(meal.sourceUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnChangeRecipe = view.findViewById(R.id.btnChangeRecipe);
        btnChangeRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getParentFragment().getParentFragment().getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.main_content, new AlternateMenuList(meal.calories, meal.id));
                transaction.addToBackStack(null);
                transaction.commit();
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

}