package com.example.firstapp_yunengli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditContactActivity extends AppCompatActivity {

    private EditText editName, editPhone;
    private Button saveButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        saveButton = findViewById(R.id.update_button);

        // Retrieve the contact info from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        position = intent.getIntExtra("position", -1);

        // Set the current values
        editName.setText(name);
        editPhone.setText(phone);

        saveButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", editName.getText().toString());
            resultIntent.putExtra("phone", editPhone.getText().toString());
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}