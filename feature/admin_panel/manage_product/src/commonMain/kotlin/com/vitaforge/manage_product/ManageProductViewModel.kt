package com.vitaforge.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.ImageFile
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.domain.ProductSpec
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import com.vitaforge.data.domain.errorMessage
import com.vitaforge.manage_product.usecase.AddColourUseCase
import com.vitaforge.manage_product.usecase.AddFlavourUseCase
import com.vitaforge.manage_product.usecase.ObserveColoursUseCase
import com.vitaforge.manage_product.usecase.ObserveFlavoursUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.Uuid

private const val ADD_FLAVOUR_ERROR = "Could not add flavour."
private const val ADD_COLOUR_ERROR = "Could not add colour."
private const val INVALID_FORM_ERROR = "Please fill in the highlighted fields."
private const val NO_IMAGE_ERROR = "No image selected"

/** The flavour or colour a picked photo belongs to. */
data class OptionImageTarget(
    val name: String,
    val isColour: Boolean,
)

data class ManageProductState(
    val id: String = Uuid.random().toHexString(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "",
    val category: ProductCategory = ProductCategory.Protein,
    val selectedFlavours: List<String> = emptyList(),
    val flavourImages: Map<String, String> = emptyMap(),
    val selectedColours: List<String> = emptyList(),
    val colourImages: Map<String, String> = emptyMap(),
    val weight: Int? = null,
    val specsServing: String = "",
    val specs: List<ProductSpec> = emptyList(),
    val price: Double = 0.0,
    val oldPrice: Double? = null,
    val isNew: Boolean = false,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false,
) {
    /** Every flavour and colour image uploaded on the form. */
    val optionImageUrls: Set<String>
        get() = (flavourImages.values + colourImages.values).toSet()
}

class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    private val observeFlavoursUseCase: ObserveFlavoursUseCase,
    private val addFlavourUseCase: AddFlavourUseCase,
    private val observeColoursUseCase: ObserveColoursUseCase,
    private val addColourUseCase: AddColourUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val productId = savedStateHandle.get<String>("id") ?: ""

    var screenState by mutableStateOf(ManageProductState())
        private set

    var thumbnailUploaderState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    var availableFlavours: RequestState<List<String>> by mutableStateOf(RequestState.Loading)
        private set

    var availableColours: RequestState<List<String>> by mutableStateOf(RequestState.Loading)
        private set

    var isSaving by mutableStateOf(false)
        private set

    var uploadingImage: OptionImageTarget? by mutableStateOf(null)
        private set

    /** Which option the next picked photo belongs to; null means the product thumbnail. */
    private var imageTarget: OptionImageTarget? = null

    var isValidationVisible by mutableStateOf(false)
        private set

    private var savedState = screenState

    val hasUnsavedChanges: Boolean
        get() = screenState != savedState

    val isTitleInvalid: Boolean
        get() = isValidationVisible && screenState.title.isBlank()

    val isDescriptionInvalid: Boolean
        get() = isValidationVisible && screenState.description.isBlank()

    val isPriceInvalid: Boolean
        get() = isValidationVisible && screenState.price == 0.0

    val isThumbnailInvalid: Boolean
        get() = isValidationVisible && !hasProductImage()

    val isOldPriceInvalid: Boolean
        get() = isValidationVisible && isOldPriceMissing()

    /** A product needs a thumbnail, or at least one flavour or colour image that can stand in for it. */
    private fun hasProductImage(): Boolean =
        screenState.thumbnail.isNotEmpty() || productFlavourImages() != null || productColourImages() != null

    private fun isOldPriceMissing(): Boolean {
        val oldPrice = screenState.oldPrice
        return screenState.isDiscounted && (oldPrice == null || oldPrice <= screenState.price)
    }

    val isFormValid: Boolean
        get() =
            !isOldPriceMissing() &&
                screenState.title.isNotBlank() &&
                screenState.description.isNotBlank() &&
                hasProductImage() &&
                screenState.price != 0.0

    init {
        observeAvailableFlavours()
        observeAvailableColours()
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                adminRepository.readProductById(id).onSuccess { product -> loadProduct(product) }
            }
        }
    }

    private fun loadProduct(product: Product) {
        updateId(product.id)
        updateCreatedAt(product.createdAt)
        updateTitle(product.title)
        screenState = screenState.copy(description = product.description)
        updateThumbnail(product.thumbnail)
        if (product.thumbnail.isNotEmpty()) updateThumbnailUploaderState(RequestState.Success(Unit))
        updateCategory(ProductCategory.valueOf(product.category))
        updateSelectedFlavours(
            product.flavours
                .orEmpty()
                .map { it.trim() }
                .filter { it.isNotEmpty() },
        )
        screenState =
            screenState.copy(selectedColours = product.colours.orEmpty(), colourImages = product.colourImages.orEmpty())
        updateWeight(product.weight)
        screenState = screenState.copy(flavourImages = product.flavourImages.orEmpty())
        screenState =
            screenState.copy(specsServing = product.specsServing.orEmpty(), specs = product.specs.orEmpty())
        updatePrice(product.price)
        updateOldPrice(product.oldPrice)
        updateNew(product.isNew)
        updatePopular(product.isPopular)
        updateDiscounted(product.isDiscounted)
        savedState = screenState
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
        screenState = screenState.copy(description = formatBullets(screenState.description, value))
    }

    fun addDescriptionBullet() {
        screenState = screenState.copy(description = appendBullet(screenState.description))
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
        updateSelectedFlavours(screenState.selectedFlavours.toggle(name))
    }

    fun toggleColour(name: String) {
        screenState = screenState.copy(selectedColours = screenState.selectedColours.toggle(name))
    }

    private fun List<String>.toggle(name: String): List<String> {
        val isSelected = any { it.equals(name, ignoreCase = true) }
        return if (isSelected) filterNot { it.equals(name, ignoreCase = true) } else this + name
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

    fun addColour(
        name: String,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            addColourUseCase(name)
                .onSuccess { colour ->
                    screenState = screenState.copy(selectedColours = screenState.selectedColours + colour.name)
                }.onFailure { error ->
                    onError(
                        if (error is IllegalArgumentException) error.message ?: ADD_COLOUR_ERROR else ADD_COLOUR_ERROR,
                    )
                }
        }
    }

    fun setImageTarget(target: OptionImageTarget?) {
        imageTarget = target
    }

    fun onPhotoSelected(
        file: ImageFile?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val target = imageTarget
        imageTarget = null
        if (target == null) uploadThumbnailToStorage(file, onSuccess) else uploadOptionImage(target, file, onSuccess, onError)
    }

    private fun uploadOptionImage(
        target: OptionImageTarget,
        file: ImageFile?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (file == null) {
            onError(NO_IMAGE_ERROR)
            return
        }
        viewModelScope.launch {
            uploadingImage = target
            val result = adminRepository.uploadImageToStorage(file)
            uploadingImage = null
            result.fold(
                onSuccess = { url ->
                    setOptionImage(target, url)
                    onSuccess()
                },
                onFailure = { onError(result.errorMessage()) },
            )
        }
    }

    fun removeFlavourImage(flavour: String) {
        setOptionImage(OptionImageTarget(name = flavour, isColour = false), url = null)
    }

    fun removeColourImage(colour: String) {
        setOptionImage(OptionImageTarget(name = colour, isColour = true), url = null)
    }

    /** Sets or, when [url] is null, removes the image of [target]. */
    private fun setOptionImage(
        target: OptionImageTarget,
        url: String?,
    ) {
        val images = if (target.isColour) screenState.colourImages else screenState.flavourImages
        discardUnsavedImage(images[target.name])
        val updated = if (url == null) images - target.name else images + (target.name to url)
        screenState =
            if (target.isColour) screenState.copy(colourImages = updated) else screenState.copy(flavourImages = updated)
    }

    /** Deletes an uploaded image straight away when it was never saved on the product, so it isn't orphaned. */
    private fun discardUnsavedImage(url: String?) {
        if (url == null || url in savedState.optionImageUrls) return
        viewModelScope.launch { adminRepository.deleteImageFromStorage(downloadUrl = url) }
    }

    private fun productFlavourImages(): Map<String, String>? = screenState.flavourImages.keptFor(productFlavours())

    private fun productColourImages(): Map<String, String>? = screenState.colourImages.keptFor(productColours().orEmpty())

    /** The images whose option is still on the product. */
    private fun Map<String, String>.keptFor(options: List<String>): Map<String, String>? =
        filter { (name, url) -> url.isNotBlank() && options.any { it.equals(name, ignoreCase = true) } }
            .takeIf { it.isNotEmpty() }

    private fun productFlavours(): List<String> =
        if (screenState.category == ProductCategory.Accessories) emptyList() else screenState.selectedFlavours

    private fun productColours(): List<String>? = screenState.selectedColours.takeIf { screenState.category == ProductCategory.Accessories }

    private fun observeAvailableFlavours() {
        viewModelScope.launch {
            observeFlavoursUseCase().toNames { it.name }.collect { availableFlavours = it }
        }
    }

    private fun observeAvailableColours() {
        viewModelScope.launch {
            observeColoursUseCase().toNames { it.name }.collect { availableColours = it }
        }
    }

    private fun <T> Flow<RequestState<List<T>>>.toNames(nameOf: (T) -> String): Flow<RequestState<List<String>>> =
        map { state ->
            when (state) {
                is RequestState.Success -> RequestState.Success(state.data.map(nameOf))
                is RequestState.Error -> state
                RequestState.Loading -> RequestState.Loading
                RequestState.Idle -> RequestState.Idle
            }
        }

    fun updateWeight(value: Int?) {
        screenState = screenState.copy(weight = value)
    }

    fun updateSpecsServing(value: String) {
        screenState = screenState.copy(specsServing = value)
    }

    fun addSpec() {
        screenState = screenState.copy(specs = screenState.specs + ProductSpec(label = "", value = ""))
    }

    fun updateSpec(
        index: Int,
        spec: ProductSpec,
    ) {
        screenState = screenState.copy(specs = screenState.specs.mapIndexed { i, old -> if (i == index) spec else old })
    }

    fun removeSpec(index: Int) {
        screenState = screenState.copy(specs = screenState.specs.filterIndexed { i, _ -> i != index })
    }

    private fun productSpecs(): List<ProductSpec>? =
        screenState.specs
            .filter { it.label.isNotBlank() && it.value.isNotBlank() }
            .takeIf { it.isNotEmpty() }

    private fun productSpecsServing(): String? = screenState.specsServing.takeIf { it.isNotBlank() && productSpecs() != null }

    fun updatePrice(value: Double) {
        screenState = screenState.copy(price = value)
    }

    fun updateOldPrice(value: Double?) {
        screenState = screenState.copy(oldPrice = value)
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
        if (!canSubmit(onError)) return
        viewModelScope.launch {
            isSaving = true
            val result =
                adminRepository.createNewProduct(
                    product =
                        Product(
                            id = screenState.id,
                            title = screenState.title,
                            description = screenState.description,
                            thumbnail = screenState.thumbnail,
                            category = screenState.category.name,
                            flavours = productFlavours(),
                            flavourImages = productFlavourImages(),
                            colours = productColours(),
                            colourImages = productColourImages(),
                            weight = screenState.weight,
                            specsServing = productSpecsServing(),
                            specs = productSpecs(),
                            price = screenState.price,
                            oldPrice = screenState.oldPrice.takeIf { screenState.isDiscounted },
                            isNew = screenState.isNew,
                            isPopular = screenState.isPopular,
                            isDiscounted = screenState.isDiscounted,
                        ),
                )
            isSaving = false
            result.fold(onSuccess = { onSaved(onSuccess) }, onFailure = { onError(result.errorMessage()) })
        }
    }

    private fun canSubmit(onError: (String) -> Unit): Boolean {
        if (isSaving) return false
        isValidationVisible = true
        if (!isFormValid) onError(INVALID_FORM_ERROR)
        return isFormValid
    }

    private suspend fun onSaved(onSuccess: () -> Unit) {
        val keptState =
            screenState.copy(
                flavourImages = productFlavourImages().orEmpty(),
                colourImages = productColourImages().orEmpty(),
            )
        val staleImages = savedState.optionImageUrls + screenState.optionImageUrls - keptState.optionImageUrls
        staleImages.forEach { adminRepository.deleteImageFromStorage(downloadUrl = it) }
        screenState = keptState
        savedState = screenState
        onSuccess()
    }

    fun uploadThumbnailToStorage(
        file: ImageFile?,
        onSuccess: () -> Unit,
    ) {
        if (file == null) {
            updateThumbnailUploaderState(RequestState.Error("No image selected"))
            return
        }

        updateThumbnailUploaderState(RequestState.Loading)

        viewModelScope.launch {
            val uploadResult = adminRepository.uploadImageToStorage(file)
            val downloadUrl = uploadResult.getOrNull()
            if (downloadUrl == null) {
                updateThumbnailUploaderState(RequestState.Error(uploadResult.errorMessage()))
                return@launch
            }
            persistThumbnail(downloadUrl).fold(
                onSuccess = {
                    onSuccess()
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                    updateThumbnail(downloadUrl)
                    markThumbnailSaved()
                },
                onFailure = { error ->
                    updateThumbnailUploaderState(RequestState.Error(error.message.orEmpty()))
                },
            )
        }
    }

    private fun markThumbnailSaved() {
        if (productId.isNotEmpty()) savedState = savedState.copy(thumbnail = screenState.thumbnail)
    }

    /** Saves [downloadUrl] on the stored product; a product that is not saved yet has nothing to update. */
    private suspend fun persistThumbnail(downloadUrl: String): Result<Unit> =
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            adminRepository.updateProductThumbnail(productId = id, downloadUrl = downloadUrl)
        } ?: Result.success(Unit)

    fun updateProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (!canSubmit(onError)) return
        viewModelScope.launch {
            isSaving = true
            val result =
                adminRepository.updateProduct(
                    product =
                        Product(
                            id = screenState.id,
                            createdAt = screenState.createdAt,
                            title = screenState.title,
                            description = screenState.description,
                            thumbnail = screenState.thumbnail,
                            category = screenState.category.name,
                            flavours = productFlavours(),
                            flavourImages = productFlavourImages(),
                            colours = productColours(),
                            colourImages = productColourImages(),
                            weight = screenState.weight,
                            specsServing = productSpecsServing(),
                            specs = productSpecs(),
                            price = screenState.price,
                            oldPrice = screenState.oldPrice.takeIf { screenState.isDiscounted },
                            isNew = screenState.isNew,
                            isPopular = screenState.isPopular,
                            isDiscounted = screenState.isDiscounted,
                        ),
                )
            isSaving = false
            result.fold(onSuccess = { onSaved(onSuccess) }, onFailure = { onError(result.errorMessage()) })
        }
    }

    fun deleteThumbnailFromStorage(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val deleteResult = adminRepository.deleteImageFromStorage(downloadUrl = screenState.thumbnail)
            if (deleteResult.isFailure) {
                onError(deleteResult.errorMessage())
                return@launch
            }
            persistThumbnail(downloadUrl = "").fold(
                onSuccess = {
                    updateThumbnail("")
                    markThumbnailSaved()
                    updateThumbnailUploaderState(RequestState.Idle)
                    onSuccess()
                },
                onFailure = { error ->
                    updateThumbnailUploaderState(RequestState.Error(error.message.orEmpty()))
                },
            )
        }
    }

    fun deleteProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                val result = adminRepository.deleteProduct(productId = id)
                result.fold(
                    onSuccess = {
                        deleteThumbnailFromStorage(onSuccess = {}, onError = {})
                        screenState.optionImageUrls.forEach { adminRepository.deleteImageFromStorage(downloadUrl = it) }
                        onSuccess()
                    },
                    onFailure = { onError(result.errorMessage()) },
                )
            }
        }
    }
}
