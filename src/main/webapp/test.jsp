<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="lib/jquery.min.js"></script>
<script type="text/javascript" src="js/test.js"></script>
<title>同步</title>
<style>
*{padding:0px; margin:0px;}
</style>
</head>

<body>
  <div>
    <!--  <div style="margin-top:30px;margin-left:30px;">
     	<h4>wcm文档数据同步到问答表</h4>
      <form class="form" style="margin-top:10px;">
         <span></span><input type="text" id="pager"/>
         <input type="button" value="确定" onclick="sures()"/>
         <input type="reset" value="重置"/>
      </form>
    </div>
    
    <div style="margin-top:30px;margin-left:30px;">
    	<h4>wcm栏目同步到智能问答的栏目表</h4>
    	<input type="button" value="一键同步" onclick="sycnChnl()" style="margin-top:10px;"/>
    </div>     -->
    <input type="button" value="清除答案为空的数据" onclick="delQuestion()"/><br><br><br><br>
    
    <input type="button" value="重新创建索引" onclick="reQuestion()"/>
  </div>
</body>
</html>
