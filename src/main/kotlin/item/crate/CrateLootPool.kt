package item.crate

import item.crate.CrateItem.*

enum class CrateLootPool(val possibleItems: List<CrateItem>) {
    PLAYER(listOf()),
    PLUSHIE(listOf(
        PENGUIN,
        MUSHROOM,
        BEE,
        STAR,
        HEART,
        SEBIANN
    ))
}