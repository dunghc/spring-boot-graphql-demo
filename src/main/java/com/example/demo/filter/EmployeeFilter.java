package com.example.demo.filter;

import lombok.Data;

@Data
public class EmployeeFilter {
	private FilterField salary;
	private FilterField age;
	private FilterField position;
}
