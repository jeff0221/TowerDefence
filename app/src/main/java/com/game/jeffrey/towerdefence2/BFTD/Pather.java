package com.game.jeffrey.towerdefence2.BFTD;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hans on 13-12-2016.
 */

public class Pather
{
    private List<ItemEntity> path = new ArrayList<>();
    private List<ItemEntity> visited = new LinkedList<>();
    private List<ItemEntity> next = new ArrayList<>();

    private WorldMap map;
    private ItemEntity lastVisited;

    private boolean foundExit = false;

    public void calculatePath(WorldMap map) {
        this.map = map;
        foundExit = false;

        ItemEntity first = map.grid[map.pathStartX][map.pathStartY];

        next.add(first);
        int x;
        int y;

        lastVisited = first;

        while (!foundExit && next.size() > 0) {
            ItemEntity current = next.get(0);
            next.remove(0);
            visited.add(current);
            lastVisited = current;

            x = current.arrayX;
            y = current.arrayY;

            if (current.type == ItemEntity.typeOfItem.GoalPoint)
            {
                Log.d("pather", "GOAL! at: " + x + ", " + y);
                foundExit = true;
            } else
            {

                lookAt(x - 1, y);
                lookAt(x + 1, y);
                lookAt(x, y - 1);
                lookAt(x, y + 1);
            }
        }

        if (!foundExit)
        {
            Log.d("pather", "no exit found!");
        } else {
            ItemEntity goal = null;

            int max = visited.size() - 1;

            for (int i = max; i > 0; i--) {
                if (visited.get(i).type == ItemEntity.typeOfItem.GoalPoint) {
                    goal = visited.get(i);
                    break;
                }
            }

            traversePath(goal);

            Collections.reverse(path);
            Log.d("full path", path.toString());

        }

    }

    private void traversePath(ItemEntity block) {
        path.add(block);
        if (block.cameFrom != null) {
            traversePath(block.cameFrom);
        }
    }

    private void lookAt(int x, int y) {
        if (isPossible(x, y)) {
            next.add(map.grid[x][y]);
        }
    }

    private boolean isPossible(int x, int y) {
        if (x > 0 && x < map.gridWidth && y > 0 && y < map.gridHeight) {
            ItemEntity current = map.grid[x][y];
            if (!visited.contains(current) && !next.contains(current))
            {
                if (current.type != ItemEntity.typeOfItem.Wall && current.type != ItemEntity.typeOfItem.Tower)
                {
                    current.cameFrom = lastVisited;
                    return true;
                }
            }
        }

        return false;
    }

    public List<ItemEntity> getPath()
    {
        return this.path;
    }
}
