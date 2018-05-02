package reflection_test;

public class getclass_test {
private Integer id;
private String name;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return name;
}
public getclass_test(Integer id, String name) {
	super();
	this.id = id;
	this.name = name;
}
public void setName(String name) {
	this.name = name;
}
}
