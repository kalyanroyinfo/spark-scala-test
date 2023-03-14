import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object WordCount extends App {

  Logger.getLogger("org").setLevel(Level.ERROR)

  val sc = new SparkContext("local[*]", "wordcount")
  val input = sc.textFile("/Users/kalyanroy/Documents/Big-Data/Week-9/search_data-201008-180523.txt")
  //red hat big data certification
  val word = input.flatMap(x => x.split(" "))
  //red
  //hat
  val lowerCaseWords = word.map(x => x.toLowerCase())
  //red
  //hat
  val wordMap = lowerCaseWords.map(x => (x, 1))
  //(red,1)
  //(hat,1)
  //(hat,1)

  val finalCount = wordMap.reduceByKey((a, b) => a + b)
  //(red,1)
  //(hat,2)
  //  val reversedTuple=finalCount.map(x=>(x._2,x._1))
  //  val sortedElement=reversedTuple.sortByKey(false).map(x=>(x._2,x._1))
  val sortedElement = finalCount.sortBy(x => x._2, false)
  var results = sortedElement.collect()
  for (result <- results) {
    val word = result._1
    val count = result._2
    println(s"$word:$count")
  }

  scala.io.StdIn.readLine()

}
