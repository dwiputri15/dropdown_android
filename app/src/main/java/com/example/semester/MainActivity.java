package com.example.semester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.layout.simple_spinner_item;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SpinnerModel> goodModelArrayList;
    private ArrayList<String> playerName = new ArrayList<String>();
    private ArrayList<String> playerSemester = new ArrayList<String>();
    private Spinner spinner1, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = findViewById(R.id.spsemester);
        spinner2 = findViewById(R.id.spmatakuliah);
        fetchJSON();
    }

    private void fetchJSON() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SpinnerInterface.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpinnerInterface api = retrofit.create(SpinnerInterface.class);

        Call<String> call = api.getJSONString();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        spinJSON(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void spinJSON(String jsonresponse) {

        try {
            JSONObject obj = new JSONObject(jsonresponse);
            if (obj.optString("status").equals("true")) {
                goodModelArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    SpinnerModel spinnerModel = new SpinnerModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    spinnerModel.setNama(dataobj.getString("nama"));
                    spinnerModel.setSemester(dataobj.getString("semester"));

                    goodModelArrayList.add(spinnerModel);
                }

                for (int i = 0; i < goodModelArrayList.size(); i++) {
                    playerName.add(goodModelArrayList.get(i).getNama().toString());
                    playerSemester.add(goodModelArrayList.get(i).getSemester().toString());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, simple_spinner_item,playerName);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner1.setAdapter(spinnerArrayAdapter);
            }
        } catch ( JSONException e){
            e.printStackTrace();
        }

    }
}