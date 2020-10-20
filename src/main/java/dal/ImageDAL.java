
package dal;

import entity.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 *
 * @author Wei Li
 */
public class ImageDAL extends GenericDAL<Image>{

    public ImageDAL() {
        super(Image.class);
    }
    
    @Override
    public List<Image> findAll(){
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Image.findAll", null);
    }
   
    @Override
    public Image findById( int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "Image.findById", map);
    }
     public List<Image> findByBoardId(int boardId){
        Map<String, Object> map = new HashMap<>();
        map.put("boardid", boardId);
        return findResults( "Image.findByBoardId", map);
    }
     public List<Image> findByTitle(String title){
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        return findResults( "Image.findByTitle", map);
    }
    public Image findByUrl(String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findResult( "Image.findByUrl", map);
    }
    public List<Image> findByDate(Date date){
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        return findResults( "Image.findByDate", map);
    }
    public Image findByLocalPath(String localPath){
        Map<String, Object> map = new HashMap<>();
        map.put("localPath", localPath);
        return findResult( "Image.findByLocalPath", map);
    }
  }


