<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="panel panel-default" style="background: rgba(255, 255, 255, 0.95); backdrop-filter: blur(10px); border: none; box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); border-radius: 8px; overflow: hidden; margin-top: 40px;">
  <!-- Header -->
  <div class="panel-heading datagrid-header" style="padding: 20px; position: relative; overflow: hidden; margin-bottom: 0; border-bottom: none;">
    <h1 style="color: #e5f1ff; margin: 0; font-weight: 700; display: inline-block; position: relative; z-index: 1;">${datagridTitle}</h1>
    <c:if test="${not empty datagridNewUrl}">
      <a class="btn btn-success new-button" href="${datagridNewUrl}" style="background: #a3ffbf; color: #041831; border: none; font-weight: 600; padding: 10px 20px; margin-left: 15px; border-radius: 25px; transition: all 0.3s ease; float: right; position: relative; z-index: 1;">
        <span class="glyphicon glyphicon-plus"></span> ${datagridNewText}
      </a>
    </c:if>
    <div style="clear: both;"></div>
  </div>


  <!-- Table -->
  <div class="panel-body" style="padding: 0; height: 510px; overflow-y: auto; background: rgba(255, 255, 255, 0.98);">
    <c:choose>
      <c:when test="${empty datagridItems}">
        <div class="text-center" style="padding: 40px; background: rgba(255, 255, 255, 0.98);">
          <span class="glyphicon glyphicon-inbox" style="font-size: 48px; color: #666;"></span>
          <h4 style="color: #333; margin-top: 15px;">Nenhum item encontrado</h4>
          <p style="color: #666;">Não há dados para exibir no momento.</p>
        </div>
      </c:when>
      <c:otherwise>
        <table class="table table-hover" style="margin-bottom: 0; background: rgba(255, 255, 255, 0.98);">
          <thead class="datagrid-table-header" style="position: sticky; top: 0; z-index: 10;">
            <tr>
              <c:forEach var="column" items="${datagridColumns}">
                <th style="color: #e5f1ff; font-weight: 600; border: none; padding: 15px 12px;">${column.label}</th>
              </c:forEach>
              <c:if test="${not empty datagridActions}">
                <th style="width: 120px; color: #e5f1ff; font-weight: 600; border: none; padding: 15px 12px;">Ações</th>
              </c:if>
            </tr>
          </thead>
          <tbody style="background: rgba(255, 255, 255, 0.98);">
            <c:forEach var="item" items="${datagridItems}" varStatus="status">
              <tr style="background: rgba(255, 255, 255, 0.98);">
                <c:forEach var="column" items="${datagridColumns}">
                  <td style="color: #333; border-color: rgba(0, 0, 0, 0.1);">
                    <c:choose>
                      <c:when test="${column.type == 'status'}">
                        <c:choose>
                          <c:when test="${item[column.field] == 'ACTIVE'}">
                            <span class="label label-success">Ativo</span>
                          </c:when>
                          <c:when test="${item[column.field] == 'INACTIVE'}">
                            <span class="label label-danger">Inativo</span>
                          </c:when>
                          <c:otherwise>
                            <span class="label label-default">${item[column.field]}</span>
                          </c:otherwise>
                        </c:choose>
                      </c:when>
                      <c:when test="${column.type == 'date'}">
                        <fmt:formatDate value="${item[column.field]}" pattern="dd/MM/yyyy HH:mm" />
                      </c:when>
                      <c:when test="${column.type == 'currency'}">
                        <fmt:formatNumber value="${item[column.field]}" type="currency" currencySymbol="R$" />
                      </c:when>
                      <c:when test="${column.type == 'number'}">
                        <fmt:formatNumber value="${item[column.field]}" />
                      </c:when>
                      <c:otherwise>
                        ${item[column.field]}
                      </c:otherwise>
                    </c:choose>
                  </td>
                </c:forEach>
                <c:if test="${not empty datagridActions}">
                  <td style="white-space: nowrap; color: #333; border-color: rgba(0, 0, 0, 0.1);">
                    <c:forEach var="action" items="${datagridActions}">
                      <c:choose>
                        <c:when test="${action.type == 'edit'}">
                          <a href="${action.url}${item[action.idField]}" class="btn btn-primary btn-sm" style="margin-right: 3px;">
                            ${action.label}
                          </a>
                        </c:when>
                        <c:when test="${action.type == 'delete'}">
                          <button onclick="confirmDelete('${action.url}${item[action.idField]}', '${item[action.nameField]}')" 
                                  class="btn btn-danger btn-sm" style="margin-right: 3px;">
                            ${action.label}
                          </button>
                        </c:when>
                        <c:when test="${action.type == 'custom'}">
                          <c:choose>
                            <c:when test="${action.conditionValue == null or item[action.conditionField] == action.conditionValue}">
                              <c:choose>
                                <c:when test="${action.actionType == 'inactivate'}">
                                  <button onclick="inactivateCourse('${item[action.idField]}')" class="btn btn-danger btn-sm" style="margin-right: 3px;">
                                    ${action.label}
                                  </button>
                                </c:when>
                                <c:otherwise>
                                  <a href="${action.url}${item[action.idField]}" class="btn ${action.cssClass} btn-sm" style="margin-right: 3px;">
                                    ${action.label}
                                  </a>
                                </c:otherwise>
                              </c:choose>
                            </c:when>
                          </c:choose>
                        </c:when>
                      </c:choose>
                    </c:forEach>
                  </td>
                </c:if>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- Pagination - Dentro do panel -->
  <div class="panel-footer" style="background: rgba(248, 249, 250, 0.95); backdrop-filter: blur(5px); border: none; box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);">
    <div class="row">
      <div class="col-md-4">
        <c:if test="${datagridTotalItems > 0}">
          <div class="form-inline">
            <label for="itemsPerPage" style="margin-right: 10px; color: #041831; font-weight: 600;">Itens por página:</label>
            <select id="itemsPerPage" class="form-control" onchange="changeItemsPerPage(this.value)" style="width: auto; border: 1px solid rgba(22, 123, 247, 0.3); color: #041831;">
              <option value="10" ${datagridItemsPerPage == 10 ? 'selected' : ''}>10</option>
              <option value="25" ${datagridItemsPerPage == 25 ? 'selected' : ''}>25</option>
              <option value="50" ${datagridItemsPerPage == 50 ? 'selected' : ''}>50</option>
              <option value="100" ${datagridItemsPerPage == 100 ? 'selected' : ''}>100</option>
            </select>
          </div>
        </c:if>
      </div>
      <div class="col-md-4 text-center">
        <span style="color: #041831; font-weight: 600;">
          Página ${datagridCurrentPage + 1} de ${datagridTotalPages}
        </span>
        <c:if test="${datagridTotalItems > 0}">
          <br>
          <small style="color: #666;">
            Mostrando ${datagridStartItem} a ${datagridEndItem} de ${datagridTotalItems} itens
          </small>
        </c:if>
      </div>
      <div class="col-md-4">
        <c:if test="${datagridTotalPages > 1}">
          <nav aria-label="Navegação de páginas">
            <ul class="pagination pagination-sm pull-right" style="margin: 0;">
              <!-- Primeira página -->
              <c:if test="${datagridCurrentPage > 0}">
                <li>
                  <a href="?page=0&size=${datagridItemsPerPage}" aria-label="Primeira" style="color: #041831; border: 1px solid rgba(22, 123, 247, 0.3); background: #fff; transition: all 0.3s ease;">
                    <span aria-hidden="true">&laquo;&laquo;</span>
                  </a>
                </li>
                <li>
                  <a href="?page=${datagridCurrentPage - 1}&size=${datagridItemsPerPage}" aria-label="Anterior" style="color: #041831; border: 1px solid rgba(22, 123, 247, 0.3); background: #fff; transition: all 0.3s ease;">
                    <span aria-hidden="true">&laquo;</span>
                  </a>
                </li>
              </c:if>
              
              <!-- Páginas numeradas -->
              <c:forEach begin="${datagridStartPage}" end="${datagridEndPage}" var="pageNum">
                <c:choose>
                  <c:when test="${pageNum == datagridCurrentPage}">
                    <li class="active">
                      <span style="background: #041831; border-color: #041831; color: #e5f1ff; font-weight: 600;">${pageNum + 1}</span>
                    </li>
                  </c:when>
                  <c:otherwise>
                    <li>
                      <a href="?page=${pageNum}&size=${datagridItemsPerPage}" style="color: #041831; border: 1px solid rgba(22, 123, 247, 0.3); background: #fff; transition: all 0.3s ease;">${pageNum + 1}</a>
                    </li>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              
              <!-- Última página -->
              <c:if test="${datagridCurrentPage < datagridTotalPages - 1}">
                <li>
                  <a href="?page=${datagridCurrentPage + 1}&size=${datagridItemsPerPage}" aria-label="Próxima" style="color: #041831; border: 1px solid rgba(22, 123, 247, 0.3); background: #fff; transition: all 0.3s ease;">
                    <span aria-hidden="true">&raquo;</span>
                  </a>
                </li>
                <li>
                  <a href="?page=${datagridTotalPages - 1}&size=${datagridItemsPerPage}" aria-label="Última" style="color: #041831; border: 1px solid rgba(22, 123, 247, 0.3); background: #fff; transition: all 0.3s ease;">
                    <span aria-hidden="true">&raquo;&raquo;</span>
                  </a>
                </li>
              </c:if>
            </ul>
          </nav>
        </c:if>
      </div>
    </div>
  </div>
