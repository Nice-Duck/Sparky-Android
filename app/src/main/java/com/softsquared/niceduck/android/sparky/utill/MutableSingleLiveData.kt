package com.softsquared.niceduck.android.sparky.utill

class MutableSingleLiveData<T> : SingleLiveData<T> {

    constructor() : super()

    constructor(value: T) : super(value)

    public override fun postValue(value: T) {
        super.postValue(value)
    }

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}