import org.apache.spark.sql.SparkSession
// Initialize SparkSession
val spark = SparkSession.builder.appName("PageRank").getOrCreate()

// Read input data from GCS
val inputUri = "gs://w6h2/input/input.txt"

val lines = spark.read.textFile(inputUri).rdd

// Create an RDD of (destination page, list of links) pairs
def parseLinks(link: String): (String, Array[String]) = {
    val links = link.split(" ")
    val destination = links(0)
    val source = links.slice(1, links.length)
    return(destination, source)
}

val links = lines.map(parseLinks)

// Initialize the PageRank values 1.0 for each web page
val ranks = links.mapValues(_ => 1.0)

val dampingFactor = 0.85

var currentRanks = ranks

for (i <- 0 until 2) {
  val contributions = links.join(currentRanks).flatMap { case (_, (pages, rank)) => pages.map(page => (page, rank / pages.length)) }
  val newRanks = contributions.reduceByKey(_ + _).mapValues(rank => (1 - dampingFactor) + dampingFactor * rank)
  currentRanks = currentRanks.join(newRanks).mapValues { case (oldRank, newRank) => newRank }
}

currentRanks.collect()


