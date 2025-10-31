Dating App - Technical Implementation Details


Implementation Decisions


The primary goal was to build a robust, scalable, and maintainable application foundation, adhering
to modern Android development best practices.


The following key decisions were made:


- Architecture: MVVM with a Repository Pattern for a clear separation of concerns as it is critical for a
scalable application.


- The ViewModel contains the business logic and exposes a single UiState object to the UI so the UI is
“dumb”, it does not have any logic in it. It is lifecycle-aware (viewModelScope) and survives
configuration changes, preventing data loss.


- Repository: This pattern abstracts the data source. The ViewModel is completely unaware of
whether the data comes from a local Room database, a remote API, or a cache. This makes the app
highly flexible and easy to adapt to future backend integration.


- UI Framework: Used Jetpack Compose as it is the modern, Google-recommended toolkit for building
native Android UI


- State Management: Unidirectional Data Flow (UDF) with StateFlow. A single ChatUiState data class
represents the entire state of the screen (loading, error, messages). This eliminates the possibility of
inconsistent UI states (e.g., showing both a loading spinner and an error message).


- Data Persistence: Room Database as it is the industry-standard abstraction layer over SQLite and it
integrates nicely with Coroutines & Flows/


- Dependency Injection: I used Koin. I could as easily use hilt and dagger but for the scale of this test
Koin was decided to be used as it is easier to implement.


- Asynchronous Operations: Kotlin Coroutines & Flow as It allows me to write asynchronous code
sequentially and doing the operations usin viewModelScope ensures that any background work is
automatically canceled when the ViewModel is cleared, preventing memory leaks and unnecessary
work or ANRs if for some reason the request delays a lot.


- Modularisation: I have two modules, :app and :data because this how Clean architecture works and
it is easier to scale the app later. The two modules are independent and as such any change to one
does not affect the other and we have separation of concerns as the data module cannot access ui
app module and It's immediately obvious where new data-related logic should live.


Architectural Assumptions


- The application works compeletely offline.There is no networking layer, and all data is sourced from
and saved to the local Room database and the user icons are saved in the app for now.


- The isSentByUser flag is a simple boolean rather than a check against a dynamic userid and all
messages are part of one global chat history. They are not tied to specific user matches or
conversation IDs.


- Static Initial Data: The initial "chat history" is static and pre-packaged with the application for
demonstration purposes and reused the chat from the screenshots in the exercise PDF.


Known Limitations


- No Real-time Functionality: This is a simulated chat. Messages are only added by the user; there is
no backend to push new messages to the device in real-time.


- Error Handling: The UiState can represent an error, which is just a simple text instead of the
messages. This was done so simply show that the app can have proper error handling if needed.
Message Read Status: The "double-tick" icon on sent messages is purely cosmetic. There is no
underlying system to track message status (e.g., "sending," "sent," "delivered," "read").


- Data Scalability: The entire chat history is currently loaded from the database into memory. For a
conversation with tens of thousands of messages, this could become a performance bottleneck and
as such a “load only 50 latest messages” functionality could be added and as the user scrolls to top,
to dynamically load another 50 messages with pagination.


- Accessibility: While standard Compose components offer good baseline accessibility, the app has
not been explicitly tested or optimized for screen readers like TalkBack (e.g., ensuring all icons have
content descriptions).


Future Improvements


- I would focus on Backend & Real-time Integration by Implementing a networking layer (REST) using
Retrofit and an appropriate data source in the MessageRepository to fetch and send messages to a
real backend database.


- UI: Moving all strings to strings.xml, adding translations and taking extra care to move the chat
bubbles and other text to be RTL instead of LTR in the languages that read and write that way e.g.
Arabic.


- Implement Real-time Messaging: Integrate a WebSocket client or Firebase Cloud Messaging (FCM)
to receive new messages in real-time and update the UI reactively.


- Authentication: Build a full authentication flow to support multiple users.


- Accessibility: optimise the app for screen readers like Talbkack.


- Testing: Right now I have added testing only at the ViewModel level to show that I know how to add
proper unit tests using Mockk. As a future improvement, tests should be added to the data module
and testTags should be added to the components so a QA team could automate testing.


- Pagination: As said before, right now it loads the whole chat history, with adding pagination we can
make this load faster and not show the scroll to latest message.


- Media Messages: Extend the system to support sending and displaying images, GIFs, or other media
types
