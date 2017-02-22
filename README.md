# RssReader

Small project of an app reading the RSS feed from the french newspaper "Le Monde"

The feed is parsed in an AsyncTask, diplayed in a list of articles with a recyclerView, 
and clicking on an article from the list allows the user to go to a more detailed activity for that article.


##Libraries :  
* Butterknife for binding views
* Picasso for loading the pictures from the urls
* Realm for storing the articles to read them offline (a bit heavy overkill but i really like using this library)
