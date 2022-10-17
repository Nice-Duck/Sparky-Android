package com.softsquared.niceduck.android.sparky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softsquared.niceduck.android.sparky.model.ScrapDataModel
import com.softsquared.niceduck.android.sparky.model.Tag
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.main.fragment.MyScrapRecyclerviewAdapter2
import com.softsquared.niceduck.android.sparky.view.main.fragment.OthersScrapRecyclerviewAdapter
import com.softsquared.niceduck.android.sparky.view.scrap.ItemEvent

class MainViewModel(): ViewModel(), ItemEvent {
    private lateinit var othersScrapRecyclerviewAdapter: OthersScrapRecyclerviewAdapter

    private val _othersScrapDataSet: MutableLiveData<ArrayList<ScrapDataModel>> = MutableLiveData()
    private val othersScrapDataSet: LiveData<ArrayList<ScrapDataModel>>
        get() = _othersScrapDataSet

    private lateinit var myScrapRecyclerviewAdapter: MyScrapRecyclerviewAdapter
    private lateinit var myScrapRecyclerviewAdapter2: MyScrapRecyclerviewAdapter2

    private val _myScrapDataSet: MutableLiveData<ArrayList<ScrapDataModel>> = MutableLiveData()
    private val  myScrapDataSet: LiveData<ArrayList<ScrapDataModel>>
        get() = _myScrapDataSet


    init {

        // 모델-레포를 통해 데이터 받아오기

        val tags = listOf(Tag("#BEBDBD", "디자인", 0),
            Tag("#BEBDBD", "IT", 1),
            Tag("#BEBDBD", "안드로이드", 2),
            Tag("#BEBDBD", "디자인", 3))

        val scrapDataModels = arrayListOf(ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ),
            ScrapDataModel(null, "바야흐로 개인화(Personalization)의 시대가 도래했습니다.사람은 누구나 자기만의 고유한", "www.naver.com", tags, "나만을 위한 취향 맞춤형 UX나만을 위한 취향 맞춤형 UX"  ))

        setOthersScrapAdapter(OthersScrapRecyclerviewAdapter(this))
        setOthersScrapDataSet(scrapDataModels)

        setMyScrapAdapter(MyScrapRecyclerviewAdapter(this))
        setMyScrapAdapter2(MyScrapRecyclerviewAdapter2(this))
        setMyScrapDataSet(scrapDataModels)
    }

    private fun setOthersScrapDataSet(newList : List<ScrapDataModel>) {
        this._othersScrapDataSet.value = newList as ArrayList<ScrapDataModel>
        othersScrapRecyclerviewAdapter.submitList(newList)
    }

    fun getOthersScrapData() = othersScrapDataSet

    fun getOthersScrapAdapter() = othersScrapRecyclerviewAdapter

    private fun setOthersScrapAdapter(customAdapter : OthersScrapRecyclerviewAdapter) {
        this.othersScrapRecyclerviewAdapter = customAdapter
    }




    private fun setMyScrapDataSet(newList : List<ScrapDataModel>) {
        this._myScrapDataSet.value = newList as ArrayList<ScrapDataModel>
        myScrapRecyclerviewAdapter.submitList(newList)
    }

    fun getMyScrapData() = myScrapDataSet

    fun getMyScrapAdapter() = myScrapRecyclerviewAdapter

    private fun setMyScrapAdapter(customAdapter : MyScrapRecyclerviewAdapter) {
        this.myScrapRecyclerviewAdapter = customAdapter
    }

    fun getMyScrapAdapter2() = myScrapRecyclerviewAdapter2

    private fun setMyScrapAdapter2(customAdapter : MyScrapRecyclerviewAdapter2) {
        this.myScrapRecyclerviewAdapter2 = customAdapter
    }



    override fun removeItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun addItem() {
        TODO("Not yet implemented")
    }

    override fun selectItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun getScrapTemplateDataSet(): ArrayList<Tag>? {
        TODO("Not yet implemented")
    }
}