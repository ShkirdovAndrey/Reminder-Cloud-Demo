# Reminder-Cloud-Demo

## О тестируемом продукте:
Reminder - приложение призванное высылать на почту Jira-юзера напоминания о каких-то конкретных Jira-задачах. 
Основная работа пользователя в приложении происходит при помощи задания данных в форме - создании напоминания (далее: "рем"):
- это адресаты напоминания,
- дата первой/единственной отправки,
- сообщение(придет на почту, иначе - дефолтное);
- частота исполнения(повторяемость)
- дата окончания повторения (можно не задавать)

после задания данных, форма сохраняется,а в блоке в Jira-задаче появляется строка, содержащая вышезаданные данные. По сути - объект рема.
Объекты рема можно создавать также не заполняя форму, а нажимая в самой задаче кнопки "дефолтных" ремов, которые задают некоторые параметры автоматически.

## Структура проекта
### Основные Технологии:
Selenide, TestNg, Lombok. Запускается через Maven.

### Тестируемые сценарии:
Расположены в файле **IssuePageTest**. Я допускаю, что они не смогут быть запущенными либо будут провалены на чужой машине либо в виду иных причин
Комментарий:контекстно - приведенные сценарии работают либо из формы, заполняя в ней данные при помощи методов, расположенных в соседних страницах, либо проверяют интерфейсы, либо негативные сценарии вызывая ошибки в UI интерфейсе. 
### Реализация паттерна Page Object
- **ReminderCreatingForm** - в файле расположены методы направленные на взаимодействие с формой; это основной рабочий фаил. Содержит в том числе локаторы, в том числе программируемые(24-99 строки), переменные-хранилища заданной информации (102-132;), конструкторы для задания данных в неизменённой форме и ассерты для неё (135-218), методы работы со списками и кнопками формы (220-386)
- **IssuePage** - в этом файле описано взаимодействие с интерейсом плагина доступным из Jira-задачи - локаторы и кнопки открытия кастомизируемой формы(58-62), кнопки создания "дефолтных" ремов(36-51), чтение и взаимодействие с созданными в рамках задачи ремами(54-56,108-110, etc), а также ассерты(64-102)

Я не исключал TODO-комментарии из репозитория, прошу не обращать внимания.
Все прочие Page и другие файлы(расположенные в блоке "test" проекта) исполняют роль исключительно инфраструктуры, без которой запуск тестов был бы невозможен.

### Реализация POJO и утилити:
Далее речь идет о файлах, расположенных в main/java/..
- Папка **utils** содержит классы описывающие кастомизируемые ожидания элементов, property-reader для чтения проектом конфигов,и генератор дат - для задания их при создании ремов (необходим в виду валидаций приложения)
- Папка **enums** содержит различные используемые enum-классы. Кроме GroupAndUserNames, расположенный в entiites, но это костыльная реализация за неимением лучшей.
- Папка **entities**: в основном тут располагаются POJO, с парой исключений - например CreatedReminderInIssueList нужен преимущественно, чтобы хранить локаторы созданного рема, по которым, единственно, можно однозначно определять созданное напоминание в интерфейсе плагина.