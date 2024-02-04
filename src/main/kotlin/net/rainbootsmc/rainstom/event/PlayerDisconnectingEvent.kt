package net.rainbootsmc.rainstom.event

import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.trait.PlayerInstanceEvent

/**
 * プレイヤーが切断する直前に発火する
 * [PlayerDisconnectEvent]は呼び出されるまでに若干の遅延がある
 */
class PlayerDisconnectingEvent(private val player: Player) : PlayerInstanceEvent {
    override fun getPlayer(): Player {
        return player
    }
}
