package com.h0uss.aimart.data.database

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.h0uss.aimart.data.converter.BigDecimalConverter
import com.h0uss.aimart.data.converter.ListIntConverter
import com.h0uss.aimart.data.converter.LocalDateTimeConverter
import com.h0uss.aimart.data.entity.FeedbackEntity
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.entity.PortfolioItemEntity
import com.h0uss.aimart.data.entity.PortfolioTagEntity
import com.h0uss.aimart.data.entity.ProductEntity
import com.h0uss.aimart.data.entity.ProductTagEntity
import com.h0uss.aimart.data.entity.Tag
import com.h0uss.aimart.data.entity.TransactionEntity
import com.h0uss.aimart.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@TypeConverters( value = [
    LocalDateTimeConverter::class,
    BigDecimalConverter::class,
    ListIntConverter::class
] )
@Database(
    entities = [
        FeedbackEntity::class,
        OrderEntity::class,
        PortfolioItemEntity::class,
        PortfolioTagEntity::class,
        ProductEntity::class,
        ProductTagEntity::class,
        Tag::class,
        TransactionEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {

    companion object{
        @Volatile
        var INSTANCE: AppDataBase? = null
        fun getDataBase(context: Context, scope: CoroutineScope): AppDataBase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = AppDataBase::class.java,
                    name = "aimart_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            scope.launch(Dispatchers.IO) {
                INSTANCE?.let { database ->
                    populateDatabase(
                    )
                } ?: Log.e("AI.MartDB", "Fatal: AppDataBase INSTANCE was null after creation.")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun populateDatabase(
) {
}
