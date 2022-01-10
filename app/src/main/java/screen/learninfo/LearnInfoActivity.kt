package screen.learninfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.squareup.picasso.Picasso
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
    }

    //MARK: - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_learn_info)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val testName = intent.getStringExtra(TEST_NAME)

        loadData()

        button1.setOnClickListener {

            val quizActivityIntent = Intent(this, QuizActivity::class.java)
            quizActivityIntent.putExtra(QuizActivity.TEST_NAME, testName)
            startActivity(quizActivityIntent)
        }
    }

    private fun loadData() {

        database.collection("Тесты").document("Список тестов").get()
            .addOnSuccessListener { snapshot ->

                val data = snapshot.data ?: return@addOnSuccessListener

                val testName = intent.getStringExtra(TEST_NAME)

                val test = data[testName] as HashMap<String, Any>

                val description = test["Описание"] as? String
                val video = test["Видео"] as? String
                val image = test["Картинка к заданию"] as? String

                if (description != null) {

                    testDescriptionTextView.visibility = View.VISIBLE
                    testDescriptionTextView.text = description
                }

                if (video != null) {

                    val mediaController = MediaController(this@LearnInfoActivity)
                    mediaController.setAnchorView(videoViewLearnInfoScreen)
                    videoViewLearnInfoScreen.visibility = View.VISIBLE
                    videoViewLearnInfoScreen.setMediaController(mediaController)

                    videoViewLearnInfoScreen.setVideoURI(Uri.parse(video))
                    videoViewLearnInfoScreen.requestFocus()
                    videoViewLearnInfoScreen.start()
                }

                if (image != null) {

                    infoImageView.visibility = View.VISIBLE

                    Picasso.get()
                        .load(image)
                        .resize(250, 250)
                        .centerCrop()
                        .placeholder(R.color.browser_actions_divider_color)
                        .into(infoImageView)
                }
            }
            .addOnFailureListener {
                Log.d("Ошибка", it.message.toString())
            }

//        ref.get().addOnSuccessListener { documentSnapshot ->
//            if (documentSnapshot != null) {
//                val data = documentSnapshot.data
//
//                video = data?.get("Видео теста") as String
//
//
//
//            } else {
//                //TODO: - вывести ошибку
//            }
    }
}