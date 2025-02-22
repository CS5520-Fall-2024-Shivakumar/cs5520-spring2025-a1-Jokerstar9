package com.example.firstapp_yunengli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private EditText editName, editPhone;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("name", editName.getText().toString());
            intent.putExtra("phone", editPhone.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}