package com.example.uhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfilePage extends AppCompatActivity {
    private Toolbar toolbar;

    private FireBaseInfo mFireBaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        toolbar = findViewById(R.id.profilepg_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("profile page");

        mFireBaseInfo = new FireBaseInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        adds to action bar if present
        inflater.inflate(R.menu.profilepg_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int v_id = item.getItemId();
        if (v_id==R.id.pp_tb_setting){
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            return true;
        }else if (v_id == R.id.pp_tb_logout){
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            logOut();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        mFireBaseInfo.mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}