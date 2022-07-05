package com.example.knifesharpening;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knifesharpening.models.Knive;
import com.example.knifesharpening.models.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class KniveAdapter extends RecyclerView.Adapter<KniveAdapter.ViewHolder> {

    private List<Knive> listKnive;
    private Context mContext;
    private Listener listener;

    public KniveAdapter(ArrayList<Knive> listKnive, Context mContext) {
        this.listKnive = listKnive;
        this.mContext = mContext;
    }

    public interface Listener {
        void onClick(Long position);
    }

    public void setListener(KniveAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) { //view for one item
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_knive, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {  //download data from list to THE ite
        Knive knive = listKnive.get(position);
        CardView cardView = viewHolder.cardView;
        viewHolder.tv_name.setText(String.valueOf        (knive.getName()));
        viewHolder.tv_angle.setText(String.valueOf       (knive.getAngle()));

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(knive.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKnive.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tv_name;
        final TextView tv_angle;
        private final CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView       = v;
            tv_name         = v.findViewById(R.id.tv_card_knive_card_name);
            tv_angle        = v.findViewById(R.id.tv_card_knive_angle);
        }
    }

}