package ru.ideaseeker.currency

import ru.ideaseeker.currency.storage.SheetStorage
import ru.ideaseeker.currency.download.CbrExcelDownloader

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Controller {

    private val storage: SheetStorage = new SheetStorage

    private val inputDatePattern: String = "dd.MM.yyyy"
    private val inputDateFormatter: DateTimeFormatter = {
        DateTimeFormatter.ofPattern(inputDatePattern)
    }
    private val downloader: CbrExcelDownloader = new CbrExcelDownloader

    def load(sheetName: String, fromDate: String, toDate: String, currencyName: String): Unit = {
        val sourceArray = downloader
            .setFromDate(LocalDate.parse(fromDate, inputDateFormatter))
            .setToDate(LocalDate.parse(toDate, inputDateFormatter))
            .setCurrency(currencyName)
            .get

        storage.loadSheet(sheetName, sourceArray)
    }
}
