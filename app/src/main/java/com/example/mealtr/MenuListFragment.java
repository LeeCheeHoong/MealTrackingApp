package com.example.mealtr;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mealtr.database.DietDB;
import com.example.mealtr.database.MealDB;
import com.example.mealtr.database.MenuDB;
import com.example.mealtr.database.MenuDatabase;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.mealGenerator.ChildCallback;

import java.util.List;

public class MenuListFragment extends Fragment {

    MenuViewModel menuViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getChildFragmentManager();

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        }
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.getAllDiet().observe(getViewLifecycleOwner(), new Observer<List<DietDB>>() {
            @Override
            public void onChanged(List<DietDB> dietDBS) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                for(DietDB diet : dietDBS){
                    GeneratedMenuFragment m = GeneratedMenuFragment.newInstance(diet);
                    m.setCallback(new ChildCallback() {
                        @Override
                        public void onDeleteClicked(long id) {
                            new DeleteDietAsync(menuViewModel,manager,transaction).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
                        }
                    });
                    transaction.add(R.id.generatedMenuContainer, m, String.valueOf(diet.dietId));
                }
                transaction.commit();
            }
        });
    }

    private class DeleteDietAsync extends AsyncTask<Long, Void, Void>{
        MenuViewModel menuViewModel;
        FragmentManager manager;
        FragmentTransaction transaction;
        long dietId;

        public DeleteDietAsync(MenuViewModel menuViewModel, FragmentManager manager, FragmentTransaction transaction){
            this.menuViewModel = menuViewModel;
            this.manager = manager;
            this.transaction = transaction;
        }
        @Override
        protected Void doInBackground(Long... longs) {
            menuViewModel.deleteDiet(longs[0]);
            dietId = longs[0];
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            Fragment childFragment = manager.findFragmentByTag(String.valueOf(dietId));

            if(childFragment != null){
                manager.beginTransaction().remove(childFragment).commit();
            }
        }
    }
}