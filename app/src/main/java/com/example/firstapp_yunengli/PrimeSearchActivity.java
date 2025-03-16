package com.example.firstapp_yunengli;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class PrimeSearchActivity extends AppCompatActivity {

    private Button findPrimesButton, terminateSearchButton;
    private CheckBox pacifierSwitch;
    private TextView currentNumberTextView, latestPrimeTextView;
    private volatile boolean isSearching = false;
    private Thread primeThread;
    private int currentNumber = 3;
    private int latestPrime = -1;

    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_search);

        findPrimesButton = findViewById(R.id.findPrimesButton);
        terminateSearchButton = findViewById(R.id.terminateSearchButton);
        pacifierSwitch = findViewById(R.id.pacifierSwitch);
        currentNumberTextView = findViewById(R.id.currentNumberTextView);
        latestPrimeTextView = findViewById(R.id.latestPrimeTextView);

        if (savedInstanceState != null) {
            isSearching = savedInstanceState.getBoolean("isSearching");
            currentNumber = savedInstanceState.getInt("currentNumber");
            latestPrime = savedInstanceState.getInt("latestPrime");
            pacifierSwitch.setChecked(savedInstanceState.getBoolean("pacifierState"));

            if (isSearching) {
                resumePrimeSearch(currentNumber);
            }
        }

        findPrimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearching) {
                    startPrimeSearch();
                }
            }
        });

        terminateSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPrimeSearch();
            }
        });

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (isSearching) {
                            new AlertDialog.Builder(PrimeSearchActivity.this)
                                    .setMessage("A search is running. Do you want to terminate and exit?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            stopPrimeSearch();
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        } else {
                            setEnabled(false);
                            PrimeSearchActivity.this.onBackPressed();
                        }
                    }
                }
        );
    }
    private void startPrimeSearch() {
        isSearching = true;
        currentNumber = 3;

        primeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int num = currentNumber;
                while (isSearching) {
                    currentNumber = num;

                    if (isPrime(num)) {
                        latestPrime = num;
                        updateUI();
                    }

                    num += 2;
                }
            }
        });
        primeThread.start();
    }

    private void resumePrimeSearch(int resumeFrom) {
        isSearching = true;

        primeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int num = resumeFrom;
                while (isSearching) {
                    currentNumber = num;
                    if (isPrime(num)) {
                        latestPrime = num;
                        updateUI();
                    }
                    num += 2;
                }
            }
        });
        primeThread.start();
    }

    private void stopPrimeSearch() {
        isSearching = false;
        if (primeThread != null) {
            primeThread.interrupt();
            primeThread = null;
        }
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    private void updateUI() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                currentNumberTextView.setText("Checking: " + currentNumber);
                latestPrimeTextView.setText("Latest Prime: " + latestPrime);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSearching", isSearching);
        outState.putInt("currentNumber", currentNumber);
        outState.putInt("latestPrime", latestPrime);
        outState.putBoolean("pacifierState", pacifierSwitch.isChecked());
    }
}