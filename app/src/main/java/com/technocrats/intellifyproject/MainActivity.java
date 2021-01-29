package com.technocrats.intellifyproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ArrayList<RestaurantItem> mRestaurantList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.Zomato);
        mRecyclerView = findViewById(R.id.recyclerView1);
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantList = new ArrayList<>();


        progressBar.setVisibility(View.VISIBLE);
        String query = "https://developers.zomato.com/api/v2.1/search?q=delhi" ;
        new MainActivity.ApiTask().execute(query);


    }
    class ApiTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String API_KEY = "3729afe4d7a3e36652605386d15e6792";

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("user-key", API_KEY);
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                String MyStr = "";
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("restaurants");
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    JSONObject ultraFinal = finalObject.getJSONObject("restaurant");
                    String name = ultraFinal.getString("name");

                    JSONObject promiseLast = ultraFinal.getJSONObject("location");
                    String address = promiseLast.getString("address");
                    String cuisines = ultraFinal.getString("cuisines");

                    mRestaurantList.add(new RestaurantItem(name, address, cuisines));

                }

                return MyStr;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mRestaurantList != null) {
                mRestaurantAdapter = new RestaurantAdapter(MainActivity.this, mRestaurantList);
                mRecyclerView.setAdapter(mRestaurantAdapter);
                progressBar.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRestaurantAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


}