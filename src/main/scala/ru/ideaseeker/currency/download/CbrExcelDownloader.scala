package ru.ideaseeker.currency.download

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CbrExcelDownloader extends FileDownloader {

    private var currencyCode: String = "R01239"

    def setCurrency(currencyName: String): CbrExcelDownloader = {
        // example: USD -> R01235
        val nameToCode = NameConverter.getMap
        currencyCode = nameToCode(currencyName)
        this // for chaining
    }

    // default values
    private var fromDate: String = "01%2F01%2F2021"
    private var   toDate: String = "01%2F01%2F2021"

    private val cbrFormatter: DateTimeFormatter = {
        // date pattern in an http request to the CBR
        DateTimeFormatter.ofPattern("MM%2'F'dd%2'F'yyyy")
    }

    def setFromDate(date: LocalDate): CbrExcelDownloader = {
        fromDate = date.format(cbrFormatter)
        this // for chaining
    }

    def setToDate(date: LocalDate): CbrExcelDownloader = {
        toDate = date.format(cbrFormatter)
        this // for chaining
    }

    override def url: String =
        "https://cbr.ru/Queries/UniDbQuery/DownloadExcel/99021" +
            "?Posted=True&so=1&mode=2&" +
            s"&VAL_NM_RQ=$currencyCode" +
            s"&FromDate=$fromDate" +
            s"&ToDate=$toDate"
}
