package modules;

import java.io.Serializable;
import java.util.Objects;

public class Index implements Serializable {
    /**
     * This class represents a slot in a 2D array (matrix). it looks like this @param row
      */

    private int row, column;

    public Index(int oRow, int oColumn){
        this.row = oRow;
        this.column = oColumn;
    }

    @Override
    public String toString(){
        return "(" + row + "," + column + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return row == index.row &&
                column == index.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public int getColNum() { return this.column; }

    public int getRowNum() { return this.row; }



}


