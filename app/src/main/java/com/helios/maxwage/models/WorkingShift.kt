package com.helios.maxwage.models

/**
 * Created by Helios on 5/30/2021.
 */

sealed class WorkingShift(val name: String, val timeRange: IntRange)

class FirstWorkingSift(name: String = "First shift", timeRange: IntRange = (0 until 8)) :
    WorkingShift(name, timeRange)

class SecondWorkingSift(name: String = "Second shift", timeRange: IntRange = (8 until 17)) :
    WorkingShift(name, timeRange)

class ThirdWorkingSift(name: String = "Third shift", timeRange: IntRange = (17 until 24)) :
    WorkingShift(name, timeRange)