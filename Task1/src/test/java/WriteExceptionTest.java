import exception.FileNameAlreadyExistsException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WriteExceptionTest {

    public final String CONTENT_STRING = "Some text";
    public final String REPEATED_STRING = "AA";
    public final String SPACE_STRING = " ";
    public final String FILE_PATH_STRING = "@D:\\JDK-intellij-downloader-info.txt";
    public final String TIC_TOC_TOE_STRING = "tictoctoe.game";

    public FileStorage storage;

    /**
     * Провайдер для положительной проверки методов работы со storage
     */
    @DataProvider(name = "testFilesForStorage")
    public Object[][] newFiles() {
        return new Object[][] { {new File(REPEATED_STRING, CONTENT_STRING)},
                {new File(SPACE_STRING, TIC_TOC_TOE_STRING)},
                {new File(FILE_PATH_STRING, CONTENT_STRING)} };
    }

    /**
     * Провайдер для проверки записи существующего файла с ошибками
     */
    @DataProvider(name = "testFileExistException")
    public Object[][] newExceptionFile() {
        return new Object[][] { {new File(REPEATED_STRING, CONTENT_STRING)} };
    }




    /*
    /**
     * Тестирование записи файла с ошибкой (дублирующийся файл
     * @param newFile
     * @throws FileNameAlreadyExistsException
     */
    /*
    @Test (dataProvider = "testFileExistException",
            expectedExceptions = FileNameAlreadyExistsException.class,
            dependsOnMethods = "testWrite")
    public void testWriteFileException(File newFile) throws FileNameAlreadyExistsException {
        try {
            Assert.assertFalse(storage.write(new File(REPEATED_STRING, CONTENT_STRING)));
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }*/

    @Test (expectedExceptions = NullPointerException.class)
    public void testWriteFileNullException() {
        try {
            Assert.assertFalse(storage.write(null));
        }catch (FileNameAlreadyExistsException ex){
            ex.getMessage();
        }
    }
}
