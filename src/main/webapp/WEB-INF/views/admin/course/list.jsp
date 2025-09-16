<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Lista de Cursos - Alura</title>
    <link
      href="<c:url value='/assets/external-libs/bootstrap/css/bootstrap.min.css'/>"
      rel="stylesheet"
    />
    <link href="<c:url value='/assets/css/admin.css'/>" rel="stylesheet" />
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

    <div class="container">
      <jsp:include page="/WEB-INF/views/shared/datagrid.jsp" />
    </div>

    <script src="<c:url value='/assets/external-libs/bootstrap/js/bootstrap.min.js'/>"></script>
    <script>
      function inactivateCourse(code) {
        if (confirm("Tem certeza que deseja inativar este curso?")) {
          fetch("/course/" + code + "/inactive", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
          })
            .then((response) => {
              if (response.ok) {
                location.reload();
              } else {
                alert("Erro ao inativar curso");
              }
            })
            .catch((error) => {
              console.error("Error:", error);
              alert("Erro ao inativar curso");
            });
        }
      }
    </script>
  </body>
</html>
