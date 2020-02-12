/*
 *  
 * The MIT License (MIT)
 * Copyright (c) 2018 Roberto Villarejo Martínez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mx.conacyt.reports;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Slice;

public class WorkbookWriter<T> {

    private SXSSFWorkbook workbook;

    private int currentRowNumber = 1;

    private SXSSFSheet sheet;

    private WorkbookWriterContext workbookMetadata;

    public WorkbookWriter(Class<T> clazz) {
        this.workbookMetadata = WorkbookFactory.getWorkbookMetadata(clazz);
        this.workbook = new SXSSFWorkbook(workbookMetadata.getworkbook());
        this.sheet = workbook.getSheetAt(0);
    }

    /**
     * Return the workbook for <T>
     * 
     * @return the workbook with headers and data if convert method was invoked or
     *         the workbook only with headers if convert method was not invoked
     */
    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void addData(Supplier<Slice<T>> dataSupplier) {
        Slice<T> slice;
        while ((slice = dataSupplier.get()) != null) {
            for (T t : slice.getContent()) {
                addRowDataFromObject(t);
            }
        }
    }

    public void addRowDataFromObject(T t) {
        List<String> data = getDataFromObject(t);
        addData(data);
    }

    public void addData(List<String> data) {
        SXSSFRow row = sheet.createRow(currentRowNumber);
        int currentCol = 0;
        for (String dataCell : data) {
            SXSSFCell cell = row.createCell(currentCol);
            cell.setCellValue(dataCell);
            currentCol++;
        }
        currentRowNumber++;
    }

    private List<String> getDataFromObject(T t) {
        List<String> result = new ArrayList<>();
        for (Method method : workbookMetadata.getGetterMethods()) {
            try {
                Object obj = method.invoke(t);
                String element = obj == null ? null : obj.toString();
                result.add(element);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                result.add("");
            }
        }
        return result;
    }

}
