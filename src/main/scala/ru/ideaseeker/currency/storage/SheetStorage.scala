package ru.ideaseeker.currency.storage

import ru.ideaseeker.currency.download.CbrExcelDownloader

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.collection.mutable

class SheetStorage {

    private val storage: mutable.Map[String, CbrExcelSheet] = mutable.Map.empty

    private val inputDatePattern: String = "dd.MM.yyyy"
    private val inputDateFormatter: DateTimeFormatter = {
        DateTimeFormatter.ofPattern(inputDatePattern)
    }
    private val downloader: CbrExcelDownloader = new CbrExcelDownloader

    def load(sheetName: String, currencyName: String, fromDate: String, toDate: String): Unit = {
        val sourceArray = downloader
            .setCurrency(currencyName)
            .setFromDate(LocalDate.parse(fromDate, inputDateFormatter))
            .setToDate(LocalDate.parse(toDate, inputDateFormatter))
            .get

        storage(sheetName) = new CbrExcelSheet(sourceArray)
    }

    def delete(sheetName: String): Unit = {
        storage.remove(sheetName)
    }

    def deleteAll(): Unit = {
        storage.clear()
    }

    def getSheets: List[String] = {
        storage.keys.toList
    }

    def show(sheetName: String): List[Double] = {
        storage.get(sheetName) match {
            case Some(sheet) => sheet.getValues
            case None => List.empty
        }
    }

    def getMin(sheetName: String): Option[Double] = {
        storage.get(sheetName).flatMap(_.getMin)
    }

    def getMax(sheetName: String): Option[Double] = {
        storage.get(sheetName).flatMap(_.getMax)
    }

    def getAverage(sheetName: String): Option[Double] = {
        storage.get(sheetName).flatMap(_.getAverage)
    }
}
