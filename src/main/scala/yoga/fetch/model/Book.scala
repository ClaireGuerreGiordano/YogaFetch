package yoga.fetch.model

import cats.effect.IO

case class Book(name: String, author: String, isbn: String)

object Book {
  def from(csvLine: String): IO[Book] = {
    val columns = csvLine.split(",")
    if (columns.length != 3) IO.raiseError(InvalidLineException(csvLine))
    else IO.pure(Book(columns(0), columns(1), columns(2)))
  }
}

case class InvalidLineException(csvLine: String) extends Throwable {
  override def getMessage: String = s"Invalid line $csvLine"
}
