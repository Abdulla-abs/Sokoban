package funny.abbas.sokoban.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import funny.abbas.sokoban.domain.LevelVo;
import funny.abbas.sokoban.domain.Location;

/**
 * Parser for Sokoban .levels files (e.g., YASS format).
 * Parses multiple levels from file content string.
 * Coordinates: Location(x=col, y=row), 0-based, top-left origin.
 * Handles symbols: #=wall, space=floor, .=goal, $=box, *=box-on-goal, @=player, +=player-on-goal.
 */
public class SokobanParser {

    /**
     * Parses the entire file content into a list of SokobanLevel objects.
     * @param fileContent Full string content of the .levels file.
     * @return Immutable list of parsed levels (in order, typically 88 levels).
     */
    public static List<LevelVo> parseLevels(String fileContent) {
        List<String> lines = Arrays.asList(fileContent.split("\n"));
        int i = 0;
        List<LevelVo> levels = new ArrayList<>();
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.startsWith(";LEVEL ")) {
                i++;  // Skip ;LEVEL N
                List<String> levelLines = new ArrayList<>();
                while (i < lines.size()) {
                    String l = lines.get(i).trim();
                    if (l.startsWith(";LEVEL ")) {
                        break;  // Next level
                    }
                    if (!l.isEmpty()) {
                        levelLines.add(lines.get(i));  // Add original line (for padding)
                    }
                    i++;
                }
                if (levelLines.isEmpty()) {
                    continue;
                }

                // Compute max width (rstrip spaces)
                int maxW = 0;
                for (String row : levelLines) {
                    int rlen = row.length();
                    while (rlen > 0 && row.charAt(rlen - 1) == ' ') {
                        rlen--;
                    }
                    maxW = Math.max(maxW, rlen);
                }

                int height = levelLines.size();
                int width = maxW;

                // Build char grid (pad right with spaces)
                char[][] grid = new char[height][width];
                for (int r = 0; r < height; r++) {
                    String rowStr = levelLines.get(r);
                    for (int c = 0; c < width; c++) {
                        grid[r][c] = (c < rowStr.length()) ? rowStr.charAt(c) : ' ';
                    }
                }

                // Parse baseMap, goals, boxes, player
                int[][] baseMap = new int[height][width];
                List<Location> goalsList = new ArrayList<>();
                List<Location> boxesList = new ArrayList<>();
                Location playerLoc = null;

                for (int r = 0; r < height; r++) {
                    for (int c = 0; c < width; c++) {
                        char ch = grid[r][c];
                        baseMap[r][c] = (ch == '#') ? 1 : 0;

                        // Goals: ., *, +
                        if (ch == '.' || ch == '*' || ch == '+') {
                            goalsList.add(new Location(c, r));  // x=col, y=row
                        }
                        // Boxes: $, *
                        if (ch == '$' || ch == '*') {
                            boxesList.add(new Location(c, r));
                        }
                        // Player: @, +
                        if (ch == '@' || ch == '+') {
                            playerLoc = new Location(c, r);
                        }
                    }
                }


                levels.add(new LevelVo(baseMap,playerLoc,boxesList,goalsList));
            } else {
                i++;
            }
        }
        return Collections.unmodifiableList(levels);
    }
}
