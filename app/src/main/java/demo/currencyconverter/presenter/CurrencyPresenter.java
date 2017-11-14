package demo.currencyconverter.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import demo.currencyconverter.Api.CurrencyApi;
import demo.currencyconverter.Model.LatestCurrencyModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijayaraj_s on 11/14/2017.
 */

public class CurrencyPresenter {

    private final Context mContext;
    private final CountryPresenterListener mListener;
    private final CurrencyApi mCountryService;

    public interface CountryPresenterListener {
        void latestCurrencyResponse(List<LatestCurrencyModel> currencyList);

        void convertedCurrencyResponse(List<LatestCurrencyModel> currencyList);
    }

    public CurrencyPresenter(CountryPresenterListener _listener, Context _context) {
        this.mListener = _listener;
        this.mContext = _context;
        this.mCountryService = new CurrencyApi();
    }

    /**
     * Get Latest Currency
     */
    public void getLatestCurrency() {

        mCountryService.getCurrencyApi().getLatestCurrency().enqueue(new Callback<LatestCurrencyModel>() {
            @Override
            public void onResponse(Call<LatestCurrencyModel> call, Response<LatestCurrencyModel> response) {
                LatestCurrencyModel mResult = response.body();
                List<LatestCurrencyModel> mResultList = new ArrayList<>();

                if (null != mResult) {
                    mResultList.add(mResult);
                    mListener.latestCurrencyResponse(mResultList);
                }
            }

            @Override
            public void onFailure(Call<LatestCurrencyModel> call, Throwable t) {
                try {
                    throw new InterruptedException("Error communicating with server!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Get Converted Currency
     */
    public void getConvertedCurrency(String _baseCurrency, String _toCurrency) {

        mCountryService.getCurrencyApi().getConvertedCurrency(_baseCurrency, _toCurrency).enqueue(new Callback<LatestCurrencyModel>() {
            @Override
            public void onResponse(Call<LatestCurrencyModel> call, Response<LatestCurrencyModel> response) {
                LatestCurrencyModel mResult = response.body();
                List<LatestCurrencyModel> mResultList = new ArrayList<>();
                if (null != mResult) {
                    mResultList.add(mResult);
                    mListener.convertedCurrencyResponse(mResultList);
                }
            }

            @Override
            public void onFailure(Call<LatestCurrencyModel> call, Throwable t) {
                try {
                    throw new InterruptedException("Error communicating with server!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}