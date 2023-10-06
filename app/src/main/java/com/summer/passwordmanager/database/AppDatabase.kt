package com.summer.passwordmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [TagEntity::class, VaultEntity::class, PassHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "pass_generator"

        operator fun invoke(context: Context, factory: SupportFactory) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context, factory
                    ).also {
                        instance = it
                    }
            }

        private fun buildDatabase(context: Context, factory: SupportFactory) =
            Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, DB_NAME
            )
                .openHelperFactory(factory)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        initTags()
                    }
                })
                .build()

        private fun initTags(){
            CoroutineScope(Dispatchers.IO).launch {
                instance?.let {
                    it.appDao().insertTagIgnore(
                        TagEntity(
                            id = AppUtils.generateXid(),
                            name = "Social Media",
                            createdAtApp = AppUtils.getCurrentTimeSecs(),
                            updatedAtApp = AppUtils.getCurrentTimeSecs()
                        )
                    )
                    it.appDao().insertTagIgnore(
                        TagEntity(
                            id = AppUtils.generateXid(),
                            name = "Banking",
                            createdAtApp = AppUtils.getCurrentTimeSecs(),
                            updatedAtApp = AppUtils.getCurrentTimeSecs()
                        )
                    )
                    it.appDao().insertTagIgnore(
                        TagEntity(
                            id = AppUtils.generateXid(),
                            name = "Credit/Debit Card",
                            createdAtApp = AppUtils.getCurrentTimeSecs(),
                            updatedAtApp = AppUtils.getCurrentTimeSecs()
                        )
                    )
                }
            }
        }
    }
}
