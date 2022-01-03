package screen.fragment

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.custom_toolbar_test_act.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_test.*
import screen.login.ui.MainActivity
import screen.test.ui.TestAdapter
import kotlinx.android.synthetic.main.fragment_test.progressBar as progressBar1

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var newRef: DatabaseReference

    private var test: HashMap<String, List<Int>> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        ref = database.getReference("users")
            .child(auth.currentUser?.uid.toString())


        newRef = database.getReference("users")
            .child(auth.currentUser?.uid.toString())
            .child("Tests")
            .child("TestsInformation")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLogOut()
        logoutButton.visibility = View.VISIBLE
        iconHelp.visibility = View.INVISIBLE
        getUserScore()

    }

    private fun getUserScore() {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.child("tests").children.forEach {
                    test[it.key.orEmpty()] = it.value as List<Int>
                }

                if (test.size == 0) {
                    noCompletedTestsTextView.visibility = View.VISIBLE
                }

                val testAdapter = ProfileAdapter(test)
                recyclerViewProfile.adapter = testAdapter

                progressBar.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("err", "${databaseError.details} - ${databaseError.message}")
            }
        }

        ref.addListenerForSingleValueEvent(postListener)
    }
    private fun initLogOut() {

        logoutButton.visibility = View.VISIBLE
        iconHelp.visibility = View.INVISIBLE

        val alertDialog = AlertDialog
            .Builder(this.view!!.context)
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