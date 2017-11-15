package demo.currencyconverter.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import demo.currencyconverter.Model.LatestCurrencyModel;
import demo.currencyconverter.Presenter.CurrencyPresenter;
import demo.currencyconverter.R;
import demo.currencyconverter.Singleton;
import demo.currencyconverter.Utils.NetworkManager;
import demo.currencyconverter.Utils.ToastUtils;

public class HomeScreen extends AppCompatActivity implements CurrencyPresenter.CountryPresenterListener {

    String TAG = HomeScreen.class.getSimpleName();

    private CurrencyPresenter mPresenter;
    private List<String> mCurrencyCodes;

    private Context mContext;

    private AutoCompleteTextView mFromCurrency;
    private AutoCompleteTextView mToCurrency;
    private TextView mAmount;
    private TextView mResultAmount;
    private ArrayAdapter<String> mCurrencyCodeAdapter;

    private String mFromCode = null;
    private String mToCode = null;
    private int mEnteredAmount;

    private LinearLayout mInputLayout;
    private LinearLayout mResultLayout;
    private LinearLayout mHistoryLayout;

    private ListView mHistoryList;
    private List<String> mHistoryListData;

    private boolean isThemeChanged = false;

    private boolean isMenushowing = true;

    private StringBuilder mHistoryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Initial Theme
        setThemes();

        setContentView(R.layout.activity_home_screen);

        mContext = this;
        init();

        // Api Call - Happening at Presenter
        mPresenter.getLatestCurrency();

        /**
         * Method is Called When User Enters The Value in Amount Field
         */
        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mFromCode = mFromCurrency.getText().toString().trim();
                mToCode = mToCurrency.getText().toString().trim();

                // For No Internet Connection
                if (NetworkManager.isNetworkAvailable()) {

                    // For Invalid / Field Currency is Empty
                    if ((null != mFromCode && mFromCode.length() != 0) && (null != mToCode && mFromCode.length() != 0)) {

                        // For Same Currency
                        if (!mFromCode.equals(mToCode) || !mToCode.equals(mFromCode)) {

                            if (0 != s.length()) {
                                mEnteredAmount = Integer.parseInt(s.toString());
                                mPresenter.getConvertedCurrency(mFromCode, mToCode);
                            }
                        } else {
                            ToastUtils.shotToast(getString(R.string.samecurr));
                        }

                    } else {
                        ToastUtils.shotToast(getString(R.string.currvalidate));
                    }
                } else {
                    ToastUtils.shotToast(getString(R.string.nointernet));
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
        if (0 != currencyList.get(0).getRates().size())
            getCurrencyCodes(currencyList.get(0).getRates());
    }

    /**
     * @param currencyList Converted Currency Response
     */
    @Override
    public void convertedCurrencyResponse(List<LatestCurrencyModel> currencyList) {

        // Displaying The Final Result
        mResultAmount.setText(mEnteredAmount + " " + currencyList.get(0).getBase() + " = " + calculateFinalValue(currencyList.get(0).getRates().get(mToCode).getAsFloat()) + " " + mToCode);

        // Adding Result into History
        if (Singleton.getPreferenceInstance().getHistory().isEmpty()) {
            Singleton.getPreferenceInstance().setHistory(mHistoryBuilder.append(mResultAmount.getText().toString() + ",").toString());
        } else {
            Singleton.getPreferenceInstance().setHistory(mHistoryBuilder.append(Singleton.getPreferenceInstance().getHistory() + mResultAmount.getText().toString() + ",").toString());
        }
    }

    /**
     * Initializing Variables
     */
    private void init() {

        mFromCurrency = findViewById(R.id.fromCurrency);
        mFromCurrency.setThreshold(0);

        mToCurrency = findViewById(R.id.toCurrency);
        mToCurrency.setThreshold(0);

        mAmount = findViewById(R.id.textAmount);
        mResultAmount = findViewById(R.id.textFinalResult);

        mCurrencyCodes = new ArrayList<>();

        mPresenter = new CurrencyPresenter(this, this);

        mInputLayout = findViewById(R.id.layout_input);
        mResultLayout = findViewById(R.id.layout_result);
        mHistoryLayout = findViewById(R.id.layout_history);

        mHistoryList = findViewById(R.id.history_list);
        mHistoryBuilder = new StringBuilder();
    }

