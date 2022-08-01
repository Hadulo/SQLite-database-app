package com.example.morningsqlitedatabaseapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var editTextName:EditText ?= null
    var editTextEmail:EditText ?= null
    var editTextIdNumber:EditText ?= null
    var buttonSave:Button ?= null
    var buttonView:Button ?= null
    var buttonDelete:Button ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextName = findViewById(R.id.mEdtName)
        editTextEmail = findViewById(R.id.mEdtEmail)
        editTextIdNumber= findViewById(R.id.mEdtIdNumber)
        buttonSave = findViewById(R.id.mBtnSave)
        buttonView = findViewById(R.id.mBtnView)
        buttonDelete = findViewById(R.id.mBtnDelete)
        //Create a database
        val db = openOrCreateDatabase("votersDb", Context.MODE_PRIVATE,null)
        //Create a table called users inside the voters Db
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafe VARCHAR, kitambulisho VARCHAR)")
        //Set a listener to button save to implement the saving
        buttonSave!!.setOnClickListener {
            //Receive the data from the user
            var userName = editTextName!!.text.toString()
            var userEmail = editTextEmail!!.text.toString()
            var userIdNumber = editTextIdNumber!!.text.toString()
            //Check if the user is submitting empty fields
            if (userName.isEmpty() || userEmail.isEmpty() || userIdNumber.isEmpty()){
                displayMessage("EMPTY FIELDS", "Please fill in the inputs!!")
            }else{
                //Proceed to save the data
                db.execSQL("INSERT INTO users VALUES('"+userName+"',"+userEmail+"','"+userIdNumber+"')")
                displayMessage("SUCCESS!!!", "Data saved successfully!!!")
                clear()
            }
            buttonView!!.setOnClickListener {
               //use cursor to select all the data from the database
                var cursor = db.rawQuery("SELECT * FROM users",null)
                //Check if if theres no record in the db
                if (cursor.count == 0){
                    displayMessage("NO DATA!!!", "Sorry, the db is empty!!")
                }else{
                    //Use a string buffer to append all the records for display
                    var buffer = StringBuffer()
                    //Use a loop to display data per row
                    while (cursor.moveToNext()){
                        buffer.append(cursor.getString(0)+"\n")//Column 0 is for name
                        buffer.append(cursor.getString(1)+"\n")//Column 1 is for Email
                        buffer.append(cursor.getString(2)+"\n")//Column 2 is for ID No
                    }
                    displayMessage("DB RECORDS", buffer.toString())
                }
            }
            buttonDelete!!.setOnClickListener {
                //receive the ID number from the user
                var idNumber = editTextIdNumber!!.text.toString().trim()
                //Check if the user is submitting an empty field
                if (idNumber.isEmpty()){
                    displayMessage("EMPTY FIELD!!!", "Please enter ID no!!!")
                }else{
                    //Use cursor to select user with provided ID
                    var cursor = db.rawQuery("SELECT * FROM WHERE kitambulisho='"+idNumber+"'",null)
                    //Check if there's s user with the provided id
                    if (cursor.count == 0){
                        displayMessage("NO RECORD!!!", "Sorry no user found!!!")
                    }else{
                        db.execSQL("DELETE FROM users WHERE kitambulisho="+idNumber+"")
                        displayMessage("SUCCESS!!!", "Record deleted successfully")
                        clear()
                    }
                }
            }
        }
    }
    fun displayMessage(kichwa:String, ujumbe:String){
        val alertDialog = AlertDialog.Builder(applicationContext)
        alertDialog.setTitle(kichwa)
        alertDialog.setMessage(ujumbe)
        alertDialog.create().show()
    }
    fun clear(){
        editTextName!!.setText("")
        editTextEmail!!.setText("")
        editTextIdNumber!!.setText("")
    }
}