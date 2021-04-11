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

    def getName(sheetName: String): Option[String] = {
        storage.get(sheetName).flatMap(_.currencyName)
    }

    def getDate(sheetName: String): Option[(String, String)] = {
        storage.get(sheetName).flatMap(_.dateRange)
    }

    def getNominal(sheetName: String): Option[Double] = {
        storage.get(sheetName).flatMap(_.currencyNominal)
    }

    def show(sheetName: String): Option[List[Double]] = {
        storage.get(sheetName).flatMap(_.getValues)
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