</div>

<style>
.pagination > li > a:hover {
  background: #041831 !important;
  color: #e5f1ff !important;
  border-color: #041831 !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(4, 24, 49, 0.3);
}

.btn-success:hover {
  background: #8de6a8 !important;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(163, 255, 191, 0.4);
}

/* Scroll personalizado com cor do header */
.panel-body::-webkit-scrollbar {
  width: 8px;
}

.panel-body::-webkit-scrollbar-track {
  background: transparent;
}

.panel-body::-webkit-scrollbar-thumb {
  background: #041831;
  border-radius: 4px;
}

.panel-body::-webkit-scrollbar-thumb:hover {
  background: #0a2a4a;
}

/* Para Firefox */
.panel-body {
  scrollbar-width: thin;
  scrollbar-color: #041831 transparent;
}

/* Efeito hover para linhas da tabela */
.table tbody tr:hover {
  background: rgba(4, 24, 49, 0.05) !important;
  transition: background 0.3s ease;
}

.table tbody tr:hover td {
  color: #041831 !important;
}

/* Cabeçalho do datagrid com gradiente linear suave */
.datagrid-header {
  background: linear-gradient(
    90deg,
    rgba(9, 51, 102, 0.7) 0%,
    rgba(6, 35, 70, 0.8) 25%,
    rgba(4, 24, 49, 0.9) 50%,
    rgba(4, 24, 49, 0.95) 75%,
    #041831 100%
  ) !important;
  border: none !important;
  border-bottom: none !important;
  margin-bottom: 0 !important;
  box-shadow: none !important;
  position: relative;
  overflow: hidden;
}

