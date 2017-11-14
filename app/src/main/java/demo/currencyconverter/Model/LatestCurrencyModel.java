package demo.currencyconverter.Model;

import com.google.gson.JsonObject;

/**
 * Created by vijayaraj_s on 11/14/2017.
 */

public class LatestCurrencyModel {

    private String base;
    private String date;
    private JsonObject rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public JsonObject getRates() {
        return rates;
    }

    public double getRate(String code) {
        return rates.has(code) ? rates.get(code).getAsDouble() : 0;
    }
}
