package xyz.taika.cryptohodler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button statusButton;
    Button aboutButton;
    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusButton = (Button) findViewById(R.id.AssetListButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        infoTextView = (TextView) findViewById(R.id.infoTextView);

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start AssetListActivity where's all the assets in a list
                Intent checkStatusIntent = new Intent(MainActivity.this, AssetListActivity.class);

                startActivity(checkStatusIntent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                showAboutDialog();
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        /*int id = item.getItemId();

        //no inspection SimplifiableIfStatement
        return id == R.id.action_settings || super.onOptionsItemSelected(item); */

        switch (item.getItemId()) {
            case R.id.action_settings:
                super.onOptionsItemSelected(item);
                return true;
            case R.id.action_exit:
                // Show toast
                Toast.makeText(getApplicationContext(), "Crypto Hodlers closed", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            default:
                return false;
        }

    }

    //Show dialog that gives possibility to edit asset from the list
    public void showAboutDialog() {

        // Creating alert Dialog with one Button
        final AlertDialog.Builder editAssetDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        editAssetDialog.setTitle("About Crypto Hodlers");

        //Show Dialog message
        editAssetDialog.setMessage("GNU GENERAL PUBLIC LICENSE Version 3.");

        // Add LinearLayout to show in custom AlertDialog
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 0, 50, 0);

        final TextView aboutTextView = new TextView(MainActivity.this);
        aboutTextView.setText(R.string.about_textview);
        layout.addView(aboutTextView);

        /* Create EditText View for asset quantity and add it to LinearLayout
        final EditText assetQuantityField = new EditText(AssetListActivity.this);
        assetQuantityField.setHint("Change quantity");
        layout.addView(assetQuantityField); */


        // Set LinearLayout to AlertDialog
        editAssetDialog.setView(layout);


        // Setting Positive "Done" Button
        editAssetDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog id DONE button is pressed
                        dialog.cancel();

                    }
                });

        // TEST Possibility to set Icon to Dialog
        editAssetDialog.setIcon(R.drawable.crypto_hodlers_icon);

        // Showing Alert Message
        editAssetDialog.show();
    }

}
