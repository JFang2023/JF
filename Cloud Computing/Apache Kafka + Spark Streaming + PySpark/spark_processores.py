from pyspark.conf import SparkConf
from pyspark.context import SparkContext
from pyspark.sql import SparkSession
from pyspark.sql.functions import udf, col
from pyspark.sql.types import ArrayType, StructType, StructField, StringType, IntegerType
from kafka import KafkaProducer
from collections import Counter

# Create SparkConf
def spark_context_creator():
    conf = SparkConf()
    conf.setAppName("ConnectingDotsSparkKafkaStreaming")
    conf.setMaster('spark://w12h1.us-west2-a.c.cs570jf.internal:8080')  # Replace with your Spark master URL
    sc = None
    try:
        sc.stop()
        sc = SparkContext(conf=conf)
    except:
        sc = SparkContext(conf=conf)
    return sc

sc = spark_context_creator()
sc.setLogLevel("WARN")

# Create SparkSession
spark = SparkSession.builder.appName("ConnectingDotsSparkKafkaStreaming").getOrCreate()

# Read from Kafka topic using Structured Streaming
kafkaStream = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "input_event") \
    .load()

# Process events
def process_events(value):
    return Counter(value.split(" ")).most_common(3)

process_udf = udf(process_events, ArrayType(StructType([StructField("word", StringType()), StructField("count", IntegerType())])))

processed_stream = kafkaStream.selectExpr("CAST(value AS STRING) as value") \
    .withColumn("processed", process_udf(col("value")))

# Write processed events back to Kafka topic
processed_query = processed_stream.selectExpr("to_json(struct(*)) AS value") \
    .writeStream \
    .format("kafka") \
    .outputMode("update") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("topic", "output_event") \
    .start()

# Wait for the query to terminate
processed_query.awaitTermination()

# Stop Spark context
spark.stop()
