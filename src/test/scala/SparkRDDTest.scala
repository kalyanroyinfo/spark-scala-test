import grizzled.slf4j.Logging
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.scalatest.{Outcome, fixture}

import java.io.InputStream

class SparkRDDTest extends fixture.FunSuite with Logging {

    type FixtureParam = SparkContext

    def withFixture(test: OneArgTest): Outcome = {
        val sc = new SparkContext("local[*]", "wordcount")
        try {
            withFixture(test.toNoArgTest(sc))
        } finally sc.stop
    }

    test("Word Count") { sc =>
        val input = sc.textFile("search_data.txt")
        val word = input.flatMap(x => x.split(" "))
        val lowerCaseWords = word.map(x => x.toLowerCase())
        val wordMap = lowerCaseWords.map(x => (x, 1))
        val finalCount = wordMap.reduceByKey((a, b) => a + b)
        val sortedElement = finalCount.sortBy(x => x._2, false)
        var results = sortedElement.collect()
        for (result <- results) {
            val word = result._1
            val count = result._2
            println(s"$word:$count")
        }
    }



    private def getInputData(name: String): Seq[String] = {
        val is: InputStream = getClass.getResourceAsStream(name)
        scala.io.Source.fromInputStream(is).getLines.toSeq
    }
}
