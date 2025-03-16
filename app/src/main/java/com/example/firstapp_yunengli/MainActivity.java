package com.example.firstapp_yunengli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutMeButton = findViewById(R.id.aboutMeButton);
        Button quickCalcButton = findViewById(R.id.quickCalcButton);
        Button contactsCollectorButton = findViewById(R.id.contactsCollectorButton);
        Button primeSearchButton = findViewById(R.id.primeSearchButton);

        quickCalcButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });

        aboutMeButton.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Name: Yuneng Li\nEmail: li.yunen@northeastern.edu", Toast.LENGTH_LONG).show()
        );

        contactsCollectorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intent);
        });

        primeSearchButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               Intent intent = new Intent(MainActivity.this, PrimeSearchActivity.class);
               startActivity(intent);
           }
        });
    }
}