<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Novo Curso - Alura</title>
    <link
      href="<c:url value='/assets/external-libs/bootstrap/css/bootstrap.min.css'/>"
      rel="stylesheet"
    />
    <link href="<c:url value='/assets/css/forms.css'/>" rel="stylesheet" />
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

    <div class="container">
      <div class="form-container">
        <div class="form-header">
          <h1>Cadastrar Curso</h1>
        </div>

        <form:form
          action="/admin/course/new"
          method="post"
          modelAttribute="newCourseForm"
          class="form-horizontal form-body"
        >
          <div class="form-group">
            <label for="name">Nome *</label>
            <form:input
              path="name"
              class="form-control"
              placeholder="Digite o nome do curso"
            />
            <form:errors path="name" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="code">Código *</label>
            <form:input
              path="code"
              class="form-control"
              placeholder="Digite o código do curso (4-10 caracteres, apenas letras e hífens)"
            />
            <form:errors path="code" cssClass="text-danger" />
            <small class="help-block">Exemplo: spring-boot-avancado</small>
          </div>

          <div class="form-group">
            <label for="instructor">Instrutor *</label>
            <form:input
              path="instructor"
              class="form-control"
              placeholder="Digite o nome do instrutor"
            />
            <form:errors path="instructor" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="category">Categoria *</label>
            <form:select path="category" class="form-control">
              <form:option value="" label="-- Selecione uma categoria --" />
              <form:options
                items="${categories}"
                itemValue="name"
                itemLabel="name"
              />
            </form:select>
            <form:errors path="category" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="description">Descrição</label>
            <form:textarea
              path="description"
              class="form-control"
              rows="4"
              placeholder="Digite a descrição do curso (opcional)"
            />
            <form:errors path="description" cssClass="text-danger" />
          </div>

          <div class="form-actions">
            <a href="<c:url value='/admin/courses'/>" class="btn btn-default">
              <span class="glyphicon glyphicon-arrow-left"></span> Voltar à
              Lista
            </a>
            <button type="submit" class="btn btn-success">Criar Curso</button>
          </div>
        </form:form>
      </div>
    </div>

    <script src="<c:url value='/assets/external-libs/bootstrap/js/bootstrap.min.js'/>"></script>
  </body>
</html>
