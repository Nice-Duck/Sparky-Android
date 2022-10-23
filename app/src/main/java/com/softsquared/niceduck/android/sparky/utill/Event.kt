package com.softsquared.niceduck.android.sparky.utill

// 이벤트를 나타내는 LiveData를 통해 노출되는 데이터의 래퍼로 사용됩니다.
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // 외부 읽기는 허용하지만 쓰기는 허용하지 않음

    // 콘텐츠를 반환하고 다시 사용하지 못하게합니다.
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    // 이미 처리된 경우에도 콘텐츠를 반환합니다.
    fun peekContent(): T = content
}
