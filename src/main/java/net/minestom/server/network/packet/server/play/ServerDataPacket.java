package net.minestom.server.network.packet.server.play;

import net.kyori.adventure.text.Component;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minestom.server.network.NetworkBuffer.*;

public record ServerDataPacket(@NotNull Component motd, byte @Nullable [] iconBase64, // Rainstom 1.19.4
                               boolean enforcesSecureChat) implements ServerPacket {
    public ServerDataPacket(@NotNull NetworkBuffer reader) {
        this(reader.read(COMPONENT), reader.readOptional(BYTE_ARRAY), // Rainstom 1.19.4
                reader.read(BOOLEAN));
    }

    @Override
    public void write(@NotNull NetworkBuffer writer) {
        writer.write(COMPONENT, this.motd); // Rainstom 1.19.4
        writer.writeOptional(BYTE_ARRAY, this.iconBase64); // Rainstom 1.19.4
        writer.write(BOOLEAN, enforcesSecureChat);
    }

    @Override
    public int getId() {
        return ServerPacketIdentifier.SERVER_DATA;
    }
}
