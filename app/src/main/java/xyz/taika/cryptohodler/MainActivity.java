package xyz.taika.cryptohodler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SettingsPrefsFile";

    Button statusButton;
    Button aboutButton;
    TextView infoTextView;
    private boolean eurFiat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusButton = (Button) findViewById(R.id.AssetListButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        infoTextView = (TextView) findViewById(R.id.infoTextView);


        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        this.eurFiat = settings.getBoolean("eurFiatMode", false);








        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start AssetListActivity where's all the assets in a list
                Intent checkStatusIntent = new Intent(MainActivity.this, AssetListActivity.class);

                if (eurFiat) {
                    checkStatusIntent.putExtra("eurFiat", true);
                } else {
                    checkStatusIntent.putExtra("eurFiat", false);
                }
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

                showSettingsDialog();


                super.onOptionsItemSelected(item);
                return true;

            case R.id.action_report_bug:
                // Show toast
                String subject = "Crypto Hodlers bug";
                String[] addresses = new String[1];
                addresses[0] = "jukka@taika.xyz";
                String mssg = "Hey, good job finding that little ****(bug). Describe your findings here and send it to Taika Inc. Thanks!: \n";

                //remove the last line(Press Order-button to confirm your order.) of Order summary, as it not needed in email confirmation
                if(mssg.lastIndexOf("\n")>0) {
                    mssg = mssg.substring(0, mssg.lastIndexOf("\n"));
                }

                //start email intent to send Order confirmation via email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("*/*");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, mssg);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

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
        editAssetDialog.setTitle("Crypto Hodlers");

        //Show Dialog message
        editAssetDialog.setMessage("ABOUT");

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

    //Show dialog that gives possibility to edit settings of the app
    public void showSettingsDialog() {


        // Creating alert Dialog with one Button
        final AlertDialog.Builder editAssetDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        editAssetDialog.setTitle("Settings");

        //Show Dialog message
        editAssetDialog.setMessage("Edit settings");

        // Add LinearLayout to show in custom AlertDialog
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 0, 50, 0);

        // Create CheckBox View for EUR option
        final CheckBox eurFiatCheckBox = new CheckBox(MainActivity.this);
        eurFiatCheckBox.setText("Use EUR instead of USD");
        eurFiatCheckBox.setChecked(eurFiat);
        layout.addView(eurFiatCheckBox);


        // Set LinearLayout to AlertDialog
        editAssetDialog.setView(layout);


        // Setting Positive "Done" Button
        editAssetDialog.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog id DONE button is pressed

                        if (eurFiatCheckBox.isChecked()) {
                            eurFiat = true;
                        } else {
                            eurFiat = false;
                        }

                        // An Editor object to make preference changes.
                        // All objects are from android.context.Context
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("eurFiatMode", eurFiat);

                        // Commit the edits!
                        editor.commit();

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Settings changed", Toast.LENGTH_SHORT).show();

                    }
                });

        // Setting Negative "Cancel" Button
        editAssetDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });



        // Showing Alert Message
        editAssetDialog.show();

    }

    public boolean getEurFiat() {
        return eurFiat;
    }

}
