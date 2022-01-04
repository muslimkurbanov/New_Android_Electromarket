package screen.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseauthexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.custom_toolbar_test_act.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_test.*
import screen.login.ui.MainActivity
import screen.test.ui.TestAdapter
import kotlinx.android.synthetic.main.fragment_test.progressBar as progressBar1

class ProfileFragment : Fragment() {

    private val database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var ref: DatabaseReference
//    private lateinit var newRef: DatabaseReference

    private var test: HashMap<String, List<Int>> = HashMap()
    private var testResultName = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLogOut()
        logoutButton.visibility = View.VISIBLE
        iconHelp.visibility = View.INVISIBLE
        loadData()
    }

    private fun loadData() {

        val userCollection = database.collection("users")
            .document(auth.currentUser?.uid.toString())

        val resultNameCollection = database.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("Тесты")
            .document("Информация по тестам")

        resultNameCollection.get().addOnSuccessListener { snapshot ->

            if (snapshot.data != null) {
                val data = snapshot.data

                testResultName = data?.get("Названия результатов тестов") as ArrayList<String>
                //TODO: ishidden false table
            }
        }

        userCollection.get().addOnSuccessListener { snapshot ->

            if (snapshot.data != null) {
                val data = snapshot.data

                val results = data?.get("Результаты по тестам") ?: run {
                    noCompletedTestsTextView.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                    return@addOnSuccessListener
                }

//                val results = data?.get("Результаты по тестам").let { it } ?: return@addOnSuccessListener

                test = data?.get("Результаты по тестам") as HashMap<String, List<Int>>

                if (test.isEmpty()) {
                    noCompletedTestsTextView.visibility = View.VISIBLE
                }

                val testAdapter = ProfileAdapter(test)
                recyclerViewProfile.adapter = testAdapter

                progressBar.visibility = View.INVISIBLE
            } else {
                //TODO: сделать вывод - тесты не пройдены
            }
        }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }

    private fun initLogOut() {

        logoutButton.visibility = View.VISIBLE
        iconHelp.visibility = View.INVISIBLE

        val alertDialog = AlertDialog
            .Builder(this.requireView().context)
            .setTitle("Инструкция")
            .setMessage("Вы действительно хотите выйти из профиля?")
            .setPositiveButton("Да") { _, _ ->

                auth.signOut()
                val intentToMainAct = Intent(context, MainActivity::class.java)
                startActivity(intentToMainAct)

            }.setNegativeButton("Нет") { _, _ ->

            }.create()

        logoutButton.setOnClickListener {

            alertDialog.show()
        }
    }
}