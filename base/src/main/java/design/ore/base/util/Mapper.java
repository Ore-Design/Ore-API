package design.ore.base.util;

import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NonNull;

public class Mapper
{
	@Getter private static ObjectMapper mapper = null;
	private static Callable<ObjectMapper> mapperFactory = null;
	
	public static void initialize(Callable<ObjectMapper> mapperFactory) throws Exception
	{
		if(mapperFactory != null) throw new Exception("Mapper is already initialized!");
		
		Mapper.mapperFactory = mapperFactory;
	}
	
	public static void registerMapperFactory(Callable<ObjectMapper> mapperFactory)
	{
		if(Mapper.mapperFactory == null)
		{
			Mapper.mapperFactory = mapperFactory;
			Mapper.mapper = Mapper.createMapper();
		}
		else { Log.getLogger().warn("Someone attempted to register a different mapper factory, but it's locked!"); }
	}
	
	public static ObjectMapper createMapper()
	{
		if(mapperFactory != null)
		{
			try { return mapperFactory.call(); }
			catch (Exception e) { Log.getLogger().warn(Log.formatThrowable("Error creating new mapper!", e)); }
		}
		else Log.getLogger().warn("Mapper Creator has not yet been registered!");
		
		return null;
	}
	
	public static <T> T quickClone(@NonNull T toClone, Class<? extends T> clazz)
	{
		try
		{
			String serialized = mapper.writeValueAsString(toClone);
			return mapper.readValue(serialized, clazz);
		}
		catch (Exception e)
		{
			Log.getLogger().warn(Log.formatThrowable("Error quick cloning object " + toClone, e));
			return null;
		}
	}
	
	public static <T> T quickClone(@NonNull T toClone, TypeReference<? extends T> type)
	{
		try { return mapper.readValue(mapper.writeValueAsString(toClone), type); }
		catch (Exception e)
		{
			Log.getLogger().warn(Log.formatThrowable("Error quick cloning object " + toClone, e));
			return null;
		}
	}
	
	public static void printItem(String name, Object obj)
	{
		try { Log.getLogger().info(name + ": " + mapper.writeValueAsString(obj)); }
		catch(Exception e) { Log.getLogger().warn(Log.formatThrowable("Failed to print item " + name + "!", e)); }
	}
}
