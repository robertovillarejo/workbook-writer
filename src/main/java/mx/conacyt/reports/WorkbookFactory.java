/*
 *  
 * The MIT License (MIT)
 * Copyright (c) 2020 Roberto Villarejo Martínez
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

import static mx.conacyt.reports.Utils.camelCaseToHumanReadable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.conacyt.reports.annotations.SheetColumn;

public class WorkbookFactory {

    private static final Logger log = LoggerFactory.getLogger(WorkbookFactory.class);

    private WorkbookFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static WeakHashMap<Class, WorkbookWriterContext> workbookWriterContextMap = new WeakHashMap<>();

    @SuppressWarnings("rawtypes")
    public static WorkbookWriterContext getWorkbookMetadata(Class clazz) {
        if (workbookWriterContextMap.containsKey(clazz)) {
            return workbookWriterContextMap.get(clazz);
        } else {
            WorkbookWriterContext workbookMetadata = buildWorkbookMetadata(clazz);
            workbookWriterContextMap.put(clazz, workbookMetadata);
            return workbookMetadata;
        }
    }

    private static WorkbookWriterContext buildWorkbookMetadata(Class<?> clazz) {
        WorkbookWriterContext wbMetadata = new WorkbookWriterContext();
        wbMetadata.setAnnotatedFields(getAnnotatedFields(clazz));
        wbMetadata.setGetterMethods(getGetterMethods(clazz, wbMetadata.getAnnotatedFields()));

        XSSFWorkbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        XSSFSheet sheet = workbook.createSheet();
        // Write headers
        List<String> headers = wbMetadata.getAnnotatedFields().stream()
                .map(WorkbookFactory::buildHeaderFromAnnotatedProperty).collect(Collectors.toList());
        addHeaders(sheet, headers);
        wbMetadata.setWorkbook(workbook);
        return wbMetadata;
    }

    public static List<Field> getAnnotatedFields(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields()).stream()
                .filter(field -> field.isAnnotationPresent(SheetColumn.class)).collect(Collectors.toList());
    }

    public static List<Method> getGetterMethods(Class<?> clazz, List<Field> fields) {
        List<Method> methods = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            char firstLetter = fieldName.charAt(0);
            char uppercaseFirstLetter = Character.toUpperCase(firstLetter);
            String getMethodName = "get" + field.getName().replaceFirst(Character.toString(firstLetter),
                    Character.toString(uppercaseFirstLetter));
            try {
                Method method = clazz.getMethod(getMethodName);
                methods.add(method);
            } catch (NoSuchMethodException | SecurityException e) {
                log.warn("No get method for property {} in class {}", field.getName(), clazz.getName());
            }
        }
        return methods;
    }

    private static void addHeaders(XSSFSheet sheet, List<String> headers) {
        XSSFRow row = sheet.createRow(0);
        int cellNum = 0;
        for (String header : headers) {
            XSSFCell cell = row.createCell(cellNum);
            cell.setCellValue(header);
            cellNum++;
        }
    }

    public static String buildHeaderFromAnnotatedProperty(Field field) {
        SheetColumn annotation = field.getAnnotation(SheetColumn.class);
        if (!"".equals(annotation.value())) {
            return annotation.value();
        } else {
            return camelCaseToHumanReadable(field.getName());
        }
    }

}
