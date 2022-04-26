package com.example.demo.student;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents(){
        return studentService.getStudents();
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student){
        studentService.addNewStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable Long studentId){
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable Long studentId,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email){
        studentService.updateStudent(studentId, name, email);
    }

    @PostMapping("/import")
    public String mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            double total = 0L;

            String nextStock = null;
            double stockValue = 0;

            //Read line
            for(int i=0; i<sheet.getPhysicalNumberOfRows();i++) {
                XSSFRow row = sheet.getRow(i);
                //Real cell
                for(int j=0;j<row.getPhysicalNumberOfCells();j++)
                    System.out.print(row.getCell(j) +" ");
                    String stringCellValue = row.getCell(2).getStringCellValue().trim();

                String stock = row.getCell(5).getStringCellValue().trim();

                    XSSFRow nextRow = sheet.getRow(row.getRowNum() + 1);
                    if (nextRow != null){
                        nextStock = nextRow.getCell(5).getStringCellValue().trim();
                    }

                    total = total + row.getCell(9).getNumericCellValue();
                    if("V".equals(stringCellValue)) {
                        total = total - row.getCell(9).getNumericCellValue();
                    }

                    if(stock.equals(nextStock)){
                        double auxValue = row.getCell(9).getNumericCellValue();
                        double nextValue = 0;
                        String nextStringCellValue = null;
                        if (nextRow != null) {
                            nextValue = nextRow.getCell(9).getNumericCellValue();
                            nextStringCellValue = nextRow.getCell(2).getStringCellValue().trim();
                        }
                        if(stockValue == 0){
                            stockValue = stockValue + auxValue;
                        }
                        if("V".equals(stringCellValue) || "V".equals(nextStringCellValue)) {
                            stockValue = stockValue - nextValue;
                        }else{
                            stockValue = stockValue + nextValue;
                        }

                    }else {
                        System.out.println("\nStock:");
                        System.out.println(stock);
                        System.out.println("\nValue:");
                        System.out.println(stockValue);
                        stockValue = 0;
                    }

                    System.out.println("\n");

            }
            System.out.println(total);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "Success";
    }
}
