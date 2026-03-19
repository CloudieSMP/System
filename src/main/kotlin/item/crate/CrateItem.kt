package item.crate

import chat.Formatting.allTags
import io.papermc.paper.datacomponent.DataComponentTypes
import item.ItemRarity.*
import item.ItemType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import io.papermc.paper.datacomponent.item.Equippable
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType.STRING
import org.checkerframework.checker.units.qual.radians
import util.Keys.GENERIC_RARITY

/**
 * @param pctChanceToRoll How likely this item is to be rolled as a percentage (Int 0-100)
 * @param itemStack The actual itemStack of a single crate item
 */
@Suppress("UnstableApiUsage")
enum class CrateItem(val pctChanceToRoll: Int, val itemStack: ItemStack) {
    PENGUIN(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Penguin"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Waddle waddle, a plushie penguin to cuddle.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/penguin"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    MUSHROOM(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Mushroom"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Fungi fun!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/mushroom"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    BEE(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Bee"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Buzzzzz")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/bee"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    STAR(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Star"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Shining star plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/star"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HEART(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Heart"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Love is in the air.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/heart"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    SEBIANN_CLASSIC(1,
        ItemStack(Material.PAPER).apply {
            val rarity = RARE
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Sebiann"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Rare version of Sebiann, the mascot of Cloudie. Only a few exist in the world!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/player/sebiann-classic"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    SEBIANN(100,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Sebiann"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>Standard.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/player/sebiann"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    COOKIE(100,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Cookie"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>I like Beauver.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/player/cookie"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    BEAUVER(100,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Beauver"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>I like Cookie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/player/beauver"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    COFFEE_CUP(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Coffee Cup"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Freshly brewed coffee, now in plushie form!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/coffee_cup"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    RAMEN_BOWL(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Ramen Bowl"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>The perfect plushie for any ramen lover. Slurp up the cuteness!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/ramen_bowl"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    CAT_EARS(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Cat Ears"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Cat ears, perfect for any feline enthusiast!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/cat_ears"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    DOG_EARS(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Dog Ears"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Dog ears, perfect for any canine enthusiast!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/dog_ears"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    FOX_EARS(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Fox Ears"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Fox ears, perfect for any vulpine enthusiast!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/fox_ears"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    COOL_GLASSES(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Cool Glasses"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Cool glasses, perfect for any stylish collector!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/cool_glasses"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HALO(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Halo"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Halo, perfect for any angelic collector!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/halo"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HEART_CROWN(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Heart Crown"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Heart crown, perfect for any romantic collector!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/heart_crown"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    ORCHID_CROWN(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Orchid Crown"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>Orchid crown, perfect for any elegant collector!")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/orchid_crown"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HEART_GLASSES(10,
        ItemStack(Material.PAPER).apply {
            val rarity = COMMON
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><${rarity.colorHex}>Heart Glasses"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${rarity.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i>If u wanna look cute.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/heart_glasses"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    )
}