package edu.sjsu.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Frag1 extends android.support.v4.app.Fragment {
    private static final String TAG = "Frag1";
    private RequestQueue requestQueue;
    private String selectIndicator;
    private String currDrawIndicator;
    private String currDrawUrl;
    private String currSelectIndicator;
    private TableLayout detailTableLayout;

    // TextView in detail table
    private TextView tableTextViewSymbol;
    private TextView tableTextViewOpen;
    private TextView tableTextViewLastPrice;
    private TextView tableTextViewClose;
    private TextView tableTextViewLow;
    private TextView tableTextViewHigh;
    private TextView tableTextViewVolume;
    private TextView tableTextViewTimeStamp;

    private TextView textViewError;
    private ImageView changeImageView;
    private ProgressBar progressBar;
    private Spinner spinnerIndicators;
    private ProgressBar progressBarIndicator;
    private TextView changeTextView;
    private WebView webViewCharts;
    private Context context;

    // Stock symbol name, get from StockActivity;
    private String symbol;
    private ImageView imageViewFav;
    private String favString;

    // FavList, store the symbol of favorite
    private ArrayList<String> favList;
    private int MY_SOCKET_TIMEOUT_MS = 8000;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        symbol = ((StockActivity) getActivity()).getSymbol();
        requestQueue = Volley.newRequestQueue(getActivity());
        context = getActivity();

        detailTableLayout = (TableLayout) view.findViewById(R.id.detailTable);
        detailTableLayout.setVisibility(View.INVISIBLE);
        textViewError = (TextView) view.findViewById(R.id.textViewError);

        // TextView in Table
        tableTextViewSymbol = (TextView) view.findViewById(R.id.textViewStockSymbol);
        tableTextViewOpen = (TextView) view.findViewById(R.id.textViewOpen);
        tableTextViewLastPrice = (TextView) view.findViewById(R.id.textViewLastPrice);
        tableTextViewClose = (TextView) view.findViewById(R.id.textViewClose);
        tableTextViewLow = (TextView) view.findViewById(R.id.textViewLow);
        tableTextViewHigh = (TextView) view.findViewById(R.id.textViewHigh);
        tableTextViewVolume = (TextView) view.findViewById(R.id.textViewVolume);
        tableTextViewTimeStamp = (TextView) view.findViewById(R.id.textViewTimeStamp);

        // Progress bar
        progressBar = (ProgressBar) view.findViewById(R.id.detailProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        // WebView for charts
        webViewCharts = (WebView) view.findViewById(R.id.webViewChart);

        // Progressbar for indicators
        progressBarIndicator = (ProgressBar) view.findViewById(R.id.progressBarIndicator);
        progressBarIndicator.setVisibility(View.INVISIBLE);

        imageViewFav = (ImageView) view.findViewById(R.id.favButton);
        currDrawUrl = "";


        changeTextView = (TextView) view.findViewById(R.id.change);
        changeTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBarIndicator.setVisibility(View.VISIBLE);
                changeTextView.setTextColor(Color.parseColor("#aca8a8"));
                changeTextView.setClickable(false);
                selectIndicator = spinnerIndicators.getSelectedItem().toString();
                currDrawIndicator = selectIndicator;
                currDrawUrl = "";
            }
        });

        spinnerIndicators = (Spinner) view.findViewById(R.id.spinnerIndicators);
        ArrayAdapter<String> indicatorAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.indicators));
        indicatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIndicators.setAdapter(indicatorAdapter);
        spinnerIndicators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeTextView.setTextColor(Color.parseColor("#000000"));
                currSelectIndicator = spinnerIndicators.getSelectedItem().toString();
                if (!currSelectIndicator.equals(currDrawIndicator)) {
                } else {
                    changeTextView.setClickable(false);
                    changeTextView.setTextColor(Color.parseColor("#aca8a8"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                changeTextView.setClickable(false);
            }
        });

        String url = "https://api.tiingo.com/iex/?tickers=" + symbol + "&token=12PrIvA32tEmYtEmpToKeN23";
        progressBar.setVisibility(View.INVISIBLE);
        detailTableLayout.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject stock = response.getJSONObject(i);
                        Log.d("test1", response.toString());
                        tableTextViewSymbol.setText(stock.getString("ticker"));
                        tableTextViewOpen.setText(stock.getString("open"));
                        tableTextViewLastPrice.setText(stock.getString("last"));
                        tableTextViewClose.setText(stock.getString("prevClose"));
                        tableTextViewLow.setText(stock.getString("low"));
                        tableTextViewHigh.setText(stock.getString("high"));
                        tableTextViewVolume.setText(stock.getString("volume"));
                        tableTextViewTimeStamp.setText(stock.getString("timestamp"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("msg1", error.toString());
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
        return view;
    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }


}
