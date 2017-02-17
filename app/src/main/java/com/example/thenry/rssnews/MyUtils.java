package com.example.thenry.rssnews;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by thenry on 17/02/2017.
 */

public class MyUtils {

    public static List<Article> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

        boolean isItem = false;
        String title = null;
        String description = null;
        String date = null;
        String imgUrl = null;
        List<Article> articleList = new ArrayList<>();

        //déclaration du parseur
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(inputStream, null);

        //parsing
        while (xpp.next() != XmlPullParser.END_DOCUMENT) {
            int eventType = xpp.getEventType();
            String name = xpp.getName();

            if (eventType == XmlPullParser.END_TAG) {
                if (name.equalsIgnoreCase("item")) {
                    isItem = false;
                }
                continue;
            }

            if (eventType == XmlPullParser.START_TAG) {
                if (name.equalsIgnoreCase("item")) {
                    isItem = true;
                    continue;
                }
                if (isItem) {
                    if (name != null) {
                        if (name.equalsIgnoreCase("title")) {
                            title = xpp.nextText();
                        } else if (name.equalsIgnoreCase("pubDate")) {
                            date = xpp.nextText();
                        } else if (name.equalsIgnoreCase("description")) {
                            description = xpp.nextText();
                        } else if (name.equalsIgnoreCase("enclosure")) {
                            imgUrl = xpp.getAttributeValue(null, "url");
                        }

                        if (title != null && date != null && description != null && imgUrl != null) { // on a récupéré toutes les infos
                            if (isItem) {

                                Article article = new Article();
                                article.setTitle(title);
                                article.setDate(date);
                                article.setDescription(description);
                                article.setImgLink(imgUrl);

                                articleList.add(article);

                                //on remet les variables à null
                                title = null;
                                date = null;
                                description = null;
                                imgUrl = null;
                                isItem = false;
                            }
                        }
                    }
                }
            }

        }
        inputStream.close();
        return articleList;
    }


    public static int getNextPrimaryKey(Realm realm)  // fonction pour avoir un id auto-incrémenté car realm ne le prend pas en charge
    {
        int key;
        try {
            key = realm.where(Article.class).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            key = 0;
        } catch (NullPointerException e) {
            key = 0;
        }
        return key;
    }
}
