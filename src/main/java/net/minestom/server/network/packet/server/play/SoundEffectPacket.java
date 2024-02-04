package net.minestom.server.network.packet.server.play;

import net.kyori.adventure.sound.Sound.Source;
import net.minestom.server.adventure.AdventurePacketConvertor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minestom.server.network.NetworkBuffer.*;

public record SoundEffectPacket(
        // only one of soundEvent and soundName may be present
        @Nullable SoundEvent soundEvent,
        @Nullable String soundName,
        @Nullable Float range, // Only allowed with soundName
        @NotNull Source source,
        int packetX, // Rainstom 再生される座標の小数点以下が無視される問題を修正する
        int packetY, // Rainstom 再生される座標の小数点以下が無視される問題を修正する
        int packetZ, // Rainstom 再生される座標の小数点以下が無視される問題を修正する
        float volume,
        float pitch,
        long seed
) implements ServerPacket {
    private static @NotNull SoundEffectPacket fromReader(@NotNull NetworkBuffer reader) {
        int soundId = reader.read(VAR_INT);
        SoundEvent soundEvent;
        String soundName;
        Float range = null;
        if (soundId == 0) {
            soundEvent = null;
            soundName = reader.read(STRING);
            range = reader.readOptional(FLOAT);
        } else {
            soundEvent = SoundEvent.fromId(soundId - 1);
            soundName = null;
        }
        return new SoundEffectPacket(
                soundEvent,
                soundName,
                range,
                reader.readEnum(Source.class),
                reader.read(INT) / 8.0, // Rainstom 再生される座標の小数点以下が無視される問題を修正する
                reader.read(INT) / 8.0, // Rainstom 再生される座標の小数点以下が無視される問題を修正する
                reader.read(INT) / 8.0, // Rainstom 再生される座標の小数点以下が無視される問題を修正する
                reader.read(FLOAT),
                reader.read(FLOAT),
                reader.read(LONG)
        );
    }

    // Rainstom start 再生される座標の小数点以下が無視される問題を修正する
    private SoundEffectPacket(@Nullable SoundEvent soundEvent, @Nullable String soundName, @Nullable Float range, @NotNull Source source,
                              double x, double y, double z,
                              float volume, float pitch, long seed) {
        this(soundEvent, soundName, range, source, (int) (x * 8), (int) (y * 8), (int) (z * 8), volume, pitch, seed);
    }
    // Rainstom end

    public SoundEffectPacket(@NotNull SoundEvent soundEvent, @Nullable Float range, @NotNull Source source,
                             @NotNull Point position, float volume, float pitch, long seed) {
        this(soundEvent, null, range, source, position.x(), position.y(), position.z(), volume, pitch, seed); // Rainstom 再生される座標の小数点以下が無視される問題を修正する
    }

    public SoundEffectPacket(@NotNull String soundName, @Nullable Float range, @NotNull Source source,
                             @NotNull Point position, float volume, float pitch, long seed) {
        this(null, soundName, range, source, position.x(), position.y(), position.z(), volume, pitch, seed); // Rainstom 再生される座標の小数点以下が無視される問題を修正する
    }

    public SoundEffectPacket(@NotNull NetworkBuffer reader) {
        this(fromReader(reader));
    }

    private SoundEffectPacket(@NotNull SoundEffectPacket packet) {
        this(packet.soundEvent, packet.soundName, packet.range, packet.source,
                packet.packetX, packet.packetY, packet.packetZ, packet.volume, packet.pitch, packet.seed); // Rainstom 再生される座標の小数点以下が無視される問題を修正する
    }

    @Override
    public void write(@NotNull NetworkBuffer writer) {
        if (soundEvent != null) {
            writer.write(VAR_INT, soundEvent.id() + 1);
        } else {
            writer.write(VAR_INT, 0);
            writer.write(STRING, soundName);
            writer.writeOptional(FLOAT, range);
        }
        writer.write(VAR_INT, AdventurePacketConvertor.getSoundSourceValue(source));
        writer.write(INT, packetX); // Rainstom 再生される座標の小数点以下が無視される問題を修正する
        writer.write(INT, packetY); // Rainstom 再生される座標の小数点以下が無視される問題を修正する
        writer.write(INT, packetZ); // Rainstom 再生される座標の小数点以下が無視される問題を修正する
        writer.write(FLOAT, volume);
        writer.write(FLOAT, pitch);
        writer.write(LONG, seed);
    }

    @Override
    public int getId(@NotNull ConnectionState state) {
        return switch (state) {
            case PLAY -> ServerPacketIdentifier.SOUND_EFFECT;
            default -> PacketUtils.invalidPacketState(getClass(), state, ConnectionState.PLAY);
        };
    }

}
