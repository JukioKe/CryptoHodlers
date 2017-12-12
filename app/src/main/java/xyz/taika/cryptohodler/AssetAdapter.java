package xyz.taika.cryptohodler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jukka13 on 8.8.2017.
 */

public class AssetAdapter extends ArrayAdapter<Asset> {
    private boolean eurFiat;


    AssetAdapter(Context context, ArrayList<Asset> assetList) {
        super(context, 0, assetList);
        // this.colorResourceId = colorResourceId;
        this.eurFiat = false;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        SharedPreferences settings = getContext().getSharedPreferences("SettingsPrefsFile", 0);
        this.eurFiat = settings.getBoolean("eurFiatMode", false);

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            if (eurFiat) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_eur, parent, false);
            } else {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }

        }

        // Get the Asset object located at this position in the list
        Asset currentAssetObject = getItem(position);

        // Get length of the asset value number so we can determine right margin
        assert currentAssetObject != null;
        String fiatValueString = currentAssetObject.getAssetValue().toString();

        int marginLength = 3; //default 3

        // Find out of how many numbers before .-mark
        for (int a=1; a<fiatValueString.length(); a++) {
            if (fiatValueString.charAt(a) == '.') {
                marginLength = a;
                break;
            }
        }

        // Find the TextView in the list_item.xml layout with the ID
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.asset_name);
        // Get the name from the current Asset object and set this text on the name TextView
        nameTextView.setText(currentAssetObject.getAssetName());

        // Find the TextView in the list_item.xml layout with the ID version_number

        TextView valueTextView = (TextView) listItemView.findViewById(R.id.asset_value);
        // Get the asset value from the current object and set this as a text on the value TextView
        if (marginLength == 1) {
            valueTextView.setText("" + String.format("%.6f", currentAssetObject.getAssetValue()));
        } else if(marginLength == 2) {
            valueTextView.setText("" + String.format("%.5f", currentAssetObject.getAssetValue()));
        } else if (marginLength == 3) {
            valueTextView.setText("" + String.format("%.4f", currentAssetObject.getAssetValue()));
        } else {
            valueTextView.setText("" + String.format("%.3f", currentAssetObject.getAssetValue()));
        }

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView changeTextView = (TextView) listItemView.findViewById(R.id.change_24h);
        // Get the 24h change of the asset value from the current object and set this as a text on the change TextView
        changeTextView.setText("(" + String.format("%.1f", currentAssetObject.getChange24h()) + "%)");
        if (currentAssetObject.getChange24h() < 0.0) {
            changeTextView.setTextColor(Color.parseColor("#FE2E2E"));
        } else {
            changeTextView.setTextColor(Color.parseColor("#298A08"));
        }

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView quantityTextView = (TextView) listItemView.findViewById(R.id.asset_quantity);

        // Get the asset quantity from the current object and set this as a text on the value TextView
        quantityTextView.setText("" + String.format("%.3f", currentAssetObject.getAssetQuantity()));

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView totalValueTextView = (TextView) listItemView.findViewById(R.id.total_value);

        // Get the asset quantity from the current object and set this as a text on the value TextView
        totalValueTextView.setText("" + String.format("%.2f", currentAssetObject.getTotalValue()));

        // Find the ImageView in the list_item.xml layout with the ID image
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.image);

        // Get the image resource ID from the current object and set the image to iconView
        iconView.setImageResource(currentAssetObject.getImageResourceId());


        /* TESTING
        // Set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);

        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), colorResourceId);

        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);
        */


        // Return the whole list item layout (containing 2 TextViews and an ImageView) so that it can be shown in the ListView
        return listItemView;
    }

}
