package ru.ideaseeker.currency

import ru.ideaseeker.currency.download._
import ru.ideaseeker.currency.storage._

import java.time.{LocalDate, Month}
import org.scalatest.funsuite.AnyFunSuite

class MainTest extends AnyFunSuite {

    test("currency name to code") {
        val map = NameConverter.getMap
        assert(map("USD") == "R01235")
        assert(map("EUR") == "R01239")
        assert(map("KZT") == "R01335")
        assert(map("UAH") == "R01720")
        assert(map("JPY") == "R01820")
        assert(map("CNY") == "R01375")
    }

    val downloader = new CbrExcelDownloader
    val sheet = new CbrExcelSheet(
        downloader
            .setCurrency("USD")
            .setFromDate(LocalDate.of(2021, Month.MARCH, 2))
            .setToDate(LocalDate.of(2021, Month.APRIL, 10))
            .get
    )

    test("currency name") {
        assert(sheet.currencyName.contains("US Dollar"))
    }

    test("currency nominal") {
        assert((sheet.currencyNominal.get - 1).abs < 1e-4) // compare two doubles
    }

    test("currency values") {
        val actual = sheet.getValues.get
        val expected = List(
            74.0448, 74.5755, 73.5187, 73.7864, 74.4275, 74.264,
            74.0393, 73.4996, 73.5081, 73.2317, 72.9619, 73.1019,
            73.6582, 74.139, 74.6085, 75.3585, 76.1535, 76.1741,
            75.7576, 75.8287, 75.7023, 75.6373, 75.8073, 76.0734,
            76.6052, 76.3802, 77.773, 77.1011, 77.1657
        )
        assert(actual.length == expected.length)
        assert((actual zip expected).map { case (a, b) => a - b }.forall(_ < 1e-4))
    }

    test("currency min") {
        assert((sheet.getMin.get - 72.9619).abs < 1e-4)
    }

    test("currency max") {
        assert((sheet.getMax.get - 77.773).abs < 1e-4)
    }

    test("currency average") {
        assert((sheet.getAverage.get - 74.99596).abs < 1e-4)
    }

    val storage = new SheetStorage

    test("empty storage") {
        assert(storage.getSheets.isEmpty)
        assert(storage.getName("value").isEmpty)
        assert(storage.getDate("value").isEmpty)
        assert(storage.getNominal("value").isEmpty)
        assert(storage.show("value").isEmpty)
        assert(storage.getMin("value").isEmpty)
        assert(storage.getMax("value").isEmpty)
        assert(storage.getAverage("value").isEmpty)
    }

    test("operations with storage") {
        storage.load("first", "USD", "02.03.2021", "10.04.2021")
        storage.load("second", "USD", "02.03.2021", "10.04.2021")
        storage.load("third", "USD", "02.03.2021", "10.04.2021")
        assert(storage.getSheets.sorted == List("first", "second", "third"))

        storage.delete("second")
        assert(storage.getSheets.sorted == List("first", "third"))

        storage.deleteAll()
        assert(storage.getSheets == Nil)
    }

    test("storage wrapper") {
        storage.load("first", "USD", "02.03.2021", "10.04.2021")
        storage.load("second", "EUR", "03.03.2021", "09.04.2021")
        storage.load("third", "AUD", "04.03.2021", "08.04.2021")

        assert(storage.getName("second").contains("Euro"))

        val actual = storage.show("first").get
        val expected = List(
            74.0448, 74.5755, 73.5187, 73.7864, 74.4275, 74.264,
            74.0393, 73.4996, 73.5081, 73.2317, 72.9619, 73.1019,
            73.6582, 74.139, 74.6085, 75.3585, 76.1535, 76.1741,
            75.7576, 75.8287, 75.7023, 75.6373, 75.8073, 76.0734,
            76.6052, 76.3802, 77.773, 77.1011, 77.1657
        )
        assert(actual.length == expected.length)
        assert((actual zip expected).map { case (a, b) => a - b }.forall(_ < 1e-4))
    }
}