package xyz.taika.cryptohodler;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jukka1 on 8.8.2017.
 */

public class AssetAdapter extends ArrayAdapter<Asset> {
    //private int colorResourceId;

    public AssetAdapter(Context context, ArrayList<Asset> assetList) {
        super(context, 0, assetList);
        //this.colorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        // Get the Asset object located at this position in the list
        Asset currentAssetObject = getItem(position);


        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.asset_name);
        // Get the name from the current Asset object and set this text on the name TextView
        if (currentAssetObject != null) {
            nameTextView.setText(currentAssetObject.getAssetName());
        }

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView valueTextView = (TextView) listItemView.findViewById(R.id.asset_value);
        // Get the asset value from the current object and set this as a text on the value TextView
        if (currentAssetObject != null) {
            valueTextView.setText("" + currentAssetObject.getAssetValue());
        }

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView quantityTextView = (TextView) listItemView.findViewById(R.id.asset_quantity);
        // Get the asset quantity from the current object and set this as a text on the value TextView
        if (currentAssetObject != null) {
            quantityTextView.setText("" + currentAssetObject.getAssetQuantity());
        }

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView totalValueTextView = (TextView) listItemView.findViewById(R.id.total_value);
        // Get the asset quantity from the current object and set this as a text on the value TextView
        if (currentAssetObject != null) {
            totalValueTextView.setText("" + String.format("%.2f", currentAssetObject.getTotalValue()));
        }

        // Find the ImageView in the list_item.xml layout with the ID image
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.image);
        // Get the image resource ID from the current Word object and set the image to iconView
        assert currentAssetObject != null;
        iconView.setImageResource(currentAssetObject.getImageResourceId());


        /*

        // Set the theme color for the list item

        View textContainer = listItemView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), colorResourceId);
        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);


        // Return the whole list item layout (containing 2 TextViews and an ImageView) so that it can be shown in the ListView



*/



        return listItemView;
    }
}
