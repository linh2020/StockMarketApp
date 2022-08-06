package edu.sjsu.stocksearch;
import java.util.ArrayList;
public class MSI {
    private static MSI mInstance;
    private ArrayList<String> list = null;
    private MSI() {
        list = new ArrayList<>();
    }
    public ArrayList<String> getFavList() {
        return this.list;
    }
    public void addToFav(String value) {
        list.add(value);
    }
    public void deleteFromFav(String value) {
        list.remove(value);
    }
    public static MSI getInstance() {
        if (mInstance == null)
            mInstance = new MSI();
        return mInstance;
    }}
