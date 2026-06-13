# More BBBoatHud

![Downloads](https://img.shields.io/modrinth/dt/more-bbboathud?logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjRweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyNHB4IiBmaWxsPSIjZTNlM2UzIj48cGF0aCBkPSJNNDgwLTMyMCAyODAtNTIwbDU2LTU4IDEwNCAxMDR2LTMyNmg4MHYzMjZsMTA0LTEwNCA1NiA1OC0yMDAgMjAwWk0yNDAtMTYwcS0zMyAwLTU2LjUtMjMuNVQxNjAtMjQwdi0xMjBoODB2MTIwaDQ4MHYtMTIwaDgwdjEyMHEwIDMzLTIzLjUgNTYuNVQ3MjAtMTYwSDI0MFoiLz48L3N2Zz4=&style=for-the-badge&color=blue) ![Release](https://img.shields.io/github/v/release/NapsterNPT/more-bbboathud?logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjRweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyNHB4IiBmaWxsPSIjZTNlM2UzIj48cGF0aCBkPSJNMzUyLjUtMzI1LjVRMjk4LTM3MSAyODQtNDQwSDgwdi04MGgyMDRxMTQtNjkgNjguNS0xMTQuNVQ0ODAtNjgwcTczIDAgMTI3LjUgNDUuNVQ2NzYtNTIwaDIwNHY4MEg2NzZxLTE0IDY5LTY4LjUgMTE0LjVUNDgwLTI4MHEtNzMgMC0xMjcuNS00NS41Wk00ODAtMzYwcTUwIDAgODUtMzV0MzUtODVxMC01MC0zNS04NXQtODUtMzVxLTUwIDAtODUgMzV0LTM1IDg1cTAgNTAgMzUgODV0ODUgMzVaIi8+PC9zdmc+&style=for-the-badge)

Adds `pressingSpace`, `pressingLeftClick`, and `pressingRightClick` to [BBBoatHud](https://modrinth.com/mod/bb_boat_hud) that is required for this mod to work.

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
