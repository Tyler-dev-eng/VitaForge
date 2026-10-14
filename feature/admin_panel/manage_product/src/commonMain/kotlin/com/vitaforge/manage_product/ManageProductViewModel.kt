package com.vitaforge.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.Flavour
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import com.vitaforge.manage_product.usecase.AddFlavourUseCase
import com.vitaforge.manage_product.usecase.ObserveFlavoursUseCase
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.Uuid

private const val ADD_FLAVOUR_ERROR = "Could not add flavour."

data class ManageProductState(
    val id: String = Uuid.random().toHexString(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "thumbnail image",
    val category: ProductCategory = ProductCategory.Protein,
    val selectedFlavours: List<String> = emptyList(),
    val weight: Int? = null,
    val price: Double = 0.0,
    val isNew: Boolean = false,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false,
)

class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    private val observeFlavoursUseCase: ObserveFlavoursUseCase,
    private val addFlavourUseCase: AddFlavourUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val productId = savedStateHandle.get<String>("id") ?: ""

    var screenState by mutableStateOf(ManageProductState())
        private set

    var thumbnailUploaderState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    var availableFlavours: RequestState<List<Flavour>> by mutableStateOf(RequestState.Loading)
        private set

    val isFormValid: Boolean
        get() =
            screenState.title.isNotEmpty() &&
                screenState.description.isNotEmpty() &&
                screenState.thumbnail.isNotEmpty() &&
                screenState.price != 0.0

    init {
        observeAvailableFlavours()
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                val selectedProduct = adminRepository.readProductById(id)
                if (selectedProduct.isSuccess()) {
                    val product = selectedProduct.getSuccessData()
                    updateId(product.id)
                    updateCreatedAt(product.createdAt)
                    updateTitle(product.title)
                    updateDescription(product.description)
                    updateThumbnail(product.thumbnail)
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                    updateCategory(ProductCategory.valueOf(product.category))
                    updateSelectedFlavours(product.flavours.orEmpty().map { it.trim() }.filter { it.isNotEmpty() })
                    updateWeight(product.weight)
                    updatePrice(product.price)
                    updateNew(product.isNew)
                    updatePopular(product.isPopular)
                    updateDiscounted(product.isDiscounted)
                }
            }
        }
    }

    fun updateId(value: String) {
        screenState = screenState.copy(id = value)
    }

    fun updateCreatedAt(value: Long) {
        screenState = screenState.copy(createdAt = value)
    }

    fun updateTitle(value: String) {
        screenState = screenState.copy(title = value)
    }

    fun updateDescription(value: String) {
        screenState = screenState.copy(description = value)
    }

    fun updateThumbnail(value: String) {
        screenState = screenState.copy(thumbnail = value)
    }

    fun updateThumbnailUploaderState(value: RequestState<Unit>) {
        thumbnailUploaderState = value
    }

    fun updateCategory(value: ProductCategory) {
        screenState = screenState.copy(category = value)
    }

    fun updateSelectedFlavours(value: List<String>) {
        screenState = screenState.copy(selectedFlavours = value)
    }

    fun toggleFlavour(name: String) {
        val current = screenState.selectedFlavours
        val isSelected = current.any { it.equals(name, ignoreCase = true) }
        updateSelectedFlavours(
            if (isSelected) current.filterNot { it.equals(name, ignoreCase = true) } else current + name,
        )
    }

    fun addFlavour(
        name: String,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            addFlavourUseCase(name)
                .onSuccess { flavour -> updateSelectedFlavours(screenState.selectedFlavours + flavour.name) }
                .onFailure { error ->
                    onError(
                        if (error is IllegalArgumentException) error.message ?: ADD_FLAVOUR_ERROR else ADD_FLAVOUR_ERROR,
                    )
                }
        }
    }

    private fun observeAvailableFlavours() {
        viewModelScope.launch {
            observeFlavoursUseCase().collect { availableFlavours = it }
        }
    }

    fun updateWeight(value: Int?) {
        screenState = screenState.copy(weight = value)
    }

    fun updatePrice(value: Double) {
        screenState = screenState.copy(price = value)
    }

    fun updateNew(value: Boolean) {
        screenState = screenState.copy(isNew = value)
    }

    fun updatePopular(value: Boolean) {
        screenState = screenState.copy(isPopular = value)
    }

    fun updateDiscounted(value: Boolean) {
        screenState = screenState.copy(isDiscounted = value)
    }

    fun createNewProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            adminRepository.createNewProduct(
                product =
                    Product(
                        id = screenState.id,
                        title = screenState.title,
                        description = screenState.description,
                        thumbnail = screenState.thumbnail,
                        category = screenState.category.name,
                        flavours = screenState.selectedFlavours,
                        weight = screenState.weight,
                        price = screenState.price,
                        isNew = screenState.isNew,
                        isPopular = screenState.isPopular,
                        isDiscounted = screenState.isDiscounted,
                    ),
                onSuccess = onSuccess,
                onError = onError,
            )
        }
    }

    fun uploadThumbnailToStorage(
        file: File?,
        onSuccess: () -> Unit,
    ) {
        if (file == null) {
            updateThumbnailUploaderState(RequestState.Error("No image selected"))
            return
        }

        updateThumbnailUploaderState(RequestState.Loading)

        viewModelScope.launch {
            try {
                val downloadUrl = adminRepository.uploadImageToStorage(file)
                if (downloadUrl.isNullOrEmpty()) {
                    throw Exception("Failed to retrieve a download URL for the image after upload.")
                }

                productId.takeIf { it.isNotEmpty() }?.let { id ->
                    adminRepository.updateProductThumbnail(
                        productId = id,
                        downloadUrl = downloadUrl,
                        onError = { message ->
                            updateThumbnailUploaderState(RequestState.Error(message))
                        },
                        onSuccess = {
                            onSuccess()
                            updateThumbnailUploaderState(RequestState.Success(Unit))
                            updateThumbnail(downloadUrl)
                        },
                    )
                } ?: run {
                    onSuccess()
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                    updateThumbnail(downloadUrl)
                }
            } catch (e: Exception) {
                updateThumbnailUploaderState(RequestState.Error("Error while uploading thumbnail: ${e.message}"))
            }
        }
    }

    fun updateProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (isFormValid) {
            viewModelScope.launch {
                adminRepository.updateProduct(
                    product =
                        Product(
                            id = screenState.id,
                            createdAt = screenState.createdAt,
                            title = screenState.title,
                            description = screenState.description,
                            thumbnail = screenState.thumbnail,
                            category = screenState.category.name,
                            flavours = screenState.selectedFlavours,
                            weight = screenState.weight,
                            price = screenState.price,
                            isNew = screenState.isNew,
                            isPopular = screenState.isPopular,
                            isDiscounted = screenState.isDiscounted,
                        ),
                    onSuccess = onSuccess,
                    onError = onError,
                )
            }
        } else {
            onError("Form is not valid.")
        }
    }

    fun deleteThumbnailFromStorage(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            adminRepository.deleteImageFromStorage(
                downloadUrl = screenState.thumbnail,
                onSuccess = {
                    productId.takeIf { it.isNotEmpty() }?.let { id ->
                        viewModelScope.launch {
                            adminRepository.updateProductThumbnail(
                                productId = id,
                                downloadUrl = "",
                                onError = { message ->
                                    updateThumbnailUploaderState(RequestState.Error(message))
                                },
                                onSuccess = {
                                    updateThumbnail("")
                                    updateThumbnailUploaderState(RequestState.Idle)
                                    onSuccess()
                                },
                            )
                        }
                    } ?: run {
                        updateThumbnail("")
                        updateThumbnailUploaderState(RequestState.Idle)
                        onSuccess()
                    }
                },
                onError = onError,
            )
        }
    }

    fun deleteProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                adminRepository.deleteProduct(
                    productId = id,
                    onSuccess = {
                        deleteThumbnailFromStorage(
                            onSuccess = {},
                            onError = { },
                        )
                        onSuccess()
                    },
                    onError = { message -> onError(message) },
                )
            }
        }
    }
}
