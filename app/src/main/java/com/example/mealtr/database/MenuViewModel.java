package com.example.mealtr.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mealtr.MenuFragment;
import com.example.mealtr.R;
import com.example.mealtr.mealGenerator.Meal;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    public LiveData<List<MenuDB>> allMenu;
    MenuDatabase menuDatabase;
    private long dietId;
    public MenuViewModel(Application application){
        super(application);
        menuDatabase = MenuDatabase.getInstance(application.getApplicationContext());
    }
    public LiveData<List<DietDB>> getAllDiet(){
        return menuDatabase.dietDAO().getDiets();
    }
    public MealDB getMealById(long id) { return menuDatabase.mealDAO().getMealById(id);}
    public LiveData<List<MenuDB>> getMenuByDietId(long dietId){
        return menuDatabase.menuDAO().getMenusFromDiet(dietId);
    }
    public LiveData<List<MealDB>> getMealsByMenuId(long menuId){
        return menuDatabase.mealDAO().getMealsFromMenu(menuId);
    }
    public RecipeDB getRecipeById(long id) {
        return menuDatabase.recipeDAO().getRecipeById(id);
    }

    public List<GeneratedRecipe> getRecipesByCalories(double calories) {
        return menuDatabase.recipeDAO().getRecipesWithCalories(calories);
    }

    public LiveData<List<RecipeDB>> getSimilarRecipeByCalories(double calories) {
        return menuDatabase.recipeDAO().getSimilarRecipeByCalories(calories);
    }

    public void insertRecipe(List<RecipeDB> recipe) {
        new InsertRecipeAsyncTask(menuDatabase.recipeDAO()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe);
    }

    private class InsertRecipeAsyncTask extends AsyncTask<List<RecipeDB>, Void, String>{
        RecipeDAO recipeDAO;
        public InsertRecipeAsyncTask(RecipeDAO recipeDAO){
            this.recipeDAO = recipeDAO;
        }
        @Override
        protected String doInBackground(List<RecipeDB>... recipeDBS) {
            recipeDAO.insertAll(recipeDBS[0]);
            return null;
        }
    }
    public long insertDiet(DietDB diet, List<MenuDB> menus, List<MealDB> meals, FragmentTransaction transaction){
        new InsertDietAsyncTask(menuDatabase.dietDAO(), menus, meals, transaction).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, diet);
        return dietId;
    }
    private class InsertDietAsyncTask extends AsyncTask<DietDB, Void, Long> {
        private DietDAO dietDAO;
        private List<MenuDB> menus;
        private List<MealDB> meals;
        private FragmentTransaction transaction;
        public InsertDietAsyncTask(DietDAO dietDAO, List<MenuDB> menus, List<MealDB> meals, FragmentTransaction transaction) {
            this.dietDAO = dietDAO;
            this.menus = menus;
            this.meals = meals;
            this.transaction = transaction;
        }

        @Override
        protected Long doInBackground(DietDB... dietDBS) {
            return dietDAO.insertDiet(dietDBS[0]);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            for(MenuDB menu : menus){
                menu.dietId = id;
                List<MealDB> todayMeals = new ArrayList<>(meals.subList(0,3));
                meals.removeAll(todayMeals);
                new InsertMenuAsyncTask(menuDatabase.menuDAO(), todayMeals).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, menu);
            }
            transaction.replace(R.id.main_content, new MenuFragment(id));
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    private class InsertMenuAsyncTask extends AsyncTask<MenuDB, Void, Long>{
        private MenuDAO menuDao;
        private List<MealDB> meals;
        public InsertMenuAsyncTask(MenuDAO menuDAO, List<MealDB> meals) {
            this.menuDao = menuDAO;
            this.meals = meals;
        }
        @Override
        protected Long doInBackground(MenuDB... menuDBS) {
            return menuDao.insertMenu(menuDBS[0]);
        }
        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            for(MealDB meal : meals){
                meal.menuId = id;
                new InsertMealAsyncTask(menuDatabase.mealDAO()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, meal);
            }
        }
    }
    private class InsertMealAsyncTask extends AsyncTask<MealDB, Void, Void>{
        private MealDAO mealDAO;
        public InsertMealAsyncTask(MealDAO mealDAO) {
            this.mealDAO = mealDAO;
        }
        @Override
        protected Void doInBackground(MealDB... mealDBS) {
            mealDAO.insertMeal(mealDBS[0]);
            return null;
        }
    }
    public void deleteDiet(long id){
        new DeleteDietAsyncTask(menuDatabase.dietDAO()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
        menuDatabase.dietDAO().deleteDiet(id);
    }
    private class DeleteDietAsyncTask extends AsyncTask<Long, Void, Void>{
        private DietDAO dietDAO;
        private long dietId;
        public DeleteDietAsyncTask(DietDAO dietDAO) {
            this.dietDAO = dietDAO;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            dietDAO.deleteDiet(longs[0]);
            this.dietId = longs[0];
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            new DeleteMenuAsyncTask(menuDatabase.menuDAO()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.dietId);
        }
    }
    private class DeleteMenuAsyncTask extends AsyncTask<Long, Void, Void>{
        private MenuDAO menuDAO;
        private List<MenuDB> deletedMenus;
        public DeleteMenuAsyncTask(MenuDAO menuDAO) {
            this.menuDAO = menuDAO;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            this.deletedMenus = menuDAO.getMenusFromDietStatic(longs[0]);
            menuDAO.deleteMenu(longs[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            new DeleteMealAsyncTask(menuDatabase.mealDAO()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.deletedMenus);
        }
    }
    private class DeleteMealAsyncTask extends AsyncTask<List<MenuDB>, Void, Void>{
        private MealDAO mealDAO;
        public DeleteMealAsyncTask(MealDAO mealDAO) {
            this.mealDAO = mealDAO;
        }

        @Override
        protected Void doInBackground(List<MenuDB>... lists) {
            for(MenuDB meal: lists[0]){
                mealDAO.deleteMealByMenuId(meal.menuId);
            }
            return null;
        }
    }

    public void updateMeal(MealDB mealDB) {
        menuDatabase.mealDAO().updateMeal(mealDB);
    }
}

