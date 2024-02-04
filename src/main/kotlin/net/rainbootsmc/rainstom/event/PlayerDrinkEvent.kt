package net.rainbootsmc.rainstom.event

import net.minestom.server.entity.Player
import net.minestom.server.entity.Player.Hand
import net.minestom.server.event.trait.ItemEvent
import net.minestom.server.event.trait.PlayerInstanceEvent
import net.minestom.server.item.ItemStack

data class PlayerDrinkEvent(private val player: Player, private val itemStack: ItemStack, val hand: Hand) : ItemEvent, PlayerInstanceEvent {
    override fun getItemStack(): ItemStack {
        return itemStack
    }

    override fun getPlayer(): Player {
        return player
    }
}
