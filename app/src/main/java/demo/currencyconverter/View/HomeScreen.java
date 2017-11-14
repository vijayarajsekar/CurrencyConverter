package demo.currencyconverter.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import demo.currencyconverter.Model.LatestCurrencyModel;
import demo.currencyconverter.Presenter.CurrencyPresenter;
import demo.currencyconverter.R;
import demo.currencyconverter.Utils.NetworkManager;

public class HomeScreen extends AppCompatActivity implements CurrencyPresenter.CountryPresenterListener {

    private CurrencyPresenter mPresenter;
    private List<String> mCurrencyCodes;

    private Context mContext;

    private AutoCompleteTextView mFromCurrency;
    private AutoCompleteTextView mToCurrency;
    private TextView mAmount;
    private TextView mResultAmount;
    private ArrayAdapter<String> mCurrencyCodeAdapter;

    String mFromCode = null;
    String mToCode = null;
    int mEnteredAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mContext = this;

        // Initializing Variables
        init();

        // Api Call
        mPresenter.getLatestCurrency();

        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mFromCode = mFromCurrency.getText().toString().trim();
                mToCode = mToCurrency.getText().toString().trim();

                if (NetworkManager.isNetworkAvailable(mContext)) {

                    if ((null != mFromCode && mFromCode.length() != 0) && (null != mToCode && mFromCode.length() != 0)) {

                        if (0 != s.length()) {
                            mEnteredAmount = Integer.parseInt(s.toString());
                            mPresenter.getConvertedCurrency(mFromCode, mToCode);
                        }

                    } else {
                        NetworkManager.shotToast(getString(R.string.validdetails), mContext);
                    }
                } else {
                    NetworkManager.shotToast(getString(R.string.nointernet), mContext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * @param currencyList Latest Currency Response
     */
    @Override
    public void latestCurrencyResponse(List<LatestCurrencyModel> currencyList) {
        System.out.println("~ ~ ~ Currency List ~ ~ ~ " + currencyList.get(0).getRates().get("AUD"));

        if (0 != currencyList.get(0).getRates().size())
            getCurrencyCodes(currencyList.get(0).getRates());
    }

    /**
     * @param currencyList Converted Currency Response
     */
    @Override
    public void convertedCurrencyResponse(List<LatestCurrencyModel> currencyList) {
        System.out.println("~ ~ ~ Converted Currency List ~ ~ ~ " + currencyList.get(0).getRates().get(mToCode));
        System.out.println(mEnteredAmount + " " + currencyList.get(0).getBase() + " = " + calculateFinalValue(currencyList.get(0).getRates().get(mToCode).getAsFloat()) + " " + mToCode);
        mResultAmount.setText(mEnteredAmount + " " + currencyList.get(0).getBase() + " = " + calculateFinalValue(currencyList.get(0).getRates().get(mToCode).getAsFloat()) + " " + mToCode);
    }

    private void init() {

        mFromCurrency = (AutoCompleteTextView) findViewById(R.id.fromCurrency);
        mFromCurrency.setThreshold(0);

        mToCurrency = (AutoCompleteTextView) findViewById(R.id.toCurrency);
        mToCurrency.setThreshold(0);

        mAmount = (TextView) findViewById(R.id.textAmount);
        mResultAmount = (TextView) findViewById(R.id.textFinalResult);

        mCurrencyCodes = new ArrayList<>();

        mPresenter = new CurrencyPresenter(this, this);

    }

    /**
     * @param rates Seperate Currency Code From The Result
     */
    private void getCurrencyCodes(JsonObject rates) {

        Set<?> s = rates.keySet();
        Iterator<?> i = s.iterator();
        do {

            String k = i.next().toString();
            System.out.println("~ ~ ~ KEYS " + k);
            mCurrencyCodes.add(k);

        } while (i.hasNext());

        System.out.println("~ ~ ~ CurrencyCode List Size ~ ~ ~ " + mCurrencyCodes.size());

        if (0 != mCurrencyCodes.size())
            setAdapter();
    }

    private void setAdapter() {
        mCurrencyCodeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mCurrencyCodes);
        mFromCurrency.setAdapter(mCurrencyCodeAdapter);
        mToCurrency.setAdapter(mCurrencyCodeAdapter);
    }

    private float calculateFinalValue(float asFloat) {
        return (float) (Math.floor(asFloat * mEnteredAmount * 100.0) / 100.0);
    }
}