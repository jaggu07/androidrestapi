package com.example.restapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText searchText;
    Button searchBtn;
    TextView userList;
    RequestQueue requestQueue;

    String baseUrl = "http://dummy.restapiexample.com/api/v1/employees";  // This is the API base URL (GitHub API)
    String url;  // This will hold the full URL which will include the username entered in the etGitHubUser.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.searchText = (EditText) findViewById(R.id.et_github_user);
        this.searchBtn = (Button) findViewById(R.id.btn_get_repos);
        this.userList = (TextView) findViewById(R.id.tv_repo_list);
        this.userList.setMovementMethod(new ScrollingMovementMethod());

        requestQueue = Volley.newRequestQueue(this);
    }
    private void clearQueue(){
        this.searchText.setText("");
    }
    private void addToRepoList(String repoName, String lastUpdated) {
        // This will add a new repo to our list.
        // It combines the repoName and lastUpdated strings together.
        // And then adds them followed by a new line (\n\n make two new lines).
        String strRow = repoName + " / " + lastUpdated;
        String currentText = userList.getText().toString();
        this.userList.setText(currentText + "\n\n" + strRow);
    }

    private void setRepoListText(String str) {
        // This is used for setting the text of our repo list box to a specific string.
        // We will use this to write a "No repos found" message if the user doens't have any.
        this.userList.setText(str);
    }


    private void getRepoList(String username){
        this.url = this.baseUrl+username+"/repos";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, this.baseUrl,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject obj = new JSONObject((Map) response);
                        Log.d("resp", String.valueOf(obj));
                        if (response.length() > 0) {

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String repoName = jsonObject.get("name").toString();
                                    String lastupdated = jsonObject.get("updated_at").toString();
                                    Log.d("resp", jsonObject.toString());
                                    //addToRepoList(repoName, lastupdated);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("Volley", "Invalid JSON Object.");
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue.add(arrayRequest);
    }
    public void getReposClicked(View v) {
        // Clear the repo list (so we have a fresh screen to add to)
        clearQueue();
        // Call our getRepoList() function that is defined above and pass in the
        // text which has been entered into the etGitHubUser text input field.
        getRepoList(searchText.getText().toString());
    }
}
