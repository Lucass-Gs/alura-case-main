<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Course Management - Alura</title>
    <link
      href="<c:url value='/assets/external-libs/bootstrap/css/bootstrap.min.css'/>"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="container">
      <div class="row">
        <div class="col-md-12">
          <h1>Course Management</h1>

          <c:if test="${not empty message}">
            <div class="alert alert-success alert-dismissible" role="alert">
              <button
                type="button"
                class="close"
                data-dismiss="alert"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
              ${message}
            </div>
          </c:if>

          <div class="row mb-3">
            <div class="col-md-12">
              <a
                href="<c:url value='/admin/course/new'/>"
                class="btn btn-primary"
              >
                <span class="glyphicon glyphicon-plus"></span> New Course
              </a>
            </div>
          </div>

          <div class="table-responsive">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Code</th>
                  <th>Instructor</th>
                  <th>Category</th>
                  <th>Status</th>
                  <th>Created At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${empty courses}">
                    <tr>
                      <td colspan="7" class="text-center">No courses found</td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="course" items="${courses}">
                      <tr>
                        <td>${course.name()}</td>
                        <td><code>${course.code()}</code></td>
                        <td>${course.instructor()}</td>
                        <td>${course.category()}</td>
                        <td>
                          <c:choose>
                            <c:when test="${course.status() == 'ACTIVE'}">
                              <span class="label label-success">Active</span>
                            </c:when>
                            <c:otherwise>
                              <span class="label label-danger">Inactive</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td>${course.createdAt()}</td>
                        <td>
                          <c:if test="${course.status() == 'ACTIVE'}">
                            <button
                              class="btn btn-warning btn-xs"
                              onclick="inactivateCourse('${course.code()}')"
                            >
                              Inactivate
                            </button>
                          </c:if>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <script src="<c:url value='/assets/external-libs/bootstrap/js/bootstrap.min.js'/>"></script>
    <script>
      function inactivateCourse(code) {
        if (confirm("Are you sure you want to inactivate this course?")) {
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
                alert("Error inactivating course");
              }
            })
            .catch((error) => {
              console.error("Error:", error);
              alert("Error inactivating course");
            });
        }
      }
    </script>
  </body>
</html>
