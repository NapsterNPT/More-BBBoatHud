# More BBBoatHud

![Downloads](https://img.shields.io/modrinth/dt/more-bbboathud?logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjRweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyNHB4IiBmaWxsPSIjZTNlM2UzIj48cGF0aCBkPSJNNDgwLTMyMCAyODAtNTIwbDU2LTU4IDEwNCAxMDR2LTMyNmg4MHYzMjZsMTA0LTEwNCA1NiA1OC0yMDAgMjAwWk0yNDAtMTYwcS0zMyAwLTU2LjUtMjMuNVQxNjAtMjQwdi0xMjBoODB2MTIwaDQ4MHYtMTIwaDgwdjEyMHEwIDMzLTIzLjUgNTYuNVQ3MjAtMTYwSDI0MFoiLz48L3N2Zz4=&style=for-the-badge&color=blue) ![Release](https://img.shields.io/github/v/release/NapsterNPT/more-bbboathud?logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iMjRweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSIyNHB4IiBmaWxsPSIjZTNlM2UzIj48cGF0aCBkPSJNMzUyLjUtMzI1LjVRMjk4LTM3MSAyODQtNDQwSDgwdi04MGgyMDRxMTQtNjkgNjguNS0xMTQuNVQ0ODAtNjgwcTczIDAgMTI3LjUgNDUuNVQ2NzYtNTIwaDIwNHY4MEg2NzZxLTE0IDY5LTY4LjUgMTE0LjVUNDgwLTI4MHEtNzMgMC0xMjcuNS00NS41Wk00ODAtMzYwcTUwIDAgODUtMzV0MzUtODVxMC01MC0zNS04NXQtODUtMzVxLTUwIDAtODUgMzV0LTM1IDg1cTAgNTAgMzUgODV0ODUgMzVaIi8+PC9zdmc+&style=for-the-badge)

Addon for the mod [BBBoatHud](https://modrinth.com/mod/bb_boat_hud) (required for this mod to work).

### Variables

| Variable             | Description                          |
|----------------------|--------------------------------------|
| `pressingSpace`      | Space is held                        |
| `pressingLeftClick`  | Left click is held                   |
| `pressingRightClick` | Right click is held                  |

### Functions

| Function                                                      | Returns | Description                                |
|---------------------------------------------------------------|---------|--------------------------------------------|
| `print(message)`                                              | -       | Prints a message to chat                   |
| `renderButton(width, height, canBePressed, anchor, function)` | -       | Draws a clickable button                   |
| `onMenuOpen(anchor, function)`                                |         | Run the function when the menu gets open   |
| `onMenuClose(function)`                                       |         | Run the function when the menu gets closed |
| `getWidth("path")`                                            | number  | Width of a texture in pixels               |
| `getHeight("path")`                                           | number  | Height of a texture in pixels              |
