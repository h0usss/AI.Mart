package com.h0uss.aimart

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.h0uss.aimart.data.database.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@RequiresApi(Build.VERSION_CODES.O)
object Graph {
    val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var db: AppDataBase
        private set


    fun provide(context: Context){
        db = AppDataBase.getDataBase(context, applicationScope)

    }
}