    /**
     * @param rates Seperate Currency Code From The Result
     */
    private void getCurrencyCodes(JsonObject rates) {

        Set<?> mSet = rates.keySet();
        Iterator<?> i = mSet.iterator();
        do {

            String mKeys = i.next().toString();
            mCurrencyCodes.add(mKeys);

        } while (i.hasNext());

        if (0 != mCurrencyCodes.size())
            setAdapter();
    }

    // Set Available Currency Code Values into InputFields
    private void setAdapter() {
        mCurrencyCodeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mCurrencyCodes);
        mFromCurrency.setAdapter(mCurrencyCodeAdapter);
        mToCurrency.setAdapter(mCurrencyCodeAdapter);
    }

    // Final Calculation Based On Given Currency Details
    private float calculateFinalValue(float asFloat) {
        return (float) (Math.floor(asFloat * mEnteredAmount * 100.0) / 100.0);
    }

    /**
     * Set Options Menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMenushowing)
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Menu - Click Event Handling
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_clear) {
            clearFieds();
            return true;
        } else if (id == android.R.id.home) {

            invalidateMenu(true);

            clearFieds();
            setHomeButtonEnabled(false);

            mInputLayout.setVisibility(View.VISIBLE);
            mResultLayout.setVisibility(View.VISIBLE);
            mHistoryLayout.setVisibility(View.GONE);
        }

        if (id == R.id.action_history) {

            invalidateMenu(false);

            setHomeButtonEnabled(true);

            mInputLayout.setVisibility(View.GONE);
            mResultLayout.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.VISIBLE);

            setHistoryAdapter();

            return true;
        }

        if (id == R.id.action_changetheme) {

            if (!Singleton.getPreferenceInstance().getTheme()) {
                isThemeChanged = true;
            } else {
                isThemeChanged = false;
            }

            // Set User Selected Theme into Shared Preferences
            Singleton.getPreferenceInstance().setTheme(isThemeChanged);

            // Set Theme Based on User Selection
            setThemes();

            // Recreate The Activity Instance
            this.recreate();

            clearFieds();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show - Hide HomeButton - When User Navigating From - To History
     */
    private void setHomeButtonEnabled(boolean mStatus) {

        getSupportActionBar().setDisplayHomeAsUpEnabled(mStatus);
        getSupportActionBar().setHomeButtonEnabled(mStatus);

        if (mStatus) {
            getSupportActionBar().setTitle(getString(R.string.history));
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    /**
     * Set History Adapte - When Click On History Menu Option
     */
    private void setHistoryAdapter() {

        mHistoryListData = new ArrayList<>(new HashSet<>(Arrays.asList(Singleton.getPreferenceInstance().getHistory().split("\\s*,\\s*"))));

        if (null != mHistoryListData && 0 != mHistoryListData.size()) {
            mHistoryList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mHistoryListData));
        } else {
            ToastUtils.shotToast(getString(R.string.nohistory));
        }
    }

    /**
     * Clear The Fields - When Click On Clear Menu Option
     */
    private void clearFieds() {
        mFromCurrency.setText("");
        mToCurrency.setText("");
        mAmount.setText("");
        mResultAmount.setText("");
    }

    /**
     * Change App Theme Based On User Selection
     */
    private void setThemes() {
        if (null != Singleton.getPreferenceInstance()) {
            setTheme(Singleton.getPreferenceInstance().getTheme() ? R.style.AppThemeTwo : R.style.AppThemeOne);
        } else {
            setTheme(R.style.AppThemeOne);
        }
    }

    private void invalidateMenu(boolean isEnabled) {
        isMenushowing = isEnabled;
        invalidateOptionsMenu();
    }
}