<%@ page pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - Alura</title>
    <link rel="stylesheet" type="text/css" href="/assets/css/login.css" />
  </head>
  <body class="login-page">
    <main class="container">
      <section class="login-section" aria-labelledby="login-title">
        <div class="login-box">
          <h2 id="login-title">Já estuda com a gente?</h2>
          <p>Faça seu login e boa aula!</p>
          <a
            href="/admin/categories"
            class="btn-login"
            role="button"
            aria-label="Fazer login no sistema"
            >ENTRAR</a
          >
        </div>
      </section>

      <section class="courses" aria-labelledby="courses-title">
        <header class="courses-header">
          <h2 id="courses-title">Ainda não estuda com a gente?</h2>
          <p>São mais de ${totalCourses} cursos nas seguintes áreas:</p>
        </header>

        <div
          class="grid"
          role="list"
          aria-label="Categorias de cursos disponíveis"
        >
          <c:forEach var="categoryData" items="${categoriesWithCourses}">
            <article
              class="card"
              role="listitem"
              aria-labelledby="category-${categoryData.category().code}"
            >
              <div class="card-icon" aria-hidden="true">
                <c:choose>
                  <c:when
                    test="${categoryData.category().name == 'Programação'}"
                  >
                    <img
                      src="/assets/images/icon-color-programacao copy.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when test="${categoryData.category().name == 'Front-end'}">
                    <img
                      src="/assets/images/icon-color-front copy.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when
                    test="${categoryData.category().name == 'Data Science'}"
                  >
                    <img
                      src="/assets/images/icon-color-data-science.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when
                    test="${categoryData.category().name == 'Inteligência Artificial'}"
                  >
                    <img
                      src="/assets/images/icon-color-ia.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when test="${categoryData.category().name == 'DevOps'}">
                    <img
                      src="/assets/images/icon-color-infra copy.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when
                    test="${categoryData.category().name == 'UX & Design'}"
                  >
                    <img
                      src="/assets/images/icon-color-ux-design.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when test="${categoryData.category().name == 'Mobile'}">
                    <img
                      src="/assets/images/icon-color-mobile copy.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:when
                    test="${categoryData.category().name == 'Inovação & Gestão'}"
                  >
                    <img
                      src="/assets/images/icon-color-inovacao-gestao.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:when>
                  <c:otherwise>
                    <img
                      src="/assets/images/icon-color-programacao copy.svg"
                      alt=""
                      width="36"
                      height="36"
                    />
                  </c:otherwise>
                </c:choose>
              </div>
              <h3 id="category-${categoryData.category().code}">
                <span
                  class="escola-prefix"
                  style="color: ${categoryData.category().color};"
                  >Escola_</span
                ><span style="color: ${categoryData.category().color};"
                  >${categoryData.category().name}</span
                >
              </h3>
              <div
                class="courses-list"
                role="list"
                aria-label="Cursos da categoria ${categoryData.category().name}"
              >
                <c:forEach
                  var="course"
                  items="${categoryData.courses()}"
                  varStatus="status"
                >
                  <span class="course-name" role="listitem"
                    >${course.name()}</span
                  ><c:if test="${!status.last}">, </c:if>
                </c:forEach>
              </div>
            </article>
          </c:forEach>
        </div>
      </section>
    </main>
  </body>
</html>
