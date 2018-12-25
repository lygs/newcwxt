package com.ep.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ep.entity.Sysuser;
@WebFilter(filterName="/LoginFilter",urlPatterns="/*")
public class LoginFilter implements Filter {
	public FilterConfig config;
	public String redirectPaths ="/login.html";
	//不过滤
	public String logonStrings ="index.html;login.html;/admin/login.js;"
			+ "codeMsg.jsp;/user/login.action;/questionSearch/search;"
			+ "/chnl/getAll;/questionAnswer/updateQuestionAnswerGoodnumById;"
			+ "/questionAnswer/getChnlNameByQid;/questionAnswer/addQuestionByExcel;"
			+ "/know/getHotKnowledge;/questionAnswer/getQuestionByComm;"
			+ "/questionAnswer/getQuestionByKnowledge;/questionAnswer/getQaByChnlid;"
			+ "/record;/praise;/questionAnswer/getQuestionByClick;/template/getSelectedTemp";
	//过滤
	public String includeStrings=".action;.jsp;.html;/know/;/questionAnswer/;/user/;/logs/";
	public LoginFilter() {
    	
    }
    
    public static boolean isContains(String container, String[] regx) {
        boolean result = false;

        for (int i = 0; i < regx.length; i++) {
            if (container.indexOf(regx[i]) != -1) {
                return true;
            }
        }
        return result;
    }
    
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		this.config = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 	HttpServletRequest hrequest = (HttpServletRequest)request;
	        HttpServletResponse wrapper = (HttpServletResponse) response;
//	        String logonStrings = config.getInitParameter("logonStrings");        // 登录登陆页面
//	        String includeStrings = config.getInitParameter("includeStrings");    // 过滤资源后缀参数
	        String redirectPath = hrequest.getContextPath() + redirectPaths;//config.getInitParameter("redirectPath");// 没有登陆转向页面
	        String[] logonList = logonStrings.split(";"); //不过滤
	        String[] includeList = includeStrings.split(";");//过滤
	        if (this.isContains(hrequest.getRequestURI(), logonList)) {//不过滤
	            chain.doFilter(request, response);
	            return;
	        }else if (!this.isContains(hrequest.getRequestURI(), includeList)) {//过滤
	            chain.doFilter(request, response);
	            return;
	        }
	        
	        Sysuser user = ( Sysuser) hrequest.getSession().getAttribute("user");//判断用户是否登录
	        if (user == null) {
	            wrapper.sendRedirect(redirectPath);
	            return;
	        }else {
	            chain.doFilter(request, response);
	            return;
	        }
	        
	      
//		chain.doFilter(request, response);
	}

	 public void init(FilterConfig filterConfig) throws ServletException {
	        config = filterConfig;
	 }

}
