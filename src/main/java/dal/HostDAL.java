package dal;

import entity.Host;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei Li
 */
public class HostDAL extends GenericDAL<Host>{

    public HostDAL() {
        super(Host.class);
    }
    
    @Override
    public List<Host> findAll(){
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Host.findAll", null);
    }
   
    @Override
    public Host findById( int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "Host.findById", map);
    }
     public Host findByName( String name){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResult( "Host.findByName", map);
    }
    public Host findByUrl( String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url",url);
        return findResult( "Host.findByUrl", map);
    }
    public List<Host> findByExtractionType( String type){
        Map<String, Object> map = new HashMap<>();
        map.put("type",type);
        return findResults( "Host.findByExtractionType", map);
    }
 }
