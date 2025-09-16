<%@ page pageEncoding="UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Editar Categoria - Alura</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link
      rel="stylesheet"
      type="text/css"
      href="/assets/external-libs/bootstrap/css/bootstrap.min.css"
    />
    <link rel="stylesheet" type="text/css" href="/assets/css/forms.css" />
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

    <div class="container">
      <div class="form-container">
        <div class="form-header">
          <h1>Editar Categoria</h1>
        </div>

        <form:form
          modelAttribute="editCategoryForm"
          cssClass="form-horizontal form-body"
          action="/admin/category/edit/${categoryId}"
          method="post"
        >
          <div class="form-group">
            <label for="editCategory-name">Nome *</label>
            <form:input
              path="name"
              id="editCategory-name"
              cssClass="form-control"
              placeholder="Digite o nome da categoria"
              required="required"
            />
            <form:errors path="name" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="editCategory-code">Código *</label>
            <form:input
              path="code"
              id="editCategory-code"
              cssClass="form-control"
              placeholder="Digite o código da categoria"
              required="required"
            />
            <form:errors path="code" cssClass="text-danger" />
            <small class="help-block"
              >Exemplo: programacao, front-end, data-science</small
            >
          </div>

          <div class="form-group">
            <label for="editCategory-color">Cor *</label>
            <form:input
              path="color"
              id="editCategory-color"
              cssClass="form-control"
              placeholder="Digite a cor da categoria (ex: #FF6B6B)"
              required="required"
            />
            <form:errors path="color" cssClass="text-danger" />
            <small class="help-block"
              >Use código hexadecimal (ex: #FF6B6B)</small
            >
          </div>

          <div class="form-group">
            <label for="editCategory-order">Ordem *</label>
            <form:input
              path="order"
              type="number"
              min="1"
              id="editCategory-order"
              cssClass="form-control"
              placeholder="Digite a ordem de exibição"
              required="required"
            />
            <form:errors path="order" cssClass="text-danger" />
            <small class="help-block">Número para ordenar as categorias</small>
          </div>

          <div class="form-actions">
            <a
              href="<c:url value='/admin/categories'/>"
              class="btn btn-default"
            >
              <span class="glyphicon glyphicon-arrow-left"></span> Voltar à
              Lista
            </a>
            <button type="submit" class="btn btn-success">
              Salvar Alterações
            </button>
          </div>
        </form:form>
      </div>
    </div>
  </body>
</html>
