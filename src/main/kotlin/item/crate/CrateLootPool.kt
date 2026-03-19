package item.crate

import item.crate.CrateItem.*

enum class CrateLootPool(val possibleItems: List<CrateItem>) {
    PLUSHIE(listOf(
        PENGUIN,
        MUSHROOM,
        BEE,
        STAR,
        HEART
    )),
    WEARABLES(listOf(
        CAT_EARS,
        DOG_EARS,
        FOX_EARS,
        COOL_GLASSES,
        HALO,
        HEART_CROWN,
        ORCHID_CROWN,
        HEART_GLASSES
    )),
    UTENSILS(listOf(
        COFFEE_CUP,
        RAMEN_BOWL
    )),
    PLAYER(listOf(
        SEBIANN,
        SEBIANN_CLASSIC,
        COOKIE,
        BEAUVER
    ))
}