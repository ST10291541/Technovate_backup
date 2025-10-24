package vcmsa.projects.chocui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Chatbot : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var chatAdapter: Adapter
    private lateinit var closeButton: ImageButton

    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        closeButton = findViewById(R.id.closeButton)

        chatAdapter = Adapter(messages) { option ->
            addUserMessage(option)
            getBotResponse(option)
        }

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        // Close chatbot & return to main
        closeButton.setOnClickListener {
            finish() // closes Chatbot activity & returns to MainActivity
        }


        // Start conversation
        addBotMessage(
            "Hi there! What would you like to know?",
            listOf(
                "What is Cancer?", "How is Childhood Cancer Different?",
                "Early Warning Signs", "Types of Childhood Cancer",
                "Blood Disorders", "Contact CHOC", "Restart"
            )
        )

        // Handle Enter key
        messageInput.setOnEditorActionListener { _, _, _ ->
            val inputText = messageInput.text.toString().trim()
            if (inputText.isNotEmpty()) {
                addUserMessage(inputText)
                getBotResponse(inputText)
                messageInput.text.clear()
            }
            true
        }
    }

    private fun addUserMessage(text: String) {
        messages.add(Message(text, true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    private fun addBotMessage(text: String, options: List<String>? = null) {
        messages.add(Message(text, false, options))
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        chatRecyclerView.scrollToPosition(messages.size - 1)
    }

    // Utility: quick keyword helper
    private fun String.hasAny(vararg needles: String): Boolean {
        val s = this.lowercase()
        return needles.any { s.contains(it.lowercase()) }
    }

    private fun getBotResponse(userInput: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            val input = userInput.lowercase()

            when {
                // =========================
                // NAV / MENUS
                // =========================
                input.hasAny("main menu", "menu", "home") -> addBotMessage(
                    "What would you like to know?",
                    listOf(
                        "What is Cancer?", "How is Childhood Cancer Different?",
                        "Early Warning Signs", "Types of Childhood Cancer",
                        "Blood Disorders", "Contact CHOC", "Restart"
                    )
                )

                input.hasAny("restart", "start over", "reset") -> {
                    messages.clear()
                    chatAdapter.notifyDataSetChanged()
                    addBotMessage(
                        "Restarted. What would you like to do?",
                        listOf(
                            "What is Cancer?", "How is Childhood Cancer Different?",
                            "Early Warning Signs", "Types of Childhood Cancer",
                            "Blood Disorders", "Contact CHOC", "Restart"
                        )
                    )
                }

                // =========================
                // EARLY WARNING SIGNS (S I L U A N) — detailed & keyword with disclaimer
                // =========================
                input.hasAny("early warning", "warning signs", "siluan", "s.i.l.u.a.n") -> addBotMessage(
                    "EARLY WARNING SIGNS (S I L U A N):\n\n" +
                            "S — SEEK: Medical help early for persistent symptoms.\n" +
                            "I — EYE: White spot, new squint, new blindness, bulging eye.\n" +
                            "L — LUMP: Lumps in abdomen/pelvis, head/neck, limbs, testes, glands.\n" +
                            "U — UNEXPLAINED: Fever >2 weeks, weight loss, pallor, fatigue, bruising/bleeding.\n" +
                            "A — ACHING: Bones/joints/back pain, easy fractures.\n" +
                            "N — NEURO: Change in walk/balance/speech, regression, headache >1 week ± vomiting, enlarging head.\n\n" +
                            "If any of these are present, please seek medical care promptly.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Lump or Swelling", "Eye / Vision Changes", "Fever / Infection",
                        "Bone / Joint Pain", "Neurological Signs", "Bruising / Bleeding", "Main Menu")
                )

                // L — Lump / Swelling
                input.hasAny("lump", "swelling", "mass", "node", "gland", "tumor", "tumour") -> addBotMessage(
                    "A lump or swelling in a child should never be ignored.\n\n" +
                            "What to look for:\n" +
                            "• Location: neck, abdomen, chest, armpit, groin, testes.\n" +
                            "• Change: growing or recurring after it settles.\n" +
                            "• Feel: hard & fixed (more concerning) vs soft/movable (still check).\n" +
                            "• Pain: painful or painless lumps both need assessment.\n\n" +
                            "What to do now:\n" +
                            "• Seek medical help as soon as possible.\n" +
                            "• Note size, when you first noticed it, and changes.\n" +
                            "• Mention any fever, weight loss, bruising, night sweats.\n\n" +
                            "Important: Only a clinician can diagnose the cause of a lump. Early evaluation matters.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Eye / Vision Changes", "Fever / Infection", "Bone / Joint Pain", "Neurological Signs", "Bruising / Bleeding", "Main Menu")
                )

                // I — Eye / Vision
                input.hasAny("eye", "vision", "squint", "white spot", "bulging eye", "crossed eyes") -> addBotMessage(
                    "Eye/vision changes can be urgent.\n\n" +
                            "Watch for:\n" +
                            "• White reflection in the pupil (often seen in photos with flash).\n" +
                            "• New squint/crossed eyes, sudden vision loss, bulging eye, persistent redness/swelling.\n\n" +
                            "Action:\n" +
                            "• Arrange an urgent exam with a doctor/eye specialist.\n" +
                            "• Do not wait for it to correct itself.\n\n" +
                            "Early detection can protect sight and be life-saving.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Lump / Swelling", "Fever / Infection", "Bone / Joint Pain","Neurological Signs", "Bruising / Bleeding", "Main Menu")
                )

                // U — Unexplained Fever / Infections / Weight loss / Pallor / Bruising
                input.hasAny("fever", "temperature", "infection", "pale", "pallor", "weight loss", "fatigue", "tired") -> addBotMessage(
                    "Persistent or unexplained systemic symptoms should be assessed.\n\n" +
                            "Warning patterns:\n" +
                            "• Fever lasting > 2 weeks without clear cause.\n" +
                            "• Repeated or unusual infections.\n" +
                            "• Pallor (pale skin), unusual fatigue, weight loss, night sweats.\n\n" +
                            "Next steps:\n" +
                            "• See a doctor for examination and blood tests.\n" +
                            "• Track temperature, weight, and any additional symptoms.\n\n" +
                            "These may suggest blood disorders or other conditions and need timely care.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Lump / Swelling", "Eye / Vision Changes", "Bone / Joint Pain","Neurological Signs", "Bruising / Bleeding", "Main Menu")
                )

                // A — Aching Bones/Joints/Back
                input.hasAny("bone pain", "joint pain", "aching", "back pain", "hip pain", "leg pain") ||
                        (input.hasAny("pain") && input.hasAny("bone", "joint", "back", "leg", "arm", "hip")) -> addBotMessage(
                    "Bone/joint/back pain can be more than growing pains if persistent.\n\n" +
                            "Concerning features:\n" +
                            "• Ongoing or worsening pain, or pain that wakes the child at night.\n" +
                            "• Limping, stiffness, refusal to walk, swelling over bone/joint.\n" +
                            "• Easy fractures from minor falls.\n\n" +
                            "What to do:\n" +
                            "• Book a medical assessment. X-rays/blood tests may be needed.\n" +
                            "• Note timing, triggers, and any fever/weight loss/fatigue.\n\n" +
                            "Persistent/severe pain warrants evaluation.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Lump / Swelling", "Eye / Vision Changes", "Fever / Infection", "Neurological Signs", "Bruising / Bleeding", "Main Menu")
                )

                // N — Neurological Signs
                input.hasAny("headache", "headaches", "migraine", "vomiting", "balance", "clumsy", "seizure", "fits", "speech", "walking", "dizzy", "enlarging head") -> addBotMessage(
                    "Neurological changes need prompt attention.\n\n" +
                            "Look for:\n" +
                            "• Headaches > 1 week, worse in the morning or with vomiting.\n" +
                            "• New seizures, repeated unexplained vomiting, profound dizziness.\n" +
                            "• Changes in balance, walking, speech, behavior, or vision.\n" +
                            "• Enlarging head size in infants.\n\n" +
                            "Action:\n" +
                            "• Seek urgent medical care. Imaging (CT/MRI) may be required.\n\n" +
                            "Early evaluation is crucial.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Lump / Swelling", "Eye / Vision Changes", "Fever / Infection", "Bone / Joint Pain", "Bruising / Bleeding", "Main Menu")
                )

                // Bruising / Bleeding (explicit)
                input.hasAny("bruise", "bruising", "bleeding", "nosebleed", "gum bleeding", "petechiae") -> addBotMessage(
                    "Unexplained bruising/bleeding can indicate a blood issue.\n\n" +
                            "Warning signs:\n" +
                            "• Bruises without clear injury or in unusual locations.\n" +
                            "• Frequent nosebleeds, gum bleeding, tiny red/purple skin spots (petechiae).\n" +
                            "• Prolonged bleeding from small cuts.\n\n" +
                            "Next steps:\n" +
                            "• Seek medical evaluation and blood tests.\n" +
                            "• Mention any recent infections, fever, fatigue, or medications.\n\n" +
                            "These symptoms require timely assessment.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Lump / Swelling", "Eye / Vision Changes", "Fever / Infection", "Bone / Joint Pain", "Neurological Signs", "Main Menu")
                )
                // General “seek help” intent
                input.hasAny("doctor", "clinic", "hospital", "help", "urgent") -> addBotMessage(
                    "If your child has persistent or unusual symptoms, please seek medical help early.\n\n" +
                            "Early detection improves outcomes.\n\n" +
                            "— This chatbot is educational and not a medical diagnosis. If you are worried about your child, please seek care from a healthcare professional.",
                    listOf("Early Warning Signs", "Main Menu")
                )

                // =========================
                // CANCER OVERVIEW
                // =========================
                input.hasAny("what is cancer") -> addBotMessage(
                    "Cancer is a disease of the body’s cells. Normally, cells grow in a controlled way. Cancer starts when some cells grow out of control, stop working properly, and may form lumps (tumors) or spread to other parts of the body.\n\n" +
                            "Key points for children:\n" +
                            "• Childhood cancers often arise in organs or blood rather than from lifestyle factors.\n" +
                            "• They can grow quickly but are often very responsive to treatment.\n" +
                            "• Early detection and specialized care are vital.",
                    listOf("How is Childhood Cancer Different?", "Types of Childhood Cancer", "Early Warning Signs", "Main Menu")
                )

                input.hasAny("how is childhood cancer different", "childhood cancer different", "children vs adults") -> addBotMessage(
                    "How childhood cancer differs from adult cancer:\n\n" +
                            "• Biology: Often from developing cells/tissues; different under the microscope.\n" +
                            "• Sites: More common in blood, brain, kidneys, soft tissues.\n" +
                            "• Treatment: Often responds well to chemotherapy; protocols are age-tailored.\n" +
                            "• Outcomes: Cure rates are generally higher in children with timely, specialized care.",
                    listOf("Types of Childhood Cancer", "Blood Disorders", "Main Menu")
                )

                // =========================
                // TYPES OF CHILDHOOD CANCER
                // =========================
                input.hasAny("types of childhood cancer", "types of cancer", "common cancers") -> addBotMessage(
                    "Common childhood cancers:\n" +
                            "1) Leukemia (~34%)\n" +
                            "2) Brain & CNS Tumors (~22%)\n" +
                            "3) Lymphoma (~11%)\n" +
                            "4) Wilms Tumor (kidney)\n" +
                            "5) Soft Tissue Sarcomas (e.g., Rhabdomyosarcoma)\n\n" +
                            "Choose one to learn more.",
                    listOf("Leukemia", "Brain Tumor", "Lymphoma", "Wilms Tumor", "Soft Tissue Sarcoma", "Main Menu")
                )

                // Leukemia
                input.hasAny("leukemia", "leukaemia") -> addBotMessage(
                    "Leukemia (blood cancer) starts in the bone marrow where blood cells are made.\n\n" +
                            "Symptoms:\n" +
                            "• Frequent infections, fever, tiredness, pallor\n" +
                            "• Easy bruising/bleeding (nose/gums), bone pain, swollen glands\n" +
                            "• Sometimes abdominal fullness (enlarged liver/spleen)\n\n" +
                            "Diagnosis:\n" +
                            "• Blood tests, bone marrow examination, sometimes imaging.\n\n" +
                            "Treatment (varies by type: ALL/AML):\n" +
                            "• Intensive chemotherapy (often 6–9 months) then maintenance (up to 2–3 years for ALL).\n" +
                            "• Targeted therapy/immunotherapy in some cases; transfusion/antibiotic support.\n\n" +
                            "Outlook:\n" +
                            "• Many children achieve long-term remission with modern protocols.",
                    listOf("Brain Tumor", "Lymphoma", "Wilms Tumor", "Soft Tissue Sarcoma", "Main Menu")
                )

                // Brain Tumor
                input.hasAny("brain tumor", "brain tumour", "brain cancer", "cns tumor", "cns tumour") -> addBotMessage(
                    "Brain & CNS tumors are abnormal growths in the brain or spinal cord.\n\n" +
                            "Symptoms:\n" +
                            "• Morning headaches ± vomiting, drowsiness, appetite loss\n" +
                            "• Weakness, clumsiness, balance or vision/speech/swallowing problems\n" +
                            "• Seizures or behavioral changes\n\n" +
                            "Diagnosis:\n" +
                            "• Neurological exam, MRI/CT, sometimes biopsy.\n\n" +
                            "Treatment:\n" +
                            "• Surgery when possible, chemotherapy, and/or radiotherapy (age- and type-dependent).\n\n" +
                            "Follow-up:\n" +
                            "• Rehabilitation and learning support may be needed.",
                    listOf("Leukemia", "Lymphoma", "Wilms Tumor", "Soft Tissue Sarcoma", "Main Menu")
                )

                // Lymphoma
                input.hasAny("lymphoma", "hodgkin", "non-hodgkin") -> addBotMessage(
                    "Lymphoma is a cancer of the lymphatic system (Hodgkin & Non-Hodgkin types).\n\n" +
                            "Symptoms:\n" +
                            "• Painless swollen glands (neck/armpit/groin)\n" +
                            "• Chest masses causing cough, breathlessness\n" +
                            "• Abdominal swelling, fever, weight loss, night sweats\n\n" +
                            "Diagnosis:\n" +
                            "• Imaging and biopsy of lymph node/mass.\n\n" +
                            "Treatment:\n" +
                            "• Chemotherapy (months to ~2 years depending on type/stage), sometimes radiotherapy.\n\n" +
                            "Many children do very well with modern regimens.",
                    listOf("Leukemia", "Brain Tumor", "Wilms Tumor", "Soft Tissue Sarcoma", "Main Menu")
                )

                // Wilms Tumor
                input.hasAny("wilms", "nephroblastoma") -> addBotMessage(
                    "Wilms Tumor is a kidney cancer, most common under age 5.\n\n" +
                            "Symptoms:\n" +
                            "• Painless, firm swelling of the tummy, sometimes pain/fever\n" +
                            "• Blood in urine may occur\n\n" +
                            "Staging: ranges from confined to one kidney to both kidneys involved.\n\n" +
                            "Treatment:\n" +
                            "• Chemotherapy before/after surgery to remove the tumor; radiotherapy in some cases.\n\n" +
                            "Outcomes are often very good with timely care.",
                    listOf("Leukemia", "Brain Tumor", "Lymphoma", "Soft Tissue Sarcoma", "Main Menu")
                )

                // Soft Tissue Sarcoma
                input.hasAny("soft tissue sarcoma", "rhabdomyosarcoma", "sarcoma") -> addBotMessage(
                    "Soft tissue sarcomas (e.g., Rhabdomyosarcoma) arise from muscle/connective tissues.\n\n" +
                            "Symptoms depend on location:\n" +
                            "• Eye: bulging/blocked tear duct\n" +
                            "• Nose/throat: blocked nose, nosebleeds, swallowing changes\n" +
                            "• Head/neck: facial paralysis, lumps\n" +
                            "• Chest/abdomen/limbs: lumps, pain, breathing issues\n\n" +
                            "Diagnosis: imaging and biopsy.\n\n" +
                            "Treatment: chemotherapy, surgery and/or radiotherapy; sometimes bone marrow transplant in select cases.",
                    listOf("Leukemia", "Brain Tumor", "Lymphoma", "Wilms Tumor", "Main Menu")
                )

                // =========================
                // BLOOD DISORDERS
                // =========================
                input.hasAny("blood disorders", "blood disorder", "blood problem", "blood issues") -> addBotMessage(
                    "Blood disorders affect how blood cells are made and function in the bone marrow.\n\n" +
                            "Examples:\n" +
                            "• Anaemia (low red cells) → weakness, tiredness, breathlessness, pallor.\n" +
                            "• Low Platelets (thrombocytopenia) → easy bruising/bleeding, nose/gum bleeds.\n" +
                            "• Low White Cells (neutropenia) → frequent infections, fevers.\n\n" +
                            "Choose one to learn more.",
                    listOf("Anaemia", "Low Platelets", "Low White Cells", "Main Menu")
                )

                input.hasAny("anaemia", "anemia") -> addBotMessage(
                    "Anaemia = low red blood cells or haemoglobin.\n\n" +
                            "Symptoms: weakness, tiredness, breathlessness, pale skin, dizziness.\n\n" +
                            "Causes:\n" +
                            "• Iron deficiency, chronic illness, blood loss.\n" +
                            "• Bone marrow problems or leukemia (needs evaluation if unexplained/persistent).\n\n" +
                            "Next steps:\n" +
                            "• See a doctor for blood tests (CBC, iron studies, etc.).\n" +
                            "• Treatment targets the cause (iron, vitamins, treating underlying disease).",
                    listOf("Low Platelets", "Low White Cells", "Main Menu")
                )

                input.hasAny("low platelets", "platelets", "thrombocytopenia") -> addBotMessage(
                    "Low platelets (thrombocytopenia) increase bleeding/bruising risk.\n\n" +
                            "Signs: easy bruising, nose/gum bleeding, petechiae (tiny red/purple spots), prolonged bleeding from cuts.\n\n" +
                            "Causes: infections, medications, immune causes, bone marrow conditions.\n\n" +
                            "Action: medical assessment and blood counts; avoid high-risk activities until reviewed.",
                    listOf("Anaemia", "Low White Cells", "Main Menu")
                )

                input.hasAny("low white cells", "neutropenia", "white cells") -> addBotMessage(
                    "Low white cells (especially neutrophils) raise infection risk.\n\n" +
                            "Signs: frequent or severe infections, fevers, mouth ulcers.\n\n" +
                            "Causes: recent infections, medications, autoimmune, bone marrow disorders.\n\n" +
                            "Action: seek care for fevers; blood counts and clinical review guide treatment.",
                    listOf("Anaemia", "Low Platelets", "Main Menu")
                )

                // =========================
                // CONTACT
                // =========================
                input.hasAny("contact choc", "contact", "contact durban", "contact pmb") -> addBotMessage(
                    "CHOC KZN Contacts:\n\n" +
                            "Durban:\n" +
                            "📞 031 240 2917\n" +
                            "✉️ dbn@choc.org.za\n" +
                            "🏥 Inkosi Albert Luthuli Central Hospital, Durban\n\n" +
                            "Pietermaritzburg (PMB):\n" +
                            "📞 033 347 1441\n" +
                            "✉️ pmb@choc.org.za\n" +
                            "🏥 29 Carnoustie Road, Chase Valley Heights, Pietermaritzburg, 3201",
                    listOf("Main Menu")
                )

                // =========================
                // FALLBACK
                // =========================
                else -> addBotMessage(
                    "I'm sorry, I didn't fully catch that. Try a topic below or type a symptom/question.\n\n" +
                            "Tip: you can type things like \"lump on neck\", \"fever for two weeks\", \"headaches with vomiting\", \"bruising\", \"bone pain\".",
                    listOf(
                        "Early Warning Signs", "Types of Childhood Cancer",
                        "What is Cancer?", "Blood Disorders",
                        "Contact CHOC", "Main Menu", "Restart"
                    )
                )
            }
        }, 400)
    }
}
