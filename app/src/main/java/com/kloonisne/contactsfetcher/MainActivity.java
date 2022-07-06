package com.kloonisne.contactsfetcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

/*
    In this program we are fetching contacts in the device using CONTENT PROVIDER
    and display it in a text view.

    For that first we need to go to AndroidManifest.xml in manifests folder and add
    permission for reading contacts from the device
 */
public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        //Checking if user has allowed permission for reading contact
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            //if has permission get contacts
            getContacts();
        } else {
            //if no permission request for permission
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},123);
            textView.setText("App cannot show contacts since you denied the contact permission");
        }

    }

    private void getContacts(){
        //creating a string array and adding what all values we need from contact
        textView.setText("");
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //creating a cursor to fetch contacts in ascending order of name
        Cursor cursor = getContentResolver().query(uri,projection,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        //go through each contact and showing them in the text view
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            textView.append(name+"\n"+number+"\n\n");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("TAG", "onRequestPermissionsResult: \n\n\\t\t\t\t\t\t\t\t"+grantResults.length );
        if(requestCode == 123){
            //checking if user accepted to give permission
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getContacts();
            }
        }
    }



}