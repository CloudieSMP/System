package util.ui

import item.crate.CrateItem
import item.crate.CrateType
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import plugin
import kotlin.math.roundToInt
import kotlin.random.Random
import util.Sounds.GAMBLING_WHEEL_STOP
import util.Sounds.GAMBLING_WHEEL_TICK
import chat.Formatting

private class GamblingWindowHolder(val crateType: CrateType) : InventoryHolder {
    var backingInventory: Inventory? = null
    var spinTask: BukkitRunnable? = null
    var wheelOffset: Int = 0
    var isSpinning: Boolean = false
    var spinStepsDone: Int = 0
    var spinTicksUntilNextStep: Int = 0
    var spinTargetSteps: Int = 0
    var winningItem: CrateItem? = null
    var rewardGranted: Boolean = false
    var spinConsumed: Boolean = false

    override fun getInventory(): Inventory {
        return requireNotNull(backingInventory) { "Gambling window inventory has not been initialized." }
    }
}

object GamblingWindow : Listener {
    private val wheelSlots = listOf(11, 12, 13, 14, 15)
    private val fillerPane = ItemStack(Material.PURPLE_STAINED_GLASS_PANE)
    private val idleWheelItem = ItemStack(Material.WHITE_WOOL)

    fun open(player: Player, crateType: CrateType) {
        val holder = GamblingWindowHolder(crateType)
        val title = crateType.displayName
        val inv = Bukkit.createInventory(holder, 27, title)
        holder.backingInventory = inv

        renderInitialLayout(inv)

        player.openInventory(inv)

        // Start automatically on next tick so the open inventory is guaranteed to be active.
        object : BukkitRunnable() {
            override fun run() {
                if (player.isOnline && player.openInventory.topInventory == inv) {
                    startSpin(holder, player)
                }
            }
        }.runTask(plugin)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val topInventory = e.view.topInventory
        if (topInventory.holder !is GamblingWindowHolder) return
        e.isCancelled = true
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val holder = e.inventory.holder as? GamblingWindowHolder ?: return
        val player = e.player as? Player

        if (holder.isSpinning && player != null) {
            awardPendingReward(holder, player, "closed early")
        }

        stopSpin(holder)
    }

    private fun startSpin(holder: GamblingWindowHolder, player: Player) {
        if (holder.isSpinning || holder.spinConsumed) return

        val pool = holder.crateType.lootPool.possibleItems
        if (pool.isEmpty()) {
            player.sendMessage(Formatting.allTags.deserialize("<red>This crate has no loot configured yet."))
            return
        }

        val winningCrateItem = pickWeightedItem(pool)
        val baseSteps = 40
        val targetIndex = pool.indexOf(winningCrateItem)
        val targetOffset = mod(targetIndex - 2, pool.size)
        val desiredRemainder = mod(targetOffset - holder.wheelOffset, pool.size)
        val alignmentDelta = mod(desiredRemainder - (baseSteps % pool.size), pool.size)
        val bonusLoops = Random.nextInt(0, 3) * pool.size

        holder.isSpinning = true
        holder.spinTargetSteps = baseSteps + alignmentDelta + bonusLoops
        holder.winningItem = winningCrateItem
        holder.rewardGranted = false
        holder.spinConsumed = true
        val minDelayTicks = 1
        val maxDelayTicks = 6
        holder.spinStepsDone = 0
        holder.spinTicksUntilNextStep = 0

        val task = object : BukkitRunnable() {
            override fun run() {
                val inventory = holder.backingInventory
                if (inventory == null || !player.isOnline || player.openInventory.topInventory != inventory) {
                    stopSpin(holder)
                    cancel()
                    return
                }

                if (holder.spinTicksUntilNextStep > 0) {
                    holder.spinTicksUntilNextStep--
                    return
                }

                holder.wheelOffset = (holder.wheelOffset + 1) % pool.size
                renderWheel(inventory, holder, holder.wheelOffset)
                player.playSound(GAMBLING_WHEEL_TICK)
                holder.spinStepsDone++

                val progress = holder.spinStepsDone.toDouble() / holder.spinTargetSteps.toDouble()
                val easedProgress = progress * progress
                holder.spinTicksUntilNextStep = (minDelayTicks + (maxDelayTicks - minDelayTicks) * easedProgress)
                    .roundToInt()
                    .coerceAtLeast(minDelayTicks)

                if (holder.spinStepsDone >= holder.spinTargetSteps) {
                    stopSpin(holder)
                    player.playSound(GAMBLING_WHEEL_STOP)
                    awardPendingReward(holder, player, "stopped")
                    cancel()
                }
            }
        }

        holder.spinTask = task
        task.runTaskTimer(plugin, 0L, 1L)
    }

    private fun stopSpin(holder: GamblingWindowHolder) {
        holder.spinTask?.cancel()
        holder.spinTask = null
        holder.isSpinning = false
        holder.spinStepsDone = 0
        holder.spinTicksUntilNextStep = 0
        holder.spinTargetSteps = 0
    }

    private fun renderWheel(inventory: Inventory, holder: GamblingWindowHolder, offset: Int) {
        val pool = holder.crateType.lootPool.possibleItems
        if (pool.isEmpty()) return

        for ((index, slot) in wheelSlots.withIndex()) {
            val crateItem = pool[(offset + index) % pool.size]
            inventory.setItem(slot, crateItem.itemStack.clone())
        }
    }

    private fun pickWeightedItem(pool: List<CrateItem>): CrateItem {
        val sanitizedWeights = pool.map { it.pctChanceToRoll.coerceAtLeast(0) }
        val totalWeight = sanitizedWeights.sum()

        if (totalWeight <= 0) {
            return pool.random()
        }

        var roll = Random.nextInt(totalWeight)
        for (index in pool.indices) {
            roll -= sanitizedWeights[index]
            if (roll < 0) return pool[index]
        }

        return pool.last()
    }

    private fun mod(value: Int, divisor: Int): Int {
        val remainder = value % divisor
        return if (remainder < 0) remainder + divisor else remainder
    }

    private fun awardPendingReward(holder: GamblingWindowHolder, player: Player, reason: String) {
        if (holder.rewardGranted) return

        val winner = holder.winningItem
        if (winner == null) {
            player.sendMessage(
                Formatting.allTags.deserialize("<red>No reward could be resolved for the ")
                    .append(holder.crateType.displayName)
                    .append(Formatting.allTags.deserialize("<red>."))
            )
            holder.rewardGranted = true
            return
        }

        val leftovers = player.inventory.addItem(winner.itemStack.clone())
        for (leftover in leftovers.values) {
            player.world.dropItemNaturally(player.location, leftover)
        }

        if (reason == "closed early") {
            player.sendMessage(
                Formatting.allTags.deserialize("<yellow>You closed the wheel early — you still won a ")
                    .append(winner.itemStack.displayName())
                    .append(Formatting.allTags.deserialize("<yellow> from the "))
                    .append(holder.crateType.displayName)
                    .append(Formatting.allTags.deserialize("<yellow>."))
            )
        } else {
            player.sendMessage(
                Formatting.allTags.deserialize("<green>You won a ")
                    .append(winner.itemStack.displayName())
                    .append(Formatting.allTags.deserialize("<green> from the "))
                    .append(holder.crateType.displayName)
                    .append(Formatting.allTags.deserialize("<green>!"))
            )
        }
        holder.rewardGranted = true
    }

    private fun renderInitialLayout(inventory: Inventory) {
        for (slot in 0 until inventory.size) {
            inventory.setItem(slot, if (slot in wheelSlots) idleWheelItem else fillerPane)
        }
    }
}