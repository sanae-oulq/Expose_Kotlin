package ma.ensaj.sqlitekotlin

import java.util.Random
data class StudentModel (
    var id: Int = 0,
    var email:String = "",
    var nom: String = ""
){
    companion object{
        private var autoIncrementId=1
        fun getAutoId():Int{
            return autoIncrementId++
        }
    }
}

