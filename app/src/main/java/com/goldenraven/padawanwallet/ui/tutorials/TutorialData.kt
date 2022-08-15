/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.tutorials

import com.goldenraven.padawanwallet.R

data class TutorialElement(val elementType: ElementType, val resourceId: Int)

enum class ElementType {
    TITLE,
    SUBTITLE,
    BODY,
    RESOURCE,
}

val tutorial1: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E1_title),
        TutorialElement(ElementType.BODY, R.string.t1_p1),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h2),
        TutorialElement(ElementType.BODY, R.string.t1_p2),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h3),
        TutorialElement(ElementType.BODY, R.string.t1_p3),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h4),
        TutorialElement(ElementType.BODY, R.string.t1_p4),
    )
)

val tutorial2: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E2_title),
        TutorialElement(ElementType.BODY, R.string.t2_p1),
        TutorialElement(ElementType.BODY, R.string.t2_p2),
        TutorialElement(ElementType.BODY, R.string.t2_p3),
    ),
    listOf(
        TutorialElement(ElementType.BODY, R.string.t2_p4),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
        TutorialElement(ElementType.BODY, R.string.t2_h2),
        TutorialElement(ElementType.BODY, R.string.t2_p5),
        TutorialElement(ElementType.BODY, R.string.t2_p6),
    )
)

val tutorial3: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E3_title),
        TutorialElement(ElementType.BODY, R.string.t3_p1),
        TutorialElement(ElementType.BODY, R.string.t3_p2),
    )
)

val tutorial4: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E4_title),
        TutorialElement(ElementType.BODY, R.string.t4_p1),
    )
)

val tutorial5: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E5_title),
        TutorialElement(ElementType.BODY, R.string.t5_p1),
    )
)

val tutorial6: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E6_title),
        TutorialElement(ElementType.BODY, R.string.t6_p1),
    )
)

val tutorial7: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E7_title),
        TutorialElement(ElementType.BODY, R.string.t7_p1),
        TutorialElement(ElementType.BODY, R.string.t7_p2),
    )
)

val tutorial8: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E8_title),
        TutorialElement(ElementType.BODY, R.string.t8_p1),
    )
)