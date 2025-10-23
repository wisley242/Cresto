package com.nevoit.cresto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.nevoit.cresto.data.EventResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

class AiViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val date = LocalDateTime.now().toString()
    private val systemInstruction = content(role = "system") {
        text(
            """
            现在时间是${date}你是一个信息提取AI助手。你的任务是分析我发送给你的文本或图片内容，并严格按照以下要求提取待办事项信息，最后以指定的JSON格式返回。

提取规则:

1.  识别待办事项: 从内容中找出所有独立的待办事项。
2.  提取title(待办事项名称):
    *   准确提取每个待办事项的内容作为title，包含完整信息。
    *   如果原文中明确提及了具体的时间点或时间段（例如 "14:30" 或 "9:00-10:00"），必须将该时间信息完整地附加在title字符串的末尾。
3.  提取date(截止日期):
    *   提取每个待办事项对应的日期。
    *   必须将提取到的日期统一格式化为yyyy-MM-dd。忽略具体时间。
4.  如果无法提取任何日程，返回Error: No tasks

输出格式要求:

*   返回结果必须是一个结构完整的JSON对象。
*   JSON对象的最外层应包含一个quantity字段，其值为提取到的待办事项总数。
*   所有待办事项应收录在名为items的数组中。
*   数组中的每一个元素都是一个独立的对象，包含title和date两个字段，其值分别对应按上述规则提取和格式化后的结果。

示例:

如果输入内容为：“学生事务办公室通知：请各宿舍于10月20日开展宿舍卫生大扫除并确保用电安全。另外，学生会将于2025年10月21日上午7:50-8:20配合开展宿舍卫生安全检查，请同学们留人配合。”

你应该返回如下所示的JSON：
{
  "quantity": 2,
  "items": [
    {
      "title": "配合开展宿舍卫生安全检查 7:50-8:20",
      "date": "2025-10-21"
    },
    {
      "title": "开展宿舍卫生大扫除并确保用电安全",
      "date": "2025-10-20"
    }
  ]
}

从现在开始处理我发送给你的信息，并仅返回符合上述要求的JSON对象，不要包含任何额外的解释或文字。
        """.trimIndent()
        )
    }

    // 2. 在初始化 GenerativeModel 时传入这个设定

    private fun cleanJsonString(rawText: String): String {
        val trimmedText = rawText.trim()

        if (trimmedText.startsWith("```json") && trimmedText.endsWith("```")) {
            return trimmedText
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()
        }

        if (trimmedText.startsWith("```") && trimmedText.endsWith("```")) {
            return trimmedText
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
        }

        return trimmedText
    }

    fun generateContent(prompt: String, apiKey: String) {
        viewModelScope.launch {
            val generativeModel = GenerativeModel(
                // 推荐使用支持多模态（图片和文字）的模型
                modelName = "gemini-2.5-flash",
                apiKey = apiKey,
                systemInstruction = systemInstruction
            )
            _uiState.value = UiState.Loading
            try {
                val response = generativeModel.generateContent(prompt)
                val rawResponseText = response.text ?: ""

                val cleanedJsonText = cleanJsonString(rawResponseText)

                if (cleanedJsonText.isEmpty()) {
                    throw Exception("模型返回了空内容")
                }

                val eventResponse = Json.decodeFromString<EventResponse>(cleanedJsonText)
                _uiState.value = UiState.Success(eventResponse)

            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(e.localizedMessage ?: "发生未知错误，可能是JSON格式不正确")
            }
        }
    }

    fun clearState() {
        _uiState.value = UiState.Initial
    }

}

sealed interface UiState {
    object Initial : UiState
    object Loading : UiState
    data class Success(val response: EventResponse) : UiState
    data class Error(val message: String) : UiState
}