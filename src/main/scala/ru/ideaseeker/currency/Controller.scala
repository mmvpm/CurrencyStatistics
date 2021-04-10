package ru.ideaseeker.currency

import ru.ideaseeker.currency.storage.SheetStorage

import scala.io.StdIn.readLine

object Controller {

    private val storage: SheetStorage = new SheetStorage

    def readQuery(): Unit = {
        val successResponse = Some("Success")
        val defaultResponse = Some("I don't understand the query")
        val emptyTableResponse = Some("Sheet is empty! Maybe you have a mistake in query")

        val command = readLine.split(' ')

        if (command.length < 1) {
            println(defaultResponse.get)
        }

        val result = command(0) match {
            // load <sheetName> <currencyName> <dd.MM.yyyy> <dd.MM.yyyy>
            case "load" =>
                if (command.length < 5) {
                    defaultResponse
                } else {
                    storage.load(command(1), command(2), command(3), command(4))
                    successResponse
                }
            // delete <sheetName>/all
            case "delete" =>
                if (command.length < 2) {
                    defaultResponse
                } else {
                    command(1) match {
                        case "all" => storage.deleteAll()
                        case name => storage.delete(name)
                    }
                    successResponse
                }
            // get downloaded sheets
            case "sheets" =>
                Some(storage.getSheets.mkString(", "))
            // exit from program
            case "exit" =>
                None
            // show <sheetName>
            case "show" =>
                if (command.length < 2) {
                    defaultResponse
                } else {
                    Some(storage.show(command(1)).mkString(", "))
                }
            // min <sheetName>
            case "min" =>
                if (command.length < 2) {
                    defaultResponse
                } else {
                    storage.getMin(command(1)) match {
                        case Some(value) => Some(s"min = $value")
                        case None => emptyTableResponse
                    }
                }
            // max <sheetName>
            case "max" =>
                if (command.length < 2) {
                    defaultResponse
                } else {
                    storage.getMax(command(1)) match {
                        case Some(value) => Some(s"max = $value")
                        case None => emptyTableResponse
                    }
                }
            // average <sheetName>
            case "average" =>
                if (command.length < 2) {
                    defaultResponse
                } else {
                    storage.getAverage(command(1)) match {
                        case Some(value) => Some(s"average = $value")
                        case None => emptyTableResponse
                    }
                }
            // otherwise
            case _ =>
                defaultResponse
        }

        result.foreach { response =>
            println(response)
            Controller.readQuery()
        }
    }
}
