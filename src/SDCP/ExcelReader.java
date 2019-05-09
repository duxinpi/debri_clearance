package SDCP;





import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExcelReader {

    public static final String SAMPLE_XLSX_FILE_PATH = "./sample-xlsx-file.xlsx";


    public static List<List<String>>  read(String path, int tableIndex) {
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

    public static void  main(String args[]) {
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

     //   Edge edge12 = new Edge(1, 2, 100, 0.94, 0.04, new double[]{0.3, 0.5}, new double[]{0.5, 0.5});
    }


}
