package com.kako351.healthconnectsample

import androidx.activity.ComponentActivity
import com.kako351.healthconnectsample.data.HealthConnectRepository
import com.kako351.healthconnectsample.module.RepositoryModule
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest{

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var repository: HealthConnectRepository


    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test() {
        Assert.assertNotNull(viewModel.healthConnectRepository)
    }
}