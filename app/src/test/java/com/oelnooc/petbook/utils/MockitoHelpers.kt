package com.oelnooc.petbook.utils

import org.mockito.Mockito

inline fun <reified T> any(): T = Mockito.any<T>()

inline fun <reified T> eq(value: T): T = Mockito.eq(value)