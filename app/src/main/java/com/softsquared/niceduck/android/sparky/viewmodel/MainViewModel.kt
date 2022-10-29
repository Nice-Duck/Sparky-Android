package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softsquared.niceduck.android.sparky.model.*
import com.softsquared.niceduck.android.sparky.utill.MutableSingleLiveData
import com.softsquared.niceduck.android.sparky.utill.SingleLiveData
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter3
import com.softsquared.niceduck.android.sparky.view.main.fragment.OthersScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel(), ItemEvent {
    private val mainRepository = MainRepository()

    private val _scrapLoadResponse: MutableLiveData<ScrapRoadResponse> = MutableLiveData()
    private val scrapLoadResponse: LiveData<ScrapRoadResponse>
        get() = _scrapLoadResponse

    private val _scrapLoadFailure = MutableLiveData<Int>()
    val scrapLoadFailure: LiveData<Int>
        get() = _scrapLoadFailure

    private lateinit var othersScrapRecyclerviewAdapter: OthersScrapRecyclerviewAdapter

    private val _othersScrapDataSet: MutableLiveData<ArrayList<Scrap>> = MutableLiveData()
    private val othersScrapDataSet: LiveData<ArrayList<Scrap>>
        get() = _othersScrapDataSet

    private lateinit var myScrapRecyclerviewAdapter: MyScrapRecyclerviewAdapter
    private lateinit var myScrapRecyclerviewAdapter2: MyScrapRecyclerviewAdapter2
    private lateinit var myScrapRecyclerviewAdapter3: MyScrapRecyclerviewAdapter3

    private val _myScrapDataSet: MutableLiveData<ArrayList<Scrap>> = MutableLiveData()
    private val myScrapDataSet: LiveData<ArrayList<Scrap>>
        get() = _myScrapDataSet

    private fun getScrapLoad(type: String? = null) {
        viewModelScope.launch {
            val response = mainRepository.getScrap(type)

            if (response.isSuccessful) {
                response.body()?.let {
                    setOthersScrapAdapter(OthersScrapRecyclerviewAdapter(this@MainViewModel))
                    it.result.recScraps?.let { scrap -> setOthersScrapDataSet(scrap) }

                    setMyScrapAdapter(MyScrapRecyclerviewAdapter(this@MainViewModel))
                    setMyScrapAdapter2(MyScrapRecyclerviewAdapter2(this@MainViewModel))
                    setMyScrapAdapter3(MyScrapRecyclerviewAdapter3(this@MainViewModel))
                    it.result.myScraps?.let { scrap -> setMyScrapDataSet(scrap) }

                }
            } else {
                _scrapLoadFailure.setValue(response.code())
            }
        }
    }


    init {

        // 모델-레포를 통해 데이터 받아오기
        getScrapLoad()
    }

    private fun setOthersScrapDataSet(newList: List<Scrap>) {
        this._othersScrapDataSet.value = newList as ArrayList<Scrap>
        othersScrapRecyclerviewAdapter.submitList(newList)
    }

    fun getOthersScrapData() = othersScrapDataSet

    fun getOthersScrapAdapter() = othersScrapRecyclerviewAdapter

    private fun setOthersScrapAdapter(customAdapter: OthersScrapRecyclerviewAdapter) {
        this.othersScrapRecyclerviewAdapter = customAdapter
    }

    private fun setMyScrapDataSet(newList: List<Scrap>) {
        this._myScrapDataSet.value = newList as ArrayList<Scrap>
        myScrapRecyclerviewAdapter.submitList(newList)
    }

    fun getMyScrapData() = myScrapDataSet

    fun getMyScrapAdapter() = myScrapRecyclerviewAdapter

    private fun setMyScrapAdapter(customAdapter: MyScrapRecyclerviewAdapter) {
        this.myScrapRecyclerviewAdapter = customAdapter
    }

    fun getMyScrapAdapter2() = myScrapRecyclerviewAdapter2

    private fun setMyScrapAdapter2(customAdapter: MyScrapRecyclerviewAdapter2) {
        this.myScrapRecyclerviewAdapter2 = customAdapter
    }

    fun getMyScrapAdapter3() = myScrapRecyclerviewAdapter3

    private fun setMyScrapAdapter3(customAdapter: MyScrapRecyclerviewAdapter3) {
        this.myScrapRecyclerviewAdapter3 = customAdapter
    }

    override fun removeItem(position: Int) {
        // 여기선 사용하지 않음
    }

    override fun addItem() {
        // 여기선 사용하지 않음
    }

    override fun selectItem(position: Int) {
       // 해당 아이템의 url로 이동
    }
}
