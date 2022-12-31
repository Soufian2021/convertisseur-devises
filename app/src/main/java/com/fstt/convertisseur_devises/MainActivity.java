package com.fstt.convertisseur_devises;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText amountEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the view components
        fromSpinner = findViewById(R.id.from_spinner);
        toSpinner = findViewById(R.id.to_spinner);
        amountEditText = findViewById(R.id.amount_edit_text);
        resultTextView = findViewById(R.id.result_text_view);

        // Set up the currency spinner adapters
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    public void convert(View view) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        String apiKey = "jxIj3jNufMU7zMXE8T3Qn2GoEzero6u4";
        String apiEndpoint = "https://api.apilayer.com/exchangerates_data/latest";

        Request request = new Request.Builder()
                .url(apiEndpoint)
                .addHeader("apikey", apiKey)
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String jsonResponse = response.body().string();

            // Parse the JSON response using the Gson library
            Gson gson = new Gson();
            ExchangeRates exchangeRates = gson.fromJson(jsonResponse, ExchangeRates.class);

            // Get the selected currencies and the amount to convert
            String fromCurrency = fromSpinner.getSelectedItem().toString();
            String toCurrency = toSpinner.getSelectedItem().toString();
            double amount = Double.parseDouble(amountEditText.getText().toString());

            // Get the exchange rate for the selected currencies
            double exchangeRate = exchangeRates.getRates().get(toCurrency);

            // Calculate the converted amount
            double convertedAmount = amount * exchangeRate;

            // Display the converted amount
            resultTextView.setText(String.format("%.2f", convertedAmount));
        } else {
            // Handle the error
            resultTextView.setText("Error converting currency");
        }
    }
}
