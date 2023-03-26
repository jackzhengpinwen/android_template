package com.example.core_common.base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_common.bean.LoadAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel: ViewModel() {
    var loadActionLiveData: MutableLiveData<LoadAction> = MutableLiveData()

    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block
        }
    }

    suspend fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    /**
     * 设置并发射加载状态
     */
    fun setStateLiveData(loadState: LoadAction) {
        loadActionLiveData.postValue(loadState)
    }

    fun loadStartLoading() {
        loadActionLiveData.postValue(LoadAction(LoadAction.STATE_LOADING))
    }

    fun loadSuccess() {
        loadActionLiveData.postValue(LoadAction(LoadAction.STATE_SUCCESS))
    }

    fun loadError(message: String?) {
        loadActionLiveData.postValue(LoadAction(LoadAction.STATE_ERROR, message))
    }

    fun loadNoData() {
        loadActionLiveData.postValue(LoadAction(LoadAction.STATE_NO_DATA))
    }

    fun loadStartProgress() {
        loadActionLiveData.postValue(LoadAction(LoadAction.STATE_PROGRESS))
    }

    fun loadHideProgress() {
        loadActionLiveData.postValue(LoadAction(LoadAction.STATE_HIDE_PROGRESS))
    }

    fun getActionLiveData(): MutableLiveData<LoadAction> {
        return loadActionLiveData
    }
}