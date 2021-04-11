package ru.ideaseeker.currency.download

import scala.xml.XML
import java.io.ByteArrayInputStream

object NameConverter {

    private val downloader: CbrXmlDownloader = new CbrXmlDownloader

    // example: USD -> R01235
    def getMap: Map[String, String] = {
        val xml = XML.load(new ByteArrayInputStream(downloader.get))
        val currencyNames = (xml \ "Item" \ "ISO_Char_Code").map(_.text.strip)
        val currencyCodes = (xml \ "Item" \ "ParentCode").map(_.text.strip)
        (currencyNames zip currencyCodes).toMap
    }
}
