package com.example.restapidemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button searchBtn;

    RequestQueue requestQueue;
    ListView listView;
    ArrayAdapter<String> adapter;

    //String baseUrl = "https://api.github.com/users/";  // This is the API base URL (GitHub API)
    String baseUrl = "https://dummy.restapiexample.com/api/v1/employees";
    String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.
    String[] countryList = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.searchBtn = (Button) findViewById(R.id.btn_get_repos);
        // this.listView = (ListView) findViewById(R.id.simpleListView);
        this.listView = (ListView) findViewById(R.id.simpleListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_employee, R.id.textView, countryList);
        //this.listView.setAdapter(arrayAdapter);
        requestQueue = Volley.newRequestQueue(this);
    }


    public void getReposClicked(View v) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.baseUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                list.add(obj.getString("employee_name"));
                            }
                            listItem(list);
                            //listView.setAdapter(new ArrayAdapter<Array>(data));

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void listItem(ArrayList<String> item) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_employee, R.id.textView, item);
        Log.d("item", item.toString());
        this.listView.setAdapter(arrayAdapter);
    }
}
