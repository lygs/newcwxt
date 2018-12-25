package com.ep.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ep.entity.Sysuser;
import com.ep.util.CMyString;

public class HandlerInterceptor implements org.springframework.web.servlet.HandlerInterceptor{

	//执行Handler完成执行此方法  
    //应用场景：统一异常处理，统一日志处理  
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		String url=request.getRequestURI(); 
		//System.out.println("----------1------");
	}

	//进入Handler方法之后，返回modelAndView之前执行  
    //应用场景从modelAndView出发：将公用的模型数据(比如菜单导航)在这里  
    //传到视图，也可以在这里统一指定视图  
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		String url=request.getRequestURI(); 
		//System.out.println("----------2-----");
	}

	//执行Handler方法之前执行  
    //用于身份认证、身份授权  
    //比如身份认证，如果认证通过表示当前用户没有登陆，需要此方法拦截不再向下执行 
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
		// TODO Auto-generated method stub
		//获取请求的url  
        String url=request.getRequestURI();  
      //不过滤
      String logonStrings ="/css/;/images/;/lib/;/js/;/index.html;/login.html;/admin/login.js;"
    			+ "codeMsg.jsp;/user/login.action;/questionSearch/search;"
    			+ "/chnl/getAll;/questionAnswer/updateQuestionAnswerGoodnumById;"
    			+ "/questionAnswer/getChnlNameByQid;/questionAnswer/addQuestionByExcel;"
    			+ "/know/getHotKnowledge;/questionAnswer/getQuestionByComm;"
    			+ "/questionAnswer/getQuestionByKnowledge;/questionAnswer/getQaByChnlid;"
    			+ "/record;/praise;/questionAnswer/getQuestionByClick;/template/getSelectedTemp;/logs/";
    	//过滤
        String includeStrings ="/know/;/questionAnswer/;/user/;/wordExpansion/;/admin/;/individualWord/;/hfw/;/praise/;/record/;/template/";
        //判断url是否是公开地址(实际使用时将公开地址配置到配置文件中)  
        String[] logonList = logonStrings.split(";"); //不过滤
        String[] includeList = includeStrings.split(";"); //不过滤
        //FilterChain chain=
        if (isContains(url, logonList)) {//不过滤
           // chain.doFilter(request, response);
            return true;
        }else if (isContains(url, includeList)) {//过滤
        	//判断session  
            HttpSession session=request.getSession();  
            //从session中取出用户份信息  
            Sysuser user=(Sysuser)session.getAttribute("user");  
              
            if(user!=null){  
                //身份存在，放行  
            	try {
            		String userName = user.getUserName();
            		if(StringUtils.isNotEmpty(userName)) {
            			return true; 
            		}else {
            			return false; 
            		}
            		
				} catch (Exception e) {
					return false; 
				}
            }else {
            	 return false;
            }
           
        } 

        //执行这里表示用户身份需要验证，跳转到登录界面  
        response.sendRedirect(request.getContextPath()+"/login.html");
       // request.getRequestDispatcher("/login.html").forward(request, response);  
          
        //return false表示拦截，不向下执行  
        //return true表示放行          
        return false;  
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

}