.datagrid-header::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    90deg,
    rgba(22, 123, 247, 0.12) 0%,
    rgba(22, 123, 247, 0.08) 15%,
    rgba(142, 45, 226, 0.04) 25%,
    transparent 40%,
    transparent 60%,
    rgba(22, 123, 247, 0.06) 85%,
    rgba(22, 123, 247, 0.1) 100%
  );
  z-index: -1;
}

/* Cabeçalho da tabela com gradiente linear suave */
.datagrid-table-header {
  background: linear-gradient(
    90deg,
    rgba(9, 51, 102, 0.7) 0%,
    rgba(6, 35, 70, 0.8) 25%,
    rgba(4, 24, 49, 0.9) 50%,
    rgba(4, 24, 49, 0.95) 75%,
    #041831 100%
  ) !important;
  border: none !important;
  border-top: none !important;
  margin-top: 0 !important;
  box-shadow: none !important;
  position: relative;
  overflow: hidden;
}

.datagrid-table-header::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    90deg,
    rgba(22, 123, 247, 0.12) 0%,
    rgba(22, 123, 247, 0.08) 15%,
    rgba(142, 45, 226, 0.04) 25%,
    transparent 40%,
    transparent 60%,
    rgba(22, 123, 247, 0.06) 85%,
    rgba(22, 123, 247, 0.1) 100%
  );
  z-index: -1;
}

.datagrid-table-header th {
  position: relative;
  z-index: 1;
}
</style>

<script>
function changeItemsPerPage(size) {
  const url = new URL(window.location);
  url.searchParams.set('size', size);
  url.searchParams.set('page', '0'); // Reset to first page
  window.location.href = url.toString();
}

function confirmDelete(url, itemName) {
  if (confirm('Tem certeza que deseja excluir "' + itemName + '"? Esta ação não pode ser desfeita.')) {
    // Create a form to submit POST request
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = url;
    
    // Add CSRF token if available
    const csrfToken = document.querySelector('meta[name="_csrf"]');
    if (csrfToken) {
      const csrfInput = document.createElement('input');
      csrfInput.type = 'hidden';
      csrfInput.name = '_csrf';
      csrfInput.value = csrfToken.getAttribute('content');
      form.appendChild(csrfInput);
    }
    
    document.body.appendChild(form);
    form.submit();
  }
}
</script>
