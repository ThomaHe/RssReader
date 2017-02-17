package com.example.thenry.rssnews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements ItemClickListener{

    // Bind des vues avec Butterknife
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeLayout;

    private Realm realm;
    private List<Article> mArticleList;
    public Context ctx;
    public RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        ctx = getApplicationContext();


        recyclerAdapter = new RecyclerAdapter(realm, ctx);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setClickListener(this);

        new FetchFeedTask().execute((Void) null);

        // fonction appelée lors du pull to refresh
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });

    }

    @Override
    public void onClick(View view, int position){
        int key = realm.where(Article.class).findAll().get(position).getId();
        Intent myIntent = new Intent(this, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("idArticle",key);
        myIntent.putExtras(extras);
        startActivity(myIntent);
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, List<Article>> {  // tâche asynchrone qui va chercher le flux rss

        private String link;

        @Override
        protected void onPreExecute() {
            swipeLayout.setRefreshing(true);
            link = "http://www.lemonde.fr/rss/une.xml";
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {

            try {
                URL url = new URL(link);
                InputStream inputStream = url.openConnection().getInputStream();
                mArticleList = MyUtils.parseFeed(inputStream); // on parse le flux rss
                return mArticleList;
            } catch (IOException e) {
                Log.e("FETCH", "Error", e);
            } catch (XmlPullParserException e) {
                Log.e("PARSER", "Error", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Article> result) {
            swipeLayout.setRefreshing(false);
            if (result == null) {
                Toast.makeText(MainActivity.this, "Erreur lors de la récupération du flux rss", Toast.LENGTH_SHORT).show();
            } else {
                // on vide le realm puisqu'on retourne chercher les données
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                    }
                });
                // on remplit le realm avec les nouvelles données
                for (final Article article : result) {
                    article.setId(MyUtils.getNextPrimaryKey(realm));
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(article);
                        }
                    });
                }
                recyclerAdapter.notifyDataSetChanged(); // notifie le changement des données à l'adapter pour qu'il les recharge
            }
        }


    }



}
