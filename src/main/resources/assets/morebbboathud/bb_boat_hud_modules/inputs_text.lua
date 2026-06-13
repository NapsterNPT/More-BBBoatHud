runOutOfBoat = true

local active = 0xFF00FF00
local inactive = 0xFF555555

local function inputText(label, isPressed)
    if isPressed then renderText("[" .. label .. "]" , active, false, "TOP_LEFT")
    else renderText(" " .. label .. " " , inactive, false, "TOP_LEFT") end
end

inputText("SPACE", pressingSpace)
offsetPosition(0, 10, function()
    inputText("LCLICK", pressingLeftClick)
end)
offsetPosition(0, 20, function()
    inputText("RCLICK", pressingRightClick)
end)
