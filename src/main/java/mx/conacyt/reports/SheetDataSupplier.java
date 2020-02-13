package mx.conacyt.reports;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

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

public class SheetDataSupplier<T> implements Supplier<Slice<T>> {

    private static int DEFAULT_PAGE_NUMBER = 0;

    private static int DEFAULT_SIZE_PAGE = 100;

    private Function<Pageable, Page<T>> function;

    private boolean hasNext = false;

    private Pageable pageable;

    public SheetDataSupplier(Function<Pageable, Page<T>> function) {
        this.function = function;
        this.pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_SIZE_PAGE);
    }

    public SheetDataSupplier(Sort sort, Function<Pageable, Page<T>> function) {
        this.function = function;
        this.pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_SIZE_PAGE, sort);
    }

    public SheetDataSupplier(Pageable pageable, Function<Pageable, Page<T>> function) {
        this.function = function;
        this.pageable = pageable;
    }

    @Override
    public Slice<T> get() {
        if (pageable.getPageNumber() == 0 || hasNext) {
            Page<T> page = function.apply(pageable);
            hasNext = page.hasNext();
            pageable = pageable.next();
            return new SliceImpl<>(page.getContent(), pageable, page.hasNext());
        }
        return null;
    }

}
