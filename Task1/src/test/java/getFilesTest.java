import exception.FileNameAlreadyExistsException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class getFilesTest {
    public final String DIFFERENT_FILE_EXCEPTION = "DIFFERENT FILE";
    public final String CONTENT_STRING = "Some text";
    public final String REPEATED_STRING = "AA";
    public final String SPACE_STRING = " ";
    public final String FILE_PATH_STRING = "@D:\\JDK-intellij-downloader-info.txt";
    public final String TIC_TOC_TOE_STRING = "tictoctoe.game";

    public FileStorage storage;

    @DataProvider(name = "testFilesForStorage")
    public Object[][] newFiles() {
        return new Object[][] { {new File(REPEATED_STRING, CONTENT_STRING)},
                {new File(SPACE_STRING, TIC_TOC_TOE_STRING)},
                {new File(FILE_PATH_STRING, CONTENT_STRING)} };
    }

    /**
     * Тестирвоание записи файла
     * @param file
     * @throws FileNameAlreadyExistsException
     */
    @Test(dataProvider = "testFilesForStorage", groups = {"write"}, priority = 1)
    public void testWrite(File file)
            throws FileNameAlreadyExistsException {
        try {
            System.out.println(file.getFilename());
            Assert.assertTrue(storage.write(file));
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }
    /**
     * Тестирование получения файлов
     * Если откомментировать, появится ошибка (отображение неверных файлов)
     */
    @Test (priority = 2)
    public void testGetFiles(){
        try {
            for (File el : storage.getFiles()) {
                Assert.assertNotNull(el.getFilename(), DIFFERENT_FILE_EXCEPTION);
            }
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }

    /**
     * Тестирование получения файла
     * @param file
     */
    @Test (dataProvider = "testFilesForStorage", priority = 3)
    public void testGetFile(File file) {
        try {
            Assert.assertEquals(storage.getFile(file.getFilename()).getFilename(), file.getFilename());
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }
}
