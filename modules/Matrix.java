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

    private void addIndexToList(Index index, int plusToRow, int plusToCol, Collection<Index> list)
    {
        int extracted;  // Just to test the "try". Won't be used
        try{
            extracted = primitiveMatrix[index.getRowNum()+plusToRow][index.getColNum()+plusToCol];
            list.add(new Index(index.getRowNum()+plusToRow,index.getColNum()+plusToCol));
        }catch (ArrayIndexOutOfBoundsException ignored){}
    }

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


    public Collection<Index> getAllMatrix() {
        Collection<Index> list = new ArrayList<>();
        for(int row=0;row<primitiveMatrix.length;row++)
        {
            for(int col=0;col<primitiveMatrix.length;col++)
                list.add(new Index(row,col));
        }
        return list;
    }

    public int getSizeOfMatrix(){ return this.primitiveMatrix.length; }

}