package mx.conacyt.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import junit.framework.TestCase;
import mx.conacyt.reports.SheetDataSupplier;
import mx.conacyt.reports.WorkbookWriter;

public class WorkbookTest extends TestCase {

    @Before
    private UserDto createUserDto() {
        UserDto user = new UserDto();
        user.setId("1");
        user.setLogin("login");
        user.setGenre(UserDto.Genre.FEMALE);
        user.setFollowers(2000);
        return user;
    }

    @Test
    public void testWorkbookCreation() throws IOException {
        SheetDataSupplier<UserDto> dataSupplier = new SheetDataSupplier<>(Sort.unsorted(), (Pageable pageable) -> {
            ArrayList<UserDto> content = new ArrayList<>();
            content.add(createUserDto());
            return new PageImpl<UserDto>(content);
        });
        WorkbookWriter<UserDto> converter = new WorkbookWriter<>(UserDto.class);
        converter.addData(dataSupplier);
        SXSSFWorkbook wb = converter.getWorkbook();
        Path path = Files.createTempFile(null, ".xlsx");
        System.out.println(path.toAbsolutePath());
        OutputStream os = new FileOutputStream(path.toFile());
        wb.write(os);
    }

}