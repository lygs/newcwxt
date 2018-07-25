package com.ep.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class PojoMapper {

    private static ObjectMapper m = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();

    public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass)
    throws JsonMappingException, JsonParseException, IOException {
        return m.readValue(jsonAsString, pojoClass);
    }

    public static <T> Object fromJson(FileReader fr, Class<T> pojoClass)
    throws JsonParseException, IOException
    {
        return m.readValue(fr, pojoClass);
    }

    public static String toJson(Object pojo, boolean prettyPrint)
    throws JsonMappingException, JsonGenerationException, IOException {
        StringWriter sw = new StringWriter();
        JsonGenerator jg = jf.createJsonGenerator(sw);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        m.writeValue(jg, pojo);
        return sw.toString();
    }

    public static void toJson(Object pojo, FileWriter fw, boolean prettyPrint)
    throws JsonMappingException, JsonGenerationException, IOException {
        JsonGenerator jg = jf.createJsonGenerator(fw);
        if (prettyPrint) {
            jg.useDefaultPrettyPrinter();
        }
        m.writeValue(jg, pojo);
    }
    
    //转换为Jquery easyUI适配的Json数组,数组total属性大小由参数List的Size决定
    public static String toJsonArray(List <Object> list){
    	String jsonArrayString = null;
    	JsonArrayFormat jaf = new JsonArrayFormat();
    	jaf.setTotal(list.size());
    	Object [] objArray = list.toArray();
    	jaf.setRows(objArray);
    	try {
    		jsonArrayString = m.writeValueAsString(jaf);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArrayString;
    	
    }
    
    //转换为Jquery easyUI适配的Json数组，数组total属性大小由方法参数total决定
    public static String toJsonArray(List <Object> list,int total){
    	String jsonArrayString = null;
    	JsonArrayFormat jaf = new JsonArrayFormat();
    	jaf.setTotal(total);
    	Object [] objArray = list.toArray();
    	jaf.setRows(objArray);
    	try {
    		jsonArrayString = m.writeValueAsString(jaf);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArrayString;
    	
    }
    
    
    public static <T> String mapToJson(T obj){
		String json=null;
		ObjectMapper mapper=null;
		try{
			mapper = new ObjectMapper();
			json=mapper.writeValueAsString(obj);//把map或者是list转换成
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mapper=null;
			return json;
		}
	}

}
