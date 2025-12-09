package funny.abbas.sokoban.core

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
                BoxType.Empty,
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
                BoxType.Empty,
                BoxType.Empty,
                BoxType.Empty,
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

        val boxes = listOf(
            Location().absoluteLocation(3, 4), Location().absoluteLocation(4, 4),
            Location().absoluteLocation(4, 5)
        )
        val targets = listOf(
            Location().absoluteLocation(4, 4), Location().absoluteLocation(4, 5),
            Location().absoluteLocation(3, 5)
        )
        val role = Location().absoluteLocation(4, 3)

        return LevelMapper.mapper(map,role,boxes,targets)
    }
}
