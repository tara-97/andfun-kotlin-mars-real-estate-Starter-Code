/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

private const val TAG = "OverviewViewModel"
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    enum class MarsApiStatus{LOADING,DONE,ERROR}
    // The internal MutableLiveData String that stores the status of the most recent request
    private var _status = MutableLiveData<MarsApiStatus>()
    val status:LiveData<MarsApiStatus>
    get() = _status

    // The external immutable LiveData for the request status String
    private var _properties = MutableLiveData<List<MarsProperty>>()
    val properties :LiveData<List<MarsProperty>>
        get() = _properties

    private var _navigateToDetailFragment = MutableLiveData<MarsProperty>()
    val navigateToDetailFragment:LiveData<MarsProperty>
    get() = _navigateToDetailFragment

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        Log.d(TAG, "getMarsRealEstateProperties: Called")
        viewModelScope.launch {
            try{
                _status.value = MarsApiStatus.LOADING
                var resultList = MarsApi.retrofitService.getProperties()
                _status.value = MarsApiStatus.DONE
                if(resultList.size > 0){
                    _properties.value = resultList
                }
            }catch (e:Exception){
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }

    }
    fun displayPropertyDetails(marsProperty: MarsProperty){
        _navigateToDetailFragment.value = marsProperty
    }
    fun displayPropertyDetailsDone(){
        _navigateToDetailFragment.value = null
    }
}
