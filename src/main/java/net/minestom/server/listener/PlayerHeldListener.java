package net.minestom.server.listener;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.network.packet.client.play.ClientHeldItemChangePacket;
import net.minestom.server.utils.MathUtils;
import net.rainbootsmc.rainstom.event.PlayerChangedHeldSlotEvent;

public class PlayerHeldListener {

    public static void heldListener(ClientHeldItemChangePacket packet, Player player) {
        if (!MathUtils.isBetween(packet.slot(), 0, 8)) {
            // Incorrect packet, ignore
            return;
        }

        final byte slot = (byte) packet.slot();
        final var previousSlot = player.getHeldSlot(); // Rainstom 下で呼び出すPlayerChangedHeldSlotEvent用

        PlayerChangeHeldSlotEvent changeHeldSlotEvent = new PlayerChangeHeldSlotEvent(player, slot);
        EventDispatcher.call(changeHeldSlotEvent);

        if (!changeHeldSlotEvent.isCancelled()) {
            // Event hasn't been canceled, process it

            final byte resultSlot = changeHeldSlotEvent.getSlot();

            // If the held slot has been changed by the event, send the change to the player
            if (resultSlot != slot) {
                player.setHeldItemSlot(resultSlot);
            } else {
                // Otherwise, simply refresh the player field
                player.refreshHeldSlot(resultSlot);
            }
            EventDispatcher.call(new PlayerChangedHeldSlotEvent(player, previousSlot, resultSlot)); // Rainstom イベント呼び出し
        } else {
            // Event has been canceled, send the last held slot to refresh the client
            player.setHeldItemSlot(player.getHeldSlot());
        }
    }

}
