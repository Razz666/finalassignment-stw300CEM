package com.rajkumarmagar.locationbasedanythingfinder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rajkumarmagar.locationbasedanythingfinder.dao.PostDAO
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post

@Database(
    entities = [(Post::class)],
    version = 2,
    exportSchema = false
)
abstract class PostDB: RoomDatabase() {
    abstract fun getUserDAO(): PostDAO

    companion object{
        @Volatile
        private var instance: PostDB? = null

        fun getInstance(context: Context): PostDB {
            if(instance == null) {
                synchronized(PostDB::class) {
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PostDB::class.java,
                "PostDB"
            ).build()
    }
}