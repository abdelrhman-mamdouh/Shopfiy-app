package com.example.exclusive.ui.auth
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.local.FakeLocalDataSource
import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSourceImpl
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AuthViewModelTest {
    @get:Rule
    val mainCroutineRule = MainCoroutineRule()
    lateinit var viewModel: AuthViewModel
    lateinit var remoteDataSource: ShopifyRemoteDataSource
    lateinit var localDataSource: ILocalDataSource
    // Arrange: Setup necessary conditions and state
    @Before
    fun setUp() {
        localDataSource = FakeLocalDataSource()
        remoteDataSource = FakeShopifyRemoteDataSource()
        viewModel = AuthViewModel(remoteDataSource, localDataSource)
    }
    @Test
    fun `getToken should update token with non-empty string`() {
        // Act: Execute the method to be tested
        viewModel.getToken()
        // Assert: Verify the result
        val token = viewModel.tokenState.value
        assertNotNull("Token should not be null", token)
        assertTrue("Token should not be empty", token!!.isNotEmpty())
    }
    @Test
    fun `sendPasswordResetEmail should update sendPasswordState with true`() {
        // Act: Execute the method to be tested
        viewModel.sendPassword("test")
        // Assert: Verify the result
        val state = viewModel.sendPasswordState.value
        assertNotNull("State should not be null", state)

    }
}