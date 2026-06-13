# More BBBoatHud

Adds `pressingSpace`, `pressingLeftClick`, and `pressingRightClick` to [BBBoatHud](https://modrinth.com/mod/bb_boat_hud) Lua modules.

## About

BBBoatHud exposes boat input states like `pressingForward`, `pressingBack`, `pressingLeft`, `pressingRight` to Lua modules, but doesn't track jump/attack/use inputs. This addon fills that gap.

## Variables exposed to Lua

| Variable             |
|----------------------|
| `pressingSpace`      |
| `pressingLeftClick`  |
| `pressingRightClick` |

These are available in any Lua module — existing or custom.

## Included modules

After installing, go to the **Add Modules** tab in `/bb_boat_hud` and find the `morebbboathud` namespace:

- **inputs** — colored indicator blocks showing each key's pressed state
- **inputs_text** — simple text display with active/inactive coloring

Both modules render regardless of whether you're in a boat.

## How to configure

Use `/bb_boat_hud` to open the web configuration interface, then:

1. Go to the **Add Modules** tab
2. Browse to the `morebbboathud` namespace
3. Click **inputs** or **inputs_text** to add it to your HUD
4. Switch to **Active Modules** to adjust position, scale, rotation, and anchor

## License

GNU General Public License v3.0
