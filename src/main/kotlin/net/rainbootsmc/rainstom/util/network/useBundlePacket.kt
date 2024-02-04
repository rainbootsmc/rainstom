package net.rainbootsmc.rainstom.util.network

import net.minestom.server.entity.Player
import net.minestom.server.network.packet.server.play.BundlePacket
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun Player.useBundlePacket(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    sendPacket(BundlePacket())
    block()
    sendPacket(BundlePacket())
}
