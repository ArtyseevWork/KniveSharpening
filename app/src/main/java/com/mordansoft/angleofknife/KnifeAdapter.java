package com.mordansoft.angleofknife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.mordansoft.angleofknife.models.Knife;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KnifeAdapter extends RecyclerView.Adapter<KnifeAdapter.ViewHolder> {

    private final List<Knife> listKnife;
    private Listener listener;
    private Context mContext;

    public KnifeAdapter(ArrayList<Knife> listKnife, Context mContext) {
        this.listKnife = listKnife;
        this.mContext = mContext;
    }

    public interface Listener {
        void onClick(Long position);
    }

    public void setListener(KnifeAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) { //view for one item
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_knife, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {  //download data from list to THE ite
        Knife knife = listKnife.get(position);
        CardView cardView = viewHolder.cardView;
        viewHolder.tv_name.setText(String.valueOf        (knife.getName()));
        DecimalFormat dF = new DecimalFormat( "#" );
        viewHolder.tv_angle.setText(dF.format(knife.getAngle()));
        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(knife.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKnife.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tv_name;
        final TextView tv_angle;
        private final CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
            tv_name  = v.findViewById(R.id.tv_card_knife_card_name);
            tv_angle = v.findViewById(R.id.tv_card_knife_angle);
        }
    }

}