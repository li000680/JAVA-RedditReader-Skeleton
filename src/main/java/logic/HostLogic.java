package logic;

import common.ValidationException;
import dal.HostDAL;
import entity.Host;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Wei Li
 */
public class HostLogic extends GenericLogic<Host, HostDAL> {

    /**
     * create static final variables with proper name of each column. this way
     * you will never manually type it again, instead always refer to these
     * variables.
     *
     * by using the same name as column id and HTML element names we can make
     * our code simpler. this is not recommended for proper production project.
     */
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String EXTRACTION_TYPE = "extraction_type";
    public static final String ID = "id";

    HostLogic() {
        super(new HostDAL());
    }

    @Override
    public List<Host> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public Host getWithId(int id) {
        return get(() -> dal().findById(id));
    }
    public Host getHostWithName(String name) {
        return get(() -> dal().findByName(name));
    }
    public Host getHostWithUrl(String url) {
        return get(() -> dal().findByUrl(url));
    }
    public List<Host> getHostWithExtractionType(String type) {
        return get(() -> dal().findByExtractionType(type));
    }
    @Override
    public Host createEntity(Map<String, String[]> parameterMap) {
        //do not create any logic classes in this method.

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        Host entity = new Host();

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
        String name = parameterMap.get(NAME)[0];
        String url = parameterMap.get(URL)[0];
        String extraction_type = parameterMap.get(EXTRACTION_TYPE)[0];

        //validate the data
        validator.accept(name, 100);
        validator.accept(url, 255);
        validator.accept(extraction_type, 45);

        //set values on entity
        entity.setName(name);
        entity.setUrl(url);
        entity.setExtractionType(extraction_type);

        return entity;
    }
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Name", "Url", "Extraction_type");
    }
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, NAME, URL, EXTRACTION_TYPE);
    }
    @Override
    public List<?> extractDataAsList(Host e) {
       return Arrays.asList(e.getId(), e.getName(), e.getUrl(), e.getExtractionType());

    }
}

