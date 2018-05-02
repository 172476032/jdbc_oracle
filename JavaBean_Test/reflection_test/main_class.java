package reflection_test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Scanner;



public class main_class {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {		
			// TODO Auto-generated constructor stub
//		    getclass_test();
 getclass();
			reflect_class();
	}
	public static void getclass_test(){
		getclass_test classType = new getclass_test(2,"1");
		Class<? extends getclass_test>	classattr =classType.getClass(); 
		String calssname = classType.getClass().getName();
		System.out.println(classattr.getConstructors().toString());
		System.out.println(calssname);
	}
	
	public static void getclass() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException{
		Date today = new java.util.Date();
		System.out.println(today.getClass());
		System.out.println(today.getClass().getName());
		Class<?> reflect_today = Class.forName("java.util.Date");
		System.out.println(reflect_today.getClass());
		System.out.println("----------------------------------------------------");
		if (today.getClass() == java.util.Date.class) {
			System.out.println("对象的getclass和类名.class一样");
		}
		System.out.println("-----------获取示例对象----------------------------------------");
         Date  yesterday = (Date) Class.forName("java.util.Date").newInstance();
         System.out.println("对象的getclass和类名.class一样");
	}
	public static void reflect_class() throws ClassNotFoundException{
		  System.out.println("-----------反射包内重要的三类----------------------------------------");
	      Scanner sin = new Scanner(System.in);
	      String Name =  sin.next();
	      Class cl = Class.forName(Name);
	      Class	supercl =cl.getSuperclass();
	      System.out.println(cl.getModifiers());
	      String modifiers = Modifier.toString(cl.getModifiers());
	      System.out.println(modifiers);
	      if (modifiers.length() >0) {
	    	  System.out.println(modifiers +" " + "class" +Name);
		}
	      if (supercl!=null && supercl != Object.class) {
			System.out.println(" extends " + supercl.getName());
		} 
	      System.out.println("\n{\n");
	      printFields(cl);
	      System.out.println("\n}\n");
	}
	
	public static void printFields(Class cl){
		Field[] fields = cl.getFields();
		for (java.lang.reflect.Field field : fields) {
			Class type = field.getType();
			String name = field.getName();
		    System.out.println("  ");
		 String modifiers = Modifier.toString(cl.getModifiers());
		 if (modifiers.length()>0) {
			System.out.println(modifiers+" ");			
		}
		 System.out.println(type.getName()+" "+name+";");
		}
	}
	public void printConstructor(){
		
		
		
	}
	
	
}
