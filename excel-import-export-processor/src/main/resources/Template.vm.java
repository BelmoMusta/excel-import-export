package $wrapper.aPackage;

public class ${wrapper.simplifiedClassName}ExcelMapper implements io.github.belmomusta.excel.importexport.ExcelMapper<$wrapper.className> {
    
    @Override
    public void extractToFile(java.util.Collection<$wrapper.className> $wrapper.paramName,
                              java.io.File destFile) {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
             final org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Sheet 0");
        int currentRow = 0;
             #foreach($pair in $wrapper.correspondanceFieldMethod)
final Row row = sheet.createRow(currentRow++);
                System.out.println();
             #end
             System.out.println();
        } catch(Exception e){
        
        }
    }
}
