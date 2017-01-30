// Databricks notebook source
// MAGIC %md # Twitter Tweet Analysis

// COMMAND ----------

// MAGIC %md This script has been tested with Spark 2.1.0. Make shure you run it on a cluster with Spark 2.0 or later. By clicking on the +/- symbol at right, you can open and close cells.

// COMMAND ----------

// Check the version of Spark
if (org.apache.spark.SPARK_VERSION < "2.0") throw new Exception("Please use a Spark 2.x cluster.")

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

// COMMAND ----------

// MAGIC %md To get more information about Databricks widget API you can call the `help` function, or access the [Widgets](https://docs.databricks.com/user-guide/notebooks/widgets.html) documention from the search field at the top-left of the page.

// COMMAND ----------

// dbutils.widgets.help()

// COMMAND ----------

// MAGIC %md ## Check connection and print tweets

// COMMAND ----------

// MAGIC %md First we check if the API keys are correct and it is possible to download some tweets. Every object in the stream as the type [twitter4j.Status](http://twitter4j.org/javadoc/twitter4j/Status.html). We create a new `StreamingContext` and print the text of some English tweets.

// COMMAND ----------

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.twitter.TwitterUtils
import spark.implicits._

// COMMAND ----------

// MAGIC %md If you do not see any tweets, something with your setup is not good.

// COMMAND ----------

val ssc = new StreamingContext(sc, Seconds(1))
val stream = TwitterUtils.createStream(ssc, Some(auth), Seq("Trump")).filter(_.getLang == "en").map(_.getText).print

// COMMAND ----------

ssc.start()
Thread.sleep(2000)
ssc.stop(stopSparkContext = false)

// COMMAND ----------

// MAGIC %md ## Stream Processing

// COMMAND ----------

// Download tweets every 5 seconds, keep a window of 5 minutes, and slide the window every 5 seconds
val batchInterval = Seconds(5)
val windowLength = Minutes(5)
val slideInterval = Seconds(5)
val candidates = Seq("trump", "hillary", "obama")

// COMMAND ----------

import java.sql.Timestamp
case class Tweet(time: Timestamp, text: String, lang: String, user: String, isRetweet: Boolean, isRetweeted: Boolean, longitude: Double, latitude: Double)
case class Vote(time: Timestamp, candidate: String, count: Integer)

// COMMAND ----------

val ssc = new StreamingContext(sc, batchInterval)
val stream = TwitterUtils.createStream(ssc, Some(auth), candidates).filter(_.getCreatedAt != null).filter(s => Seq("en","de","fr").contains(s.getLang)).flatMap(s => Seq("trump","hillary","obama").filter(s.getText.toLowerCase.contains).map(candidate => ((s.getCreatedAt, candidate),1))).reduceByKeyAndWindow((x: Int, y: Int) => x + y, windowLength, slideInterval).map(s => Vote(new Timestamp(s._1._1.getTime), s._1._2, s._2))
stream.foreachRDD { rdd => rdd.toDF("time", "candidate", "count").createOrReplaceTempView("candidate") }

// COMMAND ----------

ssc.start()
ssc.getState
Thread.sleep(5000)

// COMMAND ----------

// MAGIC %sql select candidate, sum(count) as count from candidate group by candidate order by count desc

// COMMAND ----------

// MAGIC %sql select date_format(time, "HH:mm:ss") as time, candidate, count from candidate order by time, candidate

// COMMAND ----------

// uncomment to stop stream
// ssc.stop(false)
ssc.getState

// COMMAND ----------


