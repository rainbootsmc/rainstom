package net.rainbootsmc.rainstom.util.skin

import kotlinx.serialization.Serializable

@Serializable
data class DecodedSkinTextures(
    val timestamp: Long,
    val profileId: String,
    val profileName: String,
    val signatureRequired: Boolean,
    val textures: Map<TextureType, Texture>,
) {
    val isSlimModel: Boolean
        get() = textures[TextureType.SKIN]!!.metadata?.model == "slim"

    val url: String
        get() = textures[TextureType.SKIN]!!.url

    enum class TextureType {
        SKIN,
        CAPE,
    }

    @Serializable
    @JvmRecord
    data class Texture(val url: String, val metadata: Metadata? = null)

    @Serializable
    @JvmRecord
    data class Metadata(val model: String)
}
