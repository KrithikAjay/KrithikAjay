package com.krithik.notes.viewmodel


import android.util.Patterns
import androidx.lifecycle.*
import com.krithik.notes.database.User
import com.krithik.notes.database.UserRepository
import kotlinx.coroutines.launch
import java.net.UnknownServiceException

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var userToUpdateOrDelete:User

     val inputName = MutableLiveData<String>()

     val inputEmail = MutableLiveData<String>()

     val saveOrUpdateButtonText = MutableLiveData<String>()

     val clearAllOrDeleteButtonText = MutableLiveData<String>()

    val userList : LiveData<List<User>> = repository.users

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"

    }

    fun initUpdateAndDelete(user: User) {
        inputName.value = user.name
        inputEmail.value = user.email
        isUpdateOrDelete = true
        userToUpdateOrDelete = user
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }


    fun saveOrUpdate() {

        if (inputName.value == null) {
            statusMessage.value = Event("Please enter subscriber's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter subscriber's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a valid email address")
        } else {
            if (isUpdateOrDelete) {
               userToUpdateOrDelete.name = inputName.value!!
                userToUpdateOrDelete.email = inputEmail.value!!
                updateUser(userToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insertUsers(User(0, name, email))
                inputName.value = ""
                inputEmail.value = ""
            }
        }

    }
    private fun updateUser(user : User) = viewModelScope.launch {
        val noOfRows = repository.update(user)
        if (noOfRows > 0) {
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun insertUsers(user:User) = viewModelScope.launch {
        val newRowId = repository.insert(user)
        if (newRowId > -1) {
            statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("No item to Delete")
        }
    }
    private fun userInserted(){
        inputName.value = ""
        inputEmail.value = ""

    }

    private fun deleteUser(user : User) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(user)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteUser(userToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Subscribers Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }




}

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}