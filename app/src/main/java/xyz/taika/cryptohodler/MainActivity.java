package xyz.taika.cryptohodler;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SettingsPrefsFile";

    Button statusButton;
    Button aboutButton;
    TextView infoTextView;
    private boolean eurFiat; // This stores value of user chosen fiat value. Default = USD
    private String changePercentRate; // This stores value of user chosen change percent rate. Default = 24h


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set corresponding XML layouts to views
        statusButton = (Button) findViewById(R.id.AssetListButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        infoTextView = (TextView) findViewById(R.id.infoTextView);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        this.eurFiat = settings.getBoolean("eurFiatMode", false);
        this.changePercentRate = settings.getString("changePercentRate", "DEFAULT");

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AssetListActivity where's all the assets in a list
                Intent checkStatusIntent = new Intent(MainActivity.this, AssetListActivity.class);
                checkStatusIntent.putExtra("changePercentRate", changePercentRate);

                // Check if EUR/USD setting is enabled
                if (eurFiat) {
                    checkStatusIntent.putExtra("eurFiat", true);
                } else {
                    checkStatusIntent.putExtra("eurFiat", false);
                }

                // Start activity
                startActivity(checkStatusIntent);
            }
        });

        // Set onClickListener to About button
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

        // TEST int id = item.getItemId();

        // No inspection SimplifiableIfStatement return id == R.id.action_settings || super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_settings:
                // Show settings dialog
                showSettingsDialog();
                super.onOptionsItemSelected(item);
                return true;

            case R.id.action_report_bug:

                // Set subject, recipient address and default message to bug report
                String subject = "Crypto Hodlers bug";
                String[] addresses = new String[1];
                addresses[0] = "jukka@taika.xyz";
                String bugMessage = getString(R.string.bug_message);

                // start email intent to send Order confirmation via email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("*/*");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, bugMessage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

                return true;
            case R.id.action_exit:
                // Show toast and close app
                Toast.makeText(getApplicationContext(), "Crypto Hodlers closed", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            default:
                return false;
        }

    }

    // Show dialog that gives possibility to edit asset from the list
    public void showAboutDialog() {

        // Create alert Dialog with one Button
        final AlertDialog.Builder editAssetDialog = new AlertDialog.Builder(MainActivity.this);

        // Set Dialog Title
        editAssetDialog.setTitle("Crypto Hodlers");

        // Set Dialog message
        editAssetDialog.setMessage("ABOUT");

        // Add LinearLayout to show in custom AlertDialog
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 0, 50, 0);

        final TextView aboutTextView = new TextView(MainActivity.this);
        aboutTextView.setText(R.string.about_textview);
        layout.addView(aboutTextView);

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

        // Set Icon to Dialog
        editAssetDialog.setIcon(R.drawable.crypto_hodlers_icon);

        //Show Alert Message
        editAssetDialog.show();
    }


    //Show dialog that gives possibility to edit settings of the app
    public void showSettingsDialog() {

        //Create alert Dialog with one Button
        final AlertDialog.Builder settingsDialog = new AlertDialog.Builder(MainActivity.this);

        //Set Dialog Title
        settingsDialog.setTitle("Settings");

        //Set Dialog message
        settingsDialog.setMessage("Edit settings");

        //TEST Add LinearLayout to show in custom AlertDialog. !(Replace later with XML instead)!
        LinearLayout dialogLayout = new LinearLayout(MainActivity.this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 0, 50, 0);

        final LinearLayout fiatValueLayout = new LinearLayout(MainActivity.this);
        fiatValueLayout.setOrientation(LinearLayout.VERTICAL);
        fiatValueLayout.setPadding(0, 0, 0, 20);

        final TextView currencyInfo = new TextView(MainActivity.this);
        currencyInfo.setText("Preferred fiat currency: ");
        currencyInfo.setPadding(15, 15, 0, 0);

        final RadioGroup currencyGroup = new RadioGroup(MainActivity.this);
        currencyGroup.setOrientation(LinearLayout.HORIZONTAL);
        currencyGroup.setPadding(10, 0, 0, 0);

        final RadioButton currencyEUR = new RadioButton(MainActivity.this);
        currencyEUR.setText("Euro");
        currencyEUR.setPadding(0, 0, 10, 0);

        final RadioButton currencyUSD = new RadioButton(MainActivity.this);
        currencyUSD.setText("USD");

        currencyGroup.addView(currencyUSD);
        currencyGroup.addView(currencyEUR);
        fiatValueLayout.addView(currencyInfo);
        fiatValueLayout.addView(currencyGroup);


        final LinearLayout changePercentageLayout = new LinearLayout(MainActivity.this);
        changePercentageLayout.setOrientation(LinearLayout.VERTICAL);
        changePercentageLayout.setPadding(0, 0, 0, 20);

        final TextView percentageInfo = new TextView(MainActivity.this);
        percentageInfo.setText("Data change percentage rate: ");
        percentageInfo.setPadding(15, 15, 0, 0);

        final RadioGroup percentageGroup = new RadioGroup(MainActivity.this);
        percentageGroup.setOrientation(LinearLayout.HORIZONTAL);
        percentageGroup.setPadding(10, 0, 0, 0);

        final RadioButton radio1h = new RadioButton(MainActivity.this);
        radio1h.setText("1H");
        radio1h.setPadding(0, 0, 23, 0);

        final RadioButton radio24h = new RadioButton(MainActivity.this);
        radio24h.setText("24H");
        radio24h.setPadding(0, 0, 23, 0);


        final RadioButton radio7d = new RadioButton(MainActivity.this);
        radio7d.setText("7D");

        percentageGroup.addView(radio1h);
        percentageGroup.addView(radio24h);
        percentageGroup.addView(radio7d);
        changePercentageLayout.addView(percentageInfo);
        changePercentageLayout.addView(percentageGroup);


        //Add views to dialog layout
        dialogLayout.addView(fiatValueLayout);
        dialogLayout.addView(changePercentageLayout);

        //Set dialog layout to settings dialog view
        settingsDialog.setView(dialogLayout);

        // Setting Positive "Done" Button
        settingsDialog.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Execute after dialogs DONE button is pressed

                        if (currencyEUR.isChecked()) {
                            eurFiat = true;
                        } else if (currencyUSD.isChecked()) {
                            eurFiat = false;
                        }

                        if (radio1h.isChecked()) {
                            changePercentRate = "1H";
                        } else if (radio24h.isChecked()) {
                            changePercentRate = "24H";
                        } else if (radio7d.isChecked()) {
                            changePercentRate = "7D";
                        }

                        // An Editor object to make preference changes. All objects are from android.context.Context
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor settingsEditor = settings.edit();
                        settingsEditor.putBoolean("eurFiatMode", eurFiat);
                        settingsEditor.putString("changePercentRate", changePercentRate);

                        // Commit the edits!
                        settingsEditor.commit();

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Settings changed", Toast.LENGTH_SHORT).show();

                    }
                });

        // Setting Negative "Cancel" Button
        settingsDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });


        //Showing dialog
        settingsDialog.show();

    }

}
