package edu.sjsu.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerSortBy;
    private Spinner spinnerOrder;
    ArrayAdapter<String> arrayAdapterSortBy;
    ArrayAdapter<String> arrayAdapterOrder;
    private AutoCompleteTextView acTextView;
    private ArrayAdapter adapter;
    private Context context;
    private RequestQueue requestQueue;
    private TextView textView;
    private TextView responseName;
    private ProgressBar spinnerFavLoading;
    private ListView listViewFav;
    private ArrayList<SASymbol> favInfoList;
    private ArrayList<String> favSymbolList;
    private int numFavReqDone;
    private ImageView imageViewRefresh;
    private Switch switchAutoRefresh;
    private Thread threadAutoRefresh;
    private ArrayList<String> valueArray = new ArrayList<>();
    private ArrayList<String> displayArray = new ArrayList<>();
    private boolean mValidateResult = false;
    private String pattern = "(^\\s*)";
    private ProgressBar spinnerAutoComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        setContentView(R.layout.activity_main);

        spinnerAutoComplete = (ProgressBar) findViewById(R.id.autoCompleteProgressBar);
        spinnerAutoComplete.setVisibility(View.INVISIBLE);
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context
        acTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        final Context context = this;
        acTextView.setThreshold(1);

        acTextView.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                if (text.toString().matches(pattern) || text.toString().length() == 0) {
                    mValidateResult = false;
                } else {
                    mValidateResult = true;
                }
                return mValidateResult;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return invalidText.toString();
            }
        });
        spinnerFavLoading = (ProgressBar) findViewById(R.id.progressLoadFav);
        listViewFav = (ListView) findViewById(R.id.ListViewFav);
        registerForContextMenu(listViewFav);

        imageViewRefresh = (ImageView) findViewById(R.id.imageViewRefresh);


        threadAutoRefresh = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (numFavReqDone == favSymbolList.size()) {
                                } else {
                                    Toast.makeText(context, "last refresh request not done.Cannot make new refresh request.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
        arrayAdapterSortBy = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.SortByArray)) {
        };

        arrayAdapterSortBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(arrayAdapterSortBy);

        spinnerOrder = (Spinner) findViewById(R.id.spinnerOrder);


        arrayAdapterOrder = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.OrderArray)) {
        };


        arrayAdapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(arrayAdapterOrder);
        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortBySelect = spinnerSortBy.getSelectedItem().toString();
                String orderSelect = spinnerOrder.getSelectedItem().toString();
                if (!sortBySelect.equals("Sort By")) {
                   // favInfoList = sortFavInfoList(favInfoList, sortBySelect, orderSelect);
                    SAdapter favAdapter = new SAdapter(context, R.layout.favrow, favInfoList);
                    listViewFav.setAdapter(favAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    public void getQuote(View v) {
        mValidateResult = false;
        acTextView.performValidation();
        if (mValidateResult) {
            threadAutoRefresh.interrupt();
            threadAutoRefresh = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (numFavReqDone == favSymbolList.size()) {
                                    } else {
                                        Toast.makeText(context, "last refresh request not done.Cannot make new refresh request.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Thread.sleep(10000);
                        }
                    } catch (InterruptedException e) {
                    }
                }

            };

            Intent intent = new Intent(MainActivity.this, StockActivity.class);
            String symbolName = acTextView.getText().toString().toUpperCase();
            String[] tmp = symbolName.split(" ");
            symbolName = tmp[0];
            // Put symbol data in intent, transfer to StockMainActivity
            intent.putExtra("symbol", symbolName);
            MainActivity.this.startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Please enter a stock name or symbol!",
                    Toast.LENGTH_LONG).show();
        }
    } // end of getQuote

    public void clear(View v) {
        acTextView.setText("");
    }
}
