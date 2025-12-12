package funny.abbas.sokoban.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import funny.abbas.sokoban.data.database.AppDatabase
import funny.abbas.sokoban.data.database.dao.CustomLevelDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun providerAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providerCustomDao(database: AppDatabase): CustomLevelDao {
        return database.customLevelDao
    }
}