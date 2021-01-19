package net.minestom.server.storage.systems;

import net.minestom.server.MinecraftServer;
import net.minestom.server.storage.StorageOptions;
import net.minestom.server.storage.StorageSystem;
import org.jetbrains.annotations.NotNull;
import org.rocksdb.*;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A {@link StorageSystem} which is local using OS files system
 * It does make use of the RocksDB library.
 * <p>
 * The location represents the path of the folder.
 * <p>
 * Warning: will create some log files in the location folder when opened, those are generated by RocksDB.
 */
public class FileStorageSystem implements StorageSystem {

    static {
        RocksDB.loadLibrary();
    }

    private RocksDB rocksDB;

    @Override
    public boolean exists(@NotNull String location) {
        return Files.isDirectory(Paths.get(location));
    }

    @Override
    public void open(@NotNull String location, @NotNull StorageOptions storageOptions) {
        Options options = new Options().setCreateIfMissing(true);

        if (storageOptions.hasCompression()) {
            options.setCompressionType(CompressionType.ZSTD_COMPRESSION);
            options.setCompressionOptions(new CompressionOptions().setLevel(1));
        }

        try {
            this.rocksDB = RocksDB.open(options, location);
        } catch (RocksDBException e) {
            MinecraftServer.getExceptionManager().handleException(e);
        }
    }

    @Override
    public byte[] get(@NotNull String key) {
        try {
            return rocksDB.get(getKey(key));
        } catch (RocksDBException e) {
            MinecraftServer.getExceptionManager().handleException(e);
            return null;
        }
    }

    @Override
    public void set(@NotNull String key, byte[] data) {
        try {
            this.rocksDB.put(getKey(key), data);
        } catch (RocksDBException e) {
            MinecraftServer.getExceptionManager().handleException(e);
        }
    }

    @Override
    public void delete(@NotNull String key) {
        try {
            this.rocksDB.delete(getKey(key));
        } catch (RocksDBException e) {
            MinecraftServer.getExceptionManager().handleException(e);
        }
    }

    @Override
    public void close() {
        try {
            this.rocksDB.closeE();
        } catch (RocksDBException e) {
            MinecraftServer.getExceptionManager().handleException(e);
        }
    }

    private byte[] getKey(String key) {
        return key.getBytes();
    }

}
