package ru.ideaseeker.currency.download

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CbrXlsxDownloader extends FileDownloader {

    // default values
    private var fromDate: String = "01%2F01%2F2021"
    private var   toDate: String = "01%2F01%2F2021"

    private val cbrFormatter: DateTimeFormatter = {
        DateTimeFormatter.ofPattern("MM%2'F'dd%2'F'yyyy")
    }

    def setFromDate(date: LocalDate): CbrXlsxDownloader = {
        fromDate = date.format(cbrFormatter)
        this
    }

    def setToDate(date: LocalDate): CbrXlsxDownloader = {
        toDate = date.format(cbrFormatter)
        this
    }

    private var currencyCode: String = "R01239"

    def setCurrency(name: String): CbrXlsxDownloader = {
        val nameToCode = Map(
            "USD" -> "R01235",
            "EUR" -> "R01239",
        )
        nameToCode.get(name) match {
            case Some(value) => currencyCode = value
        }
        this
    }

    override def url: String =
        "https://cbr.ru/Queries/UniDbQuery/DownloadExcel/98956?Posted=True" +
            s"&VAL_NM_RQ=$currencyCode" +
            s"&FromDate=$fromDate" +
            s"&ToDate=$toDate"
}
