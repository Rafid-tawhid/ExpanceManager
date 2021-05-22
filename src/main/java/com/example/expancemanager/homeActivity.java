package com.example.expancemanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class homeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;


    //fragment

    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpensesFragment expensesFragment;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=findViewById(R.id.drawer_id);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.navVIewid);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView=findViewById(R.id.nav_id);
        frameLayout=findViewById(R.id.main_frame);

        dashboardFragment=new DashboardFragment();
        incomeFragment=new IncomeFragment();
        expensesFragment=new ExpensesFragment();
        setFragemnt(dashboardFragment);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        setFragemnt(dashboardFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.colorAccent);
                        return true;
                    case R.id.navigation_add:
                        setFragemnt(incomeFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        return true;
                    case R.id.navigation_minus:
                        setFragemnt(expensesFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.colorPrimaryDark);



                        return true;
                    default: return false;
                }





            }
        });

    }

    private void setFragemnt(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_id);



        if (drawerLayout.isDrawerOpen(GravityCompat.END))
        {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else
        {
            super.onBackPressed();
        }


    }






    private void displaySelectedItemListener(int itemId) {
        Fragment fragment=null;
        switch (itemId)
        {
            case R.id.dash1:
                fragment=new DashboardFragment();

            break;
            case R.id.income1:
                fragment=new IncomeFragment();

                break;
            case R.id.expenses1:
                fragment=new ExpensesFragment();
                break;
            case R.id.menu_options:
                Intent intent=new Intent(homeActivity.this,menuCardView.class);
                startActivity(intent);
                break;
        }
        if (fragment!=null)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();

        }
        DrawerLayout drawerLayout=findViewById(R.id.drawer_id);
        drawerLayout.closeDrawer(GravityCompat.START);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedItemListener(item.getItemId());
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}