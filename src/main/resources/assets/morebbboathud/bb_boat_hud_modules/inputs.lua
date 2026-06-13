runOutOfBoat = true

local activeColor = 0xFF00FF00
local inactiveColor = 0x66666666
local labelColor = 0xFFCCCCCC
local rowHeight = 12
local padding = 2

local items = {
    { label = "SPACE",  var = pressingSpace },
    { label = "LCLICK", var = pressingLeftClick },
    { label = "RCLICK", var = pressingRightClick },
}

local totalHeight = #items * (rowHeight + padding) + padding
local totalWidth = 70

renderRect(totalWidth, totalHeight, 0x88000000, "TOP_LEFT")

for i, item in ipairs(items) do
    local y = padding + (i - 1) * (rowHeight + padding)

    offsetPosition(padding, y, function()
        local color
        if item.var then
            color = activeColor
        else
            color = inactiveColor
        end

        renderRect(rowHeight, rowHeight, color, "TOP_LEFT")

        offsetPosition(rowHeight + padding, 0, function()
            renderText(item.label, labelColor, false, "TOP_LEFT")
        end)
    end)
end
