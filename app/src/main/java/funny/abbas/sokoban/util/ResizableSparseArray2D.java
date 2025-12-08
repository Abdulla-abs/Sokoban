package funny.abbas.sokoban.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 可动态调整大小的二维稀疏数组类
 * 支持在调整大小时尽量保留原有数据
 *
 * @param <T> 数组元素类型
 */
public class ResizableSparseArray2D<T> {
    // 存储非默认值的元素
    private final Map<Position, T> elements;
    // 默认值
    private final T defaultValue;
    // 当前行数
    private int rows;
    // 当前列数
    private int columns;

    /**
     * 内部类：表示二维位置
     */
    private static class Position {
        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    /**
     * 构造函数
     *
     * @param rows 初始行数
     * @param columns 初始列数
     * @param defaultValue 默认值
     */
    public ResizableSparseArray2D(int rows, int columns, T defaultValue) {
        validateDimensions(rows, columns);

        this.rows = rows;
        this.columns = columns;
        this.defaultValue = defaultValue;
        this.elements = new HashMap<>();
    }

    /**
     * 从二维数组创建稀疏数组
     *
     * @param array 二维数组
     * @param defaultValue 默认值
     * @return 稀疏数组实例
     */
    public static <T> ResizableSparseArray2D<T> fromArray(T[][] array, T defaultValue) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }

        int rows = array.length;
        int columns = array[0].length;
        ResizableSparseArray2D<T> sparseArray = new ResizableSparseArray2D<>(rows, columns, defaultValue);

        for (int i = 0; i < rows; i++) {
            if (array[i].length != columns) {
                throw new IllegalArgumentException("数组的每一行必须具有相同的列数");
            }

            for (int j = 0; j < columns; j++) {
                T value = array[i][j];
                if (!Objects.equals(value, defaultValue)) {
                    sparseArray.set(i, j, value);
                }
            }
        }

