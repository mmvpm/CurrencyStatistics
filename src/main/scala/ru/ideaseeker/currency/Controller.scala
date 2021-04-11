package ru.ideaseeker.currency

import ru.ideaseeker.currency.storage.SheetStorage

import scala.util.Failure
import scala.io.StdIn.readLine

object Controller {

    private val storage: SheetStorage = new SheetStorage

    sealed trait ControllerResponse {
        val message: String
    }

    case class Success(message: String = "Success") extends ControllerResponse
    case class Default(message: String = "Invalid query") extends ControllerResponse
    case class EmptySheet(message: String = "Sheet is empty or doesn't exist") extends ControllerResponse
    case class Error(message: String = "Unknown error") extends ControllerResponse
    case class Exit(message: String = "") extends ControllerResponse
    case class SomeData(message: String) extends ControllerResponse

    def fromOption(option: Option[String]): ControllerResponse = {
        option match {
            case Some(value) => SomeData(value)
            case None => EmptySheet()
        }
    }

    private def readQuery(): ControllerResponse = {
        readLine.split(' ') match {
            case Array("load", sheetName, currencyName, fromDate, toDate) =>
                storage.load(sheetName, currencyName, fromDate, toDate) match {
                    case Failure(error) => Error(error.toString)
                    case util.Success(_) => Success()
                }
            case Array("delete", sheetName) =>
                storage.delete(sheetName)
                Success()
            case Array("delete", "all") =>
                storage.deleteAll()
                Success()
            case Array("sheets") =>
                SomeData(storage.getSheets.mkString("[", ", ", "]"))
            case Array("exit") =>
                Exit()
            case Array("name", sheetName) =>
                fromOption(storage.getName(sheetName).map { x => s"name = $x" })
            case Array("date", sheetName) =>
                fromOption(storage.getDate(sheetName).map { x => s"date range = $x" })
            case Array("nominal", sheetName) =>
                fromOption(storage.getNominal(sheetName).map { x => s"nominal = $x"})
            case Array("show", sheetName) =>
                fromOption(storage.show(sheetName).map { x => x.mkString("[", ", ", "]") })
            case Array("min", sheetName) =>
                fromOption(storage.getMin(sheetName).map { x => s"min = $x" })
            case Array("max", sheetName) =>
                fromOption(storage.getMax(sheetName).map { x => s"max = $x" })
            case Array("average", sheetName) =>
                fromOption(storage.getAverage(sheetName).map { x => s"min = $x" })
            case _ =>
                Default()
        }
    }

    private val helpMessage: String =
        """Commands:
          |  load <sheetName> <currencyName> <dd.MM.yyyy> <dd.MM.yyyy>
          |  delete <sheetName>
          |  delete all
          |  sheets
          |  exit
          |  name <sheetName>
          |  date <sheetName>
          |  nominal <sheetName>
          |  show <sheetName>
          |  min <sheetName>
          |  max <sheetName>
          |  average <sheetName>
          |""".stripMargin

    def launch(): Unit = {
        println(helpMessage)
        while (true) {
            Controller.readQuery() match {
                case Exit(_) => return
                case response => println(response.message)
            }
        }
    }
}
