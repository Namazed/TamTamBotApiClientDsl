package chat.tamtam.botsdk.client

import chat.tamtam.botsdk.client.retrofit.Success
import chat.tamtam.botsdk.model.request.Command
import chat.tamtam.botsdk.model.request.Photo
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import chat.tamtam.botsdk.model.request.Bot as RequestBot
import chat.tamtam.botsdk.model.response.Bot as ResponseBot

class BotInfoTest : ClientTest() {

    @Test
    fun `check right behavior and serialization when get bot info`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/bot_response.json", 200)
            val commands = listOf(Command("awesome", "my awesome command"),
                Command("awesome_two", "my_awesome_command_two")
            )
            val result = httpManager.getBotInfo()
            when (result) {
                is Success<ResponseBot> -> {
                    checkResponse(result, commands)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `check right behavior and serialization when edit bot info`() {
        runBlocking {
            mockServer.mockHttpResponse("/json/bot_response.json", 200)
            val commands = listOf(Command("awesome", "my awesome command"),
                Command("awesome_two", "my_awesome_command_two")
            )
            val bot = RequestBot("TestBot", description = "I can everything", commands = commands,
                photo = Photo("mail.ru", "ignore"))
            val result = httpManager.editBotInfo(bot)
            when (result) {
                is Success<ResponseBot> -> {
                    checkResponse(result, commands)
                }
                else -> {}
            }
        }
    }

    private fun checkResponse(
        result: Success<ResponseBot>,
        commands: List<Command>
    ) {
        result.response.apply {
            assert(userId == 1L)
            assert(name == "TestBot")
            assert(fullAvatarUrl == "mail.ru/full")
            assert(commands.size == 2)
            assert(commands[1].name == "awesome_two")
        }
    }
}