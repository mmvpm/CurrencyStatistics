package ru.ideaseeker.currency.storage

import ru.ideaseeker.currency.download.CbrExcelDownloader

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Try
import scala.collection.mutable

class SheetStorage {

    private val storage: mutable.Map[String, CbrExcelSheet] = mutable.Map.empty

    private val inputDatePattern: String = "dd.MM.yyyy"
    private val inputDateFormatter: DateTimeFormatter = {
        DateTimeFormatter.ofPattern(inputDatePattern)
    }
    private val downloader: CbrExcelDownloader = new CbrExcelDownloader

    def load(sheetName: String, currencyName: String, fromDate: String, toDate: String): Try[Unit] = {
        Try {
            val sourceArray = downloader
                .setCurrency(currencyName)
                .setFromDate(LocalDate.parse(fromDate, inputDateFormatter))
                .setToDate(LocalDate.parse(toDate, inputDateFormatter))
                .get
            storage(sheetName) = new CbrExcelSheet(sourceArray)
        }
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

    // wrapper for one sheet
    def access[T](function: CbrExcelSheet => Option[T])(sheetName: String): Option[T] = {
        storage.get(sheetName).flatMap(function)
    }

    def getName: String => Option[String] = access(_.currencyName)
    def getDate: String => Option[(String, String)] = access(_.dateRange)
    def getNominal: String => Option[Double] = access(_.currencyNominal)
    def show: String => Option[List[Double]] = access(_.getValues)
    def getMin: String => Option[Double] = access(_.getMin)
    def getMax: String => Option[Double] = access(_.getMax)
    def getAverage: String => Option[Double] = access(_.getAverage)
}
