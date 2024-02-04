package net.minestom.server.listener;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.client.play.ClientCreativeInventoryActionPacket;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;
import net.rainbootsmc.rainstom.item.drop.DropAmount;
import net.rainbootsmc.rainstom.item.drop.DropType;

import java.util.Objects;

public final class CreativeInventoryActionListener {
    public static void listener(ClientCreativeInventoryActionPacket packet, Player player) {
        if (!player.isCreative()) return;
        short slot = packet.slot();
        final ItemStack item = packet.item();
        if (slot == -1) {
            // Drop item
            player.dropItem(item, new DropType.Inventory(player.getInventory()), DropAmount.STACK); // Rainstom DropTypeとDropAmountを追加
            return;
        }
        // Set item
        slot = (short) PlayerInventoryUtils.convertPlayerInventorySlot(slot, PlayerInventoryUtils.OFFSET);
        PlayerInventory inventory = player.getInventory();
        if (Objects.equals(inventory.getItemStack(slot), item)) {
            // Item is already present, ignore
            return;
        }
        inventory.setItemStack(slot, item);
    }
}
