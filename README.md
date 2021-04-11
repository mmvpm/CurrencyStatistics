# CurrencyStatistics

[![Build Status](https://github.com/IdeaSeeker/CurrencyStatistics/workflows/CI/badge.svg)](https://github.com/IdeaSeeker/CurrencyStatistics/actions)

Консольное приложение, позволяющее собирать простейщую статистику о курсах валют на основании данных [Центрального Банка РФ](https://cbr.ru/)

## Запуск

- Команда `run` в `sbt shell` или вызов функции [`main`](src/main/scala/ru/ideaseeker/currency/Main.scala#L5)
- Получить `.jar` файл с приложением можно командой `assembly`

## Протокол взаимодействия

- `load <sheetName> <currencyName> <fromDate> <toDate>` - загрузить данные о валюте `currencyName` в указанный промежуток времени в память в таблицу с именем `sheetName`. Коды валют следует вводить по ISO 4217, а дату в формате `dd.MM.yyyy`
- `delete <sheetName>` - удалить данные, хранящиеся в таблице `<sheetName>`
- `delete all` - удалить все загруженные данные
- `sheets` - показать список загруженных таблиц
- `name <sheetName>` - вывести название валюты, данные о которой записаны в таблицу
- `date <sheetName>` - вывести промежуток времени, который покрывают данные в таблице
- `nominal <sheetName>` - вывести номинал, то есть количество денежных единиц валюты, равных значению курса в рублях
- `show <sheetName>` - показать все значения курса валюты из указанной таблицы
- `min <sheetName>` - найти минимальное значение курса в таблице
- `max <sheetName>` - найти максимальное значение курса в таблице
- `average <sheetName>` - найти среднее значение курса в таблице
- `exit` - выйти из приложения

## Реализация

- Получение данных http-запросами - [`scalaj`](https://github.com/scalaj/scalaj-http)
- Чтение `.xlsx` таблиц - [`apache.poi`](https://github.com/apache/poi)
- Обработка данных - [`scala-parallel-collections`](https://github.com/scala/scala-parallel-collections)
- Парсинг `.xml` файла с информацией о валютах - [`scala-xml`](https://github.com/scala/scala-xml)
- Тестирование логики - [`scalatest`](https://github.com/scalatest/scalatest)

## GitHub Actions

При каждом push/pull request запускаются тесты, минимальная проверка кода ([`scalafix`](https://github.com/scalacenter/scalafix)) и проверка установленных версий библиотек ([`sbt-updates`](https://github.com/rtimush/sbt-updates))
