package com.example.firstapp_yunengli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        contactList = loadContacts();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(contactAdapter);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            int position = data.getIntExtra("position", -1);

            if (requestCode == 1) {  // Adding new contact
                if (name != null && phone != null) {
                    contactList.add(new Contact(name, phone));
                    saveContacts();
                    contactAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == 2 && position >= 0) {  // Editing existing contact
                if (name != null && phone != null) {
                    contactList.get(position).setName(name);
                    contactList.get(position).setPhone(phone);
                    saveContacts();
                    contactAdapter.notifyItemChanged(position);
                }
            }
        }
    }

    @Override
    public void onContactClick(int position) {
        Contact contact = contactList.get(position);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.getPhone()));
        startActivity(intent);
    }

    @Override
    public void onContactLongClick(int position) {
        Contact contact = contactList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Action")
                .setMessage("Do you want to edit or delete this contact?")
                .setPositiveButton("Edit", (dialog, which) -> {
                    Intent intent = new Intent(ContactsActivity.this, EditContactActivity.class);
                    intent.putExtra("name", contact.getName());
                    intent.putExtra("phone", contact.getPhone());
                    intent.putExtra("position", position);
                    startActivityForResult(intent, 2);
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    Contact deletedContact = contactList.get(position);
                    contactList.remove(position);
                    saveContacts();
                    contactAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView, "Contact deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> {
                                contactList.add(position, deletedContact);
                                saveContacts();
                                contactAdapter.notifyItemInserted(position);
                            }).show();
                })
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveContacts() {
        SharedPreferences sharedPreferences = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactList);
        editor.putString("contacts_list", json);
        editor.apply();
    }

    private ArrayList<Contact> loadContacts() {
        SharedPreferences sharedPreferences = getSharedPreferences("contacts", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contacts_list", null);
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
        return gson.fromJson(json, type) != null ? gson.fromJson(json, type) : new ArrayList<>();
    }
}