import findspark

findspark.init('spark-3.2.1-bin-hadoop3.2')

import pyspark
from pyspark.sql import SparkSession
import os
import shutil
import time
from pyspark.sql.types import StructType, StructField, StringType, FloatType
import pyspark.sql.functions as funsp

if __name__ == "__main__":

    os.environ[
        'PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-streaming-kafka-0-10_2.12:3.2.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.2.1 pyspark-shell'

    localhost = 'localhost:9092'

    KAFKA_TOPIC_NAME = "transaction"
    KAFKA_BOOTSTRAP_SERVERS_CONS = localhost

    checkpointDir = "./CheckPoint"
    path = "./result"

    # clean checkpointDir
    try:
        shutil.rmtree(checkpointDir)
    except OSError as e:
        print("Error: %s - %s." % (e.filename, e.strerror))

    # clean checkpointDir
    try:
        shutil.rmtree(path)
    except OSError as e:
        print("Error: %s - %s." % (e.filename, e.strerror))

    spark = (SparkSession
             .builder
             .appName("PySpark Structured Streaming with Kafka")
             .getOrCreate())

    jsonSchema = '''id, symbol, name, image, current_price, market_cap, market_cap_rank, fully_diluted_valuation,
                    total_volume, high_24h, low_24h, price_change_24h, price_change_percentage_24h,
                    market_cap_change_24h, market_cap_change_percentage_24h, circulating_supply, total_supply, 
                    max_supply, ath, ath_change_percentage, ath_date, atl, atl_change_percentage, atl_date, roi,
                    last_updated'''
    '''jsonStream = (spark
                  .readStream.format("json")
                  #.schema(jsonSchema)
                  .load("./source"))

    jsonStream.show()'''

    lines = (spark
             .readStream.format("kafka")
             .option("kafka.bootstrap.servers", KAFKA_BOOTSTRAP_SERVERS_CONS)
             .option("subscribe", KAFKA_TOPIC_NAME)
             .option("startingOffsets", "earliest")
             .load())

    '''df = spark.read.json(lines.parallelize([lines]))
    df.show(truncate=False)'''
    lines = lines.selectExpr("CAST(value AS STRING)", "CAST(timestamp AS STRING)")

    # schema = StructType().add("a", StringType()).add("b", StringType())
    # lines.select(funsp.col("key").cast("string"), funsp.from_json(funsp.col("value").cast("string"), schema))

    streamingQuery = (lines
                      .repartition(1)
                      .writeStream
                      .queryName("sDF")
                      .outputMode("append")
                      .format("console")
                      .option("checkpointLocation", checkpointDir)
                      .trigger(processingTime="10 second")
                      .start())

    if (streamingQuery.isActive):
        time.sleep(10)
        newDF = spark.sql('''select * from sDF)''')

        newDF = (newDF
                 .withColumn('name', dict(funsp.lit(newDF.select('value').collect[0][0]['name'])))
                 .withColumn('price', dict(funsp.lit(newDF.select('value').collect[0][0]['current_price'])))
                 .withColumn('date_hour', funsp.lit(newDF.select('timestamp').collect[0][0])))


        print('log')

        newDF.createOrReplaceTempView("trade")



        newDF = spark.sql('''select name, price, date_hour, 
                          max(price) OVER(PARTITION BY name) as max_price,
                          min(price) OVER(PARTITION BY name) as min_price                          
                          from trade''')


        writeDF = spark.sql('''select * from trade where date_hour != (select max(date_hour) from trade)''')

        if writeDF.agg(funsp.count('name')).collect()[0][0] > 0:
            (writeDF.write
             .format("jdbc")
             .mode("append")
             .option("driver", 'org.postgresql.Driver')
             .option("url", "jdbc:postgresql:localhost:5432")
             .option("dbtable", "coinsdata")
             .option("user", "useruseruser")
             .option("password", "pass12345word")
             .save())

    streamingQuery.stop()
    spark.stop()
