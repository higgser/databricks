# Databricks Platform

Demonstration of the Databricks Platform and Apache Spark. Slides are availables
on the Github Pages of the repository.

* https://higgser.github.io/databricks

To run the examples you need to have a community or (or commercial) Databricks account. If you don't have an account, you can create one for free.

* https://databricks.com/try-databricks

## Twitter Streaming example

Twitter example require valid API keys. Since they must never be commit to a
repository, they following widgets must be added to each notebook once.

```scala
dbutils.widgets.text("accessToken", "ACCESS_TOKEN")
dbutils.widgets.text("accessTokenSecret", "ACCESS_TOKEN_SECRET")
dbutils.widgets.text("consumerKey", "CONSUMER_KEY")
dbutils.widgets.text("consumerSecret", "CONSUMER_SECRET")
```
