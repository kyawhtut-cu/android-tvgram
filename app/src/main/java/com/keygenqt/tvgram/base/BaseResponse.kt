/*
 * Copyright 2022 Vitaliy Zarubin
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
 */
package com.keygenqt.tvgram.base

import com.keygenqt.tvgram.exceptions.ApiException

/**
 * Base class for handling response
 */
sealed class BaseResponse<out R> {
    data class Success<out T>(val data: T) : BaseResponse<T>()
    data class Error(val exception: ApiException) : BaseResponse<Nothing>()
}

/**
 * Check response is success
 */
val BaseResponse<*>?.isSuccess get() = this is BaseResponse.Success

/**
 * Check response is error
 */
val BaseResponse<*>?.isError get() = this is BaseResponse.Error

/**
 * Callback response success
 */
inline infix fun <T> BaseResponse<T>.success(listener: (data: T) -> Unit): BaseResponse<T> {
    if (this is BaseResponse.Success && this.data != null) {
        listener.invoke(this.data)
    }
    return this
}

/**
 * Callback response error
 */
inline infix fun <T> BaseResponse<T>.error(predicate: (data: ApiException) -> Unit): BaseResponse<T> {
    if (this is BaseResponse.Error) {
        predicate.invoke(this.exception)
    }
    return this
}