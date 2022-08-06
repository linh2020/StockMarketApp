package edu.sjsu.stocksearch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Frag2 extends android.support.v4.app.Fragment {
    private static final String TAG = "Frag2";
    private Button btnTEST2;
    WebSettings webSettings;
    private String symbol;
    private WebView webViewCharts;
    private TextView textViewError;
    private ProgressBar progressBar;
    private Context activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        symbol = ((StockActivity) getActivity()).getSymbol();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarHis);
        progressBar.setVisibility(View.INVISIBLE);
        activity = getActivity();
        textViewError = (TextView) view.findViewById(R.id.textViewError2);
        webViewCharts = (WebView) view.findViewById(R.id.webViewChartHis);
        drawHisCharts(symbol);
        return view;
    }

    private void drawHisCharts(final String symbol) {
        webSettings = webViewCharts.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewCharts.loadUrl("https://www.tiingo.com/" + symbol + "/overview");
    }

    private class ShowProgressInterface {
        ProgressBar progressBar;
        ShowProgressInterface(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @JavascriptInterface
        public void showProgress() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @JavascriptInterface
        public void hideProgress() {
            ((Activity) activity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @JavascriptInterface
        public void showError() {
            // change UI on ui thread
            ((Activity) activity).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewError.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}