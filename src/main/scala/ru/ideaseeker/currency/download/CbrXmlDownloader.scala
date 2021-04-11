package ru.ideaseeker.currency.download

object CbrXmlDownloader extends FileDownloader {

    override def url: String = "https://www.cbr.ru/scripts/XML_valFull.asp"
}
