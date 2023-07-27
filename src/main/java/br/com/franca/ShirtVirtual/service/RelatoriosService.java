package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.Acesso;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class RelatoriosService {

    public ByteArrayInputStream generateExcelReport(List<Acesso> acessos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Acessos");

            // Crie o cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Codigo");
            headerRow.createCell(1).setCellValue("Descricao_Acesso");
            // Adicione mais colunas, se necessário.

            // Preencha os dados dos clientes
            int rowNum = 1;
            for (Acesso acesso : acessos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(acesso.getId());
                row.createCell(1).setCellValue(acesso.getDescAcesso());
                // Adicione mais colunas, se necessário.
            }

            // Escreva os dados em um stream de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }
}