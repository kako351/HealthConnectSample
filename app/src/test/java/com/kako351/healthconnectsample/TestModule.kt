package com.kako351.healthconnectsample

import com.kako351.healthconnectsample.data.HealthConnectRepository
import com.kako351.healthconnectsample.data.HealthConnectRepositoryImpl
import com.kako351.healthconnectsample.module.RepositoryModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = arrayOf(SingletonComponent::class),
    replaces = arrayOf(RepositoryModule::class)
)
interface TestModule  {
    @Binds
    fun bindHealthConnectRepository(impl: TestHealthConnectRepositoryImpl): HealthConnectRepository
}