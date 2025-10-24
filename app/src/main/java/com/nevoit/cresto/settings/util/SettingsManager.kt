package com.nevoit.cresto.settings.util

import com.tencent.mmkv.MMKV

object SettingsManager {

    private val mmkv = MMKV.defaultMMKV()
    private const val KEY_CUSTOM_PRIMARY_COLOR_ENABLED = "custom_primary_color_enabled"
    private const val KEY_USE_DYNAMIC_COLOR = "use_dynamic_color_enabled"
    private const val KEY_LITE_MODE = "lite_mode_enabled"
    private const val KEY_LIQUID_GLASS = "liquid_glass_enabled"

    private const val KEY_COLOR_MODE = "color_mode"

    var isCustomPrimaryColorEnabled: Boolean
        get() = mmkv.decodeBool(KEY_CUSTOM_PRIMARY_COLOR_ENABLED, false)
        set(value) {
            mmkv.encode(KEY_CUSTOM_PRIMARY_COLOR_ENABLED, value)
        }

    var isUseDynamicColor: Boolean
        get() = mmkv.decodeBool(KEY_USE_DYNAMIC_COLOR, false)
        set(value) {
            mmkv.encode(KEY_USE_DYNAMIC_COLOR, value)
        }

    var isLiteMode: Boolean
        get() = mmkv.decodeBool(KEY_LITE_MODE, false)
        set(value) {
            mmkv.encode(KEY_LITE_MODE, value)
        }

    var isLiquidGlass: Boolean
        get() = mmkv.decodeBool(KEY_LIQUID_GLASS, false)
        set(value) {
            mmkv.encode(KEY_LIQUID_GLASS, value)
        }

    var colorMode: Int
        get() = mmkv.decodeInt(KEY_COLOR_MODE, 0)
        set(value) {
            mmkv.encode(KEY_COLOR_MODE, value)
        }


    // 示例 3: 字符串 (String) - 用户名
    /*var username: String
        get() = mmkv.decodeString(KEY_USERNAME, "") ?: "" // 提供一个默认值空字符串
        set(value) = mmkv.encode(KEY_USERNAME, value)

    // 示例 4: 整数 (Int) - 同步频率（分钟）
    var syncFrequency: Int
        get() = mmkv.decodeInt(KEY_SYNC_FREQUENCY, 15) // 提供一个默认值 15
        set(value) = mmkv.encode(KEY_SYNC_FREQUENCY, value)*/

}