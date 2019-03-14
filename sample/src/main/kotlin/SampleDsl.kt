
import chat.tamtam.botsdk.client.ResultRequest
import chat.tamtam.botsdk.communications.longPolling
import chat.tamtam.botsdk.keyboard.keyboard
import chat.tamtam.botsdk.model.Button
import chat.tamtam.botsdk.model.ButtonType
import chat.tamtam.botsdk.model.CallbackId
import chat.tamtam.botsdk.model.ChatId
import chat.tamtam.botsdk.model.Payload
import chat.tamtam.botsdk.model.UserId
import chat.tamtam.botsdk.model.request.AnswerParams
import chat.tamtam.botsdk.model.request.InlineKeyboard
import chat.tamtam.botsdk.model.request.UploadParams
import chat.tamtam.botsdk.model.request.UploadType
import chat.tamtam.botsdk.scopes.CommandsScope
import chat.tamtam.botsdk.state.CommandState
import chat.tamtam.botsdk.model.request.SendMessage as RequestSendMessage
import chat.tamtam.botsdk.model.response.SendMessage as ResponseSendMessage

fun main() {

    longPolling("BOT_TOKEN") {

        // when User start your bot, code below will start
        onStartBot {

        }

        // when something added your bot to Chat, code below will start
        onAddBotToChat {

        }

        // when something removed your bot from Chat, code below will start
        onRemoveBotFromChat {

        }

        commands {

            onCommand("/dinner") {
                val inlineKeyboard = createInlineKeyboard()
                // send text for user
                "Your dinner is two bananas" sendFor UserId(it.command.message.sender.userId)

                // first prepare text and userId then send for user prepared text with InlineKeyboard or other Attach
                "Choose you dinner" prepareFor UserId(it.command.message.sender.userId) sendWith inlineKeyboard

                // You can create extension function if you don't want to leave code here, but you need know,
                // that all extension functions for Scopes, need be 'suspend'.
                sendTextWithKeyboard(it, inlineKeyboard)
            }

            onUnknownCommand {
                """I'm sorry, but I don't know this command, you can try write /help
                    |if you don't remember all my available command.""".trimMargin() sendFor UserId(it.command.message.sender.userId)
            }

        }

        callbacks {

            defaultAnswer {
                val resultAnswer = "It's default answer" replaceCurrentMessage CallbackId(it.callback.callbackId)

                when (resultAnswer) {
                    is ResultRequest.Success -> resultAnswer.response
                    is ResultRequest.Failure -> resultAnswer.exception
                }
            }

            // when user click on button with payload "HELLO", code below will start
            answerOnCallback(Payload("HELLO")) {
                val inlineKeyboard = createInlineKeyboard()
                "Hello" prepareReplacementCurrentMessage
                        AnswerParams(CallbackId(it.callback.callbackId), UserId(it.callback.user.userId)) answerWith inlineKeyboard
            }

            // when user click on button with payload "GOODBYE", code below will start
            answerOnCallback(Payload("GOODBYE")) {

                // send message with upload Photo which replace old message
                "Goodbye" prepareReplacementCurrentMessage
                        AnswerParams(CallbackId(it.callback.callbackId), UserId(it.callback.user.userId)) answerWith
                        UploadParams("local_file_path", UploadType.PHOTO)

                // send message which replace old message
                "Goodbye" answerFor CallbackId(it.callback.callbackId)

                // send notification (as Toast) for User
                "Goodbye" answerNotification AnswerParams(CallbackId(it.callback.callbackId), UserId(it.callback.user.userId))
            }
        }

        messages {

            // if current update is message, but not contains command, code below will start
            answerOnMessage { messageState ->
                typingOn(ChatId(messageState.message.recipient.chatId))
                val result = RequestSendMessage("I'm tired") sendFor ChatId(messageState.message.recipient.chatId)
                when (result) {
                    is ResultRequest.Success -> result.response
                    is ResultRequest.Failure -> result.exception
                }
                typingOff(ChatId(messageState.message.recipient.chatId))
            }

        }

        users {

            // if some user added in chat where your bot is member, code below will start
            onAddedUserToChat {

            }

            // if some user removed in chat where your bot is member, code below will start
            onRemovedUserFromChat {

            }

        }

    }
}

private suspend fun CommandsScope.sendTextWithKeyboard(state: CommandState, keyboard: InlineKeyboard) {
    "Choose you dinner" prepareFor UserId(state.command.message.sender.userId) sendWith keyboard
}

private fun createInlineKeyboard(): InlineKeyboard {
    return keyboard {
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
}