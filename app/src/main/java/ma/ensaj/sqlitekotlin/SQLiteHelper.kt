package ma.ensaj.sqlitekotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLiteHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME , null,DATABASE_VERSION) {


    companion object{
        private const val DATABASE_VERSION=1
        private const val DATABASE_NAME="student.db"
        private const val TBL_STUDENT = "student"
        private const val NOM ="nom"
        private const val EMAIL = "email"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE student(id INTEGER PRIMARY KEY AUTOINCREMENT , nom TEXT, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }

    fun AjouterStudent(std:StudentModel): Long{
        val db =this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(NOM,std.nom)
        contentValue.put(EMAIL,std.email)

        val sucess = db.insert(TBL_STUDENT, null, contentValue)
        db.close()
        return sucess
    }
    @SuppressLint("Range")
    fun getAllStudent(): ArrayList<StudentModel>{
        val stdList: ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_STUDENT"
        val db = this.readableDatabase

        val cursor: Cursor
        try {
            cursor= db.rawQuery(selectQuery, null )
        }catch (e : Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            db.close()
            return ArrayList()
        }
        var nom: String
        var email: String

        if (cursor.moveToFirst()){
            do{
                nom=cursor.getString(cursor.getColumnIndex("nom"))
                email=cursor.getString(cursor.getColumnIndex("email"))
                val std= StudentModel(nom=nom,email=email)
                stdList.add(std)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return stdList

    }
    fun updateStudent(std : StudentModel): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NOM, std.nom)
        contentValues.put(EMAIL, std.email)

        val whereClause = "$NOM = ?"
        val whereArgs = arrayOf(std.nom)

        val success = db.update(TBL_STUDENT, contentValues, whereClause, whereArgs)

        db.close()
        return success
    }
}