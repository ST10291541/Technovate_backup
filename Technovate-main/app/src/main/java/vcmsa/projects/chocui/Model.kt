package vcmsa.projects.chocui

data class Message(
    val text: String,
    val isUser: Boolean,
    val options: List<String>? = null,
)
