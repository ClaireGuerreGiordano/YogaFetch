package yoga.fetch

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.toTraverseOps
import fetch.Fetch
import yoga.fetch.model.Book

import java.util.concurrent._
import scala.concurrent.ExecutionContext

object YogaApp extends App {
  val executor = new ScheduledThreadPoolExecutor(4)
  val executionContext: ExecutionContext = ExecutionContext.fromExecutor(executor)


  def fetchBookFromCSV(isbn: String): Fetch[IO, Book] = {
    val csvFileDataSource = CSVFileDataSource
    Fetch(isbn, csvFileDataSource)
  }

  def fetchBookFromFile(isbn: String): Fetch[IO, Book] = {
    val fileDataSource = FileDataSource
    Fetch(isbn, fileDataSource)
  }

  def getBookFromCSV(isbn: String) = {
    Fetch
      .run[IO](fetchBookFromCSV(isbn))
  }

  private val sisyphe = "978-2-07071-966-2"
  val resultFromCSV = getBookFromCSV(sisyphe).unsafeRunSync()
  Console.println(s"Fetch from CSV isbn $sisyphe: $resultFromCSV")

  def getBookFromFile(isbn: String) = {
    Fetch
      .run[IO](fetchBookFromFile(isbn))
  }

  private val lamer = "978-2-07285-855-0"
  val resultFromFile = getBookFromFile(lamer).unsafeRunSync()
  Console.println(s"Fetch from file isbn $lamer: $resultFromFile")

  // Batching
  val neige = "978-2-07-034454-3"
  val desobeissance = "978-2-36054-551-3"
  val fetchTwoBooksConcurrently: (Book, Book) = getBookFromCSV(neige).product(getBookFromCSV(desobeissance)).unsafeRunSync()
  Console.println(s"Fetch concurrently from CSV isbn $neige, $desobeissance: $fetchTwoBooksConcurrently")

  private val list = List("978-2-07285-855-0", "999-3-53423-4")
  val fetchTwoBooksWithTraverse: List[Book] =
    list.traverse(getBookFromFile(_)).unsafeRunSync()
  Console.println(s"Fetch concurrently with traverse from CSV isbn ${list.mkString(",")}: $fetchTwoBooksWithTraverse")

  // Sequencing

  val fetchTwoBooksSequentially: (Book, Book) = getBookFromCSV(neige).flatMap(b1 => getBookFromFile(lamer).map(b2 => (b1, b2))).unsafeRunSync()
  Console.println(s"Fetch sequentially from CSV isbn $neige, $lamer: $fetchTwoBooksSequentially")

  // TODO Caching with Eval
}
