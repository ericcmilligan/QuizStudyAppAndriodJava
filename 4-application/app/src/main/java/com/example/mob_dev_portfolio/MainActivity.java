package com.example.mob_dev_portfolio;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.mob_dev_portfolio.Database.QuizDatabase;
import com.example.mob_dev_portfolio.Entities.Highscore;
import com.example.mob_dev_portfolio.Entities.Tag;
import com.example.mob_dev_portfolio.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Insert a default tag into the database if it does not exist
        QuizDatabase db = QuizDatabase.getInstance(getApplicationContext());
        this.executor = Executors.newFixedThreadPool(4);
        executor.execute(new Runnable() {
                             @Override
                             public void run() {
                                 Tag defaultTagExists =  db.tagDao().checkIfDefaultTagExists("Default");
                                 if(defaultTagExists == null){
                                     db.tagDao().insertAll(new Tag("Default"));
                                 }
                                 //Initialize high-score for default tag if not created
                                 Tag createdTag = db.tagDao().getTagByID(db.tagDao().getTagByName("Default").getTagID());
                                 Integer highScore =  db.highScoreDao().getHighScorePointsByTagID(createdTag.getTagID());

                                 //If high-score is null create a new record for high-score for this tag
                                 if(highScore == null){
                                     db.highScoreDao().insertAll(
                                             new Highscore(createdTag.getTagID(), 0, LocalDateTime.now())
                                     );
                                 }
                             }
                         });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set up nav drawer
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_question_manager, R.id.nav_quiz_category, R.id.nav_high_score)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}