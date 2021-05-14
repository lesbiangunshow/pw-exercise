package com.abbisea.pwexercise

import android.content.Context
import androidx.room.Room
import com.abbisea.pwexercise.data.AppDatabase
import com.abbisea.pwexercise.data.InspectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideInspectionDao(appDatabase: AppDatabase): InspectionDao =
        appDatabase.inspectionDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inspection_db"
        ).build()
}