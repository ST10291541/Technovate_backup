package vcmsa.projects.chocui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(
    private val messages: MutableList<Message>,
    private val onOptionClicked: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_USER = 0
        const val TYPE_BOT_TEXT = 1
    }

    override fun getItemViewType(position: Int): Int {
        val msg = messages[position]
        return if (msg.isUser) {
            TYPE_USER
        } else {
            TYPE_BOT_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_USER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_message, parent, false)
                UserViewHolder(view)
            }
            TYPE_BOT_TEXT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_bot_message, parent, false)
                BotViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is BotViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userText: TextView = itemView.findViewById(R.id.userMessage)
        fun bind(message: Message) {
            userText.text = message.text
        }
    }

    inner class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val botText: TextView = itemView.findViewById(R.id.botMessage)
        private val optionContainer: GridLayout = itemView.findViewById(R.id.optionContainer)

        fun bind(message: Message) {
            val text = message.text ?: ""
            val spannable = SpannableString(text)

            // Phone numbers
            val phonePattern = Regex("""\b\d{9,12}\b""")
            phonePattern.findAll(text).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1
                val phoneNumber = matchResult.value.replace("\\s".toRegex(), "")

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phoneNumber")
                            }
                            widget.context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(widget.context, "Cannot make calls here", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Emails
            val emailPattern = Regex("""[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}""")
            emailPattern.findAll(text).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1
                val email = matchResult.value

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$email")
                            }
                            if (intent.resolveActivity(widget.context.packageManager) != null) {
                                widget.context.startActivity(intent)
                            } else {
                                Toast.makeText(widget.context, "No email app installed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(widget.context, "Cannot send email here", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            val addressPattern = Regex("""(?m)(🏥.*|[0-9]{1,5}\s+\w+.*(?:Street|Road|Avenue|Hospital|City|Town|Village).*)""")
            addressPattern.findAll(text).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1
                val address = matchResult.value.trim()

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        try {
                            val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            intent.setPackage("com.google.android.apps.maps")
                            if (intent.resolveActivity(widget.context.packageManager) != null) {
                                widget.context.startActivity(intent)
                            } else {
                                Toast.makeText(widget.context, "Google Maps not installed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(widget.context, "Cannot open maps here", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Apply text with clickable spans
            botText.text = spannable
            botText.movementMethod = LinkMovementMethod.getInstance()

            // Handle options buttons
            optionContainer.removeAllViews()
            if (!message.options.isNullOrEmpty()) {
                optionContainer.visibility = View.VISIBLE
                optionContainer.columnCount = 3
                optionContainer.alignmentMode = GridLayout.ALIGN_BOUNDS
                optionContainer.useDefaultMargins = true

                message.options.forEach { option ->
                    val button = Button(itemView.context).apply {
                        this.text = option
                        textSize = 14f // sp → scales with user font settings
                        setTextColor(Color.BLACK)
                        backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
                        setOnClickListener { onOptionClicked(option) }
                        isAllCaps = false
                    }

                    val metrics = itemView.context.resources.displayMetrics

                    val params = GridLayout.LayoutParams().apply {
                        width = 0
                        height = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            80f, // Material standard button height
                            metrics
                        ).toInt()
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        setMargins(
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, metrics).toInt(),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, metrics).toInt(),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, metrics).toInt(),
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, metrics).toInt()
                        )
                        setGravity(android.view.Gravity.FILL)
                    }
                    button.layoutParams = params
                    optionContainer.addView(button)
                }
            } else {
                optionContainer.visibility = View.GONE
            }
        }
    }
}