package com.dileeptest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dileeptest.adapter.GridViewAdapter;
import com.dileeptest.model.GridViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    EditText search_Flicker_Images_ET;
    GridViewAdapter gridViewAdapter;
    ArrayList<GridViewModel> gridViewModelArrayList;
    ProgressDialog progressDialog;
    String serviceDataUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&\n" +
            "format=json&nojsoncallback=1&safe_search=1&text=kittens";
    String searchServiceDataUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&\n" +
            "format=json&nojsoncallback=1&safe_search=1&text=";
    String initialText = "kittens";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
        search_Flicker_Images_ET = (EditText) findViewById(R.id.search_Flicker_Images_ET);

        new FlickerImagesAsynTask(serviceDataUrl).execute();

        search_Flicker_Images_ET.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_DONE) {
                    String SearchKeyWord = search_Flicker_Images_ET.getText().toString();
                    if (SearchKeyWord != null && !SearchKeyWord.equalsIgnoreCase("")) {
                        initialText = "SearchItem";
                        new FlickerImagesAsynTask(searchServiceDataUrl + SearchKeyWord).execute();

                    } else {

                        Toast.makeText(MainActivity.this, "Please enter search keyword", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

        });
    }


    private class FlickerImagesAsynTask extends AsyncTask<String, String, String> {
        String URL;

        public FlickerImagesAsynTask(String serviceDataUrl) {
            this.URL = serviceDataUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {

                result = serviceConnection(URL);
            } catch (Exception e) {
                System.out.println("doInBackground   " + e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            dismissProgressDialog();
            System.out.println("onPostExecute_response   " + response);
            if (response != null && !response.equalsIgnoreCase("")) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("photos");
                    JSONArray jsonArray = jsonObject1.getJSONArray("photo");
                    gridViewModelArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        gridViewModelArrayList.add(new GridViewModel(jsonArray.get(i).toString()));
                    }


                } catch (Exception e) {
                    System.out.println("onPostExecute   " + e);
                }
                /***
                 * Creating Adapter and Passing Context and ArrayList
                 * Setting Adapter to GridView
                 */
                gridViewAdapter = new GridViewAdapter(MainActivity.this, gridViewModelArrayList);
                gridView.setAdapter(gridViewAdapter);


                if (initialText.equalsIgnoreCase("kittens")) {

                    search_Flicker_Images_ET.setText("kittens");
                } else if (initialText.equalsIgnoreCase("SearchItem")) {
                    search_Flicker_Images_ET.setText("");
                }
            }

        }
    }

    private void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /***
     *  Creating Http Url Connection to get Response
     */
    private String serviceConnection(String serviceDataUrl) {
        String response = null;

        try {
            URL url = new URL(serviceDataUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            InputStream inpuStream = new BufferedInputStream(httpConnection.getInputStream());
            response = convertInputStreamToString(inpuStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("serviceConnection   " + e);
        }

        return response;
    }

    /***
     *  converting InputStream response to String Response
     */
    private String convertInputStreamToString(InputStream inpuStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inpuStream));
        StringBuilder stringBuilder = new StringBuilder();
        String resultLine = "";
        try {
            while ((resultLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(resultLine).append("\n");
            }
        } catch (Exception e) {
            System.out.println("convertInputStreamToString  " + e);
        }finally {
            try {
                inpuStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }
}
