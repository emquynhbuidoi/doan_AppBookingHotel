package com.example.appbookinghotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LoactionViewHolder> implements Filterable {
    ArrayList<Hotel> dsLocation;
    ArrayList<Hotel> dsLocationFilter;
    onClick nghe;

    public LocationAdapter(onClick langNghe) {
        this.nghe = langNghe;
    }

    public void setData(ArrayList<Hotel> Locations) {
        this.dsLocation = Locations;
        this.dsLocationFilter = Locations;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LoactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_row, parent, false);
        return new LoactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoactionViewHolder holder, int position) {
        Hotel hotelLocation = dsLocationFilter.get(position);

        holder.tvLoactionName.setText(hotelLocation.getLocation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nghe.nhanVaoLocation(hotelLocation);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dsLocationFilter != null)
            return dsLocationFilter.size();
        else return 0;
    }

    @Override
    public Filter getFilter() {
        LocationFilter locationFilter = new LocationFilter();
        return locationFilter;
    }

    public class LoactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoactionName;

        public LoactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoactionName = itemView.findViewById(R.id.tvLocationName);
        }
    }

    class LocationFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String chuoiTimKiem = constraint.toString();
            if (chuoiTimKiem.isEmpty()) {
                dsLocationFilter = dsLocation;
            } else {
                List<Hotel> temp = new ArrayList<>();
                for (Hotel lc : dsLocation) {
                    if (lc.getLocation().toLowerCase().contains(chuoiTimKiem.toLowerCase())){
                        temp.add(lc);
                    }
                }
                dsLocationFilter = (ArrayList<Hotel>) temp;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = dsLocationFilter;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dsLocationFilter = (ArrayList<Hotel>) results.values;
            notifyDataSetChanged();
        }
    }

    interface onClick {
        void nhanVaoLocation(Hotel hotelLocation);
    }
}
