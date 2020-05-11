package map;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

public class GraphFromMap
{
    private LevelMap map;

    GraphFromMap(LevelMap map) {
        this.map = map;
    }

    public ArrayList<BlockMap> getPathTo(BlockMap start, BlockMap target) {
        var blocksMap = map.getMap();
        var directions = new Pair[]{
                new Pair<>(0, 1),
                new Pair<>(0, -1),
                new Pair<>(1, 0),
                new Pair<>(-1, 0)
        };
        var visited = new ArrayList<BlockMap>();
        var previous = new BlockMap[map.getHeight()][map.getWidth()];
        var queue = new ArrayList<BlockMap>();
        queue.add(start);
        while (true) {
            var flag = false;
            var newQueue = new ArrayList<BlockMap>();
            for (BlockMap block : queue)
            {
                for (Pair direction : directions)
                {
                    var x = block.getM_positionX() + (int)direction.getKey();
                    var y = block.getM_positionY() + (int)direction.getValue();
                    if (x < 0 || x >= map.getWidth()
                    || y < 0 || y>= map.getHeight())
                        continue;
                    var nextBlock = blocksMap[y][x];
                    if (!nextBlock.isAvailableForRobot())
                        continue;
                    if (visited.contains(nextBlock))
                        continue;
                    newQueue.add(nextBlock);
                    previous[y][x] = block;
                    if (nextBlock == target)
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    break;
                visited.add(block);
            }
            if (flag)
                break;
            queue = newQueue;
        }
        var path = new ArrayList<BlockMap>();
        var block = target;
        path.add(target);
        while (block != start)
        {
            block = previous[block.getM_positionY()][block.getM_positionX()];
            path.add(block);
        }
        Collections.reverse(path);
        return path;
    }
}

