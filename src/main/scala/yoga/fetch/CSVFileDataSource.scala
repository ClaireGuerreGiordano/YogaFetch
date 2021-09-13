package yoga.fetch

import cats.effect.{Concurrent, IO}
import cats.implicits.toTraverseOps
import fetch.{Data, DataSource}
import yoga.fetch.model.Book

object CSVFileDataSource extends DataSource[IO, String, Book] {
  override def data: Data[String, Book] = new Data[String, Book] {
    override def name: String = "Book from csv"
  }

  override def CF: Concurrent[IO] = Concurrent[IO]

  override def fetch(isbn: String): IO[Option[Book]] = {
    val line = IO(io.Source.fromFile("books.csv").getLines.drop(1).find(_.split(",")(2) == isbn))
    line.flatMap(l => l.map(Book.from(_)).sequence)
  }
}

