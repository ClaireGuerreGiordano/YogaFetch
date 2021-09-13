package yoga.fetch

import cats.effect.{Concurrent, IO}
import fetch.{Data, DataSource}
import yoga.fetch.model.Book

import scala.util.matching.Regex

object FileDataSource extends DataSource[IO, String, Book]{

  def pattern: Regex = "([\\D]+)\\swas\\swritten\\sby\\s([\\D]+)\\sISBN\\sis\\s([0-9-]+)".r

  override def data: Data[String, Book] = new Data[String, Book] {
    override def name: String = "Book from file"
  }

  override def CF: Concurrent[IO] = Concurrent[IO]

  override def fetch(id: String): IO[Option[Book]] = for {
    lines <- IO(List.from(io.Source.fromFile("books2.txt").getLines))
    triplets = lines.map(l => pattern.unapplySeq(l))
      .filter(l => l.isDefined && l.get.length == 3).map(_.get)
  } yield triplets.map(t => Book(t(0), t(1), t(2))).find(_.isbn == id)
}
