package com.ot.springbatch.job;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public interface Record {

	public void add(String columnName, Object object);


	public int size();

	public void setColumnNames(List<String> columnNames);


	
	public void setTotalRecords(BigInteger totalRecords);
	
	public BigInteger getTotalRecords();
	
	public List<String> getColumnNames();
	
	public Object get(int index);
	
	public Object get(String columnName);
	
	public Object  remove(String columnName);
	
	public void setColumnAndValues(Map<String,Object> columnAndValues);
	
	public Map<String,Object> getColumnAndValues();
	
	public void markProcess(String processName);
	
	public boolean getProcessMark(String processName);
	
	public boolean isNullFlag();
	
	public void setNullFlag(boolean isNullFlag);
}
