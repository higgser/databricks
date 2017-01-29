// Databricks notebook source
dbutils.widgets.text("filter", "Trump", "Text Filter")

// COMMAND ----------

dbutils.widgets.dropdown("retweets", "No", Seq("No", "Yes"), "Retweets")

// COMMAND ----------

// MAGIC %sql refresh table tweets

// COMMAND ----------

// MAGIC %sql refresh table votes

// COMMAND ----------

// MAGIC %sql select count(*) from tweets

// COMMAND ----------

// MAGIC %sql select date_format(time, "dd.MM.yyyy HH:mm:ss") as Time, text as Text, user as User, isRetweet as Retweet from tweets
// MAGIC where text like concat("%", getArgument("filter"),"%") and isRetweet == (getArgument("retweets") == "Yes") limit 100

// COMMAND ----------

// MAGIC %sql select lang, count(*) from tweets group by lang

// COMMAND ----------

// MAGIC %sql select date_format(time, "HH:mm") as Time, isRetweet as Retweet, count(*) as Tweets from tweets
// MAGIC group by time, isRetweet order by Time

// COMMAND ----------

// MAGIC %sql select date_format(time, "HH:mm") as Time, candidate as Candiate, count as Tweets from votes order by Time

// COMMAND ----------

