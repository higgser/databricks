// Databricks notebook source
// MAGIC %md ## Setup Twitter Auth

// COMMAND ----------

// MAGIC %md ### Import Twitter Streaming Utils 
// MAGIC 
// MAGIC The [Apache Bahir](https://github.com/apache/bahir) library implements a streaming source to easily download tweets from [Twitter](http://twitter.com). It is an Apache Spark wrapper around the common the Java library [Twitter4J](http://twitter4j.org/). To use it in this script, you first have to import in your workspace. Make a right click in your workspace folder and click on 'Create' and 'Library'. Select 'Maven coordinates' in the dropdown and copy and paste. the coordinates `org.apache.bahir:spark-streaming-twitter_2.11:2.0.1`. Alternatively you can open the Search Dialog and type 'bahir' in the search field.

// COMMAND ----------

import twitter4j.{TwitterFactory, FilterQuery}
import twitter4j.conf.ConfigurationBuilder

// COMMAND ----------

// MAGIC %md ### Setup OAuth keys for Twitter API
// MAGIC 
// MAGIC Before running this notebook, you need OAuth access keys from Twitter to download the latest tweets. To get Twitter Access keys, you need to create Twitter Application which is mandatory to access Twitter. A detailed description with screenshots how to that is available on the following page https://iag.me/socialmedia/how-to-create-a-twitter-app-in-8-easy-steps. Afterward you created your application, fill the **Consumer Key** (API Key), **Consumer Secret** (API Secret) as well as the OAuth **
// MAGIC Access Token** and ** Access Token Secret** in the **top widgets**. Now you are ready the click the 'Run All' button at the top of the notebook.

// COMMAND ----------

// Read authorization keys from the widgets at the top of the notebook
val authConf = new ConfigurationBuilder().setDebugEnabled(true)
  .setOAuthConsumerKey(dbutils.widgets.get("consumerKey"))
  .setOAuthConsumerSecret(dbutils.widgets.get("consumerSecret"))
  .setOAuthAccessToken(dbutils.widgets.get("accessToken"))
  .setOAuthAccessTokenSecret(dbutils.widgets.get("accessTokenSecret")).build()
val twitterFactory = new TwitterFactory(authConf)
val auth = twitterFactory.getInstance.getAuthorization