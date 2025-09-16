<%@ page pageEncoding="UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Lista de Categorias - Alura</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link
      rel="stylesheet"
      type="text/css"
      href="/assets/external-libs/bootstrap/css/bootstrap.min.css"
    />
    <link rel="stylesheet" type="text/css" href="/assets/css/admin.css" />
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

    <div class="container">
      <jsp:include page="/WEB-INF/views/shared/datagrid.jsp" />
    </div>
  </body>
</html>
