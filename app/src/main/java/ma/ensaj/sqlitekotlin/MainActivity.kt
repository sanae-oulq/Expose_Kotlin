package ma.ensaj.sqlitekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter:StudentAdapter? = null
    private var std: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()

        sqliteHelper = SQLiteHelper( this)
        btnAdd.setOnClickListener { addStudent()}
        btnView.setOnClickListener{getStudents()}
        btnUpdate.setOnClickListener{updateStudent()}
        adapter?.setOnClickItem {
            Toast.makeText(this,it.nom ,Toast.LENGTH_SHORT).show()
            //Update record
            edName.setText(it.nom)
            edEmail.setText(it.email)
            std = it
        }
    }

    private fun updateStudent() {
        val nom = edName.text.toString()
        val email = edEmail.text.toString()

        if (nom==std?.nom && email == std?.email){
            Toast.makeText(this, "Enregistrement n'est pas changé", Toast.LENGTH_SHORT).show()
            return
        }
        val std = StudentModel(nom= nom, email = email )
        val status = sqliteHelper.updateStudent(std)
        if(status > -1){
            clearEditText()
            getStudents()

        }else{
            Toast.makeText(this, "Modification non effectué", Toast.LENGTH_SHORT).show()
        }

    }


    private fun getStudents() {
        val stdList = sqliteHelper.getAllStudent()
        Log.e("allStudents test",stdList.size.toString())

        adapter?.addItems(stdList)
    }


    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()
        if(name.isEmpty() || email.isEmpty()){
            Toast.makeText(  this,"Please enter requried field", Toast.LENGTH_SHORT).show()
        } else {
            val std = StudentModel(nom= name, email =email)
            val status = sqliteHelper.AjouterStudent(std)
//Check Insert success or not success
            if (status > -1) {
                Toast.makeText(this, "Etudiant Ajouté...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudents()
            }else {
                Toast.makeText(this,"Record Non Enregistré", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }

    private fun initView() {
        edName =findViewById(R.id.edNom)
        edEmail =findViewById(R.id.edEmail)
        btnAdd =findViewById(R.id.btnAjouter)
        btnView =findViewById(R.id.btnAfficher)
        btnUpdate= findViewById(R.id.btnUpdate  )
        recyclerView=findViewById(R.id.recyclerView)
    }
    private fun initRecyclerView(){
        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter= StudentAdapter()
        recyclerView.adapter= adapter

    }



}