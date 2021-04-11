package ru.ideaseeker.currency.download

import scala.xml.XML
import java.io.ByteArrayInputStream

object NameConverter {

    // example: USD -> R01235
    def getMap: Map[String, String] = {
        val xml = XML.load(new ByteArrayInputStream(CbrXmlDownloader.get))
        val currencyNames = (xml \ "Item" \ "ISO_Char_Code").map(_.text.trim)
        val currencyCodes = (xml \ "Item" \ "ParentCode").map(_.text.trim)
        (currencyNames zip currencyCodes).toMap
    }
}
