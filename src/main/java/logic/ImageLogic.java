package logic;

import common.ValidationException;
import dal.ImageDAL;
import entity.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
/**
 *
 * @author Wei Li
 */
public class ImageLogic extends GenericLogic<Image,ImageDAL> {

    /**
     * create static final variables with proper name of each column. this way
     * you will never manually type it again, instead always refer to these
     * variables.
     *
     * by using the same name as column id and HTML element names we can make
     * our code simpler. this is not recommended for proper production project.
     */
    public static final String BOARD_ID = "boardId";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String LOCAL_PATH = "localPath";
    public static final String DATE = "date";
    public static final String ID = "id";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

    ImageLogic() {
        super(new ImageDAL());
    }

    @Override
    public List<Image> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public Image getWithId(int id) {
        return get(() -> dal().findById(id));
    }
    public List<Image> getImagesWithBoardId(int boardID) {
      return get(() -> dal().findByBoardId(boardID));
     }
    public List<Image> getImagesWithTitle(String title) {
      return get(() -> dal().findByTitle(title));
     }
    public Image getImageWithUrl(String url) {
      return get(() -> dal().findByUrl(url));
     }
    public Image getImageWithLocalPath(String path) {
      return get(() -> dal().findByLocalPath(path));
     }
    public List<Image> getImagesWithDate(Date date) {
      return get(() -> dal().findByDate(date));
     }
    public String convertDate(Date date){
     return FORMATTER.format(date);
    }

   @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        //do not create any logic classes in this method.

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        Image entity = new Image();

        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                throw new ValidationException("value cannot be null, empty or larger than " + length + " characters");
            }
        };

        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.
        String title = parameterMap.get(TITLE)[0];
        String url = parameterMap.get(URL)[0];
        String local_path = parameterMap.get(LOCAL_PATH)[0];
        
        //validate the data
        validator.accept(title, 1000);
        validator.accept(url, 255);
        validator.accept(local_path, 255);

        //set values on entity
        entity.setTitle(title);
        entity.setUrl(url);
        entity.setLocalPath(local_path);
        entity.setDate(new Date());
       // entity.setDate(Date.from(Instant.now(Clock.systemDefaultZone())));
       
        return entity;
    }
//    public Image updateEntity(Map<String, String[]> parameterMap) {
//        return null;
//}
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "BoardId", "Title", "Url","LocalPath", "Date");
    }
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, BOARD_ID, TITLE, URL,LOCAL_PATH,DATE);
    }
    @Override
    public List<?> extractDataAsList(Image e) {
       return Arrays.asList(e.getId(), e.getBoard().getId(), e.getTitle(), e.getUrl(),e.getLocalPath(),e.getDate());

    }
}
