package com.ly.spider.util;

import com.ly.spider.rule.Rule;
import com.ly.spider.rule.RuleException;

public class TextUtil
{
	public static  boolean isEmpty(String str)
	{
		if(str == null || str.trim().length() == 0)
		{
			return true ;
		}
		return false ;
	}
	public static int atoi(String input){
		int beginIndex=input.indexOf("单价");
		int endIndex=input.indexOf("元/平米");
		String price=input.substring(beginIndex+2, endIndex);
		return Integer.parseInt(price);
	}
	/**
	 * 对传入的参数进行必要的校验
	 */
	public static void validateRule(Rule rule)
	{
		String url = rule.getUrl();
		if (TextUtil.isEmpty(url))
		{
			throw new RuleException("url不能为空！");
		}
		if (!url.startsWith("http://"))
		{
			throw new RuleException("url的格式不正确！");
		}

		if (rule.getParams() != null && rule.getValues() != null)
		{
			if (rule.getParams().length != rule.getValues().length)
			{
				throw new RuleException("参数的键值对个数不匹配！");
			}
		}

	}
}
