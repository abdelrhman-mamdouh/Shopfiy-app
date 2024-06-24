import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.ui.search.SearchViewModel
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue

import kotlinx.coroutines.test.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4



@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val mainCroutineRule = MainCoroutineRule()


    private lateinit var viewModel: SearchViewModel
    private lateinit var remoteDataSource: ShopifyRemoteDataSource
    // Arrange: Setup necessary conditions and state
    @Before
    fun setUp() {

        remoteDataSource = FakeShopifyRemoteDataSource()
        viewModel = SearchViewModel(remoteDataSource)
    }



    @Test
    fun `getProducts should update products with non-empty list`() = runBlockingTest {



        // Act: Execute the method to be tested
        viewModel.getProducts()

        // Assert: Verify the result
        val products = viewModel.products.value
        assertNotNull( "Products should not be null", products)
        assertTrue("Products list should not be empty",products.isNotEmpty())
    }
}
