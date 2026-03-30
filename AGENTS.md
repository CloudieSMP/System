# AGENTS.md — Cloudie SMP System Plugin

A Paper 1.21.11 plugin for Cloudie SMP Season 10, written in Kotlin. It acts as the all-in-one server management plugin: commands, chat, crates, homes, mail, resource packs, and Discord integration.

## Build & Run

```bash
./gradlew shadowJar          # Build fat JAR → build/libs/system-INDEV-Build-<hash>-all.jar
./gradlew runServer          # Spin up a local Paper test server under run/
```

- Version is auto-derived from the short git commit hash (`INDEV-Build-<hash>`).
- The shadow JAR **must** be used (not the plain JAR) — Cloud, Configurate, and FastBoard are relocated into `moe.oof.system.shade.*`.
- Relocated packages: `org.incendo`, `org.spongepowered`, `fr.mrmicky`.
- Java toolchain: **JVM 21**.

## Architecture

```
System.kt              — JavaPlugin entry point; wires events, Cloud command manager, config
Config.kt              — Spongepowered Configurate data class (mapped from src/main/resources/config.yml)
command/               — One class per command, all discovered via Cloud's annotationParser.parseContainers()
event/                 — Bukkit event listeners (player/, block/, entity/)
library/               — Stateful singletons: HomeStorage, MailStorage, CardPullCounterStorage, VanishHelper
item/                  — Enums for rarities/types; crate/, booster/, binder/ sub-packages
util/                  — Extensions, Keys registry, UI windows
chat/                  — MiniMessage formatting, notifications, ChatUtility broadcasts
```

## Adding a Command

1. Create a class in `command/` annotated with `@CommandContainer`.
2. Annotate methods with `@Command`, `@Permission`, `@CommandDescription`.
3. Use `css.requirePlayer()` (extension in `util/CommandSourceStackExtensions.kt`) to guard player-only commands.
4. No registration needed — `annotationParser.parseContainers()` in `System.kt` auto-discovers all `@CommandContainer` classes via kapt.
5. Declare the permission node in `src/main/resources/paper-plugin.yml` and add it to the appropriate group.

Example skeleton:
```kotlin
@Suppress("unused")
@CommandContainer
class MyCommand {
    @Command("mycommand")
    @CommandDescription("Does something cool.")
    @Permission("cloudie.cmd.mycommand")
    fun run(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return
        player.sendMessage(Formatting.allTags.deserialize("<cloudiecolor>Hello!"))
    }
}
```

## Text Formatting

Always use `Formatting.allTags` for trusted/system messages and `Formatting.restrictedTags` for player-input messages.

Custom MiniMessage tags available:
| Tag | Meaning |
|-----|---------|
| `<cloudiecolor>` | Pink brand colour (#C45889) |
| `<notifcolor>` | Notification red (#DB0060) |
| `<prefix:NAME>` | Unicode glyph prefix (e.g. `admin`, `dev`, `live`, `warning`) |
| `<skull:PLAYERNAME>` | Player skull glyph |

Hardcoded join/quit message templates live in `library/Translation.kt`.

## Storage Pattern

`HomeStorage`, `MailStorage`, and `CardPullCounterStorage` all follow the same pattern:
- In-memory `ConcurrentHashMap` cache per player UUID.
- **Async reads** via callback: `HomeStorage.listHomeNamesAsync(uuid) { names -> ... }` (runs on a Bukkit async thread, callback on same thread).
- **Sync flush** on plugin disable: `HomeStorage.flushAllSync()`.
- Data files live in `plugins/System/homes/<uuid>.yml`, `plugins/System/mail/<uuid>.yml`, etc.
- Call `preload(uuid)` on player join to warm the cache early (done in `PlayerJoin.kt`).

## Item System

- All `NamespacedKey` values are centralized in `util/Keys.kt`.
- Custom item models use `DataComponentTypes.ITEM_MODEL` with keys like `NamespacedKey("cloudie", "crates/blue")` — the path maps to the server resource pack.
- `ItemRarity` holds display color + Unicode glyph; `CardRarity` extends this with weighted drop probabilities and broadcast behavior.
- Crate items are `Material.PAPER` with food/consumable data components to make them right-click-activatable without placing.

## UI Windows

Inventory GUIs use the **Noxcrew Interfaces** library (`util/ui/`) and the `GamblingWindow`/`CrateBrowserWindow`/`BinderWindow` pattern:
- Each window is an `object : Listener` registered at startup.
- Uses a custom `InventoryHolder` to attach state to the inventory.

## Config

Config is loaded via Spongepowered Configurate from `src/main/resources/config.yml` into the `Config` data class. Access it via `plugin.config` (the field is named `config` on the `System` class, shadowing `JavaPlugin.getConfig()`). Reload at runtime with `/cloudie reload` (permission `cloudie.cmd.reload`).

## External Integrations

| Integration | Where |
|-------------|-------|
| Discord reports webhook | `util/DiscordWebhook.kt` — Ktor CIO, URL in `config.discord.reportWebhookUrl` |
| Resource pack CDN | `util/ResourcePacker.kt` — downloads & SHA-1 hashes packs on startup; reapplied on join |
| FastBoard (scoreboard) | `fr.mrmicky:fastboard` — relocated |

## Top-level Convenience Accessors

`plugin` and `logger` are top-level `val`s (defined in `System.kt`) that delegate to the plugin instance — use them freely anywhere instead of passing the plugin reference around.