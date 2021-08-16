package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button btnADD;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnADD = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.ettext);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener );
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() -1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was Added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    public void loadItems(){
        try{
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    public void saveItems(){
        try{
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e){
            Log.e("MainActivity", "Error writing Items", e);
        }
    }
}