package util

import net.kyori.adventure.key.Key.key
import net.kyori.adventure.sound.Sound.Source
import net.kyori.adventure.sound.Sound.sound

object Sounds {
    val ADMIN_MESSAGE = sound(key("ui.button.click"), Source.MASTER, 0.5f, 2f)
    val SERVER_ANNOUNCEMENT = sound(key("block.note_block.pling"), Source.MASTER, 1f, 1f)
    val RENAME_ITEM = sound(key("block.smithing_table.use"), Source.PLAYER, 1f, 1f)
    val PLING = sound(key("minecraft:block.note_block.pling"), Source.PLAYER, 0.5f, 1f)
    val SILENT = sound(key("minecraft:ui.toast.in"), Source.PLAYER, 0f, 0f)
    val CHEST_OPEN = sound(key("minecraft:block.chest.open"), Source.PLAYER, 1f, 1f)
    val GAMBLING_WHEEL_TICK = sound(key("minecraft:block.note_block.hat"), Source.PLAYER, 0.5f, 1.4f)
    val GAMBLING_WHEEL_STOP = sound(key("minecraft:block.note_block.pling"), Source.PLAYER, 0.9f, 1.9f)
    val ERROR_DIDGERIDOO = sound(key("minecraft:block.note_block.didgeridoo"), Source.PLAYER, 0.5f, 1f)
    val VENDING_MACHINE = sound(key("minecraft:block.note_block.chime"), Source.BLOCK, 0.5f, 1f)
}