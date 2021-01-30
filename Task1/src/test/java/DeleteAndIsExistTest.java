import exception.FileNameAlreadyExistsException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DeleteAndIsExistTest {

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
     * Провайдер для положительной проверки удаления файла
     */
    @DataProvider(name = "testFilesForDelete")
    public Object[][] filesForDelete() {
        return new Object[][] { {REPEATED_STRING} };
    }
    /**
     * Провайдер для проверки удаления файла с ошибками
     */
    @DataProvider(name = "testExceptionFileForDelete ")
    public Object[][] filesForDeleteException(){
        return new Object[][] { {TIC_TOC_TOE_STRING} };
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
            Assert.assertTrue(storage.write(file));
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }

    /* Тестирование проверки существования файла */
    @Test(dataProvider = "testFilesForStorage", groups = { "testExistFunction", "a" } ,priority = 2)
    public void testIsExists(File file) {
        try {
            String name = file.getFilename();
            Assert.assertTrue(storage.isExists(name));
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }

    /**
     * Тестирование удаления файла
     * @param fileName
     */
    @Test (dataProvider = "testFilesForDelete", groups = {"delete"}, priority = 3)
    public void testDelete(String fileName) {
        try {
            Assert.assertTrue(storage.delete(fileName));
        }catch (NullPointerException ex){
            ex.getMessage();
        }
    }
}
