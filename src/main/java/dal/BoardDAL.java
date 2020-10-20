package dal;

import entity.Board;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei Li
 */
public class BoardDAL extends GenericDAL<Board>{

    public BoardDAL() {
        super(Board.class);
    }
    
    @Override
    public List<Board> findAll(){
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Board.findAll", null);
    }
    
    @Override
    public Board findById( int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "Board.findById", map);
    }

    public List<Board> findByHostid(int hostId){
        Map<String, Object> map = new HashMap<>();
        map.put("hostId", hostId);
        return findResults( "Board.findByHostid", map);
    }
    public Board findByUrl(String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findResult( "Board.findByUrl", map);
    }
    public  List<Board> findByName(String name){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResults( "Board.findByName", map);
    }
 }
