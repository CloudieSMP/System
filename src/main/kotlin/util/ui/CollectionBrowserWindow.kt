package util.ui

import com.noxcrew.interfaces.InterfacesConstants
import com.noxcrew.interfaces.drawable.Drawable
import com.noxcrew.interfaces.element.StaticElement
import com.noxcrew.interfaces.interfaces.buildChestInterface
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object CollectionBrowserWindow {
    fun <T> openSelector(
        player: Player,
        title: Component,
        entries: List<T>,
        fillerPane: ItemStack,
        itemForEntry: (T) -> ItemStack,
        onEntryClick: (Player, T) -> Unit,
    ) {
        val rows = ((entries.size + 8) / 9).coerceIn(1, 6)

        val iface = buildChestInterface {
            this.rows = rows
            titleSupplier = { title }

            withTransform { pane, _ ->
                val filler = StaticElement(Drawable.drawable(fillerPane))
                for (row in 0 until rows) {
                    for (col in 0 until 9) {
                        pane[row, col] = filler
                    }
                }

                entries.forEachIndexed { index, entry ->
                    val row = index / 9
                    val col = index % 9
                    pane[row, col] = StaticElement(Drawable.drawable(itemForEntry(entry))) { ctx ->
                        onEntryClick(ctx.player, entry)
                    }
                }
            }
        }

        InterfacesConstants.SCOPE.launch {
            iface.open(player)
        }
    }

    fun <T> openPreview(
        player: Player,
        title: Component,
        entries: List<T>,
        fillerPane: ItemStack,
        backButton: ItemStack,
        itemForEntry: (T) -> ItemStack,
        onBackClick: (Player) -> Unit,
    ) {
        val rows = ((entries.size + 1 + 8) / 9).coerceIn(1, 6)
        val size = rows * 9
        val backSlot = size - 1

        val iface = buildChestInterface {
            this.rows = rows
            titleSupplier = { title }

            withTransform { pane, _ ->
                val filler = StaticElement(Drawable.drawable(fillerPane))
                for (row in 0 until rows) {
                    for (col in 0 until 9) {
                        pane[row, col] = filler
                    }
                }

                entries.take(backSlot).forEachIndexed { index, entry ->
                    val row = index / 9
                    val col = index % 9
                    pane[row, col] = StaticElement(Drawable.drawable(itemForEntry(entry)))
                }

                val backRow = backSlot / 9
                val backCol = backSlot % 9
                pane[backRow, backCol] = StaticElement(Drawable.drawable(backButton.clone())) { ctx ->
                    onBackClick(ctx.player)
                }
            }
        }

        InterfacesConstants.SCOPE.launch {
            iface.open(player)
        }
    }
}

