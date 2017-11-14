package demo.currencyconverter.Api;

import demo.currencyconverter.Model.LatestCurrencyModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vijayaraj_s on 11/14/2017.
 */

public class CurrencyApi {

    private static String BASE_URL = "http://api.fixer.io/";

    public CurrencyAPI getCurrencyApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(CurrencyAPI.class);
    }


    public interface CurrencyAPI {
        /**
         * GET Method - Get All The Latest Currency Details
         */
        @GET("latest")
        Call<LatestCurrencyModel> getLatestCurrency();

        /**
         * GET Method - Get Specified Currency Details
         */
        @GET("latest")
        Call<LatestCurrencyModel> getConvertedCurrency(@Query("base") String _base, @Query("symbols") String _symbols);
    }
}