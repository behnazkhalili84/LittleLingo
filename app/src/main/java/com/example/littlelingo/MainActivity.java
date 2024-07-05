package com.example.littlelingo;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.littlelingo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.stripe.android.PaymentConfiguration;
import com.example.littlelingo.ui.SharedViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        PaymentConfiguration.init(
                getApplicationContext(),
                "your-publishable-key-from-stripe"
        );

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_vocabularylearning, R.id.nav_vocabulayquiz,R.id.nav_grammarLearning, R.id.sign_out, R.id.nav_shopping, R.id.nav_profile,R.id.nav_shopping)

                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //for sign out
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.sign_out) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                    return true;
                }else {
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

                        DrawerLayout drawer = binding.drawerLayout;
                        drawer.closeDrawer(GravityCompat.START);

                    return handled;
                }

            }
        });

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // get arg from intent
        String userName = getIntent().getStringExtra("name");
        String userID = getIntent().getStringExtra("userID");

        sharedViewModel.setName(userName);
        sharedViewModel.setUserID(userID);

//        Bundle bundle = new Bundle();
//        bundle.putString("name", userName);
//        bundle.putString("userID", userID);
        //navController.navigate(R.id.nav_home, bundle);
        // pass bundle args to the starting navigation graph
        //navController.setGraph(navController.getGraph(), bundle);
        // Set values in SharedViewModel

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