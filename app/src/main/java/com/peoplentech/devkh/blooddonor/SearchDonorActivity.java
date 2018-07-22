package com.peoplentech.devkh.blooddonor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.peoplentech.devkh.blooddonor.Adapter.BloodDonorAdapter;
import com.peoplentech.devkh.blooddonor.model.ResObj;
import com.peoplentech.devkh.blooddonor.model.User;
import com.peoplentech.devkh.blooddonor.model.Users;
import com.peoplentech.devkh.blooddonor.remote.ApiUtils;
import com.peoplentech.devkh.blooddonor.remote.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchDonorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerViewUsers;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //recyclerview code
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerviewDonor);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final UserService service = retrofit.create(UserService.class);

       //Spinner Code
        Spinner spinnerCustom= (Spinner) findViewById(R.id.donorSpinner);
        // Spinner Drop down elements
        ArrayList<String> areas = new ArrayList<String>();
        areas.add("Select Blood Group");
        areas.add("A+");
        areas.add("A-");
        areas.add("AB+");
        areas.add("AB-");
        areas.add("B+");
        areas.add("B-");
        areas.add("O+");
        areas.add("O-");

        CustomSpinnerAdapter customSpinnerAdapter= new CustomSpinnerAdapter(this, areas);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Call<Users> call = service.getUsers();

                    call.enqueue(new Callback<Users>() {

                        @Override
                        public void onResponse(Call<Users> call, retrofit2.Response<Users> response) {
                            adapter = new BloodDonorAdapter(response.body().getUsers(), getApplicationContext());
                            recyclerViewUsers.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {

                        }
                    });
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    //Post
                    Call<ResObj> call = service.getDonor(item);

                    call.enqueue(new Callback<ResObj>() {

                        @Override
                        public void onResponse(Call<ResObj> call, retrofit2.Response<ResObj> response) {
                            adapter = new BloodDonorAdapter(response.body().getUsers(), getApplicationContext());
                            recyclerViewUsers.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<ResObj> call, Throwable t) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Side Navigation Code
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        getMenuInflater().inflate(R.menu.search_donor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public void onRefresh() {
        ConnectivityManager conntivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conntivityManager != null && (conntivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                conntivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            //We are connected to Internet
            recyclerViewUsers.setAdapter(null);
            loadData();
        }
    }*/

    private void loadData() {
        //recyclerview code
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerviewDonor);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(SearchDonorActivity.this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final UserService service = retrofit.create(UserService.class);

        //Spinner Code
        Spinner spinnerCustom= (Spinner) findViewById(R.id.donorSpinner);
        // Spinner Drop down elements
        ArrayList<String> areas = new ArrayList<String>();
        areas.add("Select Blood Group");
        areas.add("A+");
        areas.add("A-");
        areas.add("AB+");
        areas.add("AB-");
        areas.add("B+");
        areas.add("B-");
        areas.add("O+");
        areas.add("O-");

        CustomSpinnerAdapter customSpinnerAdapter= new CustomSpinnerAdapter(this, areas);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Call<Users> call = service.getUsers();

                    call.enqueue(new Callback<Users>() {

                        @Override
                        public void onResponse(Call<Users> call, retrofit2.Response<Users> response) {
                            adapter = new BloodDonorAdapter(response.body().getUsers(), getApplicationContext());
                            recyclerViewUsers.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {

                        }
                    });
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    //Post
                    Call<ResObj> call = service.getDonor(item);

                    call.enqueue(new Callback<ResObj>() {

                        @Override
                        public void onResponse(Call<ResObj> call, retrofit2.Response<ResObj> response) {
                            adapter = new BloodDonorAdapter(response.body().getUsers(), getApplicationContext());
                            recyclerViewUsers.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<ResObj> call, Throwable t) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //Adapter for our custom spinner
    //This is an inner class for the ease of coding
    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(SearchDonorActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txt.setText(asr.get(position));
            if (position > 0) {
                txt.setTextColor(Color.parseColor("#000000"));
            } else {
                txt.setTextColor(Color.parseColor("#808080"));
            }
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(SearchDonorActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(22);
            //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop, 0);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#ffffff"));
            return txt;
        }
    }
}
