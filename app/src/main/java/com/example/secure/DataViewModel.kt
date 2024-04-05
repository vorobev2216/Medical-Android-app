package com.example.secure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class DataViewModel(private val repository: DrudItemRepository) : ViewModel() {
    /////////// Google stats
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userNumber = MutableLiveData<String>()
    val userNumber: LiveData<String> get() = _userNumber

    private val _userPhoto = MutableLiveData<String>()
    val userPhoto: LiveData<String> get() = _userPhoto

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    fun setUserNumber(number: String) {
        _userNumber.value = number
    }

    fun setUserPhoto(photo: String) {
        _userPhoto.value = photo
    }

    /////////// Google stats


    /////////// Drug Data

    private val _drugName = MutableLiveData<String>()
    val drugName: LiveData<String> get() = _drugName

    fun setDrugName(drugName: String) {
        _drugName.value = drugName
    }

    var drugItems: LiveData<List<DrugItem>> = repository!!.allDrugItems.asLiveData()

    fun addDrugItem(newDrug: DrugItem) = viewModelScope.launch {
        repository?.insertDrugItem(newDrug)
    }

    fun updateDrugItem(drugItem: DrugItem) = viewModelScope.launch {
        repository?.updateDrugItem(drugItem)
    }

    fun setCompleted(drugItem: DrugItem) = viewModelScope.launch {
        if(!drugItem.isCompleted()){
            drugItem.completedDateString = DrugItem.dateFormatter.format(LocalDate.now())
        }
        repository?.updateDrugItem(drugItem)
    }


    /////////// Drug Data

    /////////// Health Rating Data

    private val _ratingHealth = MutableLiveData<MutableList<Int>>(mutableListOf())
    val ratingHealth: LiveData<MutableList<Int>> get() = _ratingHealth
    fun setRating(rating: Int){
        _ratingHealth.value?.apply {
            add(rating)
            _ratingHealth.value = this
        }
    }

    /////////// Health Rating Data


}

class DrugItemModelFactory(private val repository: DrudItemRepository) : ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(DataViewModel::class.java))
            return DataViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}