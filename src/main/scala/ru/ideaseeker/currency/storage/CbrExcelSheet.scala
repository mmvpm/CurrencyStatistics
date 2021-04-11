package ru.ideaseeker.currency.storage

import java.io.ByteArrayInputStream
import scala.util.{Failure, Success, Try}
import scala.collection.parallel.mutable.ParArray
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}

class CbrExcelSheet(sourceArray: Array[Byte]) {

    private val sheet: XSSFSheet = new XSSFWorkbook(
        new ByteArrayInputStream(sourceArray)
    ).getSheetAt(0)

    /* expected table structure:
    *
    * | nominal | data      | curs    | cdx       |
    * |---------|-----------|---------|-----------|
    * | 1       | 4/10/2021 | 77.1657 | US Dollar |
    * | 1       | 4/9/2021  | 77.1011 | US Dollar |
    * ...
    */
    private val index = Map(
        "nominal" -> 0,
        "date"    -> 1,
        "value"   -> 2,
        "name"    -> 3,
    )

    private val height: Int = sheet.getLastRowNum + 1

    private val values: ParArray[Double] = ParArray.fromIterables(
        (1 until height).map { i =>
            getCell(i, index("value")) match {
                case Failure(error) => throw error
                case Success(value) => value.toDouble
            }
        }.reverse.toArray
    )

    // get cell from excel sheet
    private def getCell(i: Int, j: Int): Try[String] = {
        Try(sheet.getRow(i).getCell(j).toString)
    }


    // None, if values.isEmpty
    def currencyName: Option[String] = {
        getCell(1, index("name")).toOption
    }

    def dateRange: Option[(String, String)] = {
        (for {
            firstDate <- getCell(height - 1, index("date"))
            lastDate <- getCell(1, index("date"))
        } yield (firstDate, lastDate)).toOption
    }

    def currencyNominal: Option[Double] = {
        getCell(1, index("nominal")).map(_.toDouble).toOption
    }

    def getValues: Option[List[Double]] = {
        values.toList match {
            case Nil  => None
            case list => Some(list)
        }
    }

    def getMin: Option[Double] = {
        Try(values.min).toOption
    }

    def getMax: Option[Double] = {
        Try(values.max).toOption
    }

    def getAverage: Option[Double] = {
        Try(values.sum / values.length).toOption
    }
}
