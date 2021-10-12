package modules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class Matrix implements Serializable {

    int[][] primitiveMatrix;

    public Matrix(int[][] oArray){
        List<int[]> list = new ArrayList<>();
        for (int[] row : oArray) {
            int[] clone = row.clone();
            list.add(clone);
        }
        primitiveMatrix = list.toArray(new int[0][]);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : primitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * It checks to see if an index in actually available. if so, it is added to the list
     *
     * @param index Where to start from
     * @param plusToRow How much to add to the row
     * @param plusToCol How much to add to the column
     * @param list  If the Index is valid it will be added to here
     */
    private void addIndexToList(Index index, int plusToRow, int plusToCol, Collection<Index> list)
    {
        int extracted;  // Just to test the "try". Won't be used
        try{
            extracted = primitiveMatrix[index.getRowNum()+plusToRow][index.getColNum()+plusToCol];
            list.add(new Index(index.getRowNum()+plusToRow,index.getColNum()+plusToCol));
        }catch (ArrayIndexOutOfBoundsException ignored){}
    }

    /**
     * Gets the neighbors of a given index where there is 1 value.
     * @param index where to check from
     * @param withDiagonals should we include diagonals indices?
     * @return a list of all the neighbors
     */
    public Collection<Index> getNeighbors(final Index index, boolean withDiagonals){
        Collection<Index> list = new ArrayList<>();

        addIndexToList(index, 1,0,list);
        addIndexToList(index, 0,1,list);
        addIndexToList(index, -1,0,list);
        addIndexToList(index, 0,-1,list);

        if (withDiagonals) {
            addIndexToList(index, -1, -1, list);
            addIndexToList(index, 1, 1, list);
            addIndexToList(index, -1, 1, list);
            addIndexToList(index, 1, -1, list);
        }

        return list;
    }

    public int getValue(Index index) {
        return primitiveMatrix[index.getRowNum()][index.getColNum()];
    }


    public int getSizeOfMatrix(){ return this.primitiveMatrix.length; }

}