package com.example.sbendakhlia.rapace;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class NotifAction extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar tb;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_action);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        drawer = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.visualisation:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new VisualisationFragment()).commit();
                        break;

                    case R.id.appel:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new AppelFragment()).commit();
                        break;

                    case R.id.affichage_map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new AffichageMapFragment()).commit();
                        break;

                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //To not reset the activity when we change orientation for example
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new VisualisationFragment()).commit();
            nv.setCheckedItem(R.id.visualisation);
        }

        //FragmentManager fm = getFragmentManager();
        //FragmentTransaction ft = fm.beginTransaction();
        //Initiate class FragmentTest ftt = new FragmentTest();
        //ft.add(R.id.frag_container,ftt);
        //ft.commit();
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}
