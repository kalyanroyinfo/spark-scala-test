import java.io.InputStream
import org.apache.spark.sql.SparkSession
import org.scalatest.{Outcome, fixture}
import grizzled.slf4j.Logging

class SparkStructuredAPITest extends fixture.FunSuite with Logging {

    type FixtureParam = SparkSession

    def withFixture(test: OneArgTest): Outcome = {
        val sparkSession = SparkSession.builder
                .appName("Test-Spark-Local")
                .master("local[2]")
                .getOrCreate()
        try {
            withFixture(test.toNoArgTest(sparkSession))
        } finally sparkSession.stop
    }

    test("empsRDD rowcount") { spark =>
        val empsRDD = spark.sparkContext.parallelize(getInputData("/data/employees.json"), 5)
        assert(empsRDD.count === 1000)
    }



    private def getInputData(name: String): Seq[String] = {
        val is: InputStream = getClass.getResourceAsStream(name)
        scala.io.Source.fromInputStream(is).getLines.toSeq
    }
}
