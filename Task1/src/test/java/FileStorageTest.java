import exception.FileNameAlreadyExistsException;
import files.File;
import files.FileStorage;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FileStorageTest {
    private final String FILE_WAS_NOT_WRITTEN_EXCEPTION = "The file wasn't written";

    private final String SPACE_STRING = " ";
    private final String FILE_PATH_STRING = "@D:\\JDK-intellij-downloader-info.txt";
    private final String CONTENT_STRING = "Some text";
    private final String REPEATED_STRING = "AA";
    private final String WRONG_SIZE_CONTENT_STRING = "TEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtext";
    private final String TIC_TOC_TOE_STRING = "tictoctoe.game";

    private final ArrayList<File> files = new ArrayList<>(Arrays.asList(new File(FILE_PATH_STRING, CONTENT_STRING),
            new File(SPACE_STRING, WRONG_SIZE_CONTENT_STRING),new File(REPEATED_STRING, CONTENT_STRING)));

    private final int NEW_SIZE = 5;

    @BeforeTest
    public void setStorage(){
        storage = new FileStorage(NEW_SIZE);
    }

    @BeforeMethod
    public void setNewStorage() {
        storage.deleteAllFiles();
    }

    @DataProvider(name = "testFilesForStorage")
    public Object[][] newFiles() {
        return new Object[][] { {new File(REPEATED_STRING, CONTENT_STRING)},
                {new File(SPACE_STRING, WRONG_SIZE_CONTENT_STRING)},
                {new File(FILE_PATH_STRING, CONTENT_STRING)} };
    }

    @DataProvider(name = "testFilesForDelete")
    public Object[][] filesForDelete() {
        return new Object[][] { {REPEATED_STRING}, {TIC_TOC_TOE_STRING}};
    }

    @DataProvider(name = "nullExceptionTest")
    public Object[][] dataNullExceptionTest(){
        return new Object[][] { {new File (null, null)}, {null} };
    }

    public FileStorage storage;

    /**
     * Тест на добавлние файла
     * @param file
     */
    @Test (dataProvider = "testFilesForStorage")
    public void writeUniqueFile(File file) {
        try {
            Assert.assertTrue(storage.write(file), FILE_WAS_NOT_WRITTEN_EXCEPTION);
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Тест на добавление существующего файла
     * @param file
     */
    @Test (dataProvider = "testFilesForStorage")
    public void writeExistingFile(File file) {
        try {
            storage.write(file);
            Assert.assertFalse(storage.write(file));
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверка существоания файла
     * @param file
     */
    @Test (dataProvider = "testFilesForStorage")
    public void checkIsFileExist(File file) {
        try {
            storage.write(file);
            Assert.assertTrue(storage.isExists(file.getFilename()));
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Тестирование удаления файла
     * @param fileName
     */
    @Test (dataProvider = "testFilesForDelete")
    public void DeleteTest(String fileName) {
        setFilesList();
        Assert.assertTrue(storage.delete(fileName));
    }

    /**
     *  Тестирование получения файла
     * @param file
     */
    @Test (dataProvider = "testFilesForStorage")
    public void testGetFile(File file) {
        try {
            storage.write(file);
            Assert.assertEquals(storage.getFile(file.getFilename()), file);
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Тестирование получения файлов
     */
    @Test
    public void testGetFiles() {
        setFilesList();
        for (File el: storage.getFiles()) {
            Assert.assertNotNull(el);
        }
    }

    private void setFilesList() {
        try {
            for (File f : files) {
                storage.write(f);
            }
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }
}
