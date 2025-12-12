package funny.abbas.sokoban.page

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import funny.abbas.sokoban.R
import funny.abbas.sokoban.core.skin.Skin

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)


        // 找到 key = "skin" 的 ListPreference
        val skinPreference = findPreference<ListPreference>("skin")

        // 监听变化（返回 false 会阻止本次修改，return true 允许修改）
        skinPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val skinIndex = (newValue as String).toInt()   // 因为你 entryValues 是 "0","1","2"...

            // 立刻应用新主题（举例）
            applySkin(skinIndex)

            // 必须 return true 才会真正保存到 SharedPreferences
            true
        }
    }

    private fun applySkin(skinCode: Int) {
        Skin.getInstance().setTheme(
            Skin.valueOfThemeCode(skinCode)
        )
    }

}