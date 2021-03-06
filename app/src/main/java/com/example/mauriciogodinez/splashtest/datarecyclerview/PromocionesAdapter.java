package com.example.mauriciogodinez.splashtest.datarecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mauriciogodinez.splashtest.R;

import java.util.ArrayList;

public class PromocionesAdapter extends RecyclerView.Adapter<PromocionesAdapter.ViewHolders> {
    private ArrayList<PromocionesItem> categoriaList;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String text);
    }

    @Override
    public PromocionesAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        Context vhContext = parent.getContext();
        int layoutIdForListItem = R.layout.card_promociones_item;
        LayoutInflater inflater = LayoutInflater.from(vhContext);
        final boolean shouldAttachToParentImmediately = false;

        View itemView = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new PromocionesAdapter.ViewHolders(itemView);
    }

    public PromocionesAdapter(ArrayList<PromocionesItem> categoriaList, ListItemClickListener listener) {
        this.mOnClickListener = listener;
        this.categoriaList = categoriaList;
    }

    @Override
    public void onBindViewHolder(PromocionesAdapter.ViewHolders holder, int position) {
        PromocionesItem ci = categoriaList.get(position);
        final String mensaje = ci.getTitleItem();

        holder.vImagenCategoria.setImageResource((ci.getImagenItem()));
        holder.vTituloCategoria.setText(ci.getTitleItem());
        holder.vContenidoCaterogia.setText(ci.getContentItem());

    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }


    public void setFilter(ArrayList<PromocionesItem> promo) {
        categoriaList = new ArrayList<>();
        categoriaList.addAll(promo);
        notifyDataSetChanged();
    }



    public class ViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView vImagenCategoria;
        private TextView vTituloCategoria;
        private TextView vContenidoCaterogia;

        public ViewHolders(View itemView) {
            super(itemView);

            vImagenCategoria = (ImageView) itemView.findViewById(R.id.image_promo);
            vTituloCategoria = (TextView) itemView.findViewById(R.id.text_view_title_promo);
            vContenidoCaterogia = (TextView) itemView.findViewById(R.id.text_view_content_promo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String text = vTituloCategoria.getText().toString();
            mOnClickListener.onListItemClick(clickedPosition, text);
        }

    }
}
