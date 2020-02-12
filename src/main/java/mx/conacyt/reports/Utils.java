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

import java.lang.reflect.Field;
import java.util.Optional;

import mx.conacyt.reports.annotations.SheetColumn;

/**
 * Utils
 * 
 * @author Roberto Villarejo Martínez
 *
 */
public class Utils {

    private Utils() {

    }

    /**
     * Split a camel case string into tokens
     * 
     * @param s the camel case string
     * @return the tokens
     */
    public static String splitCamelCase(String s) {
        return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }

    /**
     * Converts a camel case string to human readable string
     * 
     * @param camelCase
     * @return the human readable string
     */
    public static String camelCaseToHumanReadable(String camelCase) {
        String element = splitCamelCase(camelCase);
        return element.replaceFirst(Character.toString(element.charAt(0)),
                Character.toString(element.charAt(0)).toUpperCase());
    }

    public static String buildHeaderFromAnnotatedProperty(Field field) {
        Optional<SheetColumn> annotation = getSheetColumnAnnotation(field);
        if (annotation.isPresent()) {
            if (!"".equals(annotation.get().title())) {
                return annotation.get().title();
            } else {
                return camelCaseToHumanReadable(field.getName());
            }
        }
        return null;
    }

    public static Optional<SheetColumn> getSheetColumnAnnotation(Field field) {
        if (field.isAnnotationPresent(SheetColumn.class)) {
            return Optional.of(field.getAnnotation(SheetColumn.class));
        }
        return Optional.empty();
    }

}
