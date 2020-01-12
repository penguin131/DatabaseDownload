<%
    String stringTime = String.valueOf(request.getAttribute("time"));
    long time = Long.parseLong(stringTime);
%>

Download success!
time: <%=time%> ms