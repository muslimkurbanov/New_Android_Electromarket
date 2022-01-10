package screen.quiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_learn_info.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.activity_quiz.button1
import kotlinx.android.synthetic.main.activity_registration.*
import screen.fragment.TestFragment
import screen.test.ui.TestActivity
import kotlin.collections.ArrayList

class QuizActivity : AppCompatActivity() {

    //MARK: - Properties

    companion object {
        const val TEST_NAME: String = "test_name"
//        const val TEST_RESULT_NAME: String = "test_result_name"
    }

    private val database = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    //Убрать нахер
    private var firebaseQuestions = arrayListOf<String>()
    private var firebaseRightAnswers = arrayListOf<String>()
    private var firebaseAnswers = ArrayList<HashMap<String, String>>()

    private var testResults = arrayListOf<Int>()
    //До сюды

    private var questions = arrayListOf<String>()
    private var answers = arrayListOf<HashMap<String, Any>>()

    private var index = 0
    private var score = 0

    private val buttons by lazy {
        arrayListOf(
            button1,
            button2,
            button3,
            button4
        )
    }

    //MARK: - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val testName = intent.getStringExtra(TEST_NAME)

        auth = FirebaseAuth.getInstance()

        loadData()
        initListeners()

        //TODO: - Выпилить
//        val resultsRef = database.collection("users")
//            .document(auth.currentUser?.uid.toString())
//
//        getResultsFromFB(resultsRef)
//
//        val testRef = database.collection("users")
//            .document(auth.currentUser?.uid.toString())
//            .collection("Тесты")
//            .document(testName.toString())
//
//        getTestFromFB(testRef)
    }

    private fun loadData() {

        val testName = intent.getStringExtra(TEST_NAME)

        database.collection("Тесты").document("Список тестов").get()
            .addOnSuccessListener {

                val data = it.data ?: return@addOnSuccessListener

                val test = data[testName] as? HashMap<String, Any>

                answers = (test?.get("Варианты ответов") ?: return@addOnSuccessListener) as ArrayList<HashMap<String, Any>>
                questions = (test["Вопросы"] ?: return@addOnSuccessListener) as ArrayList<String>
            }

            .addOnFailureListener {
                Log.d("Ошибка", it.message.toString())
            }
    }

//    private fun getResultsFromFB(resultsRef: DocumentReference) {
//
//        val testName = intent.getStringExtra(TEST_NAME)
//        val testResultName = intent.getStringExtra(TEST_RESULT_NAME)
//
//        resultsRef.get().addOnSuccessListener { documentSnapshot ->
//
//            if (documentSnapshot != null) {
//                val data = documentSnapshot.data
//
//                val testres = data?.get("Результаты по тестам") as HashMap<String, ArrayList<Int>>
//
//                if (testres[testResultName.toString()] != null) {
//                    testResults = testres[testResultName.toString()]!!
//                } else {
//                    //TODO: - Вывести ошибку
//                }
//
//            } else {
//                //TODO: - вывести ошибку
//            }
//        }
//    }

//    private fun getTestFromFB(testRef: DocumentReference) {
//
//        testRef.get().addOnSuccessListener { documentSnapshot ->
//
//            if (documentSnapshot != null) {
//                val data = documentSnapshot.data
//
//                firebaseQuestions = data?.get("Вопросы") as ArrayList<String>
//                firebaseRightAnswers = data?.get("Правильные ответы") as ArrayList<String>
//                firebaseAnswers = data?.get("Ответы") as ArrayList<HashMap<String, String>>
//
//                textView.text = firebaseQuestions[index]
//                applyButtons()
//            } else {
//                //TODO: - вывести ошибку
//            }
//        }
//    }


    private fun applyButtons() {

        //TODO - Сделать изменение вариантов ответа под новую архитектуру
//        buttons.forEachIndexed { index, button ->
//            button.text = firebaseAnswers[this.index][index.toString()]
//        }
    }
    
    private fun initListeners() {

        finishButton.setOnClickListener {

            //TODO: - Переделать добавление результатов тестов на бд
//            val testResultName = intent.getStringExtra(TEST_RESULT_NAME)
//            testResults.add(score)
//
//            val testResultRef = database.collection("users")
//                .document(auth.currentUser?.uid.toString())
//
//            val resultHashMap = hashMapOf(
//                "Результаты по тестам" to hashMapOf(testResultName to testResults)
//            )
//            testResultRef.set(resultHashMap, SetOptions.merge())
//
//            val intentNice = Intent(this, TestActivity::class.java)
//            startActivity(intentNice)

        }

        buttons.forEach { button ->

            button.setOnClickListener {

                //TODO: - Переделать логику тестов

//                if (button.text == firebaseRightAnswers[index]) {
//
//                    index++
//                    score++
//                    textView.text = firebaseQuestions[index]
//
//                } else if (index <= firebaseQuestions.size) {
//
//                    index++
//                    textView.text = firebaseQuestions[index]
//
//                } else {
//                    return@setOnClickListener
//                }
//
//                if (index == (firebaseQuestions.size - 1)) {
//                    for (i in buttons) {
//                        i.visibility = View.GONE
//                        finishButton.visibility = View.VISIBLE
//                        resultTextView.visibility = View.VISIBLE
//                        resultTextView.text = "Ваш результат: $score"
//                    }
//                } else {
//                    applyButtons()
//                }
            }
        }
    }
}