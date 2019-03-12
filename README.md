# TamTamBotApiClientDsl

Kotlin DSL для TamTam Bot API. С помощью этой библиотеки можно упростить работу с Bot API.
С чем помогает эта библиотека:
* Подписывается на updates с помощью LongPolling (WebHook будет позже)
* Распределяет updates по определенным зонам ответственности, которые вы опишите. Все updates обрабатываются параллельно.
* Парсит команды
* Предоставляет удобный интерфейс для работы с запросами к Bot API

## Что необходимо, что бы начать


## Примеры

### Scopes
Ниже пример того, как начать процесс longPolling:
```kotlin
fun main() {
  longPolling("BOT_TOKEN") {

  }
}
```
Все остальные методы вызываются из longPolling scope (lambda).
Для того что бы подписаться на запуск бота, достаточно вызвать метод:
```kotlin
onStartBot { startedBotState ->
    //some actions
}
```
Если вы хотите подписаться на какую то определенную команду (например, /actions):
```kotlin
commands {

    onCommand("/actions") { commandState ->
        //some actions
    }

    onUnknownCommand { commandState ->
        //some actions
    }

}
```
Для работы с Callbacks (т.е. когда пользователь нажал на кнопку в клавиатуре, которую ваш бот ему отправил), 
необходимо создать scope callbacks:
```kotlin
callbacks {

    defaultAnswer { callbackState ->
        //some actions
    }

    answerOnCallback(Payload("test_button_one")) { callbackState ->
        //some actions
    }

    answerOnCallback(Payload("test_button_two")) { callbackState ->
        //some actions
    }
}
```
Payload - это inline class, который содержит в себе payload кнопки, update о нажатии на которую будет попадать в эту lambda.

И последний из основных scopes это scope обработки новых сообщений от пользователя (которые не содержат команду):
```kotlin
messages {

    answerOnMessage { messageState ->
        //some actions
    }

}
```
Также есть такие методы как: 
* ```onAddBotToChat```
* ```onRemoveBotFromChat``` 
* Users scope, в котором есть необходимые методы для обработки updates
связанными с добавлением пользователя в чат или удалением его

Во всех lambdas, которые были выше указаны есть параметр State, который содержит всю необходимую информацию о текущем update.
Например CommandState содержит timestamp, когда событие это произошло (т.е. пользователь прислал вам команду) и класс Command.

### Requests
Запросы можно слать в большинстве случаев с помощью удобного DSL, либо напрямую вызывая методы у класса ```RequestsManager```, 
который доступен во всех Scopes.
Например, если вам необходимо послать какой то текст пользователю, достаточно вызвать метод ```sendFor``` или ```sendText```:

Через DSL
```kotlin
"Two bananas" sendFor UserId(commandState.command.message.sender.userId)
```
Пока есть необходимость оборачивать в inline class UserId или ChatId, в будущем эти действия будут не нужны.
Через RequestsManager
```kotlin
requestsManager.sendText(UserId(commandState.command.message.sender.userId), "Two bananas")
```
