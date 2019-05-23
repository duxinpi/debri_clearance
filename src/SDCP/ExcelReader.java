package SDCP;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static SDCP.GlobalData.RC;


public class ExcelReader {


    public static List<List<List<String>>> read(String path) {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        List<List<List<String>>> res = new ArrayList<>();


        Workbook workbook = null;
        try {

            workbook = WorkbookFactory.create(new File(path));

            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                List<List<String>> eachTable = new ArrayList<>();
                Sheet sheet = workbook.getSheetAt(i);

                DataFormatter dataFormatter = new DataFormatter();


                // 2. Or you can use a for-each loop to iterate over the rows and columns
                System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
                for (Row row : sheet) {
                    List<String> eachRow = new ArrayList<>();
                    for (Cell cell : row) {
                        String cellValue = dataFormatter.formatCellValue(cell);

                        eachRow.add(cellValue);

                    }
                    eachTable.add(eachRow);
                }
                res.add(eachTable);
            }


            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return res;
    }


    public static List<List<String>> read(String path, int tableIndex) {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        List<List<String>> res = new ArrayList<>();


        Workbook workbook = null;
        try {

            workbook = WorkbookFactory.create(new File(path));

            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

            Sheet sheet = workbook.getSheetAt(tableIndex);

            DataFormatter dataFormatter = new DataFormatter();


            // 2. Or you can use a for-each loop to iterate over the rows and columns
            System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
            for (Row row : sheet) {
                List<String> eachRow = new ArrayList<>();
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);

                    eachRow.add(cellValue);

                }
                res.add(eachRow);

            }
/*
            System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            for (List<String> row: res) {
                for(String cell: row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }*/
            // Closing the workbook
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean write(String path, List<Edge> bestAction, double[] bestY) {
        List<List<List<String>>> originData = read(path);
        List<Integer> nodes = Utility.getNodeIndex(bestAction);
        if (nodes.size() == 0) return false;


        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            for (int i = 0; i < originData.size(); i++) {

                XSSFSheet sheet = workbook.createSheet("Sheet" + i);
                int rowNum = 0;
                System.out.println("Creating excel");
                for (int k = 0;k < originData.get(i).size();k++) {// k is row index.
                    List<String> eachRow = originData.get(i).get(k);

                    if (i == 0) { // table 1.
                        if (k !=0 ) {
                            eachRow.set(6, ""+bestY[k-1]);
                        }
                        for (int j = 0; j < nodes.size(); j++) {
                            if (k != 0 && Integer.parseInt(eachRow.get(0)) == nodes.get(j) && eachRow.get(1).equals("d")) {
                                eachRow.set(1, "t"); // node type
                                eachRow.set(3, "0.0"); // RD
                                eachRow.set(7, "0.0"); // B
                            }
                        }
                    } else if (i == 1) {
                        for (int j = 0; j < bestAction.size(); j++) {
                            int nodeI = bestAction.get(j).i;
                            int nodeJ = bestAction.get(j).j;

                            if (k != 0 && ((Integer.parseInt(eachRow.get(0)) == nodeI && Integer.parseInt(eachRow.get(1)) == nodeJ) ||
                                    (Integer.parseInt(eachRow.get(0)) == nodeJ && Integer.parseInt(eachRow.get(1)) == nodeI))) {
                                eachRow.set(5, "0.0");
                                eachRow.set(6, "0.0");
                                eachRow.set(7, "1");
                                eachRow.set(8, "1");
                                eachRow.set(9, "U");

                            }

                        }


                    } else if (i == 2) {
                        double cost = Utility.getWSum(bestAction);
                        RC = Math.max(0, RC - cost);
                        if (eachRow.get(0).equals("RC")) {
                            eachRow.set(1, ""+RC);
                        }
                    }
                    int colNum = 0;
                    Row row = sheet.createRow(rowNum++);
                    for (String each : eachRow) {
                        Cell cell = row.createCell(colNum++);

                        cell.setCellValue(each);
                    }
                }
            }


            try {
                FileOutputStream outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                workbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String args[]) {

        write("./Doc/test_data.xlsx", null, null);
/*
        List<List<String>> nodes = read("./Doc/data.xlsx", 0);
        List<List<String>>  edges = read("./Doc/data.xlsx", 1);
        nodes.remove(0);
        edges.remove(0);

        List<Edge> edgeList = new ArrayList<>();
        List<Node> nodeList = new ArrayList<>();

        for (List<String> eachNode : nodes) {

            Node node = new Node(Integer.parseInt(eachNode.get(0)),eachNode.get(1), Double.parseDouble(eachNode.get(2)), Double.parseDouble(eachNode.get(3)) , Double.parseDouble(eachNode.get(4)) , Double.parseDouble(eachNode.get(5)) , Double.parseDouble(eachNode.get(6)) , Double.parseDouble(eachNode.get(7)));

            double[]R0 = new double[8];
            for (int i =0; i < R0.length;i++){
                R0[i] = Double.parseDouble(eachNode.get(i+8));
            }
            node.setR0(R0);
        //    N.add(d1);
            nodeList.add(node);
        }

        for (List<String> eachEdge : edges) {
            edgeList.add(new Edge(Integer.parseInt(eachEdge.get(0)), Integer.parseInt(eachEdge.get(1)), Double.parseDouble(eachEdge.get(2)),  Double.parseDouble(eachEdge.get(3)), Double.parseDouble(eachEdge.get(4)), new double[]{Double.parseDouble(eachEdge.get(5)), Double.parseDouble(eachEdge.get(6))}, new double[]{Double.parseDouble(eachEdge.get(7)), Double.parseDouble(eachEdge.get(8))}));

        }
*/
        //   Edge edge12 = new Edge(1, 2, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
    }


}
