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
            case Array("show", sheetName) =>
                storage.show(sheetName) match {
                    case Some(list) => SomeData(list.mkString("[", ", ", "]"))
                    case None => EmptySheet()
                }
            case Array("min", sheetName) =>
                storage.getMin(sheetName) match {
                    case Some(value) => SomeData(s"min = $value")
                    case None => Error("Sheet is empty or doesn't exist")
                }
            case Array("max", sheetName) =>
                storage.getMax(sheetName) match {
                    case Some(value) => SomeData(s"max = $value")
                    case None => EmptySheet()
                }
            case Array("average", sheetName) =>
                storage.getAverage(sheetName) match {
                    case Some(value) => SomeData(s"min = $value")
                    case None => EmptySheet()
                }
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
          |  help
          |  exit
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
