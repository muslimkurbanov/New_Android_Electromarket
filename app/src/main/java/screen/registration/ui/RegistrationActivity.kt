package screen.registration.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthexample.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_registration.*
import screen.test.ui.TestActivity
import java.util.*
import kotlin.collections.HashMap

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
//    private lateinit var reference: DatabaseReference
    private val database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
//        reference = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser?.uid.toString())

        registrationBtnRegAct.setOnClickListener {
            registerUser()
        }
    }

    override fun onBackPressed() {

    }

    // Полная регистрация пользователя (Имя, Фамилия, Номер телефона)
    private fun registerUser(){

        if(inputNameEditTextRegistrationAct.text!!.isNotEmpty()
            && inputSernameEditTextRegistrationAct.text!!.isNotEmpty()
            && inputUserNumberRegistrationAct.text!!.isNotEmpty()){

            val userCollection = database.collection("users").document(auth.currentUser?.uid.toString())

            val userMap = hashMapOf(
                "Имя" to inputNameEditTextRegistrationAct.text.toString(),
                "Фамилия" to inputSernameEditTextRegistrationAct.text.toString(),
                "Номер телефона" to inputUserNumberRegistrationAct.text.toString()
            )

            userCollection.set(userMap)

//            val map: HashMap<String, Any> = hashMapOf(
//                "Имя" to inputNameEditTextRegistrationAct.text.toString(),
//                "Фамилия" to inputSernameEditTextRegistrationAct.text.toString(),
//                "Номер телефона" to inputUserNumberRegistrationAct.text.toString()
//            )
//            reference.setValue(map)
            val intentToTestFrag = Intent(this, TestActivity::class.java)
            startActivity(intentToTestFrag)

        }else{

            Toast.makeText(this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show()
        }
    }
}