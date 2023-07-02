from pyspark.sql import SparkSession
import sys


# Initialize SparkSession
spark = SparkSession.builder.appName("PageRank").getOrCreate()


# Read input data from GCS
if len(sys.argv) != 2:
  raise Exception("Exactly 1 arguments are required: <inputUri>")
inputUri = sys.argv[1]


lines = spark.read.text(inputUri).rdd.map(lambda r: r[0])


# Create an RDD of (destination page, list of links) pairs
def parse_links(link):
  links = link.split(" ")
  destination = links[0]
  source = links[1:]
  return (destination, source)


links = lines.map(parse_links)


# Initialize the PageRank values 1.0 for each web page
ranks = links.map(lambda page: (page[0], 1.0))


damping_factor = 0.85


# 2 iterations
for i in range(2):
   contributions = links.join(ranks).flatMap(lambda x: [(page, x[1][1] / len(x[1][0])) for page in x[1][0]])
   ranks = contributions.reduceByKey(lambda x, y: x + y).mapValues(lambda rank: (1-damping_factor) + damping_factor * rank)
   # Print each iteration result
   print(ranks.collect())


# Print
for (page, rank) in ranks.collect():
   print("%s has rank: %s." % (page, rank))


# Stop the SparkSession
spark.stop()
