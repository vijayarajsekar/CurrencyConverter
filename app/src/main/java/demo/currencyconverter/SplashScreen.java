package demo.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import demo.currencyconverter.Model.LatestCurrencyModel;
import demo.currencyconverter.presenter.CurrencyPresenter;

public class SplashScreen extends AppCompatActivity implements CurrencyPresenter.CountryPresenterListener {

    private CurrencyPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mPresenter = new CurrencyPresenter(this, this);
        mPresenter.getLatestCurrency();

        mPresenter.getConvertedCurrency("USD", "INR");
    }

    @Override
    public void latestCurrencyResponse(List<LatestCurrencyModel> currencyList) {
        System.out.println("~ ~ ~ Currency List ~ ~ ~ " + currencyList.get(0).getRates().get("AUD"));
    }

    @Override
    public void convertedCurrencyResponse(List<LatestCurrencyModel> currencyList) {
        System.out.println("~ ~ ~ Converted Currency List ~ ~ ~ " + currencyList.get(0).getRates().get("INR"));
    }
}
