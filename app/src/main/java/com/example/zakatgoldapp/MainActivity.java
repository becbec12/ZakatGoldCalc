package com.example.zakatgoldapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etPrice;
    private Spinner spinnerType;
    private Button btnCalculate, btnClear;
    private TextView tvTotalValue, tvZakatPay, tvTotalZakat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);
        spinnerType = findViewById(R.id.spinnerType);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatPay = findViewById(R.id.tvZakatPay);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gold_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            openAboutPage();
            return true;
        } else if (id == R.id.action_share) {
            shareApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openAboutPage() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out this Gold Zakat Calculator app: https://github.com/yourusername/zakat-gold-calculator");
        startActivity(Intent.createChooser(shareIntent, "Share App via"));
    }

    private void calculateZakat() {
        String weightStr = etWeight.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString().toLowerCase();

        if (weightStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please enter weight and price", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight;
        double price;

        try {
            weight = Double.parseDouble(weightStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (weight < 0 || price < 0) {
            Toast.makeText(this, "Values cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        double uruf;
        if (type.equals("keep")) {
            uruf = 85.0;
        } else { // "wear"
            uruf = 200.0;
        }

        double totalGoldValue = weight * price;
        double goldAboveUruf = weight - uruf;
        if (goldAboveUruf < 0) {
            goldAboveUruf = 0;
        }

        double zakatPay = goldAboveUruf * price;
        double totalZakat = zakatPay * 0.025;

        tvTotalValue.setText(String.format("RM %.2f", totalGoldValue));
        tvZakatPay.setText(String.format("RM %.2f", zakatPay));
        tvTotalZakat.setText(String.format("RM %.2f", totalZakat));
    }

    private void clearAll() {
        // Clear input fields
        etWeight.setText("");
        etPrice.setText("");

        // Reset spinner to first item
        spinnerType.setSelection(0);

        // Reset result fields to default values
        tvTotalValue.setText("RM 0.00");
        tvZakatPay.setText("RM 0.00");
        tvTotalZakat.setText("RM 0.00");

        // Optional: Show toast message
        Toast.makeText(this, "All fields have been cleared", Toast.LENGTH_SHORT).show();
    }
}