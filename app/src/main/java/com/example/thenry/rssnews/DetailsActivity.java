package com.example.thenry.rssnews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DetailsActivity extends AppCompatActivity {

    private Realm realm;

    @BindView(R.id.article_title)TextView title;
    @BindView(R.id.article_img)ImageView image;
    @BindView(R.id.article_date)TextView date;
    @BindView(R.id.article_description)TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("idArticle");
        Article article = realm.where(Article.class).equalTo("id",id).findFirst();

        title.setText(article.getTitle());
        Picasso.with(getApplicationContext()).load(article.getImgLink()).into(image);
        date.setText(article.getDate());
        description.setText(article.getDescription());


    }
}
