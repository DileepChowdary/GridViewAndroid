package com.dileeptest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dileeptest.MainActivity;
import com.dileeptest.R;
import com.dileeptest.model.GridViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter
{
    Context context;
    ArrayList<GridViewModel> gridViewModelArrayList;
    public GridViewAdapter(Context context, ArrayList<GridViewModel> gridViewModelArrayList) {
        this.context = context;
        this.gridViewModelArrayList = gridViewModelArrayList;
    }

    @Override
    public int getCount() {
        return gridViewModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gridViewModelArrayList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(view == null)
        {
            view = inflater.inflate(R.layout.flicker_photo_item,null);
        }
        else {
            view = convertView;
        }
        ImageView flicker_Photo_IMG = view.findViewById(R.id.flicker_Photo_IMG);

        GridViewModel gridViewModel = gridViewModelArrayList.get(position);

        String imageViewURL = "http://farm"+gridViewModel.getFarm()+".static.flickr.com/"+gridViewModel.getServer()+"/"+gridViewModel.getId()+"_"+gridViewModel.getSecret()+".jpg";

      /*  http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
        http://farm1.static.flickr.com/578/23451156376_8983a8ebc7.jpg*/

        Picasso.with(context).load(imageViewURL).into(flicker_Photo_IMG);
        return view;
    }
}
