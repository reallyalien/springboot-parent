package com.ot.springbatch.job;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DefaultRecord implements Record ,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private int cursor;
	private BigInteger totalRecords;
	private Map<String, Object> columunAndValues;
	private Map<String,Boolean>  processMark =new LinkedHashMap<>();
	private List<String> columnNames;
    private boolean isNullFlag =false;
	public boolean isNullFlag() {
		return isNullFlag;
	}

	public void setNullFlag(boolean isNullFlag) {
		this.isNullFlag = isNullFlag;
	}

	public DefaultRecord() {
		columunAndValues = new LinkedHashMap<>();
	}

	@Override
	public void setTotalRecords(BigInteger totalRecords) {
		this.totalRecords = totalRecords;

	}

	@Override
	public BigInteger getTotalRecords() {
		return this.totalRecords;
	}


	@Override
	public void add(String columun, Object value) {
		columunAndValues.put(columun, value);
		this.cursor++;
	}

//	@Override
//	public void add(Object field) {
//		add(cursor, field);
//	}

	public void add(Record item) {
		this.columunAndValues.putAll(item.getColumnAndValues());
	}

	@Override
	public int size() {
		return columunAndValues.size();
	}

	@Override
	public String toString() {

		return columunAndValues.toString();
	}

	@Override
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}


	@Override
	public List<String> getColumnNames() {
		return new ArrayList<>(columunAndValues.keySet());

	}

	@Override
	public Object get(int index) {
		return columunAndValues.get(getColumnNames().get(index));

	}

	@Override
	public Object remove(String columnName) {

		return columunAndValues.remove(columnName);
	}

	@Override
	public Object get(String columnName) {
		// TODO Auto-generated method stub
		return columunAndValues.get(columnName);
	}

	@Override
	public void setColumnAndValues(Map<String, Object> columunAndValues) {
		this.columunAndValues = columunAndValues;

	}

	@Override
	public Map<String, Object> getColumnAndValues() {
		return columunAndValues;
	}
    
	@Override
	public void markProcess(String processName) {
		processMark.put(processName, true);
	}
	
	@Override
	public boolean getProcessMark(String processName) {
		 if(processMark.containsKey(processName)){
			 return false;
		 }else {
			 return true;
		 }
	}

}
