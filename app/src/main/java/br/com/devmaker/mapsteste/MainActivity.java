package br.com.devmaker.mapsteste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.devmaker.mapsteste.adapter.RelatorioAdapter;
import br.com.devmaker.mapsteste.model.Street;


public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipe;
    String street = "";
    ArrayList<Street> ruas = new ArrayList<>();
    Gson gson = new Gson();
    RelatorioAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        listView = (ListView) findViewById(R.id.listView);




        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requesVolleyGet();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Street street = new Street();
                street = (Street) parent.getItemAtPosition(position);
                Intent it = new Intent(getBaseContext(),MapsActivity.class);
                it.putExtra("obj",street);
                startActivity(it);

            }
        });

        requesVolleyGet();
    }

    public void requesVolleyGet(){
        ruas.clear();
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Atualizando dados...");
//        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://estar-92225.firebaseio.com/address.json",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("return", response.toString());
                        Iterator<?> keys = response.keys();

                        while( keys.hasNext() ) {
                            String key = (String)keys.next();

                            try{
                                if ( response.get(key) instanceof JSONObject ) {
                                    JSONObject obj = response.getJSONObject(key);
                                    Street rua = new Street();
                                    rua.setStreet(obj.getString("street"));
                                    rua.setLongitude(obj.getString("longitude"));
                                    rua.setLatitude(obj.getString("latitude"));
                                    ruas.add(rua);
                                    Log.i("json","json");

                                }
                            }catch (JSONException e){
                                Log.e("errojson",e.getMessage());
                            }
                        }
                        swipe.setRefreshing(false);
                        adapter = new RelatorioAdapter(getBaseContext(),ruas);
                        listView.setAdapter(adapter);
                        //dialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("return", "Error: " + error.getMessage());
               // textView.setText(error.getMessage());
                //dialog.dismiss();
                swipe.setRefreshing(false);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(1000 * 15, 0, 1f));
        Volley.newRequestQueue(this).add(request);
    }


}
