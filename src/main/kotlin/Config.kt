import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.net.URI

@ConfigSerializable
data class Config(
    val links: List<Link>,
    val resourcePacks: List<ResourcePack>,
    val tpa: TpaConfig = TpaConfig(),
    val home: HomeConfig = HomeConfig()
)

@ConfigSerializable
data class TpaConfig(
    val requestExpireTime: Int = 30,
    val tpaDelay: Int = 2
)

@ConfigSerializable
data class HomeConfig(
    val maxHomes: Int = 5
)


@ConfigSerializable
data class Link(val component: String, val uri: URI, val order: Int)

@ConfigSerializable
data class ResourcePack(val uri: URI, val hash: String, val priority: Int)