<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>New Course - Alura</title>
    <link
      href="<c:url value='/assets/external-libs/bootstrap/css/bootstrap.min.css'/>"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="container">
      <div class="row">
        <div class="col-md-8 col-md-offset-2">
          <h1>New Course</h1>

          <form:form
            action="/admin/course/new"
            method="post"
            modelAttribute="newCourseForm"
            class="form-horizontal"
          >
            <div class="form-group">
              <label for="name" class="col-sm-2 control-label">Name *</label>
              <div class="col-sm-10">
                <form:input
                  path="name"
                  class="form-control"
                  placeholder="Enter course name"
                />
                <form:errors path="name" cssClass="text-danger" />
              </div>
            </div>

            <div class="form-group">
              <label for="code" class="col-sm-2 control-label">Code *</label>
              <div class="col-sm-10">
                <form:input
                  path="code"
                  class="form-control"
                  placeholder="Enter course code (4-10 characters, letters and hyphens only)"
                />
                <form:errors path="code" cssClass="text-danger" />
                <small class="help-block">Example: spring-boot-avancado</small>
              </div>
            </div>

            <div class="form-group">
              <label for="instructor" class="col-sm-2 control-label"
                >Instructor *</label
              >
              <div class="col-sm-10">
                <form:input
                  path="instructor"
                  class="form-control"
                  placeholder="Enter instructor name"
                />
                <form:errors path="instructor" cssClass="text-danger" />
              </div>
            </div>

            <div class="form-group">
              <label for="category" class="col-sm-2 control-label"
                >Category *</label
              >
              <div class="col-sm-10">
                <form:select path="category" class="form-control">
                  <form:option value="" label="-- Select a category --" />
                  <form:options
                    items="${categories}"
                    itemValue="name"
                    itemLabel="name"
                  />
                </form:select>
                <form:errors path="category" cssClass="text-danger" />
              </div>
            </div>

            <div class="form-group">
              <label for="description" class="col-sm-2 control-label"
                >Description</label
              >
              <div class="col-sm-10">
                <form:textarea
                  path="description"
                  class="form-control"
                  rows="4"
                  placeholder="Enter course description (optional)"
                />
                <form:errors path="description" cssClass="text-danger" />
              </div>
            </div>

            <div class="form-group">
              <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-primary">
                  <span class="glyphicon glyphicon-save"></span> Create Course
                </button>
                <a
                  href="<c:url value='/admin/courses'/>"
                  class="btn btn-default"
                >
                  <span class="glyphicon glyphicon-arrow-left"></span> Back to
                  List
                </a>
              </div>
            </div>
          </form:form>
        </div>
      </div>
    </div>

    <script src="<c:url value='/assets/external-libs/bootstrap/js/bootstrap.min.js'/>"></script>
  </body>
</html>
