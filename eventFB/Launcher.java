package eventFB;
import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Launcher {
	
	// change  YOURPAGEID and BRIGHTEVENTTOKEN
    public static void main(String[] args)
    {
    	DataPart dp = new DataPart();
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();  
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();  
      
	    SessionFactory factory = meta.getSessionFactoryBuilder().build();  
	    Session session = factory.openSession();   
    	
    	
    	try {
    		
    		String accessToken = dp.getDataAccess();
    		dp.loadEventFromFB(accessToken, YOURPAGEID, session);
			
    		
    	    dp.LoadEventFromBE(session, BRIGHTEVENTTOKEN,48.4274965,-71.0603851,200,1);
    	    dp.LoadEventFromBE(session, BRIGHTEVENTTOKEN,48.4274965,-71.0603851,200,2);
    	    dp.LoadEventFromBE(session, BRIGHTEVENTTOKEN,48.4274965,-71.0603851,200,3);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        System.out.println("successfully saved");    
        factory.close();  
        session.close();
    }
}
