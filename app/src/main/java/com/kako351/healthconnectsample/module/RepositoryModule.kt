package com.kako351.healthconnectsample.module

import com.kako351.healthconnectsample.data.HealthConnectRepository
import com.kako351.healthconnectsample.data.HealthConnectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindHealthConnectRepository(impl: HealthConnectRepositoryImpl): HealthConnectRepository
}