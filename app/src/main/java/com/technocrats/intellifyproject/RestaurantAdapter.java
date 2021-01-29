package com.technocrats.intellifyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<RestaurantItem> mRestaurantList;
    List<RestaurantItem> restaurantsListAll;

    public RestaurantAdapter(Context context, ArrayList<RestaurantItem> restaurantList){
        mContext=context;
        mRestaurantList=restaurantList;
        this.restaurantsListAll = new ArrayList<>(restaurantList);
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.restaurant_item,parent,false);
        return new RestaurantViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantItem currentItem=mRestaurantList.get(position);

        String name=currentItem.getRname();
        String address= currentItem.getRaddress();
        String cuisines= currentItem.getRcuisines();

        holder.mName.setText(name);
        holder.mAddress.setText(address);
        holder.mCuisines.setText(cuisines);

    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<RestaurantItem> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(restaurantsListAll);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (RestaurantItem items : restaurantsListAll) {
                    if (items.getRname().toLowerCase().contains(filterPattern)) {
                        filterList.add(items);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            mRestaurantList.clear();
            mRestaurantList.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{

        public TextView mName;
        public TextView mAddress;
        public TextView mCuisines;
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.nameid1);
            mAddress=itemView.findViewById(R.id.addressid1);
            mCuisines=itemView.findViewById(R.id.cuisinesid1);

        }
    }
}
