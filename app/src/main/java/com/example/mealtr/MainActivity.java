package com.example.mealtr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;

import com.example.mealtr.database.DietDB;
import com.example.mealtr.database.GeneratedRecipe;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.database.RecipeDB;
import com.example.mealtr.databinding.ActivityMainBinding;
import com.example.mealtr.mealGenerator.AddAllRecipe;
import com.example.mealtr.waterTracker.WaterBroadcast;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.navbar.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.navbar.toolbar, R.string.menu_open, R.string.menu_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        menuViewModel.getAllDiet().observe(this, new Observer<List<DietDB>>() {
            @Override
            public void onChanged(List<DietDB> dietDBS) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(dietDBS.size()>0){
                    transaction.replace(R.id.main_content, new MenuListFragment());
                    transaction.commit();
                } else {
                    transaction.replace(R.id.main_content, new HomeFragment());
                    transaction.commit();
                }
            }
        });

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager manager = getSupportFragmentManager();
                for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
                    manager.popBackStack();
                }

                FragmentTransaction transaction = manager.beginTransaction();

                int id = item.getItemId();

                if(id==R.id.menu_home){
                    transaction.replace(R.id.main_content, new HomeFragment());
                    transaction.commit();
                } else if (id==R.id.menu_generate_menu) {
                    transaction.replace(R.id.main_content, new MenuListFragment());
                    transaction.commit();
                } else if (id == R.id.menu_water_tracking) {
                    transaction.replace(R.id.main_content, new WaterTrackingFragment());
                    transaction.commit();
                }

                binding.drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        AddAllRecipe defaultAdd = new AddAllRecipe(getApplicationContext(), R.raw.allrecipes,menuViewModel);
        defaultAdd.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        notificationChannel();

        Intent intent = new Intent(MainActivity.this, WaterBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 30, pendingIntent);
    }

    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount() > 0){
                manager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void notificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel("WATER_NOTIFICATION");
            if(channel == null) {
                channel = new NotificationChannel("WATER_NOTIFICATION", "WaterTracker", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
        }
    }
}