package com.example.final_nitt

import android.content.Context
import androidx.room.Room
import com.example.final_nitt.mainscreen.AskNittDatabase
import com.example.final_nitt.mainscreen.myDoubtScreen.myTable.QuestionRepository
import com.example.final_nitt.network.Network

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AskNittDatabase? = null

    fun getDatabase(context: Context): AskNittDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                AskNittDatabase::class.java,
                "asknitt_database"
            ).build()

            INSTANCE = instance

            instance
        }
    }
}

class AppContainer(context: Context) {

    private val database =
        DatabaseProvider.getDatabase(context)

    val questionRepository =
        QuestionRepository(
            questionDao = database.questionDao(),
            ktorClient = Network
        )
}