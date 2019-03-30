[![](https://jitpack.io/v/Namazed/TamTamBotApiClientDsl.svg)](https://jitpack.io/#Namazed/TamTamBotApiClientDsl)
# TamTamBotApiClientDsl

Kotlin DSL for TamTam Bot API. Using this library you can simplify working with the Bot API. 
What this library helps with:
* Subscribes to updates using LongPolling (WebHook will be later)
* Coordinates updates to specific areas of responsibility that you describe. All updates are processed in parallel.
* Handles commands
* Provides a convenient interface for working with requests to the Bot API

The bot API documentation is [here](https://dev.tamtam.chat).

## What you need to start

To access the library, you must add the dependency on ```jitpack``` and ```kotlinx```

### Gradle
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://dl.bintray.com/kotlin/kotlinx' }
		maven { url 'https://jitpack.io' }
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
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
You must also add a dependency.

### Gradle
```groovy
dependencies {
    implementation 'com.github.Namazed:TamTamBotApiClientDsl:0.1.0'
}
```
### Maven
```xml
<dependency>
	 <groupId>com.github.Namazed</groupId>
	 <artifactId>TamTamBotApiClientDsl</artifactId>
	 <version>0.1.0</version>
</dependency>
```

## Examples

The example of a finished bot can be found [here](https://github.com/Namazed/TamTamOrthoBot).

### Scopes
Below you find example of how start process longPolling:
```kotlin
fun main() {
  longPolling("BOT_TOKEN") {

  }
}
```
All other methods are called from longPolling scope (lambda).
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

    answerOnCallback(Payload("test_button_one")) { callbackState ->
        //some actions
    }

    answerOnCallback(Payload("test_button_two")) { callbackState ->
        //some actions
    }
}
```
Payload - this is an inline class, which contains payload of button. The update about click on button will fall into this lambda.

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
"Two bananas" sendFor UserId(commandState.command.message.sender.userId)
```
Now there is a need to wrap in inline class UserId or ChatId, but in the future these actions will not be needed.

#### With RequestsManager
```kotlin
requestsManager.sendText(UserId(commandState.command.message.sender.userId), "Two bananas")
```
To create an InlineKeyboard, you can use the keyboard DSL. Below is an example of creating InlineKeyboard in three ways using: ```operator unaryPlus```, via infix function ```add```, or simply call the function ```add```.
The ```buttonRow``` creates a row with buttons.

```kotlin
keyboard {
    +buttonRow {
        +Button(
            ButtonType.CALLBACK.value,
            "Create dreams",
            payload = "DREAMS"
        )
        +Button(
            ButtonType.CALLBACK.value,
            "Imagine that you are Dragon",
            payload = "DRAGON"
        )
    }

    this add buttonRow {
        this add Button(
            ButtonType.LINK.value,
            "Find new dreams"
        )
    }

    add(buttonRow {
        add(
            Button(
                ButtonType.LINK.value,
                "Find new dreams"
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
