/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.38
 * Generated at: 2016-10-20 11:52:37 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class home1_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(2);
    _jspx_dependants.put("jar:file:/C:/Users/a_kayerov/workspace_ipra/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/MyFirstMVCSpring/WEB-INF/lib/jstl-1.2.jar!/META-INF/c.tld", Long.valueOf(1153370682000L));
    _jspx_dependants.put("/WEB-INF/lib/jstl-1.2.jar", Long.valueOf(1460534077410L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
return;
}

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("<head>\n");
      out.write("\t<meta charset=\"utf-8\" />\n");
      out.write("\t<title>Image-less CSS3 Glowing Form Implementation</title>\n");
      out.write("\t<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js\"></script>\n");
      out.write("\t<script>\n");
      out.write("\t\t$(function(){\n");
      out.write("\t\t  var $form_inputs =   $('form input');\n");
      out.write("\t\t  var $rainbow_and_border = $('.rain, .border');\n");
      out.write("\t\t  /* Used to provide loping animations in fallback mode */\n");
      out.write("\t\t  $form_inputs.bind('focus', function(){\n");
      out.write("\t\t  \t$rainbow_and_border.addClass('end').removeClass('unfocus start');\n");
      out.write("\t\t  });\n");
      out.write("\t\t  $form_inputs.bind('blur', function(){\n");
      out.write("\t\t  \t$rainbow_and_border.addClass('unfocus start').removeClass('end');\n");
      out.write("\t\t  });\n");
      out.write("\t\t  $form_inputs.first().delay(800).queue(function() {\n");
      out.write("\t\t\t$(this).focus();\n");
      out.write("\t\t  });\n");
      out.write("\t\t});\n");
      out.write("\t</script>\n");
      out.write("\t\t<style>\n");
      out.write("\t\t\tbody{\n");
      out.write("\t\t\t\tbackground: #000;\n");
      out.write("\t\t\t\tcolor: #DDD;\n");
      out.write("\t\t\t\tfont-family: 'Helvetica', 'Lucida Grande', 'Arial', sans-serif;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t.border,\n");
      out.write("\t\t\t.rain{\n");
      out.write("\t\t\t\theight: 170px;\n");
      out.write("\t\t\t\twidth: 320px;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t/* Layout with mask */\n");
      out.write("\t\t\t.rain{\n");
      out.write("\t\t\t\t padding: 10px 12px 12px 10px;\n");
      out.write("\t\t\t\t -moz-box-shadow: 10px 10px 10px rgba(0,0,0,1) inset, -9px -9px 8px rgba(0,0,0,1) inset;\n");
      out.write("\t\t\t\t -webkit-box-shadow: 8px 8px 8px rgba(0,0,0,1) inset, -9px -9px 8px rgba(0,0,0,1) inset;\n");
      out.write("\t\t\t\t box-shadow: 8px 8px 8px rgba(0,0,0,1) inset, -9px -9px 8px rgba(0,0,0,1) inset;\n");
      out.write("\t\t\t\t margin: 100px auto;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t/* Artifical \"border\" to clear border to bypass mask */\n");
      out.write("\t\t\t.border{\n");
      out.write("\t\t\t\tpadding: 1px;\n");
      out.write("\t\t\t\t-moz-border-radius: 5px;\n");
      out.write("\t\t\t    -webkit-border-radius: 5px;\n");
      out.write("\t\t\t\tborder-radius: 5px;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t.border,\n");
      out.write("\t\t\t.rain,\n");
      out.write("\t\t\t.border.start,\n");
      out.write("\t\t\t.rain.start{\n");
      out.write("\t\t\t\tbackground-repeat: repeat-x, repeat-x, repeat-x, repeat-x;\n");
      out.write("\t\t\t\tbackground-position: 0 0, 0 0, 0 0, 0 0;\n");
      out.write("\t\t\t\t/* Blue-ish Green Fallback for Mozilla */\n");
      out.write("\t\t\t\tbackground-image: -moz-linear-gradient(left, #09BA5E 0%, #00C7CE 15%, #3472CF 26%, #00C7CE 48%, #0CCF91 91%, #09BA5E 100%);\n");
      out.write("\t\t\t\t/* Add \"Highlight\" Texture to the Animation */\n");
      out.write("\t\t\t\tbackground-image: -webkit-gradient(linear, left top, right top, color-stop(1%,rgba(0,0,0,.3)), color-stop(23%,rgba(0,0,0,.1)), color-stop(40%,rgba(255,231,87,.1)), color-stop(61%,rgba(255,231,87,.2)), color-stop(70%,rgba(255,231,87,.1)), color-stop(80%,rgba(0,0,0,.1)), color-stop(100%,rgba(0,0,0,.25)));\n");
      out.write("\t\t\t\t/* Starting Color */\n");
      out.write("\t\t\t\tbackground-color: #39f;\n");
      out.write("\t\t\t\t/* Just do something for IE-suck */\n");
      out.write("\t\t\t\tfilter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#00BA1B', endColorstr='#00BA1B',GradientType=1 );\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t/* Non-keyframe fallback animation */\n");
      out.write("\t\t\t.border.end,\n");
      out.write("\t\t\t.rain.end{\n");
      out.write("\t\t\t\t-moz-transition-property: background-position;  \n");
      out.write("\t\t\t\t-moz-transition-duration: 30s;\n");
      out.write("\t\t\t\t-moz-transition-timing-function: linear;\n");
      out.write("\t\t\t\t-webkit-transition-property: background-position;  \n");
      out.write("\t\t\t\t-webkit-transition-duration: 30s;  \n");
      out.write("\t\t\t\t-webkit-transition-timing-function: linear;\n");
      out.write("\t\t\t\t-o-transition-property: background-position;  \n");
      out.write("\t\t\t\t-o-transition-duration: 30s;  \n");
      out.write("\t\t\t\t-o-transition-timing-function: linear;\n");
      out.write("\t\t\t\ttransition-property: background-position;  \n");
      out.write("\t\t\t\ttransition-duration: 30s;  \n");
      out.write("\t\t\t\ttransition-timing-function: linear;\n");
      out.write("\t\t\t\tbackground-position: -5400px 0, -4600px 0, -3800px 0, -3000px 0;\t\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t/* Keyfram-licious animation */\n");
      out.write("\t\t\t@-webkit-keyframes colors {\n");
      out.write("\t\t\t    0% {background-color: #39f;}\n");
      out.write("\t\t\t    15% {background-color: #F246C9;}\n");
      out.write("\t\t\t    30% {background-color: #4453F2;}\n");
      out.write("\t\t\t    45% {background-color: #44F262;}\n");
      out.write("\t\t\t    60% {background-color: #F257D4;}\n");
      out.write("\t\t\t    75% {background-color: #EDF255;}\n");
      out.write("\t\t\t    90% {background-color: #F20006;}\n");
      out.write("\t\t\t    100% {background-color: #39f;}\n");
      out.write("\t\t    }\n");
      out.write("\t\t    .border,.rain{\n");
      out.write("\t\t\t    -webkit-animation-direction: normal;\n");
      out.write("\t\t\t    -webkit-animation-duration: 20s;\n");
      out.write("\t\t\t    -webkit-animation-iteration-count: infinite;\n");
      out.write("\t\t\t    -webkit-animation-name: colors;\n");
      out.write("\t\t\t    -webkit-animation-timing-function: ease;\n");
      out.write("\t\t    }\n");
      out.write("\t\t    \n");
      out.write("\t\t    /* In-Active State Style */\n");
      out.write("\t\t\t.border.unfocus{\n");
      out.write("\t\t\t\tbackground: #333 !important;\t\n");
      out.write("\t\t\t\t -moz-box-shadow: 0px 0px 15px rgba(255,255,255,.2);\n");
      out.write("\t\t\t\t -webkit-box-shadow: 0px 0px 15px rgba(255,255,255,.2);\n");
      out.write("\t\t\t\t box-shadow: 0px 0px 15px rgba(255,255,255,.2);\n");
      out.write("\t\t\t\t -webkit-animation-name: none;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t.rain.unfocus{\n");
      out.write("\t\t\t\tbackground: #000 !important;\t\n");
      out.write("\t\t\t\t-webkit-animation-name: none;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\t\n");
      out.write("\t\t\t/* Regular Form Styles */\n");
      out.write("\t\t\tform{\n");
      out.write("\t\t\t\tbackground: #212121;\n");
      out.write("\t\t\t\t-moz-border-radius: 5px;\n");
      out.write("\t\t\t\t-webkit-border-radius: 5px;\n");
      out.write("\t\t\t    border-radius: 5px;\n");
      out.write("\t\t\t\theight: 100%;\n");
      out.write("\t\t\t\twidth: 100%;\n");
      out.write("\t\t\t\tbackground: -moz-radial-gradient(50% 46% 90deg,circle closest-corner, #242424, #090909);\n");
      out.write("\t\t\t\tbackground: -webkit-gradient(radial, 50% 50%, 0, 50% 50%, 150, from(#242424), to(#090909));\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\tform label{\n");
      out.write("\t\t\t\tdisplay: block;\n");
      out.write("\t\t\t\tpadding: 10px 10px 5px 15px;\n");
      out.write("\t\t\t\tfont-size: 11px;\n");
      out.write("\t\t\t\tcolor: #777;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\tform input{\n");
      out.write("\t\t\t\tdisplay: block;\n");
      out.write("\t\t\t\tmargin: 5px 10px 10px 15px;\n");
      out.write("\t\t\t\twidth: 85%;\n");
      out.write("\t\t\t\tbackground: #111;\n");
      out.write("\t\t\t\t-moz-box-shadow: 0px 0px 4px #000 inset;\n");
      out.write("\t\t\t\t-webkit-box-shadow: 0px 0px 4px #000 inset;\n");
      out.write("\t\t\t\tbox-shadow: 0px 0px 4px #000 inset;\n");
      out.write("\t\t\t\toutline: 1px solid #333;\n");
      out.write("\t\t\t\tborder: 1px solid #000;\n");
      out.write("\t\t\t\tpadding: 5px;\n");
      out.write("\t\t\t\tcolor: #444;\n");
      out.write("\t\t\t\tfont-size: 16px;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\tform input:focus{\n");
      out.write("\t\t\t\toutline: 1px solid #555;\n");
      out.write("\t\t\t\tcolor: #FFF;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t\tinput[type=\"submit\"]{\n");
      out.write("\t\t\t\tcolor: #999;\n");
      out.write("\t\t\t\tpadding: 5px 10px;\n");
      out.write("\t\t\t\tfloat: right;\n");
      out.write("\t\t\t\tmargin: 20px 0;\n");
      out.write("\t\t\t\tborder: 1px solid #000;\n");
      out.write("\t\t\t\tfont-weight: lighter;\n");
      out.write("\t\t\t\t-moz-border-radius: 15px;\n");
      out.write("\t\t\t    -webkit-border-radius: 15px;\n");
      out.write("\t\t\t\tborder-radius: 15px;\n");
      out.write("\t\t\t\tbackground: #45484d;\n");
      out.write("\t\t\t\tbackground: -moz-linear-gradient(top, #222 0%, #111 100%);\n");
      out.write("\t\t\t\tbackground: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#222), color-stop(100%,#111));\n");
      out.write("\t\t\t\tfilter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#22222', endColorstr='#11111',GradientType=0 );\n");
      out.write("\t\t\t\t-moz-box-shadow: 0px 1px 1px #000, 0px 1px 0px rgba(255,255,255,.3) inset;\n");
      out.write("\t\t\t\t-webkit-box-shadow: 0px 1px 1px #000, 0px 1px 0px rgba(255,255,255,.3) inset;\n");
      out.write("\t\t\t\tbox-shadow: 0px 1px 1px #000,0px 1px 0px rgba(255,255,255,.3) inset;\n");
      out.write("\t\t\t\ttext-shadow: 0 1px 1px #000;\n");
      out.write("\t\t\t}\n");
      out.write("\t\t</style>\n");
      out.write("\t</head>\n");
      out.write("\t<body id=\"home\">\n");
      out.write("\t\t<div class=\"rain\">\n");
      out.write("\t\t\t<div class=\"border start\">\n");
      out.write("\t\t\t\t<form>\n");
      out.write("\t\t\t\t\t<label for=\"email\">Email</label>\n");
      out.write("\t\t\t\t\t<input name=\"email\" type=\"text\" placeholder=\"Email\"/>\n");
      out.write("\t\t\t\t\t<label for=\"pass\">Password</label>\n");
      out.write("\t\t\t\t\t<input name=\"pass\" type=\"password\" placeholder=\"Password\"/>\n");
      out.write("                                        <input type=\"submit\" value=\"LOG IN\"/>\n");
      out.write("\t\t\t\t</form>\n");
      out.write("\t\t\t</div>\n");
      out.write("\t\t</div>\n");
      out.write("\t</body>\n");
      out.write("</html>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
