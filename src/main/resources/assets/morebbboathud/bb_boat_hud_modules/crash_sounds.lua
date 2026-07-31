if not touchedBlockLastFrame then
    touchedBlockLastFrame = false
end

if not touchedEntityLastFrame then
    touchedEntityLastFrame = false
end

if touchingBlock and not touchedBlockLastFrame then
    playSound("metal_pipe", 1, 1)
end

if touchingEntity and not touchedEntityLastFrame then
    playSound("vineboom", 1, 1)
end

touchedBlockLastFrame = touchingBlock
touchedEntityLastFrame = touchingEntity
