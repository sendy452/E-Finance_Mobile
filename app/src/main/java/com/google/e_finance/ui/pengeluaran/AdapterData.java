package com.google.e_finance.ui.pengeluaran;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.e_finance.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private final List<ModelData> mItems;
    private final Context context;

    /**
     * Deklarasi untuk menampung banyak data
     */
    public AdapterData(Context context, List<ModelData> items) {
        this.mItems = items;
        this.context = context;
    }

    /**
     * Deklarasi layout_row yang menampung tiap data
     */
    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_pengeluaran, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    /**
     * Deklarasi tiap variabel yang menampung data sesuai dengan data di database
     */
    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelData md = mItems.get(position);
        holder.tvid.setText(md.getId());
        holder.tvtgl.setText(md.getTgl());
        holder.tvjumlah.setText(md.getJumlah());
        holder.tvketerangan.setText(md.getKeterangan());
        holder.tvnomer.setText(md.getIduser());
        holder.md = md;
    }

    /**
     * Menghitung banyaknya data yang diambil
     */
    @Override
    public int getItemCount() {
        return mItems.size();
    }


    /**
     * Membuat class HolderData turunan dari RecyclerView.ViewHolder
     */
    class HolderData extends RecyclerView.ViewHolder {
        TextView tvid, tvtgl, tvjumlah, tvketerangan, tvnomer;
        ModelData md;

        /**
         * Deklarasikan setiap widget yang ada di layout_row
         */
        @SuppressLint("ResourceType")
        public HolderData(View view) {
            super(view);
            tvnomer = view.findViewById(R.id.nomer);
            tvid = view.findViewById(R.id.id_pendapatan);
            tvtgl = view.findViewById(R.id.tgl);
            tvjumlah = view.findViewById(R.id.jumlah);
            tvketerangan = view.findViewById(R.id.keterangan);

            view.setOnClickListener(new View.OnClickListener() {
                /** Apabila salah satu data di klik maka akan menampilkan detail data*/
                @Override
                public void onClick(View view) {

                    Intent update = new Intent(context, UpdateData.class);
                    update.putExtra("update", 1);
                    update.putExtra("id_pengeluaran", md.getId());
                    update.putExtra("tgl_pengeluaran", md.getTgl());
                    update.putExtra("jumlah", md.getJumlah());
                    update.putExtra("keterangan", md.getKeterangan());
                    update.putExtra("id_user", md.getIduser());

                    context.startActivity(update);
                }
            });
        }
    }
}