package item.crate

import chat.Formatting.allTags
import io.papermc.paper.datacomponent.DataComponentTypes
import item.ItemRarity.COMMON
import item.ItemType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import io.papermc.paper.datacomponent.item.Equippable
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType.STRING
import util.Keys.GENERIC_RARITY

/**
 * @param pctChanceToRoll How likely this item is to be rolled as a percentage (Int 0-100)
 * @param itemStack The actual itemStack of a single crate item
 */
@Suppress("UnstableApiUsage")
enum class CrateItem(val pctChanceToRoll: Int, val itemStack: ItemStack) {
    PENGUIN(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Penguin"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/penguin_plushie"))
        }
    ),
    MUSHROOM(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Mushroom"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/mushroom_plushie"))
        }
    ),
    BEE(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Bee"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/bee_plushie"))
        }
    ),
    STAR(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Star"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/star_plushie"))
        }
    ),
    HEART(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Heart"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/heart_plushie"))
        }
    ),
    SEBIANN(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Sebiann"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/sebiann"))
        }
    ),
    COOKIE(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Cookie"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/cookie"))
        }
    ),
    BEAUVER(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Beauver"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/beauver"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    COFFEE_CUP(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Coffee Cup"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "coffeecup"))
        }
    ),
    RAMEN_BOWL(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Ramen Bowl"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "ramen_bowl"))
        }
    ),
    CAT_EARS(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Cat Ears"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/cat_ears"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    DOG_EARS(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Dog Ears"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/dog_ears"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    FOX_EARS(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Fox Ears"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/fox_ears"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    COOL_GLASSES(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Cool Glasses"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/cool_glasses"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HALO(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Halo"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/halo"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HEART_CROWN(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Heart Crown"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/heart_crown"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    ORCHID_CROWN(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Orchid Crown"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/orchid_crown"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    ),
    HEART_GLASSES(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Heart Glasses"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "wearables/heart_glasses"))
            setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.HEAD).build())
        }
    )
}