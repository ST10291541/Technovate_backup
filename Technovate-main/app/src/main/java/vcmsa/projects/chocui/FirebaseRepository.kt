package vcmsa.projects.chocui

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        const val COLLECTION_APP_DATA = "appData"
        const val DOCUMENT_CONTACT_INFO = "contactInfo"
        const val DOCUMENT_ABOUT_US = "aboutUs"
        const val DOCUMENT_DONATION_INFO = "donationInfo"

        const val DOCUMENT_VOLUNTEER_INFO = "volunteerInfo"
    }

    // Contact Info
    suspend fun getContactInfo(): ContactInfo? {
        return try {
            db.collection(COLLECTION_APP_DATA)
                .document(DOCUMENT_CONTACT_INFO)
                .get()
                .await()
                .toObject(ContactInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // About Us Info
    suspend fun getAboutUsInfo(): AboutUsInfo? {
        return try {
            db.collection(COLLECTION_APP_DATA)
                .document(DOCUMENT_ABOUT_US)
                .get()
                .await()
                .toObject(AboutUsInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Donation Info
    suspend fun getDonationInfo(): DonationInfo? {
        return try {
            db.collection(COLLECTION_APP_DATA)
                .document(DOCUMENT_DONATION_INFO)
                .get()
                .await()
                .toObject(DonationInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Volunteer Info
    suspend fun getVolunteerInfo(): VolunteerInfo? {
        return try {
            db.collection(COLLECTION_APP_DATA)
                .document("volunteerInfo")
                .get()
                .await()
                .toObject(VolunteerInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}