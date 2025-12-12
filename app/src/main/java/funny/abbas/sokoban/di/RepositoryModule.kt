package funny.abbas.sokoban.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import funny.abbas.sokoban.data.repository.CustomLevelRepository
import funny.abbas.sokoban.data.repository.CustomLevelRepositoryImpl
import funny.abbas.sokoban.data.repository.StandardLevelRepository
import funny.abbas.sokoban.data.repository.StandardLevelRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindStandardRepository(repository: StandardLevelRepositoryImpl): StandardLevelRepository

    @Binds
    abstract fun bindCustomRepository(repository: CustomLevelRepositoryImpl): CustomLevelRepository

}