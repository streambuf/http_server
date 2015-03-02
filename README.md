HTTP веб-сервер
=========================
Запуск и настройка
---------------------------------
1. git clone https://github.com/streambuf/http_server.git
2. cd http_server
3. make
4. java -jar httpd.jar

Параметры
-----------------------
- **-c** количество потоков обслуживающих очередь запросов
- **-r** путь к DOCUMENT_ROOT
- **-debug** [true, false] включает или выключает вывод отладочных логов в консоль
- **-port** устанавливает порт сервера

**Например:** java -jar httpd.jar **-c** 4 **-r** /var/www/ **-debug** true **-port** 8080

Функционал
---------------------------------
- Отвечает на GET-запросы и HEAD-запросы
- Возвращает index.html как индекс директории
- Возвращает файлы по произвольному пути в DOCUMENT_ROOT
- Отвечает следующими заголовками для успешных GET-запросов:
  - Date
  - Server
  - Content-Length
  - Content-Type
  - Connection
- Корректный Content-Type для: .html, .css, js, jpg, .jpeg, .png, .gif, .swf
- Понимает пробелы и %XX в именах файлов
- Стабильно работает
- Защита от доступа к файлам вне DOCUMENT_ROOT
- Корректно передаёт файлы 2G+

Производительность
--------------------------------
ab -n 50000 -c 100 -r http://localhost:8080/wikipedia_russia.html
- nginx: 1000 requests per second
- http_server: 550 requests per second

