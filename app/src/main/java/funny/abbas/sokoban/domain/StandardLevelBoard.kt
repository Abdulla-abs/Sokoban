package funny.abbas.sokoban.domain

class StandardLevelBoard {

    fun level1(): Level? {
        val map = arrayOf(
            arrayOf(
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall
            ),
            arrayOf(
                BoxType.Wall,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Wall
            ),
            arrayOf(
                BoxType.Wall,
                BoxType.Empty,
                BoxType.Wall,
                BoxType.Role,
                BoxType.Empty,
                BoxType.Wall
            ),
            arrayOf(
                BoxType.Wall,
                BoxType.Empty,
                BoxType.Box,
                BoxType.Box,
                BoxType.Empty,
                BoxType.Wall
            ),
            arrayOf(
                BoxType.Wall,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Box,
                BoxType.Empty,
                BoxType.Wall
            ),
            arrayOf(
                BoxType.Wall,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Wall
            ),
            arrayOf(
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall,
                BoxType.Wall
            )
        )

        var target = listOf(
            Location().absoluteLocation(4, 4), Location().absoluteLocation(4, 5),
            Location().absoluteLocation(3, 5)
        )

        return LevelMapper.mapper(map, target)
    }
}
