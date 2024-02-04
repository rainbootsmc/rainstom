package net.rainbootsmc.rainstom.util.point

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec

fun Point.asVec(): Vec {
    return Vec(x, y, z)
}

/**
 * [Pos]のコンストラクターのシンタックスシュガー
 */
fun pos(x: Number, y: Number, z: Number): Pos =
    Pos(x.toDouble(), y.toDouble(), z.toDouble())

/**
 * [Pos]のコンストラクターのシンタックスシュガー
 */
fun pos(x: Number, y: Number, z: Number, yaw: Number, pitch: Number): Pos =
    Pos(x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())

/**
 * [Pos]のコンストラクターのシンタックスシュガー
 */
fun pos(value: Number): Pos =
    Pos(value.toDouble(), value.toDouble(), value.toDouble())

/**
 * [Vec]のコンストラクターのシンタックスシュガー
 */
fun vec(x: Number, y: Number, z: Number): Vec =
    Vec(x.toDouble(), y.toDouble(), z.toDouble())

/**
 * [Vec]のコンストラクターのシンタックスシュガー
 */
fun vec(value: Number): Vec =
    Vec(value.toDouble(), value.toDouble(), value.toDouble())
