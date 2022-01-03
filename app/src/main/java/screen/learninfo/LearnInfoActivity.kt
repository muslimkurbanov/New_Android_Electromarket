package screen.learninfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_learn_info.*
import kotlinx.android.synthetic.main.fragment_test.*
import screen.quiz.QuizActivity
import screen.test.ui.TestAdapter

class LearnInfoActivity : AppCompatActivity() {

    //MARK: - Properties

    private val database = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private var video: String = ""

    companion object {
        const val TEST_NAME: String = "test_name"
        const val TEST_RESULT_NAME: String = "test_result_name"
    }

    //MARK: - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_learn_info)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val testName = intent.getStringExtra(TEST_NAME)
        val testResultName = intent.getStringExtra(TEST_RESULT_NAME)

        val ref = database.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("Тесты")
            .document(testName.toString())

        getVideoUrlFromFB(ref)

        button1.setOnClickListener {

            val quizActivityIntent = Intent(this, QuizActivity::class.java)
            quizActivityIntent.putExtra(QuizActivity.TEST_NAME, testName)
            quizActivityIntent.putExtra(QuizActivity.TEST_RESULT_NAME, testResultName)
            startActivity(quizActivityIntent)
        }
    }

    private fun getVideoUrlFromFB(ref: DocumentReference) {

        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val data = documentSnapshot.data

                video = data?.get("Видео теста") as String

                val mediaController = MediaController(this@LearnInfoActivity)
                mediaController.setAnchorView(videoViewLearnInfoScreen)
                videoViewLearnInfoScreen.setMediaController(mediaController)

                videoViewLearnInfoScreen.setVideoURI(Uri.parse(video))
                videoViewLearnInfoScreen.requestFocus()
                videoViewLearnInfoScreen.start()

            } else {
                //TODO: - вывести ошибку
            }
        }
    }
}