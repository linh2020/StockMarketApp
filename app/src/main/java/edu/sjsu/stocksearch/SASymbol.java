package edu.sjsu.stocksearch;

public class SASymbol {
    private String symbol;
    private String price;
    private int timestamp;
    private String change;
    private String changePercent;
    private String changeString;

    public SASymbol(String symbol, String price, int timestamp, String change,
                    String changePercent) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
        this.change = change;
        this.changePercent = changePercent;
        this.changeString = change + "(" + changePercent + ")";
    }


    public float getChangeFloat() {
        return Float.parseFloat(change);
    }

    public float getPriceFloat() {
        String priceNew = price.replace(",", "");
        return Float.parseFloat(priceNew);
    }

    public float getChangePercentFloat() {
        String part[] = changePercent.split("%");
        String changePercentFloat = part[0];
        return Float.parseFloat(changePercentFloat);
    }


    public String getSymbolName() {
        return symbol;
    }

    public String getPrice() {
        return price;
    }

    public int getTimeStampt() {
        return timestamp;
    }

    public String getChangeString() {
        return changeString;
    }
}
