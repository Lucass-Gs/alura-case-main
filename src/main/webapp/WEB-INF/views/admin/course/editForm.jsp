<%@ page pageEncoding="UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Editar Curso - Alura</title>
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
          <h1>Editar Curso</h1>
        </div>

        <form:form
          modelAttribute="editCourseForm"
          cssClass="form-horizontal form-body"
          action="/admin/course/edit/${courseId}"
          method="post"
        >
          <div class="form-group">
            <label for="editCourse-name">Nome *</label>
            <form:input
              path="name"
              id="editCourse-name"
              cssClass="form-control"
              placeholder="Digite o nome do curso"
              required="required"
            />
            <form:errors path="name" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="editCourse-code">Código *</label>
            <form:input
              path="code"
              id="editCourse-code"
              cssClass="form-control"
              placeholder="Digite o código do curso"
              required="required"
            />
            <form:errors path="code" cssClass="text-danger" />
            <small class="help-block"
              >Exemplo: java-oo, spring-boot, react-js</small
            >
          </div>

          <div class="form-group">
            <label for="editCourse-instructor">Instrutor *</label>
            <form:input
              path="instructor"
              id="editCourse-instructor"
              cssClass="form-control"
              placeholder="Digite o nome do instrutor"
              required="required"
            />
            <form:errors path="instructor" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="editCourse-category">Categoria *</label>
            <form:select
              path="category"
              id="editCourse-category"
              cssClass="form-control"
              required="required"
            >
              <form:option value="" label="Selecione uma categoria" />
              <form:options
                items="${categories}"
                itemValue="name"
                itemLabel="name"
              />
            </form:select>
            <form:errors path="category" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="editCourse-description">Descrição</label>
            <form:textarea
              path="description"
              id="editCourse-description"
              cssClass="form-control"
              placeholder="Digite a descrição do curso"
              rows="4"
            />
            <form:errors path="description" cssClass="text-danger" />
          </div>

          <div class="form-group">
            <label for="editCourse-status">Status *</label>
            <form:select
              path="status"
              id="editCourse-status"
              cssClass="form-control"
              required="required"
            >
              <form:option value="ACTIVE" label="Ativo" />
              <form:option value="INACTIVE" label="Inativo" />
            </form:select>
            <form:errors path="status" cssClass="text-danger" />
          </div>

          <div class="form-actions">
            <a href="<c:url value='/admin/courses'/>" class="btn btn-default">
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
