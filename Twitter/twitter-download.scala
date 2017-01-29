// Databricks notebook source
// MAGIC %md # Download Twitter Tweets

// COMMAND ----------

// MAGIC %md This script has been tested with Spark 2.1.0. Make shure you run it on a cluster with Spark 2.0 or later. By clicking on the +/- symbol at right, you can open and close cells.

// COMMAND ----------

// Check the version of Spark
if (org.apache.spark.SPARK_VERSION < "2.0") throw new Exception("Please use a Spark 2.x cluster.")

// COMMAND ----------

// MAGIC %run ./twitter-auth

// COMMAND ----------

// MAGIC %md ## Script Parameters

// COMMAND ----------

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils
import spark.implicits._

// COMMAND ----------

// Download tweets every 5 seconds, keep a window of 5 minutes, and slide the window every 5 seconds
val batchInterval = Seconds(5)
// filter tweets with the following keywords
val candidates = Seq("trump", "hillary", "obama")

// COMMAND ----------

// Is it possible to use a FilterQuery?
val twitterFilter = new FilterQuery("trump", "hillary", "obama").language("en", "de", "fr")

// COMMAND ----------

// MAGIC %md ## Stream Tweets to a (temporary) Table

// COMMAND ----------

import java.sql.Timestamp
case class Tweet(time: Timestamp, text: String, lang: String, user: String, isRetweet: Boolean, isRetweeted: Boolean, longitude: Double, latitude: Double)
case class Vote(time: Timestamp, candidate: String, count: Integer)

// COMMAND ----------

// MAGIC %md You can write the tweets to a temporary table (`df.createOrReplaceTempView("tweets")`) or a persistent table (`df.write.saveAsTable("tweets")`).

// COMMAND ----------

val ssc = new StreamingContext(sc, batchInterval)
val tweetStream = TwitterUtils.createStream(ssc, Some(auth), candidates)
  .filter(_.getCreatedAt != null)
  .map(s => Tweet(new Timestamp(s.getCreatedAt.getTime), s.getText, s.getLang, s.getUser.getName, s.isRetweet, s.isRetweeted, if (s.getGeoLocation != null) s.getGeoLocation.getLongitude else Double.NaN, if (s.getGeoLocation != null) s.getGeoLocation.getLatitude else Double.NaN))

tweetStream.foreachRDD { rdd => rdd.toDF().write.mode(SaveMode.Append).saveAsTable("tweets") }

// COMMAND ----------

// MAGIC %md In the same stream we aggregate the tweets per candidate and second and write the counts to a second table named `votes`.

// COMMAND ----------

val voteStream = tweetStream
  .flatMap(s => Seq("trump","hillary","obama").filter(s.text.toLowerCase.contains).map(candidate => ((s.time, candidate),1)))
  .reduceByKey(_ + _)
  .map(s => Vote(new Timestamp(s._1._1.getTime), s._1._2, s._2))

voteStream.foreachRDD { rdd => rdd.toDF("time", "candidate", "count").write.mode(SaveMode.Append).saveAsTable("votes") }

// COMMAND ----------

// MAGIC %md As soon as we start the `StreamingContext` data will be append to two tables.

// COMMAND ----------

ssc.start()

// COMMAND ----------

// %sql select * from tweets

// COMMAND ----------

// %sql select * from votes

// COMMAND ----------

ssc.getState

// COMMAND ----------

// ssc.stop(stopSparkContext = false)

// COMMAND ----------

// MAGIC %sql select count(*), max(time), min(time) from tweets

// COMMAND ----------

// MAGIC %sql select count(*), max(time), min(time) from votes

// COMMAND ----------

