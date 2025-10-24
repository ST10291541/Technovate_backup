package vcmsa.projects.chocui

import java.io.Serializable

data class Symptom(
    val title: String,
    val snacks: List<String>,
    val color: String
) : Serializable