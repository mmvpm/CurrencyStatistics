package ru.ideaseeker.currency.storage

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.io.ByteArrayInputStream
import scala.collection.parallel.mutable.ParArray
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}


class CbrExcelSheet(val sourceArray: Array[Byte]) {

    private val sheet: XSSFSheet = new XSSFWorkbook(
        new ByteArrayInputStream(sourceArray)
    ).getSheetAt(0)

    // expected table structure
    private val index = Map(
        "nominal" -> 0,
        "date"    -> 1,
        "value"   -> 2,
        "name"    -> 3,
    )

    private val height: Int = sheet.getLastRowNum + 1

    private val values: ParArray[Double] = ParArray.fromIterables(
        (1 until height).map { i =>
            getOrDefault(i, index("value")).toDouble
        }.reverse.toArray
    )

    private def getOrDefault(i: Int, j: Int, default: String = "-1"): String = {
        if (i < height && j < sheet.getRow(i).getLastCellNum) {
            sheet.getRow(i).getCell(j).toString
        } else {
            default
        }
    }


    def isEmpty: Boolean = {
        height < 2
    }

    def currencyNominal: Double = {
        getOrDefault(1, index("nominal")).toDouble
    }

    def currencyName: String = {
        getOrDefault(1, index("name"), "empty")
    }

    def dateRange: (String, String) = {
        val firstDate = getOrDefault(1, index("date"))
        val lastDate = getOrDefault(height - 1, index("date"))
        (firstDate, lastDate)
    }

    def getMin: Option[Double] = {
        if (isEmpty) {
            return None
        }
        Some(values.min)
    }

    def getMax: Option[Double] = {
        if (isEmpty) {
            return None
        }
        Some(values.max)
    }

    def getAverage: Option[Double] = {
        if (isEmpty) {
            return None
        }
        Some(values.sum / values.length)
    }
}
