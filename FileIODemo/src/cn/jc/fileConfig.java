package cn.jc;

import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.junit.Test;

public class fileConfig {

	@Test
	public void readXml() throws ConfigurationException{
		
		/*try {
			XMLConfiguration config= new XMLConfiguration("cephPathMap.xml");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Configuration config = new XMLConfiguration(fileConfig.class  
                .getResource("cephPathMap.xml"));  
        String ip = config.getString("ip");  
        String account = config.getString("account");  
        String password = config.getString("password");  
        List<?> roles = config.getList("roles.role");  
        System.out.println("IP: " + ip);  
        System.out.println("Account: " + account);  
        System.out.println("Password: " + password);          
        for (int i = 0; i < roles.size(); i++) {  
            System.out.println("Role: " + roles.get(i));  
        }   
	}
}
