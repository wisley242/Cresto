package com.nevoit.cresto.util

import com.kyant.capsule.continuities.G2Continuity
import com.kyant.capsule.continuities.G2ContinuityProfile

val g2 = G2Continuity(
    profile = G2ContinuityProfile.RoundedRectangle.copy(
        extendedFraction = 0.52867,
        arcFraction = 0.55556,
        bezierCurvatureScale = 1.07321,
        arcCurvatureScale = 1.07321
    ),
    capsuleProfile = G2ContinuityProfile.Capsule.copy(
        extendedFraction = 0.52867,
        arcFraction = 0.25
    )
)