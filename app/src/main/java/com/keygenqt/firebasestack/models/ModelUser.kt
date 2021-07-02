/*
 * Copyright 2021 Vitaliy Zarubin
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
 
package com.keygenqt.firebasestack.models

import androidx.compose.runtime.Immutable

@Immutable
data class ModelUser(
    var email: String = "",
    var first_name: String = "",
    var last_name: String = ""
) {
    val nickname: String?
        get() = (if ("$first_name $last_name".isBlank()) email else "$first_name $last_name".trim()).let {
            if (it.isBlank()) null else it
        }

    operator fun plus(modelUser: ModelUser): ModelUser {
        return ModelUser(
            email = if (this.email.isBlank()) modelUser.email else this.email,
            first_name = if (this.first_name.isBlank()) modelUser.first_name else this.first_name,
            last_name = if (this.last_name.isBlank()) modelUser.last_name else this.last_name
        )
    }

    companion object {
        fun mock() = ModelUser(
            email = "mock@email.com",
            first_name = "Vitaly",
            last_name = "Zarubin",
        )
    }
}
