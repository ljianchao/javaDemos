package cn.damai;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println("cn.damai.App");
    	List<String> list = new ArrayList<String>();
    	list.add("a");
    	list.add("b");
    	
    	String listToString = StringUtils.join(list, ",");
    	
        System.out.println(listToString);
    	
    }
}
