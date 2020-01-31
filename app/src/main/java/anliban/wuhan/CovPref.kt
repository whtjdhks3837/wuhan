package anliban.wuhan

import android.content.SharedPreferences

class CovPref(pref: SharedPreferences) : PreferenceModel(pref) {

    var cityCoV by stringPreference("city_cov_list")
}