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
import kotlinx.coroutines.delay
import screen.learninfo.LearnInfoActivity
import screen.test.mvp.TestView
import screen.test.ui.TestAdapter
import java.lang.Exception

class TestFragment : Fragment(), TestView, TestAdapter.OnItemClickListener {

    //MARK: - Properties

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private val testListKeys = arrayListOf<String>()
    private val images = arrayListOf<String>()

    //MARK: - Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        loadDataFromFB()
        initAlertDialog()
    }

    //MARK: - Private funcs

    // Загрузка названий тестов
    private fun loadDataFromFB() {

        testListKeys.clear()
        images.clear()

        database.collection("Тесты").document("Список тестов")
            .get()
            .addOnSuccessListener { snapshot ->

                val data = snapshot.data ?: return@addOnSuccessListener

                val testImages = arrayListOf<HashMap<String, Any>>()

                data.map {

                    testImages.add(it.value as HashMap<String, Any>)
                    testListKeys.add(it.key)
                }


                for (value in testImages) {

                    images.add(value["Картинка"] as String)
                }

                if (testListKeys.size == 0) {

                    noTestTextView.visibility = View.VISIBLE

                } else {

                    Log.d("HELLO", images.size.toString())
                    val testAdapter = TestAdapter(testListKeys, images, this@TestFragment)
                    recyclerViewTestAct.adapter = testAdapter

                    progressBar.visibility = View.INVISIBLE
                }
            }
            .addOnFailureListener { exception: Exception ->

                Log.d("Ошибка", exception.message.toString())
            }
    }

    override fun onItemClick(position: Int) {

        val intent = Intent(activity, LearnInfoActivity::class.java)
        intent.putExtra(LearnInfoActivity.TEST_NAME, testListKeys[position])

        recyclerViewTestAct.adapter?.notifyItemChanged(position)
        startActivity(intent)
    }

    private fun initAlertDialog() {

        logoutButton.visibility = View.INVISIBLE
        iconHelp.visibility = View.VISIBLE

        val alertDialog =
            AlertDialog.Builder(this.requireView().context).setTitle("Инструкция").setMessage(
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