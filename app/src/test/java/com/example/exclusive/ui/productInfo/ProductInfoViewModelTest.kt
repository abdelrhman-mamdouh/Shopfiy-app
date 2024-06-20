package com.example.exclusive.ui.productInfo



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.local.FakeLocalDataSource
import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.Images
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.Variants
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.ui.productinfo.viewmodel.ProductInfoViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(JUnit4::class)
class ProductInfoViewModelTest {

    @get:Rule
    val mainCroutineRule = MainCoroutineRule()



    private lateinit var viewModel: ProductInfoViewModel


    private lateinit var remoteDataSource: ShopifyRemoteDataSource


    private lateinit var localDataSource: ILocalDataSource
    // Arrange: Setup necessary conditions and state
    @Before
    fun setUp() {

        localDataSource = FakeLocalDataSource()
        remoteDataSource = FakeShopifyRemoteDataSource()
        viewModel = ProductInfoViewModel(remoteDataSource, localDataSource)
    }


    @Test
    fun `addProductToRealtimeDatabase should set isWatchList to true`() = runBlockingTest {
        // Act: Execute the method to be tested
        val product = ProductNode("1", "test", "test", "test", "test", Images(emptyList()), Variants(
            emptyList()
        )
        )
        viewModel.addProductToRealtimeDatabase(product)
        // Assert: Verify the result
        val result = viewModel.isWatchList.value
        assertTrue(result)


    }

    @Test
    fun `removeProductFromRealtimeDatabase should set isWatchList to false`() = runBlockingTest {
        // Act: Execute the method to be tested
        val product = ProductNode("1", "test", "test", "test", "test", Images(emptyList()), Variants(
            emptyList()
        )
        )
        viewModel.removeProductFromWatchList(product.id)
        // Assert: Verify the result
        val result = viewModel.isWatchList.value
        assertTrue(!result)
    }
}
