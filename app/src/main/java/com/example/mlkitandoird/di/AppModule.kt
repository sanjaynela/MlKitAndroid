package com.example.mlkitandoird.di

import com.example.mlkitandoird.data.repository.MLKitRepository
import com.example.mlkitandoird.data.repository.MLKitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindMLKitRepository(
        mlKitRepositoryImpl: MLKitRepositoryImpl
    ): MLKitRepository
}
