package com.example.challengechapter5

object Data{
    const val EMAIL = "EMAIL"
    const val PASS = "PASS"

//    object Preferences {
//        const val PREF_NAME = "MyDoctor"
//        const val IS_LOGIN = "IS_LOGIN"
//        const val PREF_FULLNAME = "NAME"
//        const val PREF_JOB = "JOB"
//        const val PREF_EMAIL = "EMAIL"
//        const val PREF_PASS = "PASS"
//    }

    object Preferences {
        const val PREF_NAME = "MyDoctor"
        object KEY {
            const val TOKEN = "TOKEN"
            const val EMAIL = "EMAIL"
            const val PASSWORD = "PASSWORD"
            const val DARK_MODE = "DARK_MODE"
            const val APP_LANGUAGE = "APP_LANGUAGE"
        }
    }
}