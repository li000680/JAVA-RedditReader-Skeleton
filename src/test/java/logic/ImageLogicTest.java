package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import entity.Image;
import common.TomcatStartUp;
import common.ValidationException;
import dal.EMFactory;
import entity.Board;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import static logic.ImageLogic.FORMATTER;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Wei Li
 */
class ImageLogicTest {

    private ImageLogic logic;
    private BoardLogic blogic;
    private Image expectedImage;
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/RedditReader", "common.ServletListener");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {
        blogic = LogicFactory.getFor( "Board");
        
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the image to not rely on any logic functionality , just for testing
        Image image = new Image();
        image.setBoard(blogic.getWithId(1));
        image.setTitle("JunitTest");
        image.setUrl("https://test");
        image.setLocalPath("C:\\Users\\95364/My Documents/test.jpg");
        image.setDate(new Date());
        
        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction 
        em.getTransaction().begin();
        //add an image to hibernate, image is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedImage= em.merge(image);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();

        logic = LogicFactory.getFor( "Image");
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedImage != null) {
            logic.delete(expectedImage);
        }
    }
    
     @Test
    final void testGetAll() {
        //get all the images from the DB
        List<Image> list = logic.getAll();
        //store the size of list, this way we know how many images exits in DB
        int originalSize = list.size();

        //make sure image was created successfully
        assertNotNull(expectedImage);
        //delete the new image
        logic.delete(expectedImage);

        //get all images again
        list = logic.getAll();
        //the new size of images must be one less
        assertEquals(originalSize - 1, list.size());
    }
    
    private void assertBoardEquals(Board expected, Board actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
    }
/**
     * helper method for testing all image fields
     *
     * @param expected
     * @param actual
     */
    private void assertImageEquals(Image expected, Image actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getLocalPath(), actual.getLocalPath());
        assertTrue(Math.abs(expected.getDate().getTime()-actual.getDate().getTime())<2000);
        assertBoardEquals(expected.getBoard(), actual.getBoard());
    }
    
    @Test
    final void testGetWithId() {
        //using the id of test image get another image from logic
        Image returnedImage = logic.getWithId(expectedImage.getId());

        //the two iamges (testImages and returnedImages) must be the same
        assertImageEquals(expectedImage, returnedImage);
    }
    @Test
    final void testGetImagesWithBoardId() {
       int foundFull = 0;
        List<Image> returnedImages = logic.getImagesWithBoardId(expectedImage.getBoard().getId());
        for (Image image : returnedImages) {
            //all images must have the same boardId
            assertEquals(expectedImage.getBoard().getId(), image.getBoard().getId());
            //exactly one image must be the same
            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    
    @Test
    final void testGetImagesWithTitle() {
        int foundFull = 0;
        List<Image> returnedImages = logic.getImagesWithTitle(expectedImage.getTitle());
        for (Image image : returnedImages) {
            //all images must have the same title
            assertEquals(expectedImage.getTitle(), image.getTitle());
            //exactly one iamge must be the same
            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void testGetImageWithUrl() {
        Image returnedImage = logic.getImageWithUrl(expectedImage.getUrl());

         //the two images (testImages and returnedImages) must be the same
        assertImageEquals(expectedImage, returnedImage);
    }

     @Test
    final void testGetImageWithLocalPath() {
        Image returnedImage = logic.getImageWithLocalPath(expectedImage.getLocalPath());

         //the two images (testImages and returnedImages) must be the same
        assertImageEquals(expectedImage, returnedImage);
    }
    @Test
    final void testGetImagesWithDate() {
        int foundFull = 0;
        List<Image> returnedImages = logic.getImagesWithDate(expectedImage.getDate());
       for (Image image : returnedImages) {
            //all images must have the same date
            //assertEquals(logic.convertDate(expectedImage.getDate()), logic.convertDate(image.getDate()));
            assertTrue(Math.abs(expectedImage.getDate().getTime()-image.getDate().getTime())<2000);
            //exactly one image must be the same
            if (image.getId().equals(expectedImage.getId())) {
               assertImageEquals(expectedImage, image);
                foundFull++;
        }
    }
    } 
    @Test
    final void testconvertDate() {
        String returnedImage = logic.convertDate(expectedImage. getDate());

         //the two images (testImages and returnedImages) must be the same
        assertEquals( FORMATTER.format(expectedImage. getDate()), returnedImage);
    }
   
    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        
        sampleMap.put(ImageLogic.TITLE, new String[]{"testCreateImage"});
        sampleMap.put(ImageLogic.URL, new String[]{"/image/createImage"});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{"createImage"});
     
        Image returnedImage = logic.createEntity(sampleMap);
        returnedImage.setBoard(blogic.getWithId(expectedImage.getBoard().getId()));
        
        logic.add(returnedImage);

        returnedImage = logic.getImageWithUrl(returnedImage.getUrl());

        assertEquals(sampleMap.get(ImageLogic.TITLE)[0], returnedImage.getTitle());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.LOCAL_PATH)[0], returnedImage.getLocalPath());
      
        logic.delete(returnedImage);
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
        sampleMap.put(ImageLogic.TITLE, new String[]{expectedImage.getTitle()});
        sampleMap.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{expectedImage.getLocalPath()});
       
        Image returnedImage = logic.createEntity(sampleMap);
        returnedImage.setBoard(blogic.getWithId(expectedImage.getBoard().getId()));
        
        assertImageEquals(expectedImage, returnedImage);
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
            map.put(ImageLogic.TITLE, new String[]{expectedImage.getTitle()});
            map.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
            map.put(ImageLogic.LOCAL_PATH, new String[]{expectedImage.getLocalPath()});
          };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.TITLE, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.TITLE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.URL, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.URL, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.LOCAL_PATH, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.LOCAL_PATH, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
           map.clear();
            map.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
            map.put(ImageLogic.BOARD_ID, new String[]{Integer.toString(expectedImage.getBoard().getId())});
            map.put(ImageLogic.TITLE, new String[]{expectedImage.getTitle()});
            map.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
            map.put(ImageLogic.LOCAL_PATH, new String[]{expectedImage.getLocalPath()});
            map.put(ImageLogic.DATE, new String[]{FORMATTER.format(expectedImage.getDate())});
        };

        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.ID, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.ID, new String[]{"12b"});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

