package screen.fragment

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseauthexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.custom_toolbar_test_act.*
import kotlinx.android.synthetic.main.fragment_test.*
import screen.learninfo.LearnInfoActivity
import screen.test.mvp.TestView
import screen.test.ui.TestAdapter
import java.lang.Exception

class TestFragment : Fragment(), TestView, TestAdapter.OnItemClickListener {

    //MARK: - Properties

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private var testNamesList = ArrayList<String>()
    private var testImagesList = ArrayList<String>()
    private var testResultNamesList = ArrayList<String>()

    //MARK: - Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loadDataFromFB()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAlertDialog()
    }

    //MARK: - Private funcs

    // Загрузка названий тестов
    private fun loadDataFromFB() {

//        database.collection("users")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//
//            }
//            .addOnFailureListener { exception: Exception ->
//
//            }

        val ref = database.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("Тесты")
            .document("Информация по тестам")

        ref.get().addOnSuccessListener { document ->

            if (document.data != null) {
                val data = document.data
                testImagesList = data?.get("Картинки тестов") as ArrayList<String>
                testNamesList = data?.get("Названия тестов") as ArrayList<String>
                testResultNamesList = data?.get("Названия результатов тестов") as ArrayList<String>

                if (testNamesList.size == 0) {
                    noTestTextView.visibility = View.VISIBLE
                }

                val testAdapter = TestAdapter(testNamesList, testImagesList, this@TestFragment)
                recyclerViewTestAct.adapter = testAdapter

                progressBar.visibility = View.INVISIBLE
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }


     override fun onItemClick(position: Int) {

         val intent = Intent(activity, LearnInfoActivity::class.java)
         intent.putExtra(LearnInfoActivity.TEST_NAME, testNamesList[position])
         intent.putExtra(LearnInfoActivity.TEST_RESULT_NAME, testResultNamesList[position])

         recyclerViewTestAct.adapter?.notifyItemChanged(position)
         startActivity(intent)
     }


     private fun initAlertDialog() {

         logoutButton.visibility = View.INVISIBLE
         iconHelp.visibility = View.VISIBLE

        val alertDialog =
            AlertDialog.Builder(this.view!!.context).setTitle("Инструкция").setMessage(
                "После нажатия кнопки 'Приступить к тесту' вы перейдете к просмотру"
                        + "\n"
                        + "видеоролика с материалом по тесту."
                        + "\n"
                        + "После просмотра видеоролика"
                        + "\n"
                        + "нажмите на кнопку 'Начать тест' и приступайте к выполнению теста"
            ).setPositiveButton("OK") { _, _ ->
            }.create()

        iconHelp.setOnClickListener {

            alertDialog.show()
        }
    }
}