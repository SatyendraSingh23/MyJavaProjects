package com.thinking.machines.network.common;
public enum Managers
{
	DESIGNATION, EMPLOYEE;
	public enum Designation
	{
		ADD_DESIGNATION , UPDATE_DESIGNATION, REMOVE_DESIGNATION, GET_DESIGNATION_COUNT,GET_DESIGNATION_BY_CODE, DESIGNATION_CODE_EXISTS, DESIGNATION_TITLE_EXISTS, GET_DESIGNATIONS;
	}
	public enum Employee
	{
		ADD , UPDATE, REMOVE, GET;
	}
	private Managers()
	{

	}
	public static String getManagerType(Managers m)
	{
		return m.toString();
	}
	public static String getAction(Designation d)
	{
		return d.toString();
	}
	public static String getAction(Employee e)
	{
		return e.toString();
	}

}