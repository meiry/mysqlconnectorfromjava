package com.test.MySqlConnectorSpringBoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//Hibernate automatically translates the entity into a table.
@Entity // This tells Hibernate to make a table out of this class
public class Registration {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;	
	private String first;
    private String last;
    private Integer age;
    
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

    

}