//        fillMap.accept(sampleMap);
//        sampleMap.replace(ImageLogic.BOARD_ID, new String[]{""});
//        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
//        sampleMap.replace(ImageLogic.BOARD_ID, new String[]{"12b"});
//        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        
        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.TITLE, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.TITLE, new String[]{generateString.apply(1001)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.URL, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.URL, new String[]{generateString.apply(256)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.LOCAL_PATH, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.LOCAL_PATH, new String[]{generateString.apply(256)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.TITLE,new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.URL, new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{generateString.apply(1)});
       
        //idealy every test should be in its own method
        Image returnedImage = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.ID)[0]), returnedImage.getId());
        assertEquals(sampleMap.get(ImageLogic.TITLE)[0], returnedImage.getTitle());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.LOCAL_PATH)[0], returnedImage.getLocalPath());
       
        sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(1)});
        //sampleMap.put(ImageLogic.BOARD_ID,new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.TITLE, new String[]{generateString.apply(1000)});
        sampleMap.put(ImageLogic.URL, new String[]{generateString.apply(255)});
        sampleMap.put(ImageLogic.LOCAL_PATH, new String[]{generateString.apply(255)});
      
        //idealy every test should be in its own method
        returnedImage = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.ID)[0]), returnedImage.getId());
        assertEquals(sampleMap.get(ImageLogic.TITLE)[0], returnedImage.getTitle());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
        assertEquals(sampleMap.get(ImageLogic.LOCAL_PATH)[0], returnedImage.getLocalPath());
    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "BoardId", "Title", "Url","LocalPath", "Date"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(ImageLogic.ID, ImageLogic.BOARD_ID, ImageLogic.TITLE, ImageLogic.URL,ImageLogic.LOCAL_PATH,ImageLogic.DATE), list);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedImage);
        assertEquals(expectedImage.getId(), list.get(0));
        assertEquals(expectedImage.getBoard().getId(), list.get(1));
        assertEquals(expectedImage.getTitle(), list.get(2));
        assertEquals(expectedImage.getUrl(), list.get(3));
        assertEquals(expectedImage.getLocalPath(), list.get(4));
        assertEquals(expectedImage.getDate(), list.get(5));     
    }
}

  


