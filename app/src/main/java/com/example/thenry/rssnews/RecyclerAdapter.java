package com.example.thenry.rssnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

/**
 * Created by thenry on 16/02/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private OrderedRealmCollection<Article> articleList;
    private Context ctx;
    private ItemClickListener clickListener;

    public RecyclerAdapter(Realm realm, Context context){ // constructeur avec la liste de données
        this.articleList = realm.where(Article.class).findAll();
        this.ctx = context;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())   // on inflate le holder à partir du layout de ligne
                .inflate(R.layout.item_rss, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{   // Holder correspond à une ligne de la liste

        public ImageView img_article;
        public TextView title_article;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_article=(ImageView) itemView.findViewById(R.id.img_list);
            title_article=(TextView) itemView.findViewById(R.id.title_list);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) { //méthode qui lie les données au holder
        final Article article = articleList.get(position);

        holder.title_article.setText(article.getTitle());
        Picasso.with(ctx).load(article.getImgLink()).into(holder.img_article); // on récupère l'image avec la librairie picasso
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}
