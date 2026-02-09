package leetcode.trees;

/*
Given a 2D grid where '1' represents land and '0' represents water,
count and return the number of islands.

An island is formed by connecting adjacent lands horizontally or vertically and is surrounded by water.
You may assume water is surrounding the grid (i.e., all the edges are water).

Example 1:
    Input: grid = [
        ["0","1","1","1","0"],
        ["0","1","0","1","0"],
        ["1","1","0","0","0"],
        ["0","0","0","0","0"]
      ]
    Output: 1

Example 2:
    Input: grid = [
        ["1","1","0","0","1"],
        ["1","1","0","0","1"],
        ["0","0","1","0","0"],
        ["0","0","0","1","1"]
      ]
    Output: 4

Constraints:
    1 <= grid.length, grid[i].length <= 100
    grid[i][j] is '0' or '1'.
 */
public class NumberOfIslands {
    /*
    Understanding the question:
        The idea is that once we find a piece of land, we want to:
            - Increment our land count
            - Mark every node of that piece of land with a zero (to prevent duplicate counts)
     */

    /*
    Option 1:
        Use recursion to explore where land is.
        We iterate the entire 2D array.
        When we find a 1, we kick off a series of recursive calls to check up/down/left/right (all marking 1s as 0s)
        Increment our count once per new island found.
        This is our DFS approach.
     */
    public static int numIslands(char[][] grid) {
        int numIslands = 0;

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == '1') {
                    sinkIslandDFS(grid, x, y);
                    numIslands++;
                }
            }
        }

        return numIslands;
    }

    public static void sinkIslandDFS(char[][] grid, int x, int y) {
        grid[x][y] = '0';

        // Left
        if (x - 1 >= 0 && grid[x - 1][y] == '1')
            sinkIslandDFS(grid, x - 1, y);

        // Right
        if (x + 1 < grid.length && grid[x + 1][y] == '1')
            sinkIslandDFS(grid, x + 1, y);

        // Up
        if (y - 1 >= 0 && grid[x][y - 1] == '1')
            sinkIslandDFS(grid, x, y - 1);

        // Down
        if (y + 1 < grid[x].length && grid[x][y + 1] == '1')
            sinkIslandDFS(grid, x, y + 1);
    }

    static void main() {
        char[][] input;

        input = new char[][]{
                {'0', '1', '1', '1', '0'},
                {'0', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };
        System.out.println(numIslands(input)); // Output: 1

        input = new char[][]{
                {'1', '1', '0', '0', '1'},
                {'1', '1', '0', '0', '1'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        System.out.println(numIslands(input)); // Output: 4

        input = new char[][]{
                {'1'},
                {'1'}
        };
        System.out.println(numIslands(input)); // Output: 1
    }
}
