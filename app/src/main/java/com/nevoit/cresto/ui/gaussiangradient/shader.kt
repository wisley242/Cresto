package com.nevoit.cresto.ui.gaussiangradient

const val GAUSSIAN_COLOR_INTERPOLATION_SHADER = """
    uniform float2 iResolution; // 画布的尺寸 (宽, 高)
    uniform half4 startColor;  // 起始颜色
    uniform half4 centerColor; // 中心颜色
    uniform half4 endColor;    // 结束颜色
    uniform float mean;        // 中心位置 (均值 μ, 0.0-1.0)
    uniform float sigma;       // 扩散范围 (标准差 σ)
    uniform float horizontal;  // 0.0 for vertical, 1.0 for horizontal

    half4 main(float2 fragCoord) {
        // 1. 根据方向，确定当前像素的归一化坐标 'p'
        float p = horizontal > 0.5 ? (fragCoord.x / iResolution.x) : (fragCoord.y / iResolution.y);

        // 2. 计算当前坐标 'p' 的高斯权重
        // 这个权重代表了 centerColor 的影响力
        float dist = p - mean;
        float exponent = -0.5 * pow(dist / sigma, 2.0);
        float gaussianInfluence = exp(exponent);

        // 3. 根据 'p' 是在中心点的左边还是右边，选择正确的“边缘颜色”
        half4 edgeColor = p <= mean ? startColor : endColor;

        // 4. 使用 mix() 函数，根据高斯权重在“边缘颜色”和“中心颜色”之间进行插值
        return mix(edgeColor, centerColor, gaussianInfluence);
    }
"""