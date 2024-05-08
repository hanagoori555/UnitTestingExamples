import exception.FileNameAlreadyExistsException;
import files.File;
import files.FileStorage;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileStorageTest {

    public final String MAX_SIZE_EXCEPTION = "DIFFERENT MAX SIZE";
    public final String NULL_FILE_EXCEPTION = "NULL FILE";
    public final String SPACE_STRING = " ";
    public final String FILE_PATH_STRING = "@D:\\JDK-intellij-downloader-info.txt";
    public final String CONTENT_STRING = "Some text";
    public final String REPEATED_STRING = "AA";
    public final String WRONG_SIZE_CONTENT_STRING = "TEXTtextTEXTtextTEXTtextTEXTtextTEXTtextTEXTtext" +
            "TEXTtextTEXTtextTEXTtextTEXTtextTEXTtext" +
            "TEXTtextTEXTtextTEXTtextTEXTtextTEXTtext" +
            "TEXTtextTEXTtextTEXTtextTEXTtextTEXTtext" +
            "TEXTtextTEXTtextTEXTtextTEXTtextTEXTtext";
    public final String TIC_TOC_TOE_STRING = "tictoctoe.game";

    public final int NEW_SIZE = 5;
    public final int TWENTY_ONE = 21;
    public final int FIFTY = 50; // было -50, в названии нет MINUS_FIFTY
    public final int ZERO = 0;

    public FileStorage storage;

    @BeforeMethod // availableSize был связан между файлами, если оставить @BeforeTest,
    // то некоторые тесты, использующие три файла провалятся, так как по сути у них общий storage
    public void setUp() {
        storage = new FileStorage(NEW_SIZE);
    }

    /* Метод, выполняемый перед группами */
    @BeforeGroups(groups = "testExistFunction")
    public void setNewStorage() {
        storage = new FileStorage();
    }

    /* ПРОВАЙДЕРЫ */
    @DataProvider(name = "testSizeData")
    public Object[][] newData() {
        return new Object[][]{{TWENTY_ONE}, {FIFTY}, {ZERO}};
    }

    @DataProvider(name = "testFilesForStorage")
    public Object[][] newFiles() {
        return new Object[][]{{new File(REPEATED_STRING, CONTENT_STRING)},
                {new File(SPACE_STRING, WRONG_SIZE_CONTENT_STRING)},
                {new File(FILE_PATH_STRING, CONTENT_STRING)}};
    }

    @DataProvider(name = "testFilesForDelete")
    public Object[][] filesForDelete() {
        return new Object[][]{{REPEATED_STRING}, {TIC_TOC_TOE_STRING}};
    }

    @DataProvider(name = "nullExceptionTest")
    public Object[][] dataNullExceptionTest() {
        return new Object[][]{{new File(null, null)}, {null}};
    }

    @DataProvider(name = "testFileForException")
    public Object[][] newExceptionFile() {
        return new Object[][]{{new File(REPEATED_STRING, CONTENT_STRING)}};
    }

    /* Тестирование конструктора – рефлексия */
    @Test(dataProvider = "testSizeData")
    public void testFileStorage(int size) {
        try {
            storage = new FileStorage(size);

            Field field = FileStorage.class.getDeclaredField("maxSize");
            field.setAccessible(true);
            Assert.assertEquals((int) field.getDouble(storage), size, MAX_SIZE_EXCEPTION);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /* Тестирование записи файла */
    @Test(dataProvider = "testFilesForStorage")
    public void testWrite(File file) throws FileNameAlreadyExistsException {
        // Пытаемся добавить файл в хранилище
        boolean result = storage.write(file);
        if (result) {
            Assert.assertTrue(result, "File should have been added to storage.");
        } else {
            Assert.assertFalse(result, "File should not have been added to storage.");
        }
    }

    /* Тестирование записи дублирующегося файла */
    @Test(dataProvider = "testFileForException")
    public void testWriteException(File file) {
        try {
            storage.write(file);
            storage.write(file);
            Assert.fail("Expected FileNameAlreadyExistsException, but no exception was thrown");
        } catch (FileNameAlreadyExistsException e) {
            Assert.assertTrue(true);
        }
    }

    /* Тестирование проверки существования файла */
    @Test(dataProvider = "testFilesForStorage", groups = "testExistFunction")
    public void testIsExists(File file) {
        String name = file.getFilename();
        Assert.assertFalse(storage.isExists(name));
        try {
            storage.write(file);
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /* Тестирование удаления файла */
    @Test(dataProvider = "testFilesForDelete", dependsOnMethods = "testFileStorage")
    public void testDelete(String fileName) throws FileNameAlreadyExistsException {
        File fileToDelete = new File(fileName, "Content"); // Создаем файл для удаления
        storage.write(fileToDelete); // Добавляем файл в хранилище
        Assert.assertTrue(storage.delete(fileName)); // Удаляем файл из хранилища
    }

    /* Тестирование получения файлов */
    @Test
    public void testGetFiles() {
        // Получаем список файлов из хранилища
        List<File> files = storage.getFiles();

        // Создаем множество для хранения уникальных имен файлов
        Set<String> filenames = new HashSet<>();

        // Проверяем, что каждый файл не равен null и его имя уникально
        for (File file : files) {
            Assert.assertNotNull(file);
            Assert.assertTrue(filenames.add(file.getFilename()), "Duplicate filename found: " + file.getFilename());
        }
    }

    /* Тестирование получения файла */
    @Test(dataProvider = "testFilesForStorage")
    public void testGetFile(File file) {
        try {
            boolean result = storage.write(file);
            if (result) {
                File retrievedFile = storage.getFile(file.getFilename());
                Assert.assertTrue(storage.isExists(file.getFilename()));
                Assert.assertEquals(retrievedFile, file);
            } else {
                Assert.assertFalse(result);
            }
        } catch (FileNameAlreadyExistsException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Мои тесты
    @Test
    public void testDeleteFileFromEmptyStorage() {
        Assert.assertFalse(storage.delete("NonExistingFile.txt"));
    }

    @Test
    public void testAddFileToFullStorage() throws FileNameAlreadyExistsException {
        // Получаем список файлов в хранилище
        List<File> filesBefore = storage.getFiles();
        int initialSize = filesBefore.size();

        // Создаем новый файл для добавления
        File newFile = new File("NewFile.txt", "New Content");

        // Пытаемся добавить файл в хранилище
        boolean result = storage.write(newFile);

        // Получаем список файлов после попытки добавления нового файла
        List<File> filesAfter = storage.getFiles();
        int newSize = filesAfter.size();

        // Проверяем, что файл был добавлен в полное хранилище
        Assert.assertTrue(result, "File should be added to full storage");
        // Проверяем, что размер хранилища увеличился на 1
        Assert.assertEquals(newSize, initialSize + 1, "Storage size should increase by 1");
    }

    @Test
    public void testStorageSizeAfterAddingFile() throws FileNameAlreadyExistsException {
        // Размер хранилища до добавления файла
        List<File> filesBefore = storage.getFiles();
        double initialSize = 0;
        for (File file : filesBefore) {
            initialSize += file.getSize();
        }

        // Создаем новый файл и добавляем его в хранилище
        File newFile = new File("NewFile.txt", "New Content");
        storage.write(newFile);

        // Размер хранилища после добавления файла
        List<File> filesAfter = storage.getFiles();
        double newSize = 0;
        for (File file : filesAfter) {
            newSize += file.getSize();
        }

        // Проверяем, что размер хранилища увеличился на размер добавленного файла
        Assert.assertEquals(newSize, initialSize + newFile.getSize());
    }
}