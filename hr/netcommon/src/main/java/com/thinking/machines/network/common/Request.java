package com.thinking.machines.network.common;
public class Request implements java.io.Serializable
{
	private Object [] arguments;
	private String action;
	private String manager;
	public void setArguments(Object ...arguments)
	{
		this.arguments=arguments;
	}
	public Object [] getArguments()
	{
		return this.arguments;
	}
	public void setAction(String action)
	{
		this.action=action;
	}
	public String getAction()
	{
		return this.action;
	}
	public void setManager(String manager)
	{
		this.manager=manager;
	}
	public String getManager()
	{
		return this.manager;
	}
}