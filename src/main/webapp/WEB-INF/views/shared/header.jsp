<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="/assets/css/header.css" />

<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="/"> <strong>Alura</strong> Admin </a>
    </div>
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav">
        <li
          class="${pageContext.request.requestURI.contains('/admin/category') ? 'active' : ''}"
        >
          <a href="/admin/categories">Categorias</a>
        </li>
        <li
          class="${pageContext.request.requestURI.contains('/admin/course') ? 'active' : ''}"
        >
          <a href="/admin/courses">Cursos</a>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li>
          <a href="/" class="btn btn-link">
            <span class="glyphicon glyphicon-home"></span> Voltar ao Login
          </a>
        </li>
      </ul>
    </div>
  </div>
</nav>
