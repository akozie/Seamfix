package com.seamfix.seamfix.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seamfix.seamfix.data.remote.apiResponses.SOSResponse
import com.seamfix.seamfix.data.remote.dto.SOSRequestDTO
import com.seamfix.seamfix.domain.repository.Repository
import com.seamfix.seamfix.presentation.viewState.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeamfixViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _sosResponse: MutableLiveData<Resource<SOSResponse?>> = MutableLiveData()
    val sosResponse: LiveData<Resource<SOSResponse?>> get() = _sosResponse


    /**
     * sends SOS request
     * */
    fun sendSOSRequest(sosRequestDTO: SOSRequestDTO) {
        viewModelScope.launch {
            _sosResponse.value = repository.sendSOSRequest(sosRequestDTO)
        }
    }


}