package com.example.thenry.rssnews;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by thenry on 16/02/2017.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // on initialise le realm à la création de l'application
        Realm.init(this);
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("lemonderss.realm")
                .deleteRealmIfMigrationNeeded() //nettoie si on change le modèle du realm
                .build();
        Realm.setDefaultConfiguration(myConfig);
    }
}
