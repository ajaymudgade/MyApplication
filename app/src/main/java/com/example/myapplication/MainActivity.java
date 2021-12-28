package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Model> list = new ArrayList<>();
    String url = "https://jsonplaceholder.typicode.com/posts";
    Adapter adapter;
    AutoCompleteTextView editText;
    List<Model> filterList = new ArrayList<>();
    RadioButton radioButton1, radioButton2;
    Button postButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        postButton = findViewById(R.id.postButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (list.equals(list)){
                    showKeyboard(editText);
                    recyclerView.setAdapter(new Adapter(getApplicationContext(), filterList, new Adapter.OnItemClick() {
                        @Override
                        public void OnItemClick(String userId, String title, String postDesc) {
                            editText.setText(userId + "\n" + title + "\n" + postDesc);
                        }
                    }));
                    recyclerView.setVisibility(View.VISIBLE);


                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList.clear();
                showKeyboard(editText);

                if (s.toString().isEmpty()) {
                    recyclerView.setAdapter(new Adapter(getApplicationContext(), filterList, new Adapter.OnItemClick() {
                        @Override
                        public void OnItemClick(String userId, String title, String postDesc) {
                            editText.setText(userId + "\n" + title + "\n" + postDesc);
                        }
                    }));
                    recyclerView.setVisibility(View.GONE);

                } else {
                    Filter(s.toString());
                }

            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( MainActivity.this, PostBtnActivity.class );
                intent.putExtra ( "TextBox", editText.getText().toString() );
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Posted successfully", Toast.LENGTH_LONG).show();
            }
        });



        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!radioButton1.isSelected()){
                            radioButton1.setChecked(true);
                            radioButton1.setSelected(true);
                            radioButton2.setChecked(false);
                            radioButton2.setSelected(false);
                        }
                        else if (!radioButton2.isSelected()){
                            radioButton2.setChecked(true);
                            radioButton2.setChecked(true);
                            radioButton1.setSelected(false);
                            radioButton1.setChecked(false);
                        }
                    }
                });

                if (!radioButton1.isSelected()){
                    radioButton1.setChecked(true);
                    radioButton1.setSelected(true);
                    radioButton2.setChecked(false);
                    radioButton2.setSelected(false);
                }
                else if (!radioButton2.isSelected()){
                    radioButton2.setChecked(true);
                    radioButton2.setChecked(true);
                    radioButton1.setSelected(false);
                    radioButton1.setChecked(false);
                }

            }
        });

        GetData();

    }


    private void Filter(String text) {
        for (Model model: list) {
            if (Integer.toString(model.getUserId()).equals(text)){
                filterList.add(model);
            }
        }
        recyclerView.setAdapter(new Adapter(getApplicationContext(), filterList, new Adapter.OnItemClick() {
            @Override
            public void OnItemClick(String userId, String title, String postDesc) {
                editText.setText(userId + "\n" + title + "\n" + postDesc);
            }
        }));
        adapter.notifyDataSetChanged();
    }


    private void hideKeyboard(EditText etWriteQuestion) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        editText.requestFocus();
    }

    private void showKeyboard(EditText etWriteQuestion) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText.getRootView(), InputMethodManager.SHOW_IMPLICIT);
        editText.requestFocus();
    }






    private void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i<=response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        list.add(new Model(
                                jsonObject.getInt("userId"),
                                jsonObject.getString("title"),
                                jsonObject.getString("body")
                        ));


                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                    adapter = new Adapter(getApplicationContext(), list, new Adapter.OnItemClick() {
                        @Override
                        public void OnItemClick(String userId, String title, String postDesc) {
                            editText.setText(userId + "\n" + title + "\n" + postDesc);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonArrayRequest);

    }
    
    
}