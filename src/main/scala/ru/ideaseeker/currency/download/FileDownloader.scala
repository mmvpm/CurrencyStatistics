package ru.ideaseeker.currency.download

trait FileDownloader {

    def url: String

    def get: Array[Byte] = {
        scalaj.http.Http(url).asBytes.body
    }
}
