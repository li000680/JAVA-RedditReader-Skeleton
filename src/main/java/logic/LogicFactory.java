package logic;
import common.ValidationException;
import java.lang.reflect.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wei Li
 */
public class LogicFactory {
    private static final String PACKAGE="logic.";
    private static final String SUFFIX="Logic";
    
   
private LogicFactory(){
    }


   public static <T> T getFor(String entityName) {
//     try{
//         return getFor((Class<T>)Class.forName(PACKAGE+entityName + SUFFIX));
//         }catch(ClassNotFoundException ex){
//         throw new IllegalArgumentException();
//   }
       try{
           return getFor((Class<T>)Class.forName(PACKAGE+entityName + SUFFIX));
           }catch(ClassNotFoundException ex){
           Logger.getGlobal().log(Level.SEVERE,ex.getMessage(),ex);
           throw new ValidationException(ex);
 }
   }
   public static <T> T getFor(Class<T> type){
     try{
         Constructor<T> declaredConstructor=type.getDeclaredConstructor();
         return declaredConstructor.newInstance();
        }catch(InstantiationException | IllegalAccessException | IllegalArgumentException |InvocationTargetException | NoSuchMethodException | SecurityException ex){
          throw new IllegalArgumentException();
    }
    
   }
 }
    