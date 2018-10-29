package com.ep.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CMyString {
	
	// ==============================================================================
    // 常用字符串函数

    /**
     * 判断指定字符串是否为空
     * 
     * @param _string
     *            指定的字符串
     * @return 若字符串为空对象（_string==null）或空串（长度为0），则返回true；否则，返回false.
     */
    public static boolean isEmpty(String _string) {
        return ((_string == null) || (_string.trim().length() == 0));
    }
    
	
	// ==============================================================================
	// 字符串过滤函数

	/**
	 * SQL语句特殊字符过滤处理函数
	 * <p>
	 * 用于：构造SQL语句时，填充字符串参数时使用
	 * </p>
	 * <p>
	 * 如：
	 * <code>String strSQL = "select * from tbName where Name='"+CMyString.filterForSQL("a'bc")+"'" </code>
	 * </p>
	 * <p>
	 * 说明：需要处理的特殊字符及对应转化规则：如： <code> ' ---&gt;''</code>
	 * </p>
	 * <p>
	 * 不允许使用的特殊字符： <code> !@#$%^&*()+|-=\\;:\",./&lt;&gt;? </code>
	 * </p>
	 * 
	 * @param _sContent
	 *            需要处理的字符串
	 * @return 过滤处理后的字符串
	 */
	public static String filterForSQL(String _sContent) {
		if (_sContent == null)
			return "";

		int nLen = _sContent.length();
		if (nLen == 0)
			return "";

		char[] srcBuff = _sContent.toCharArray();
		StringBuffer retBuff = new StringBuffer((int) (nLen * 1.5));

		// caohui@0508 各个应用都需要不去除特殊字符，特修改
		for (int i = 0; i < nLen; i++) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case '\'': {
				retBuff.append("''");
				break;
			}
			case ';':// caohui@0516为了查询Unicode字符
				boolean bSkip = false;
				for (int j = (i + 1); j < nLen && !bSkip; j++) {
					char cTemp2 = srcBuff[j];
					if (cTemp2 == ' ')
						continue;
					if (cTemp2 == '&')
						retBuff.append(';');
					bSkip = true;
				}
				if (!bSkip)
					retBuff.append(';');
				break;
			// case '[': //niuzhao@2005-08-11 处理SQL Server中的通配符 []
			// retBuff.append("[[]");
			// break;
			// case '_': //niuzhao@2005-08-11 处理SQL Server中的通配符 _
			// retBuff.append("[_]");
			// break;
			default:
				retBuff.append(cTemp);
			}// case
		}// end for
		/*
		 * for( int i=0; i <nLen; i++ ){ char cTemp = srcBuff[i]; switch( cTemp
		 * ){ case '\'':{ retBuff.append( "''" ); break; } case '!': case '@':
		 * case '#': case '$': case '%': case '^': case '&': case '*': case '(':
		 * case ')': case '+': case '|': case '-': case '=': case '\\': case
		 * ';': case ':': case '\"': case ',': case '.': case '/': case ' <':
		 * case '>': case '?': break; //skip default : retBuff.append( cTemp );
		 * }//case }//end for
		 */

		return retBuff.toString();
	}
	
	
	 /**
     * HTML元素value值过滤处理函数：将 <code> & &lt; &gt;\ </code> 等特殊字符作转化处理
     * 
     * @sample <code>
     *    &lt;input type="text" name="Name" value="<%=CMyString.filterForHTMLValue(sContent)%>"&gt;
     * </code>
     * @param _sContent
     *            指定的文本内容
     * @return 处理后的文本内容
     */
    public static String filterForHTMLValue(String _sContent) {
        if (_sContent == null)
            return "";

        // _sContent = replaceStr(_sContent,"</script>","<//script>");
        // _sContent = replaceStr(_sContent,"</SCRIPT>","<//SCRIPT>");

        char[] srcBuff = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";

        StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
            case '&': // 转化：& -->&amp;why: 2002-3-19
                // caohui@0515
                // 处理unicode代码
                if ((i + 1) < nLen) {
                    cTemp = srcBuff[i + 1];
                    if (cTemp == '#')
                        retBuff.append("&");
                    else
                        retBuff.append("&amp;");
                } else
                    retBuff.append("&amp;");
                break;
            case '<': // 转化：< --> &lt;
                retBuff.append("&lt;");
                break;
            case '>': // 转化：> --> &gt;
                retBuff.append("&gt;");
                break;
            case '\"': // 转化：" --> &quot;
                retBuff.append("&quot;");
                break;
            default:
                retBuff.append(cTemp);
            }// case
        }// end for

        return retBuff.toString();
    }
    
    /**
     * XML文本过滤处理函数：将 <code> & &lt; &gt;\ </code> 等特殊字符做转化处理
     * 
     * @param _sContent
     *            指定的XML文本内容
     * @return 处理后的文本内容
     */
    public static String filterForXML(String _sContent) {
        if (_sContent == null)
            return "";

        char[] srcBuff = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";

        StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
            case '&': // 转化：& -->&amp;
                retBuff.append("&amp;");
                break;
            case '<': // 转化：< --> &lt;
                retBuff.append("&lt;");
                break;
            case '>': // 转化：> --> &gt;
                retBuff.append("&gt;");
                break;
            case '\"': // 转化：" --> &quot;
                retBuff.append("&quot;");
                break;
            case '\'': // 转化：' --> &apos;
                retBuff.append("&apos;");
                break;
            default:
                retBuff.append(cTemp);
            }// case
        }// end for

        return retBuff.toString();
    }
    
    /**
     * URL过滤处理函数：将 <code> # & </code> 等特殊字符作转化处理
     * 
     * @param _sContent
     *            指定的URL内容
     * @return 处理后的字符串
     */
    public static String filterForUrl(String _sContent) {
        if (_sContent == null)
            return "";

        char[] srcBuff = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";

        StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
            case '%':
                retBuff.append("%25");
                break;
            case '?':
                retBuff.append("%3F");
                break;
            case '#': // 转化：# --> %23
                retBuff.append("%23");
                break;
            case '&': // 转化：& --> %26
                retBuff.append("%26");
                break;
            case ' ': // 转化：空格 --> %20
                retBuff.append("%20");
                break;
            default:
                retBuff.append(cTemp);
            }// case
        }// end for

        return retBuff.toString();
    }
    
    /**
     * JavaScript过滤处理函数：将指定文本中的 <code> " \ \r \n</code> 等特殊字符做转化处理
     * 
     * @sample <code>
     *      <br>&lt;script language="javascript"&gt;
     *      <br>     document.getElementById("id_txtName").value = "<%=CMyString.filterForJs(sValue)%>";
     *      <br>&lt;/script&gt;
     * </code>
     * @param _sContent
     *            指定的javascript文本
     * @return 转化处理后的字符串
     */
    public static String filterForJs(String _sContent) {
        if (_sContent == null)
            return "";

        char[] srcBuff = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";

        StringBuffer retBuff = new StringBuffer((int) (nLen * 1.8));

        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
            case '"': // 转化：" --> \"
                retBuff.append("\\\"");
                break;
            case '\'': // 转化：' --> \'
                retBuff.append("\\\'");
                break;
            case '\\': // 转化：\ --> \\
                retBuff.append("\\\\");
                break;
            case '\n':
                retBuff.append("\\n");
                break;
            case '\r':
                retBuff.append("\\r");
                break;
            case '\f':
                retBuff.append("\\f");
                break;
            case '\t':
                retBuff.append("\\t");
                break;
            case '/':
                retBuff.append("\\/");
                break;
            default:
                retBuff.append(cTemp);
            }// case
        }// end for

        return retBuff.toString();
    }
    
    /**
	 * 验证翻页参数是否是数字
	 * @author  Zhaoxh
	 * @version 创建时间：2018年7月4日 下午5:28:31
	 * @Description: TODO
	 * @param bot
	 * @return 是数字返回true
	 */
	public static boolean isMatches(String bot){
		 boolean flag=false;
		 try{
		  String regex="^[1-9]+[0-9]*$";
		  Pattern p=Pattern.compile(regex);
		  Matcher m=p.matcher(bot);
		  if(m.find()){
		  return true;
		  }else{
		  }
		 }catch(Exception e){
		  e.printStackTrace();
		 }
		 return flag;
   }
	
	/*****
	 * 防止 XXE漏洞 注入实体攻击
	 * @param xmlStr
	 * @return
	 */
	public static String filterXXE(String xmlStr){
		xmlStr = xmlStr.replace("DOCTYPE", "").replace("SYSTEM", "").replace("ENTITY", "").replace("PUBLIC", "");
		return xmlStr;
	}
}
