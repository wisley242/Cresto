package com.nevoit.cresto.settings

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.nevoit.cresto.R
import com.nevoit.cresto.ui.components.ConfigItemContainer
import com.nevoit.cresto.ui.components.DynamicSmallTitle
import com.nevoit.cresto.ui.components.GlasenseButton
import com.nevoit.cresto.ui.components.ZeroHeightDivider
import com.nevoit.cresto.ui.theme.glasense.CalculatedColor
import com.nevoit.cresto.util.g2
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalHazeApi::class)
@Composable
fun AboutScreen() {
    val activity = LocalActivity.current

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val density = LocalDensity.current
    val thresholdPx = if (statusBarHeight > 0.dp) {
        with(density) {
            (statusBarHeight + 24.dp).toPx()
        }
    } else 0f

    val hazeState = rememberHazeState()

    val onSurfaceContainer = CalculatedColor.onSurfaceContainer
    val onBackground = MaterialTheme.colorScheme.onBackground

    val surfaceColor = CalculatedColor.hierarchicalBackgroundColor
    val hierarchicalSurfaceColor = CalculatedColor.hierarchicalSurfaceColor

    val lazyListState = rememberLazyListState()

    val isSmallTitleVisible by remember(thresholdPx) { derivedStateOf { ((lazyListState.firstVisibleItemIndex == 0) && (lazyListState.firstVisibleItemScrollOffset > thresholdPx)) || lazyListState.firstVisibleItemIndex > 0 } }

    val dp = with(density) {
        1.dp.toPx()
    }
    val context = LocalContext.current

    val packageInfo: PackageInfo? = remember {
        try {
            val packageName = context.packageName
            context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .hazeSource(hazeState, 0f)
                .fillMaxSize()
                .padding(0.dp)
                .background(surfaceColor),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 0.dp,
                end = 12.dp,
                bottom = 136.dp
            )
        ) {
            item {
                Box(modifier = Modifier.padding(top = 48.dp + statusBarHeight + 12.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .aspectRatio(3f / 4f)
                        .fillMaxWidth()
                        .clip(ContinuousRoundedRectangle(12.dp, g2))
                        .paint(
                            painter = painterResource(R.drawable.about_background),
                            contentScale = ContentScale.Crop
                        )
                        .drawBehind {
                            val outline = ContinuousRoundedRectangle(12.dp, g2).createOutline(
                                size = size,
                                layoutDirection = LayoutDirection.Ltr,
                                density = density
                            )
                            drawOutline(
                                outline = outline,
                                style = Stroke(4.dp.toPx()),
                                color = Color.White.copy(.2f),
                                blendMode = BlendMode.Plus
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ConfigItemContainer(
                    backgroundColor = hierarchicalSurfaceColor,
                    title = "Developer"
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.avatar),
                                contentDescription = "Developer Avatar",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 4.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Nevoit",
                                    fontSize = 20.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.W500,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                                Text(
                                    text = "Create awesome.",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                )
                                Row(
                                    modifier = Modifier
                                        .offset((-4).dp)
                                        .padding(top = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .background(
                                                MaterialTheme.colorScheme.onBackground.copy(.1f),
                                                ContinuousCapsule
                                            )
                                    ) {
                                        Text(
                                            text = "Main Developer",
                                            fontSize = 10.sp,
                                            lineHeight = 10.sp,
                                            fontWeight = FontWeight.W400,
                                            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                            modifier = Modifier
                                                .padding(vertical = 2.dp, horizontal = 8.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.onBackground.copy(.1f),
                                                ContinuousCapsule
                                            )
                                    ) {
                                        Text(
                                            text = "Designer",
                                            fontSize = 10.sp,
                                            lineHeight = 10.sp,
                                            fontWeight = FontWeight.W400,
                                            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                            modifier = Modifier
                                                .padding(vertical = 2.dp, horizontal = 8.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_forward),
                                tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                                contentDescription = "Enter icon",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .offset(4.dp)
                                    .height(40.dp)
                                    .width(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ZeroHeightDivider(
                            color = onBackground.copy(.1f), width = 1.dp,
                            modifier = Modifier,
                            blendMode = BlendMode.SrcOver
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.kyant_avatar),
                                contentDescription = "Developer Avatar",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 4.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Kyant0",
                                    fontSize = 20.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.W500,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                                Text(
                                    text = "Create rubbish.",
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                )
                                Row(
                                    modifier = Modifier
                                        .offset((-4).dp)
                                        .padding(top = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .background(
                                                MaterialTheme.colorScheme.onBackground.copy(.1f),
                                                ContinuousCapsule
                                            )
                                    ) {
                                        Text(
                                            text = "Overscroll Animation Developer",
                                            fontSize = 10.sp,
                                            lineHeight = 10.sp,
                                            fontWeight = FontWeight.W400,
                                            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                            modifier = Modifier
                                                .padding(vertical = 2.dp, horizontal = 8.dp)

                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_forward),
                                tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                                contentDescription = "Enter icon",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .offset(4.dp)
                                    .height(40.dp)
                                    .width(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Version Info",
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                        modifier = Modifier
                            .padding(
                                start = 12.dp,
                                top = 0.dp,
                                end = 12.dp,
                                bottom = 12.dp
                            )
                            .fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = hierarchicalSurfaceColor,
                                shape = ContinuousRoundedRectangle(12.dp, g2)
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.height(32.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_mini_info),
                                    contentDescription = "Version name",
                                    colorFilter = ColorFilter.tint(onBackground),
                                    alpha = .5f,
                                    modifier = Modifier
                                        .size(24.dp)

                                )
                                Text(
                                    text = "Version Name",
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp,
                                    color = onBackground.copy(.5f),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Text(
                                    text = "${packageInfo?.versionName}",
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.End,
                                    color = onBackground,
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.height(32.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_mini_version_code),
                                    contentDescription = "Version name",
                                    colorFilter = ColorFilter.tint(onBackground),
                                    alpha = .5f,
                                    modifier = Modifier
                                        .size(24.dp)

                                )
                                Text(
                                    text = "Version Code",
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp,
                                    color = onBackground.copy(.5f),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Text(
                                    text = "${packageInfo?.longVersionCode}",
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.End,
                                    color = onBackground,
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .weight(1f)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        DynamicSmallTitle(
            modifier = Modifier.align(Alignment.TopCenter),
            title = "About",
            statusBarHeight = statusBarHeight,
            isVisible = isSmallTitleVisible,
            hazeState = hazeState,
            surfaceColor = surfaceColor
        ) {
        }
        GlasenseButton(
            enabled = true,
            shape = CircleShape,
            onClick = { activity?.finish() },
            modifier = Modifier
                .padding(top = statusBarHeight, start = 12.dp)
                .size(48.dp)
                .align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(
                containerColor = onSurfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_forward_nav),
                contentDescription = "Back",
                modifier = Modifier.width(32.dp)
            )
        }
    }
}