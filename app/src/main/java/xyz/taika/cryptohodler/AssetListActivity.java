package xyz.taika.cryptohodler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AssetListActivity extends AppCompatActivity {

    private AssetList assetList;
    private AssetAdapter assetAdapter;
    private boolean needDelay;
    private boolean eurFiat;
    private String changePercentRate;
    UpdateDataTask updateDataTask;

    // this way we know we're looking at the response from our own action
    //private static final int SELECT_PICTURE = 101;
    //public static final String IMAGE_TYPE = "image/*";


    @Override
    public void onResume() {
        super.onResume();
        needDelay = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        //Get Intent data from MainActivity
        this.eurFiat = getIntent().getExtras().getBoolean("eurFiat");
        this.changePercentRate = getIntent().getExtras().getString("changePercentRate");

        //Read file from internal storage
        this.assetList = new AssetList();
        this.assetList = readFromInternalStorage(AssetListActivity.this);

        //By default user don't need to wait to update data
        needDelay = false;

        //TEST Create  all default Asset objects
        assetList.addNewAssetToList("Bitcoin", "BTC", 1.0, R.mipmap.bitcoin);
        assetList.addNewAssetToList("Ethereum", "ETH", 1.0, R.mipmap.ethereum);
        assetList.addNewAssetToList("Komodo", "KMD", 1.0, R.mipmap.komodo);
        assetList.addNewAssetToList("Byteball", "GBYTE", 1.0, R.mipmap.byteball);
        assetList.addNewAssetToList("NEO", "NEO", 1.0, R.mipmap.neo);
        assetList.addNewAssetToList("GAS", "GAS", 1.0, R.mipmap.gas);
        assetList.addNewAssetToList("Bgold", "BTG", 1.0, R.mipmap.bgold);
        assetList.addNewAssetToList("Bdiamond", "BCD", 1.0, R.mipmap.bdiamond);
        assetList.addNewAssetToList("Litecoin", "LTC", 1.0, R.mipmap.litecoin);
        assetList.addNewAssetToList("Bitcore", "BTX", 1.0, R.mipmap.bitcore);
        assetList.addNewAssetToList("Bcash", "BCH", 1.0, R.mipmap.bcash);
        assetList.addNewAssetToList("HEAT", "HEAT", 1.0, R.mipmap.heat);
        assetList.addNewAssetToList("NEM", "XEM", 1.0, R.mipmap.nem);
        assetList.addNewAssetToList("Zcash", "ZEC", 1.0, R.mipmap.zcash);
        assetList.addNewAssetToList("Stellar", "XLM", 1.0, R.mipmap.stellar);
        assetList.addNewAssetToList("Monero", "XMR", 1.0, R.mipmap.monero);
        assetList.addNewAssetToList("Factom","FCT", 1.0, R.mipmap.factom);


        //Create a AssetAdapter and give this (AssetListActivity) as a context
        assetAdapter = new AssetAdapter(this, assetList.getAssetList());

        //Create a ListView object and allocate correct XML-layout to it
        ListView listView = (ListView) findViewById(R.id.asset_list);

        if (listView != null) {
            listView.setAdapter(assetAdapter);
        }


        //Add floating action button with add new asset -functionality
        FloatingActionButton newAssetFAB = (FloatingActionButton) findViewById(R.id.new_asset_fab);
        newAssetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show Add new asset dialog
                showNewAssetDialog();

            }
        });


        // Set a click listener to show custom Dialog that enables a change to change specific list view item when the list item is clicked on
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get and store just clicked word object temporarily.
                Asset assetJustClicked = assetList.getAssetList().get(position);

                //Show dialog
                showEditAssetDialog(assetJustClicked);
            }
        });


        //Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("AssetListActivity", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        getApiData();

                        // The method calls setRefreshing(false) when it's finished.
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    //Show dialog that gives possibility to edit asset from the list
    public void showEditAssetDialog(Asset assetJustClicked) {

        final Asset clickedAsset = assetJustClicked;

        // Creating alert Dialog with one Button
        final AlertDialog.Builder editAssetDialogBuilder = new AlertDialog.Builder(AssetListActivity.this);
        editAssetDialogBuilder.setTitle("EDIT ASSET");
        editAssetDialogBuilder.setIcon(clickedAsset.getImageResourceId());
        editAssetDialogBuilder.setMessage("Edit your " + clickedAsset.getAssetName() + " asset");

        // Inflate layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_asset_dialog, null);
        editAssetDialogBuilder.setView(dialogView);

        // Set views
        final EditText assetNameField = (EditText) dialogView.findViewById(R.id.asset_name_field);
        final EditText assetQuantityField = (EditText) dialogView.findViewById(R.id.asset_quantity_field);
        final Button changeAssetLogoButton = (Button) dialogView.findViewById(R.id.change_logo_button);


        // Setting Positive "Done" Button
        editAssetDialogBuilder.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog's DONE button is pressed
                        for (Asset asset : assetList.getAssetList()) {
                            if (clickedAsset.getAssetName().toLowerCase().equals(asset.getAssetName().toLowerCase())) {
                                String assetQuantityString = assetQuantityField.getText().toString();
                                String assetName = assetNameField.getText().toString();

                                //If user has put data in Asset quantity field, do update
                                if (!assetQuantityString.isEmpty()) {
                                    Double assetQuantityDouble = Double.valueOf(assetQuantityString);
                                    asset.setAssetQuantity(assetQuantityDouble);
                                }

                                //If user has put data in Asset name field, do update
                                if (!assetName.isEmpty()) {
                                    asset.setAssetName(assetName);
                                }
                            }
                        }
                        assetList.updateTotalValues();

                        //Sort list
                        assetList.sortList();

                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

                        //Delete old saved file and save new one with changed data
                        deleteFile("assetListData");
                        saveAssetListToInternalStorage(AssetListActivity.this);

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Asset data changed", Toast.LENGTH_SHORT).show();
                    }
                });

        // Setting Negative "Cancel" Button
        editAssetDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Add delete button to edit asset dialog
        editAssetDialogBuilder.setNeutralButton("DELETE ASSET",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        assetList.deleteAssetFromList(clickedAsset.getAssetName());

                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

                        //Delete old assetlist -file and save new one with fresh data
                        deleteFile("assetListData");
                        saveAssetListToInternalStorage(AssetListActivity.this);

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Asset deleted", Toast.LENGTH_SHORT).show();

                        //Dismiss dialog
                        dialog.cancel();
                    }
                });

        // Show dialog to user
        AlertDialog editAssetDialog = editAssetDialogBuilder.create();
        editAssetDialog.show();
    }

    //Show dialog that gives possibility to add new asset to the list
    public void showNewAssetDialog() {

        // Creating alert Dialog with one Button
        AlertDialog.Builder newAssetDialogBuilder = new AlertDialog.Builder(AssetListActivity.this);
        newAssetDialogBuilder.setTitle("ADD NEW ASSET");
        newAssetDialogBuilder.setMessage("Give asset name, symbol and quantity");

        // Inflate layout
        LayoutInflater newAssetDialogInflater = this.getLayoutInflater();
        View dialogView2 = newAssetDialogInflater.inflate(R.layout.new_asset_dialog, null);
        newAssetDialogBuilder.setView(dialogView2);

        // Set views
        final EditText newAssetNameField = (EditText) dialogView2.findViewById(R.id.new_asset_name_field);
        final EditText newAssetSymbolField = (EditText) dialogView2.findViewById(R.id.new_asset_symbol_field);
        final EditText newAssetQuantityField = (EditText) dialogView2.findViewById(R.id.new_asset_quantity_field);

        // Setting Positive "Done" Button
        newAssetDialogBuilder.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog's DONE button is pressed

                        String assetName = newAssetNameField.getText().toString();
                        String assetSymbol = newAssetSymbolField.getText().toString();
                        String assetQuantity = newAssetQuantityField.getText().toString();

                        //If user haven't givem any quantity value, use default data
                        if (assetQuantity.isEmpty()) {
                            assetQuantity = "0.0";
                        }
                        Double quantity = Double.valueOf(assetQuantity);

                        //Add this new Asset to the Assetlist
                        if (assetList.addNewAssetToList(assetName, assetSymbol, quantity)) {
                            Toast.makeText(getApplicationContext(), "Asset created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Asset already in the list", Toast.LENGTH_SHORT).show();
                        }

                        //Execute UpdateDataTask to get fresh API data
                        getApiData();

                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

                    }
                });

        // Setting Negative "Cancel" Button
        newAssetDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        AlertDialog newAssetDialog = newAssetDialogBuilder.create();
        newAssetDialog.show();
    }

    //This method starts UpdateDataTask that tries to get fresh API data from internet
    public void getApiData() {
        //Check if user has updated data already within 10 seconds otherwise skip and show toast
        if (!needDelay) {

            //Create new UpdateDataTask object
            this.updateDataTask = new UpdateDataTask(this,
                    this.assetList,
                    this.assetAdapter,
                    this.changePercentRate,
                    this.eurFiat);

            //Execute UpdateDataTask to get fresh API data
            if (this.eurFiat) {
                //Create new UpdateDataTask object
                this.updateDataTask.execute("https://api.coinmarketcap.com/v1/ticker/?convert=EUR&limit=0");
            } else {
                this.updateDataTask.execute("https://api.coinmarketcap.com/v1/ticker/?limit=0");
            }

            //Set 10sec delay before user can update data again
            needDelay = true;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    needDelay = false;
                }
            }, 10000);
        } else {
            // Show toast
            Toast.makeText(getApplicationContext(), "Fresh asset data can updated once in every 10 seconds", Toast.LENGTH_SHORT).show();
        }
    }

    // Save assetList to internal storage
    public void saveAssetListToInternalStorage(Context context) {
        try {
            String filename = "AssetListData";
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(this.assetList);

            ous.flush();
            ous.close();
            fos.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    //Get assetList data from internal Storage
    public AssetList readFromInternalStorage(Context context) {
        AssetList toReturn = new AssetList();
        FileInputStream fis;
        try {
            fis = context.openFileInput("AssetListData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            toReturn = (AssetList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

}
