package com.example.attendancebackend.importdata;

import com.example.attendancebackend.model.Account;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelAccountImporter {

    private final DataFormatter dataFormatter = new DataFormatter();

    public List<Account> importAccounts(InputStream inputStream)
            throws IOException {

        List<Account> accounts = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {

                // Skip header
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Skip completely empty rows
                if (row.getLastCellNum() == -1) {
                    continue;
                }

                Account account = convertRow(row);
                accounts.add(account);
            }
        }

        return accounts;
    }

    private Account convertRow(Row row) {

        String firstName =
                dataFormatter.formatCellValue(row.getCell(0));

        String lastName =
                dataFormatter.formatCellValue(row.getCell(1));

        String email =
                dataFormatter.formatCellValue(row.getCell(2));

        Account account = new Account();

        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);

        return account;
    }
}
