package screen.login.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration.*
import screen.registration.ui.RegistrationActivity
import screen.test.ui.TestActivity
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.firestore
//    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

//        reference = FirebaseDatabase.getInstance().getReference("users")

        setContentView(R.layout.activity_main)

        // Кнопка регистрации
        registrationBtnMainAct.setOnClickListener {
            if (inputEmailEditTextMainAct.text!!.isNotEmpty() && inputPasswordEditTextMainAct.text!!.isNotEmpty()) {
                createUser(
                    inputEmailEditTextMainAct.text!!.trim().toString(),
                    inputPasswordEditTextMainAct.text!!.trim().toString()
                )
            } else {
                Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка входа
        logInBtnMainAct.setOnClickListener {
            if (inputEmailEditTextMainAct.text!!.isNotEmpty() && inputPasswordEditTextMainAct.text!!.isNotEmpty()) {
                loginUser(
                    inputEmailEditTextMainAct.text!!.trim().toString(),
                    inputPasswordEditTextMainAct.text!!.trim().toString()
                )
            } else {
                Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод создания нового пользователя (регистрация)
    private fun createUser(email: String, password: String) {
        progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {


                    val userCollection = database.collection("users").document(auth.currentUser?.uid.toString())
                    val map = hashMapOf(
                        "email" to auth.currentUser?.email.toString()
                    )
                    userCollection.set(map)


//                    val userReference = reference.child(auth.currentUser?.uid.toString())
//                    val map: HashMap<String, Any> = hashMapOf(
//                        "email" to auth.currentUser?.email.toString()
//                    )
//                    userReference.setValue(map)

                    val intentToRegistrationAct = Intent(this, RegistrationActivity::class.java)
                    startActivity(intentToRegistrationAct)
                    Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()
                } else {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Ошибка: " + task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Метод входа уже реганного пользователя (войти)
    private fun loginUser(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val intentToTestAct = Intent(this, TestActivity::class.java)
                    startActivity(intentToTestAct)
                } else {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Ошибка: " + task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Если пользователь зареган, то сразу открывать экран с тестами
    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null) {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
    }
}