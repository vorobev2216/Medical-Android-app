package com.example.secure

import android.provider.ContactsContract.CommonDataKinds.Photo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthCredential

class DataViewModel: ViewModel() {
    /////////// Google stats
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userNumber = MutableLiveData<String>()
    val userNumber: LiveData<String> get() = _userNumber

    private val _userPhoto = MutableLiveData<String>()
    val userPhoto: LiveData<String> get() = _userPhoto

    fun setUserName(name: String){
        _userName.value = name
    }

    fun setUserEmail(email: String){
        _userEmail.value = email
    }

    fun setUserNumber(number: String){
        _userNumber.value = number
    }

    fun setUserPhoto(photo: String){
        _userPhoto.value = photo
    }

    ///////////


    /////////// Drug Data

    private val _drugName = MutableLiveData<String>()
    val drugName: LiveData<String> get() = _drugName

    fun setDrugName(drugName: String){
        _drugName.value = drugName
    }

}