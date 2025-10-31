package com.example.datingapp

import com.example.data.Message
import com.example.data.MessageRepository
import com.example.datingapp.pages.ChatViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.LocalDateTime
import kotlin.test.Test

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: MessageRepository
    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
    }

    @Test
    fun `when repository emits messages, uiState is updated correctly`() {
        val testMessages =
            listOf(
                Message("Hello", LocalDateTime.now(), isSentByUser = false),
            )

        every { repository.allMessages } returns flowOf(testMessages)

        viewModel = ChatViewModel(repository)

        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(testMessages, uiState.messages)
        assertNull(uiState.error)
    }

    @Test
    fun `when repository flow throws error, uiState is updated with error`() {
        val errorMessage = "Database error"
        val exception = RuntimeException(errorMessage)
        every { repository.allMessages } returns flow { throw exception }

        viewModel = ChatViewModel(repository)

        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertTrue(uiState.messages.isEmpty())
        assertEquals(errorMessage, uiState.error)
    }

    @Test
    fun `with proper message, repository insert is called`() {
        every { repository.allMessages } returns flowOf(emptyList())
        viewModel = ChatViewModel(repository)

        val textToSend = " test "
        val messageSlot = slot<Message>()
        coEvery { repository.insert(capture(messageSlot)) } returns Unit

        viewModel.sendMessage(textToSend)

        coVerify(exactly = 1) { repository.insert(any()) }
        assertEquals("test", messageSlot.captured.text)
        assertTrue(messageSlot.captured.isSentByUser)
    }

    @Test
    fun `if send message with blank text,repository insert is not called`() {
        every { repository.allMessages } returns flowOf(emptyList())
        viewModel = ChatViewModel(repository)
        val textToSend = "   "

        viewModel.sendMessage(textToSend)

        coVerify(exactly = 0) { repository.insert(any()) }
    }

    @Test
    fun `when repository insert throws exception, uiState is updated with error`() {
        every { repository.allMessages } returns flowOf(emptyList())
        coEvery { repository.insert(any()) } throws RuntimeException("Insertion failed")
        viewModel = ChatViewModel(repository)

        viewModel.sendMessage("message")

        val uiState = viewModel.uiState.value
        assertEquals("Failed to send message.", uiState.error)
    }
}
