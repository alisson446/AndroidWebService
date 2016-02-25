package com.example.desenvolvimento_02.testewebservice;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText texto;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        texto = (EditText) findViewById(R.id.textDados);
        progressDialog = ProgressDialog.show(MainActivity.this,"Aguarde","Carregando dados requisitados...",true,false);
        new TarefaAsicrona().execute("cavalks");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class TarefaAsicrona extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String http = "http://192.168.25.203:9090/wscomercial/login/" + params[0];
            String result;
            try {
                URL url = new URL(http);
                HttpURLConnection httpurl = (HttpURLConnection) url.openConnection();
                /*httpurl.setDoOutput(true);
                httpurl.setUseCaches(false);*/
                httpurl.setConnectTimeout(10000);
                httpurl.setReadTimeout(10000);
                httpurl.setRequestMethod("GET");
                httpurl.connect();

                /*JSONObject json = new JSONObject();
                json.put("nome", "alisson");
                OutputStreamWriter out = new OutputStreamWriter(httpurl.getOutputStream());
                out.write(json.toString());
                out.close();*/

                int response = httpurl.getResponseCode();
                if(response == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            httpurl.getInputStream(),"utf-8"));
                    nome = br.readLine();
                }
            } catch(MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return nome;
        }

        @Override
        protected void onPostExecute(String nome) {
            progressDialog.dismiss();
            texto.setText(nome);
        }
    }
}
