-- -*- sql-product: sqlite; -*-

SELECT id, north, south, outcome, start, end
FROM game
ORDER BY start
LIMIT ?2
OFFSET ?1 * ?2

