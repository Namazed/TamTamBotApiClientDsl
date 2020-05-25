[![GitHub license](https://img.shields.io/github/license/Namazed/TamTamBotApiClientDsl)](https://github.com/Namazed/TamTamBotApiClientDsl/blob/master/LICENSE)
[ ![Download](https://api.bintray.com/packages/namazed/tamtam_bot_dsl_client/botsdk/images/download.svg)](https://bintray.com/namazed/tamtam_bot_dsl_client/botsdk/link)
# TamTamBotApiClientDsl

Kotlin DSL for TamTam Bot API. Using this library you can simplify working with the Bot API. 
What this library helps with:
* Subscribes to updates using LongPolling or WebHook
* Coordinates updates to specific areas of responsibility that you describe. All updates are processed in parallel.
* Handles commands
* Provides a convenient interface for working with requests to the Bot API

The bot API documentation is [here](https://dev.tamtam.chat).

## What you need to start

You should build your project with JDK 8.

To access the library, you must add the dependency on ```kotlinx```

### Gradle
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://dl.bintray.com/kotlin/kotlinx' }
	}
}
```
### Maven
```xml
<repositories>
	<repository>
	    <id>kotlinx</id>
	    <url>https://dl.bintray.com/kotlin/kotlinx</url>
	</repository>
</repositories>
```
You must also add a dependency.

### Gradle
```groovy
dependencies {
    implementation "com.namazed.botsdk:library:0.4.0"
}
```
### Maven
```xml
<dependency>
	 <groupId>com.namazed.botsdk</groupId>
	 <artifactId>library</artifactId>
	 <version>0.4.0</version>
	<type>pom</type>
</dependency>
```

## Examples

The example of a finished bot can be found [here](https://github.com/Namazed/TamTamOrthoBot).

### Scopes
Below you find example of how start process sync longPolling on main thread:
```kotlin
fun main() {
  longPolling(LongPollingStartingParams("BOT_TOKEN")) {

  }
}
```
Async longPolling on other single thread:
```kotlin
fun main() {
  longPolling(LongPollingStartingParams("BOT_TOKEN", async = true)) {

  }
}
```
If you want disable parallel work with update:
```kotlin
fun main() {
  longPolling(LongPollingStartingParams("BOT_TOKEN", parallelWorkWithUpdates = false)) {

  }
}
```
If you want disable http logs:
```kotlin
fun main() {
  longPolling(LongPollingStartingParams("BOT_TOKEN", httpLogsEnabled = false)) {

  }
}
```
If you want use webhook:
```kotlin
val coordinator: Coordinator = webhook(WebhookStartingParams("BOT_TOKEN", subscription = Subscription(url = "https://your_url"))) {

}
// then you can call coordinator where you want and pass your json like String
coordinator.coordinateAsync(yourJson)
```
All other methods are called from longPolling scope or webhook scope (lambda).
In order to subscribe to the launch of the bot, just call the method:
```kotlin
onStartBot { startedBotState ->
    //some actions
}
```
If you want subscribe on specific ***command*** (for example, /actions):
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
To work with ***Callbacks*** (i.e. when the user pushed a button on the keyboard that your bot sent him),
need to create scope callbacks:
```kotlin
callbacks {

    defaultAnswer { callbackState ->
        //some actions
    }

    answerOnCallback("test_button_one") { callbackState ->
        //some actions
    }

    answerOnCallback("test_button_two") { callbackState ->
        //some actions
    }
}
```
The update about click on button will fall into this lambda.

And last from main scopes is the scope of processing new messages from the user (which do not contain the command):
```kotlin
messages {

    answerOnMessage { messageState ->
        //some actions
    }

}
```
Also have this methods: 
* ```onAddBotToChat```
* ```onRemoveBotFromChat``` 
* Users scope, in which there are necessary methods for processing updates
associated with adding a user to the chat or deleting it

In all the lambdas that were mentioned above, there is a [State](https://github.com/Namazed/TamTamBotApiClientDsl/blob/master/library/src/main/kotlin/chat/tamtam/botsdk/state/UpdateState.kt) parameter that contains all the necessary information about the current update.
For example, a CommandState contains a timestamp when an event occurred (that is, the user sent you a command) and the [Command](https://github.com/Namazed/TamTamBotApiClientDsl/blob/master/library/src/main/kotlin/chat/tamtam/botsdk/model/Command.kt) class.

### Requests
In most cases, requests can be sent using a convenient DSL, or by directly calling methods on the class [RequestsManager](https://github.com/Namazed/TamTamBotApiClientDsl/blob/master/library/src/main/kotlin/chat/tamtam/botsdk/client/RequestsManager.kt), which is available in all Scopes.
For example, if you need to send some text to the user, simply call the ```sendFor``` or ```sendText```:

#### With DSL
```kotlin
"Two bananas" sendFor commandState.command.message.sender.userId
```

#### With RequestsManager
```kotlin
requestsManager.sendText(commandState.command.message.sender.userId, "Two bananas")
```
To create an InlineKeyboard, you can use the keyboard DSL. Below is an example of creating InlineKeyboard in three ways using: ```operator unaryPlus```, via infix function ```add```, or simply call the function ```add```.
The ```buttonRow``` creates a row with buttons.

```kotlin
keyboard {
    +buttonRow {
        +Button(
            ButtonType.CALLBACK,
            "Create dreams",
            payload = "DREAMS"
        )
        +Button(
            ButtonType.CALLBACK,
            "Imagine that you are Dragon",
            payload = "DRAGON"
        )
    }

    this add buttonRow {
        this add Button(
            ButtonType.LINK,
            "Find new dreams",
	    url = "dreams.com"
        )
    }

    add(buttonRow {
        add(
            Button(
                ButtonType.LINK,
                "Find new dreams",
		url = "dreams.com"
            )
        )
    })
}
```
## Dependencies
- [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Retrofit](https://square.github.io/retrofit/)
- [Logback](https://logback.qos.ch/)
- [JUnit 5](https://junit.org/junit5/)
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)

## Contributing

Pull requests are welcome. If you find any bugs or have a great idea, please create an issue for this.

## License
This library uses a license [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).
