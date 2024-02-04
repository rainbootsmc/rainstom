package net.rainbootsmc.rainstom.util.skin

import kotlinx.serialization.json.Json
import net.minestom.server.entity.PlayerSkin
import java.util.*

object SkinTexturesDecoder {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @JvmStatic
    fun decode(skin: PlayerSkin): DecodedSkinTextures {
        val decodedBase64 = Base64.getDecoder().decode(skin.textures())
        return json.decodeFromString(decodedBase64.decodeToString())
    }
}
