package com.apostpapad.dailytips;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoriteFragment";

    private PreferencesConfig preferencesConfig;
    private int[] favoriteTipsIndexes;
    private int favoritesNum;
    private String[] tips;

    private TextView noFavoriteTipsTV;
    private ListView listView;
    private CustomAdapter customAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        initLayout(view);

        tips = getResources().getStringArray(R.array.tips);

        preferencesConfig = new PreferencesConfig(getContext());
        favoriteTipsIndexes = preferencesConfig.readFavoriteTips();
        noFavoriteTipsTV.setText("No favorite Tips");


        if (favoriteTipsIndexes.length == 1 && favoriteTipsIndexes[0] == -1) {
            noFavoriteTipsTV.setText("No favorite Tips");
        } else {
            noFavoriteTipsTV.setVisibility(View.INVISIBLE);

            favoritesNum = favoriteTipsIndexes.length;
            customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);

        }

    }

    private void initLayout(View view) {
        noFavoriteTipsTV = view.findViewById(R.id.noFavoriteTipsTextView);

        listView = view.findViewById(R.id.favoriteTipsListView);

    }

    /**
     * Remove tip from favorite list and update listView
     *
     * @param position Tip position
     */
    private void removeFavorite(int position) {
        int index = favoriteTipsIndexes[position];
        preferencesConfig.writeRemoveFavoriteTip(index);

        favoriteTipsIndexes =preferencesConfig.readFavoriteTips();

        if(favoriteTipsIndexes.length==1 && favoriteTipsIndexes[0]==-1)
        {
            Log.d(TAG, "removeFromFavorite: len=1");
            int[] favoriteTemp = new int[1];
            favoriteTemp[0]=-1;
            listView.setVisibility(View.INVISIBLE);
            noFavoriteTipsTV.setVisibility(View.VISIBLE);
            preferencesConfig.writeFavoriteTips(favoriteTemp);
        }
        else {
            favoritesNum = favoriteTipsIndexes.length;
            customAdapter.notifyDataSetChanged();

        }
    }

    /**
     * ListView custom adapter that supports different listView object layout and buttons.
     */
    private class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return favoritesNum;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.favorite_tip_layout, null);

            TextView tipTV = convertView.findViewById(R.id.tipTextView);
            Log.d(TAG, "getView: position = " + position + " is " + favoriteTipsIndexes[position]);
            tipTV.setText(tips[favoriteTipsIndexes[position]]);
            ImageButton removeBtn = convertView.findViewById(R.id.removeButton);
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Press remove on " + position, Toast.LENGTH_SHORT).show();
                    removeFavorite(position);
                }
            });


            return convertView;
        }
    }


}
