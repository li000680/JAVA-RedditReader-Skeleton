package logic;

import common.ValidationException;
import dal.BoardDAL;
import entity.Board;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Wei Li
 */
public class BoardLogic extends GenericLogic<Board, BoardDAL> {

    /**
     * create static final variables with proper name of each column. this way
     * you will never manually type it again, instead always refer to these
     * variables.
     *
     * by using the same name as column id and HTML element names we can make
     * our code simpler. this is not recommended for proper production project.
     */
    public static final String HOST_ID = "hostId";
    public static final String URL = "url";
    public static final String NAME = "username";
    public static final String ID = "id";

    BoardLogic() {
        super(new BoardDAL());
    }

    @Override
    public List<Board> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public Board getWithId(int id) {
        return get(() -> dal().findById(id));
    }
    public List<Board> getBoardsWithHostID(int hostId) {
        return get(() -> dal().findByHostid(hostId));
    }
    public List<Board> getBoardsWithName(String name) {
        return get(() -> dal().findByName(name));
    }
    @Override
    public Board createEntity(Map<String, String[]> parameterMap) {
        //do not create any logic classes in this method.

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        Board entity = new Board();

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
        String url = parameterMap.get(URL)[0];
        String name = parameterMap.get(NAME)[0];

        //validate the data
        validator.accept(url, 255);
        validator.accept(name, 100);

        //set values on entity
        entity.setUrl(url);
        entity.setName(name);
       
        return entity;
    }
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Url", "Name");
    }
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, URL, NAME);
    }
    @Override
    public List<?> extractDataAsList(Board e) {
        return Arrays.asList(e.getId(), e.getHostid().getId(), e.getUrl(), e.getName());

    }
    public Board getBoardWithUrl(String url) {
        return get(() -> dal().findByUrl(url));
    }
}

