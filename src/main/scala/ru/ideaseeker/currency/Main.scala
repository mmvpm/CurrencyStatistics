package ru.ideaseeker.currency

object Main {

    def main(args: Array[String]): Unit = {
        println("CurrencyStatistics!")

        Controller.load("a", "03.04.2021", "10.04.2021", "USD")

    }
}
