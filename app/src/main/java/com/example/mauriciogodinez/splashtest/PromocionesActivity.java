package com.example.mauriciogodinez.splashtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.AccessToken;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

import java.util.ArrayList;

public class PromocionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PromocionesAdapter.ListItemClickListener {

    private Toast mToast;
    ProfileTracker profileTracker;
    PromocionesAdapter mAdapter;
    ArrayList<PromocionesItem> mArrayList;
    ArrayList<PromocionesItem> mListIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.promociones_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mArrayList = createListPromo();
        mListIntent = mArrayList;
        mAdapter = new PromocionesAdapter(mArrayList, this);
        mRecyclerView.setAdapter(mAdapter);


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() != null) {
            // If there is an access token then Login Button was used
            // Check if the profile has already been fetched
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                displayProfileInfo(currentProfile);
            } else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        } else {
            // Otherwise, get Account Kit login information
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // get Account Kit ID
                    String accountKitId = account.getId();

                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    if (account.getPhoneNumber() != null) {
                        // if the phone number is available, display it
                        String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                        mToast = Toast.makeText(PromocionesActivity.this, accountKitId + formattedPhoneNumber, Toast.LENGTH_LONG);
                        mToast.show();
                    } else {
                        // if the email address is available, display it
                        String emailString = account.getEmail();
                        mToast = Toast.makeText(PromocionesActivity.this, accountKitId + emailString, Toast.LENGTH_LONG);
                        mToast.show();
                    }

                }

                @Override
                public void onError(final AccountKitError error) {
                    String toastMessage = error.getErrorType().getMessage();
                    Toast.makeText(PromocionesActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // unregister the profile tracker receiver
        profileTracker.stopTracking();
    }

    private void displayProfileInfo(Profile profile) {
        // get Profile ID
        String profileId = profile.getId();

        // display the Profile name
        String name = profile.getName();

        mToast = Toast.makeText(PromocionesActivity.this, profileId + name, Toast.LENGTH_LONG);
        mToast.show();

//        display the profile picture
//        Uri profilePicUri = profile.getProfilePictureUri(100, 100);
//        displayProfilePic(profilePicUri);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.promociones, menu);

        MenuItem search = menu.findItem(R.id.search);
        search.collapseActionView();
        final android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(search);

        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<PromocionesItem> promociones = new ArrayList<>();
                for (PromocionesItem promocion : mArrayList) {
                    String name = promocion.getTitleItem().toLowerCase();
                    if (name.contains(newText)) {
                        promociones.add(promocion);

                    }
                }
                mAdapter.setFilter(promociones);
                mListIntent = promociones;

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AccountKit.logOut();
            // logout of Login Button
            LoginManager.getInstance().logOut();

            launchLoginActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<PromocionesItem> createListPromo() {

        ArrayList<PromocionesItem> result = new ArrayList<>();

        result.add(new PromocionesItem(R.drawable.promopapajohns,
                getResources().getString(R.string.papa_johns),
                getResources().getString(R.string.contenido_papa_johns)));
        result.add(new PromocionesItem(R.drawable.promoidea,
                getResources().getString(R.string.idea_interior),
                getResources().getString(R.string.contenido_idea_interior)));
        result.add(new PromocionesItem(R.drawable.promoburguerking,
                getResources().getString(R.string.burguer_king),
                getResources().getString(R.string.contenido_burguer_king)));
        result.add(new PromocionesItem(R.drawable.promobenavides,
                getResources().getString(R.string.farmacias_benavides),
                getResources().getString(R.string.contenido_farmacias_benavides)));
        result.add(new PromocionesItem(R.drawable.promotizoncito,
                getResources().getString(R.string.el_tizoncito),
                getResources().getString(R.string.contenido_el_tizoncito)));
        result.add(new PromocionesItem(R.drawable.promochilis,
                getResources().getString(R.string.chillis),
                getResources().getString(R.string.contenido_chillis)));
        result.add(new PromocionesItem(R.drawable.promozonafitness,
                getResources().getString(R.string.zona_fitness),
                getResources().getString(R.string.contenido_zona_fitness)));
        result.add(new PromocionesItem(R.drawable.promocinepolis,
                getResources().getString(R.string.cinepolis),
                getResources().getString(R.string.contenido_cinepolis)));
        result.add(new PromocionesItem(R.drawable.promowingstop,
                getResources().getString(R.string.wingstop),
                getResources().getString(R.string.contenido_wingstop)));
        result.add(new PromocionesItem(R.drawable.promoitaliannis,
                getResources().getString(R.string.italiannis),
                getResources().getString(R.string.contenido_italiannis)));
        return result;

    }

    @Override
    public void onListItemClick(int clickedItemIndex, String text) {
        Context vhContext = getApplicationContext();
        PromocionesItem imagen = mListIntent.get(clickedItemIndex);
        Integer drawableImage = imagen.getImagenItem();

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(vhContext, "click imagen " + text, Toast.LENGTH_SHORT);
        mToast.show();

        Intent detalle = new Intent(getBaseContext(), DetallePromocionActivity.class);
        detalle.putExtra("imagen", drawableImage);
        startActivity(detalle);

    }

}
