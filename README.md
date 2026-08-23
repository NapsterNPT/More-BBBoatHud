# More BBBoatHud

![Downloads](https://img.shields.io/modrinth/dt/more-bbboathud?logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjRweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyNHB4IiBmaWxsPSIjZTNlM2UzIj48cGF0aCBkPSJNNDgwLTMyMCAyODAtNTIwbDU2LTU4IDEwNCAxMDR2LTMyNmg4MHYzMjZsMTA0LTEwNCA1NiA1OC0yMDAgMjAwWk0yNDAtMTYwcS0zMyAwLTU2LjUtMjMuNVQxNjAtMjQwdi0xMjBoODB2MTIwaDQ4MHYtMTIwaDgwdjEyMHEwIDMzLTIzLjUgNTYuNVQ3MjAtMTYwSDI0MFoiLz48L3N2Zz4=&style=for-the-badge&color=blue) ![Release](https://img.shields.io/github/v/release/NapsterNPT/more-bbboathud?logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjRweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyNHB4IiBmaWxsPSIjZTNlM2UzIj48cGF0aCBkPSJNMzUyLjUtMzI1LjVRMjk4LTM3MSAyODQtNDQwSDgwdi04MGgyMDRxMTQtNjkgNjguNS0xMTQuNVQ0ODAtNjgwcTczIDAgMTI3LjUgNDUuNVQ2NzYtNTIwaDIwNHY4MEg2NzZxLTE0IDY5LTY4LjUgMTE0LjVUNDgwLTI4MHEtNzMgMC0xMjcuNS00NS41Wk00ODAtMzYwcTUwIDAgODUtMzV0MzUtODVxMC01MC0zNS04NXQtODUtMzVxLTUwIDAtODUgMzV0LTM1IDg1cTAgNTAgMzUgODV0ODUgMzVaIi8+PC9zdmc+&style=for-the-badge)

Adds `pressingSpace`, `pressingLeftClick`, `pressingRightClick`, and `menuOpen` to [BBBoatHud](https://modrinth.com/mod/bb_boat_hud) that is required for this mod to work.

## About

BBBoatHud exposes boat input states like `pressingForward`, `pressingBack`, `pressingLeft`, `pressingRight` to Lua modules, but doesn't track jump/attack/use inputs. This addon fills that gap.

### Variables

| Variable             | Description                                        |
|----------------------|----------------------------------------------------|
| `pressingSpace`      | Space/jump key is held                             |
| `pressingLeftClick`  | Left click/attack key is held                      |
| `pressingRightClick` | Right click/use key is held                        |
| `menuOpen`           | True while the Boat HUD Button key (unbound by default — set it under **Options → Controls → More BBBoatHud**) is held; while held the mouse cursor is unlocked so you can use the mouse |

### Functions

| Function                   | Returns      | Description                                |
|----------------------------|--------------|--------------------------------------------|
| `print(message)`           | —            | Prints a message to chat                   |
| `renderButton(width, height, canBePressed, anchor, callback)` | — | Draws a clickable button; runs `callback` when clicked while the Boat HUD Button key is held (mouse is unlocked then) |
| `getWidth("path")`         | number       | Width of a texture in pixels (0 if missing)|
| `getHeight("path")`        | number       | Height of a texture in pixels (0 if missing)|

The `print()` function shows a blue underlined message in chat. Hover to see which module sent it.

`renderButton` anchors like `renderRect` (`"TOP_LEFT"`, `"MIDDLE_CENTER"`, `"BOTTOM_RIGHT"`...). The anchor is relative to the module's configured placement position (same convention as all bb_boat_hud render functions), so moving/scaling/rotating the module in the config moves both the button and its clickable area. The button uses the vanilla Minecraft button texture (normal / highlighted on hover / disabled when `canBePressed` is false), and `callback` fires on click.

Texture paths work the same as `renderTexture` — relative to `textures/bb_boat_hud_modules/` in the module's namespace.

```lua
local w = getWidth("my_texture.png")
local h = getHeight("my_texture.png")
```

## Included modules

After installing, go to the **Add Modules** tab in `/bb_boat_hud` and find the `morebbboathud` namespace:

- **button_test** — shows two vanilla-style buttons while the Boat HUD Button key is held; exactly one is clickable at a time, and clicking the active one swaps which button is enabled

## How to configure

Use `/bb_boat_hud` to open the web configuration interface, then:

1. Go to the **Add Modules** tab
2. Browse to the `morebbboathud` namespace
4. Switch to **Active Modules** to adjust position, scale, rotation, and anchor

## License

GNU General Public License v3.0
