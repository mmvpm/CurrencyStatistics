package ru.ideaseeker.currency.storage

import scala.collection.mutable

class SheetStorage {

    private val storage: mutable.Map[String, CbrExcelSheet] = mutable.Map.empty

    def loadSheet(name: String, sourceArray: Array[Byte]): Unit = {
        storage(name) = new CbrExcelSheet(sourceArray)
    }
}