        return sparseArray;
    }

    /**
     * 设置指定位置的元素值
     *
     * @param row 行索引（0-based）
     * @param col 列索引（0-based）
     * @param value 值
     * @throws IndexOutOfBoundsException 如果索引超出当前范围
     */
    public void set(int row, int col, T value) {
        checkBounds(row, col);

        Position pos = new Position(row, col);

        if (Objects.equals(value, defaultValue)) {
            // 如果设置的值等于默认值，则从映射中移除
            elements.remove(pos);
        } else {
            // 否则存储该值
            elements.put(pos, value);
        }
    }

    /**
     * 安全设置指定位置的元素值（如果位置超出范围，自动扩容）
     *
     * @param row 行索引（0-based）
     * @param col 列索引（0-based）
     * @param value 值
     */
    public void setSafe(int row, int col, T value) {
        if (row < 0 || col < 0) {
            throw new IndexOutOfBoundsException("行和列索引不能为负数: row=" + row + ", col=" + col);
        }

        // 如果需要扩容
        if (row >= rows || col >= columns) {
            int newRows = Math.max(rows, row + 1);
            int newCols = Math.max(columns, col + 1);
            resize(newRows, newCols);
        }

        set(row, col, value);
    }

    /**
     * 获取指定位置的元素值
     *
     * @param row 行索引（0-based）
     * @param col 列索引（0-based）
     * @return 元素值，如果未设置则返回默认值
     * @throws IndexOutOfBoundsException 如果索引超出当前范围
     */
    public T get(int row, int col) {
        checkBounds(row, col);

        Position pos = new Position(row, col);
        return elements.getOrDefault(pos, defaultValue);
    }

    /**
     * 安全获取指定位置的元素值（如果位置超出范围，返回默认值）
     *
     * @param row 行索引（0-based）
     * @param col 列索引（0-based）
     * @return 元素值，如果未设置或超出范围则返回默认值
     */
    public T getSafe(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            return defaultValue;
        }

        Position pos = new Position(row, col);
        return elements.getOrDefault(pos, defaultValue);
    }

    /**
     * 检查指定位置是否已设置非默认值
     *
     * @param row 行索引（0-based）
     * @param col 列索引（0-based）
     * @return 如果已设置非默认值返回true，否则返回false
     * @throws IndexOutOfBoundsException 如果索引超出当前范围
     */
    public boolean isSet(int row, int col) {
        checkBounds(row, col);

        Position pos = new Position(row, col);
        return elements.containsKey(pos);
    }

    /**
     * 调整数组大小
     *
     * @param newRows 新的行数
     * @param newCols 新的列数
     * @param keepData 是否保留原有数据
     */
    public void resize(int newRows, int newCols, boolean keepData) {
        validateDimensions(newRows, newCols);

        if (keepData) {
            // 创建新映射，只保留在新范围内的数据
            Map<Position, T> newElements = new HashMap<>();

            for (Map.Entry<Position, T> entry : elements.entrySet()) {
                Position pos = entry.getKey();
                if (pos.row < newRows && pos.col < newCols) {
                    newElements.put(pos, entry.getValue());
                }
            }

            elements.clear();
            elements.putAll(newElements);
        } else {
            // 不保留数据，清空所有
            elements.clear();
        }

        this.rows = newRows;
        this.columns = newCols;
    }

    /**
     * 调整数组大小（默认保留数据）
     *
     * @param newRows 新的行数
     * @param newCols 新的列数
     */
    public void resize(int newRows, int newCols) {
        resize(newRows, newCols, true);
    }

    /**
     * 扩展行数
     *
     * @param additionalRows 要增加的行数
     */
    public void expandRows(int additionalRows) {
        if (additionalRows <= 0) {
            throw new IllegalArgumentException("增加的行数必须为正数");
        }
        resize(rows + additionalRows, columns, true);
    }

    /**
     * 扩展列数
     *
     * @param additionalCols 要增加的列数
     */
    public void expandColumns(int additionalCols) {
        if (additionalCols <= 0) {
            throw new IllegalArgumentException("增加的列数必须为正数");
        }
        resize(rows, columns + additionalCols, true);
    }

    /**
     * 缩减行数
     *
     * @param rowsToRemove 要减少的行数
     * @param keepData 是否尝试保留可保留的数据
     */
    public void shrinkRows(int rowsToRemove, boolean keepData) {
        if (rowsToRemove <= 0) {
            throw new IllegalArgumentException("减少的行数必须为正数");
        }
        if (rowsToRemove >= rows) {
            throw new IllegalArgumentException("减少的行数不能大于或等于当前行数");
        }
        resize(rows - rowsToRemove, columns, keepData);
    }

    /**
     * 缩减列数
     *
     * @param colsToRemove 要减少的列数
     * @param keepData 是否尝试保留可保留的数据
     */
    public void shrinkColumns(int colsToRemove, boolean keepData) {
        if (colsToRemove <= 0) {
            throw new IllegalArgumentException("减少的列数必须为正数");
        }
        if (colsToRemove >= columns) {
            throw new IllegalArgumentException("减少的列数不能大于或等于当前列数");
        }
        resize(rows, columns - colsToRemove, keepData);
    }

    /**
     * 获取行数
     *
     * @return 行数
     */
    public int getRows() {
        return rows;
    }

    /**
     * 获取列数
     *
     * @return 列数
     */
    public int getColumns() {
        return columns;
    }

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * 获取非默认值元素的数量
     *
     * @return 非默认值元素数量
     */
    public int getNonDefaultCount() {
        return elements.size();
    }

    /**
     * 获取稀疏度（非默认值元素占总元素的比例）
     *
     * @return 稀疏度（0.0到1.0之间）
     */
    public double getSparsity() {
        long totalElements = (long) rows * columns;
        if (totalElements == 0) return 1.0;
        return (double) elements.size() / totalElements;
    }

    /**
     * 获取压缩率（1 - 稀疏度）
     *
     * @return 压缩率
     */
    public double getCompressionRatio() {
        return 1.0 - getSparsity();
    }

    /**
     * 获取所有非默认值元素的位置和值
     *
     * @return 包含所有非默认值元素的位置和值的映射
     */
    public Map<Position, T> getAllNonDefaultElements() {
        return new HashMap<>(elements);
    }

    /**
     * 获取所有非默认值元素的位置
     *
     * @return 位置集合
     */
    public Set<Position> getNonDefaultPositions() {
        return new HashSet<>(elements.keySet());
    }

    /**
     * 获取指定行中所有非默认值元素的列索引和值
     *
     * @param row 行索引
     * @return 列索引到值的映射
     */
    public Map<Integer, T> getRowElements(int row) {
        checkRow(row);

        return elements.entrySet().stream()
                .filter(entry -> entry.getKey().row == row)
                .collect(Collectors.toMap(
                        entry -> entry.getKey().col,
                        Map.Entry::getValue
                ));
    }

    /**
     * 获取指定列中所有非默认值元素的行索引和值
     *
     * @param col 列索引
     * @return 行索引到值的映射
     */
    public Map<Integer, T> getColumnElements(int col) {
        checkColumn(col);

        return elements.entrySet().stream()
                .filter(entry -> entry.getKey().col == col)
                .collect(Collectors.toMap(
                        entry -> entry.getKey().row,
                        Map.Entry::getValue
                ));
    }

    /**
     * 将稀疏数组转换为二维数组
     *
     * @param arrayClass 数组元素类型的Class对象
     * @return 二维数组
     */
    public T[][] toArray(Class<T> arrayClass) {
        @SuppressWarnings("unchecked")
        T[][] array = (T[][]) java.lang.reflect.Array.newInstance(arrayClass, rows, columns);

        // 用默认值填充数组
        for (int i = 0; i < rows; i++) {
            Arrays.fill(array[i], defaultValue);
        }

        // 设置非默认值
        for (Map.Entry<Position, T> entry : elements.entrySet()) {
            Position pos = entry.getKey();
            array[pos.row][pos.col] = entry.getValue();
        }

        return array;
    }

    /**
     * 清除指定位置的元素（设置为默认值）
     *
     * @param row 行索引（0-based）
     * @param col 列索引（0-based）
     */
    public void clear(int row, int col) {
        set(row, col, defaultValue);
    }

    /**
     * 清除所有元素（全部设置为默认值）
     */
    public void clearAll() {
        elements.clear();
    }

    /**
     * 获取子数组
     *
     * @param startRow 起始行（包含）
     * @param startCol 起始列（包含）
     * @param endRow 结束行（包含）
     * @param endCol 结束列（包含）
     * @return 子数组的稀疏表示
     */
    public ResizableSparseArray2D<T> getSubArray(int startRow, int startCol, int endRow, int endCol) {
        validateSubArrayBounds(startRow, startCol, endRow, endCol);

        int subRows = endRow - startRow + 1;
        int subCols = endCol - startCol + 1;
        ResizableSparseArray2D<T> subArray = new ResizableSparseArray2D<>(subRows, subCols, defaultValue);

        for (Map.Entry<Position, T> entry : elements.entrySet()) {
            Position pos = entry.getKey();
            if (pos.row >= startRow && pos.row <= endRow &&
                    pos.col >= startCol && pos.col <= endCol) {
                int newRow = pos.row - startRow;
                int newCol = pos.col - startCol;
                subArray.set(newRow, newCol, entry.getValue());
            }
        }

        return subArray;
    }

    /**
     * 将另一个稀疏数组合并到当前数组（相同位置会被覆盖）
     *
     * @param other 要合并的稀疏数组
     * @param offsetRow 行偏移量
     * @param offsetCol 列偏移量
     */
    public void merge(ResizableSparseArray2D<T> other, int offsetRow, int offsetCol) {
        if (other == null) return;

        // 检查是否需要扩容
        int maxRowNeeded = offsetRow + other.rows;
        int maxColNeeded = offsetCol + other.columns;

        if (maxRowNeeded > rows || maxColNeeded > columns) {
            int newRows = Math.max(rows, maxRowNeeded);
            int newCols = Math.max(columns, maxColNeeded);
            resize(newRows, newCols);
        }

        // 合并数据
        for (Map.Entry<Position, T> entry : other.elements.entrySet()) {
            Position pos = entry.getKey();
            int targetRow = pos.row + offsetRow;
            int targetCol = pos.col + offsetCol;

            if (targetRow < rows && targetCol < columns) {
                set(targetRow, targetCol, entry.getValue());
            }
        }
    }

    /**
     * 检查边界是否合法
     */
    private void checkBounds(int row, int col) {
        checkRow(row);
        checkColumn(col);
    }

    private void checkRow(int row) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException("行索引越界: " + row + "，有效范围: 0-" + (rows - 1));
        }
    }

    private void checkColumn(int col) {
        if (col < 0 || col >= columns) {
            throw new IndexOutOfBoundsException("列索引越界: " + col + "，有效范围: 0-" + (columns - 1));
        }
    }

    private void validateDimensions(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("行数和列数必须为正数: rows=" + rows + ", columns=" + columns);
        }
    }

    private void validateSubArrayBounds(int startRow, int startCol, int endRow, int endCol) {
        if (startRow < 0 || startRow >= rows || endRow < 0 || endRow >= rows || startRow > endRow) {
            throw new IllegalArgumentException("无效的行范围: [" + startRow + ", " + endRow + "]");
        }
        if (startCol < 0 || startCol >= columns || endCol < 0 || endCol >= columns || startCol > endCol) {
            throw new IllegalArgumentException("无效的列范围: [" + startCol + ", " + endCol + "]");
        }
    }

    /**
     * 获取字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResizableSparseArray2D[")
                .append(rows).append("x").append(columns)
                .append(", 非默认值元素: ").append(elements.size())
                .append(", 稀疏度: ").append(String.format("%.2f", getSparsity() * 100)).append("%")
                .append("]");

        return sb.toString();
    }

    /**
     * 获取矩阵的文本表示（适合小矩阵）
     *
     * @return 矩阵文本表示
     */
    public String toMatrixString() {
        StringBuilder sb = new StringBuilder();

        sb.append("尺寸: ").append(rows).append("x").append(columns).append("\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                T value = get(i, j);
                sb.append(value).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * 获取压缩的矩阵表示（只显示非默认值）
     *
     * @return 压缩的矩阵表示
     */
    public String toCompressedMatrixString() {
        if (elements.isEmpty()) {
            return "空数组 (所有元素均为默认值)\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("尺寸: ").append(rows).append("x").append(columns).append("\n");
        sb.append("非默认值元素 (").append(elements.size()).append(" 个):\n");

        // 按行分组显示
        Map<Integer, List<Position>> positionsByRow = elements.keySet().stream()
                .collect(Collectors.groupingBy(pos -> pos.row));

        List<Integer> sortedRows = new ArrayList<>(positionsByRow.keySet());
        Collections.sort(sortedRows);

        for (int row : sortedRows) {
            sb.append("  行 ").append(row).append(": ");
            List<Position> rowPositions = positionsByRow.get(row);
            rowPositions.sort(Comparator.comparingInt(p -> p.col));

            for (Position pos : rowPositions) {
                T value = elements.get(pos);
                sb.append("[").append(pos.col).append("]=").append(value).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * 示例使用方法
     */
    public static void main(String[] args) {
        System.out.println("=== 可调整大小的二维稀疏数组示例 ===\n");

        // 示例1：创建和基本操作
        System.out.println("示例1: 创建和基本操作");
        ResizableSparseArray2D<Integer> sparseArray = new ResizableSparseArray2D<>(5, 5, 0);

        sparseArray.set(0, 0, 1);
        sparseArray.set(2, 3, 5);
        sparseArray.set(4, 4, 9);

        System.out.println(sparseArray);
        System.out.println("矩阵表示:");
        System.out.println(sparseArray.toMatrixString());

        // 示例2：调整大小（保留数据）
        System.out.println("\n示例2: 调整大小（保留数据）");
        System.out.println("调整前: " + sparseArray);
        sparseArray.resize(8, 8, true);  // 扩容
        System.out.println("扩容到8x8后: " + sparseArray);
        System.out.println("压缩矩阵表示:");
        System.out.println(sparseArray.toCompressedMatrixString());

        // 添加新元素到新区域
        sparseArray.set(7, 7, 20);
        sparseArray.set(5, 5, 15);

        // 示例3：缩减大小（选择是否保留数据）
        System.out.println("\n示例3: 缩减大小");
        sparseArray.resize(6, 6, true);  // 缩减，保留可保留的数据
        System.out.println("缩减到6x6后（保留数据）: " + sparseArray);
        System.out.println("压缩矩阵表示:");
        System.out.println(sparseArray.toCompressedMatrixString());

        // 示例4：安全设置和获取
        System.out.println("\n示例4: 安全设置和获取");
        sparseArray.setSafe(10, 10, 100);  // 自动扩容
        System.out.println("安全设置(10,10)=100后: " + sparseArray);

        System.out.println("获取(10,10): " + sparseArray.getSafe(10, 10));  // 100
        System.out.println("获取(20,20)（超出范围）: " + sparseArray.getSafe(20, 20));  // 默认值0

        // 示例5：扩展和缩减特定维度
        System.out.println("\n示例5: 扩展和缩减特定维度");
        System.out.println("当前大小: " + sparseArray.getRows() + "x" + sparseArray.getColumns());

        sparseArray.expandRows(5);  // 增加5行
        System.out.println("增加5行后: " + sparseArray.getRows() + "x" + sparseArray.getColumns());

        sparseArray.expandColumns(3);  // 增加3列
        System.out.println("增加3列后: " + sparseArray.getRows() + "x" + sparseArray.getColumns());

        sparseArray.shrinkRows(2, true);  // 减少2行，保留数据
        System.out.println("减少2行后: " + sparseArray.getRows() + "x" + sparseArray.getColumns());

        // 示例6：获取子数组
        System.out.println("\n示例6: 获取子数组");
        ResizableSparseArray2D<Integer> subArray = sparseArray.getSubArray(2, 2, 6, 6);
        System.out.println("子数组(2,2 到 6,6): " + subArray);
        System.out.println("子数组内容:");
        System.out.println(subArray.toCompressedMatrixString());

        // 示例7：合并数组
        System.out.println("\n示例7: 合并数组");
        ResizableSparseArray2D<Integer> otherArray = new ResizableSparseArray2D<>(3, 3, 0);
        otherArray.set(0, 0, 50);
        otherArray.set(2, 2, 60);

        System.out.println("其他数组: " + otherArray);
        sparseArray.merge(otherArray, 3, 3);  // 合并到位置(3,3)
        System.out.println("合并后: " + sparseArray);
        System.out.println("检查(3,3): " + sparseArray.get(3, 3));  // 应该是50
        System.out.println("检查(5,5): " + sparseArray.get(5, 5));  // 应该是60

        // 示例8：性能演示
        System.out.println("\n示例8: 性能演示");
        ResizableSparseArray2D<String> largeSparse = new ResizableSparseArray2D<>(1000, 1000, "");

        // 在少数位置存储数据
        largeSparse.set(10, 20, "A");
        largeSparse.set(500, 600, "B");
        largeSparse.set(999, 999, "C");

        System.out.println("大型稀疏数组: " + largeSparse);
        System.out.println("稀疏度: " + String.format("%.6f%%", largeSparse.getSparsity() * 100));
        System.out.println("压缩率: " + String.format("%.6f%%", largeSparse.getCompressionRatio() * 100));

        // 扩容并保留数据
        largeSparse.resize(2000, 2000, true);
        System.out.println("扩容到2000x2000后: " + largeSparse);
    }
}