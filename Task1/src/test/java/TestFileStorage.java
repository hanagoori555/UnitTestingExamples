
import exception.FileNameAlreadyExistsException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

public class TestFileStorage {


    public final String MAX_SIZE_EXCEPTION = "DIFFERENT MAX SIZE";

    public final String SPACE_STRING = " ";
    public final String FILE_PATH_STRING = "@D:\\JDK-intellij-downloader-info.txt";
    public final String CONTENT_STRING = "Some text";
    public final String REPEATED_STRING = "AA";
    public final String TIC_TOC_TOE_STRING = "tictoctoe.game";


    public final int TWENTY_ONE = 21;
    public final int ZERO = 0;
    public final int MINUS_FIFTY = -50;


    public FileStorage storage;

    @BeforeTest
    public void setUp(){
        storage = new FileStorage();
    }

    /**
     * Провайдер для положительной проверки конструктора
     */
    @DataProvider(name = "testSizeData")
    public Object[][] newData() {
        return new Object[][]{{TWENTY_ONE}, {ZERO}};
    }

    /**
     * Провайдер для проверки передачи данных size = -50
     */
    @DataProvider(name = "testConstructorException")
    public Object[][] filesForConstructorException(){
        return new Object[][] { {MINUS_FIFTY} };
    }

    /**
     * Тестирование конструктора
     * @param size
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test(dataProvider = "testSizeData", groups = {"constructor"}, priority = 1)
    public void testFileStorage(int size)
                            throws NoSuchFieldException, IllegalAccessException {
        storage = new FileStorage(size);
        Field field = FileStorage.class.getDeclaredField("maxSize");
        field.setAccessible(true);
        Assert.assertEquals( (int) field.getDouble(storage), size, MAX_SIZE_EXCEPTION );
    }

    /**
     * Проверка передачи данных size = -50
     * Если откомментировать, появится ошибка
     * @param size
     * @throws FileNameAlreadyExistsException
     */
   @Test(dataProvider = "testConstructorException",groups = {"constructor"}, priority = 2)
    public void testFileStorageException(int size) {
       try {
           storage = new FileStorage(size);
           Field field = FileStorage.class.getDeclaredField("maxSize");
           field.setAccessible(true);
           Assert.assertEquals((int) field.getDouble(storage), size, MAX_SIZE_EXCEPTION);
       }catch (NoSuchFieldException ex) {
           ex.getMessage();
       }catch (IllegalAccessException ex) {
           ex.getMessage();
       }
    }



}
