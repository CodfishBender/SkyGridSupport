# SkyGridSupport

A lightweight Paper plugin that adds quality-of-life features for SkyGrid servers:

- **Random Teleport** — `/gridrtp` sends a player to a random safe location in any world.
- **Brush Archaeology** — using a Brush on Suspicious Sand or Suspicious Gravel converts the block, plays the completion sound, and drops loot from vanilla archaeology loot tables.
- **Protection Integration** — optionally respects [GriefPrevention](https://github.com/TechFortress/GriefPrevention) claims and [WorldGuard](https://enginehub.org/worldguard) regions.

---

## Commands

| Command | Description |
|---|---|
| `/gridrtp <world>` | Teleport yourself to a random location in `<world>`. |
| `/gridrtp <world> <player>` | Teleport `<player>` to a random location in `<world>`. Required when run from console. |

### How the teleport works

1. A random X/Z coordinate is chosen within the world border.
2. Y is set to **124** (the top block height for standard SkyGrid generation).
3. A 5×5 area around the initial point is searched for a block that is not **AIR** or **LAVA**.
4. If a valid block is found the player is teleported two blocks above it; otherwise the attempt fails.
5. The command retries up to **10 times** before giving up.

---

## Permissions

| Permission | Description | Default |
|---|---|---|
| `SkyGridSupport.RTP` | Allows use of the `/gridrtp` command. | OP |

> **Note:** This permission is checked in code but is not explicitly declared in `plugin.yml`. Grant it through your permissions plugin (e.g. LuckPerms) to the desired players or groups.

---

## Placeholders

This plugin does **not** register any PlaceholderAPI placeholders. No `%placeholder%` values are available, and PlaceholderAPI is not required.

---

## Brush Archaeology

When a player right-clicks a suspicious block while holding a **Brush**, the plugin:

1. Checks GriefPrevention / WorldGuard permissions (if those plugins are present).
2. Converts the block back to its normal variant and plays the brushing-complete sound.
3. Rolls a random loot table and drops the resulting items on top of the block.

### Loot tables

**Suspicious Gravel**

| Chance | Loot Table |
|---|---|
| 50 % | `OCEAN_RUIN_COLD_ARCHAEOLOGY` |
| 40 % | `TRAIL_RUINS_ARCHAEOLOGY_COMMON` |
| 10 % | `TRAIL_RUINS_ARCHAEOLOGY_RARE` |

**Suspicious Sand**

| Chance | Loot Table |
|---|---|
| ~33 % | `DESERT_WELL_ARCHAEOLOGY` |
| ~33 % | `OCEAN_RUIN_WARM_ARCHAEOLOGY` |
| ~34 % | `DESERT_PYRAMID_ARCHAEOLOGY` |

---

## Optional Dependencies

| Plugin | Purpose |
|---|---|
| [GriefPrevention](https://github.com/TechFortress/GriefPrevention) | Prevents brush loot from dropping inside claims where the player cannot build. |
| [WorldGuard](https://enginehub.org/worldguard) | Prevents brush loot from dropping in regions where `BLOCK_BREAK` is denied (unless the player has bypass). |

Both are **soft dependencies** — the plugin works fine without them.

---

## Compatibility

| Property | Value |
|---|---|
| API version | `1.20` |
| Built against | Paper API `1.21.11-R0.1-SNAPSHOT` |
| Java target | `1.8` |

---

## Building

```bash
mvn clean package
```

The shaded jar will be output to the `target/` directory.

## Installation

1. Place the jar in your server's `plugins/` folder.
2. Restart (or reload) the server.
3. *(Optional)* Install GriefPrevention and/or WorldGuard for protection-aware brush behaviour.
4. Grant the `SkyGridSupport.RTP` permission to any players or groups that should use `/gridrtp`.

