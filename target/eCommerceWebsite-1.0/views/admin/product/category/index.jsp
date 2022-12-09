<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/common/taglib.jsp"%>
<c:url value="/admin/product/category?search=" var="urlProc"/>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Document</title>
</head>
<body>
<div id="loading">
  <div id="loading-center">
  </div>
</div>
<!-- loader END -->
<!-- Wrapper Start -->
<div class="wrapper">
  <div id="content-page" class="content-page">
    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-12">
          <div class="iq-card">
            <div class="iq-card-header d-flex justify-content-center">
              <div class="iq-header-title">
                <h4 class="card-title">QUẢN LÝ TẤT CẢ THỂ LOẠI</h4>
              </div>
            </div>
            <div class="iq-card-body">
              <div class="table-responsive">
                <div class="d-flex align-items-center justify-content-between">
                <h6>Tổng thể loại: ${categories.size() + totalItemInPage*(tag-1)} / ${countP}</h6>
                  <div>
                    <label>Tìm kiếm theo tên thể loại:</label>
                    <div class="iq-card-transparent mb-0">
                      <div class="d-block">
                        <div class="w-100 iq-search-filter">
                          <div class="iq-search-bar search-book d-flex align-items-center">
                            <form action="#" class="searchbox m-0">
                              <input type="text" class="text search-input"
                                     placeholder="Nhập tên thể loại cần tìm..."
                                     id="input-search"
                                     name=""
                                     value="${search}"
                              >
                            </form>
                            <button id="btn-search" onclick="Search()"
                                    class="btn btn-warning search-data ml-2">Tìm</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <select class="form-control w-25" id="selectCate">
                  <option value="false" ${state == "false" ? "selected" : ""}>Chưa bị xoá</option>
                  <option value="true" ${state == "true" ? "selected" : ""}>Đã bị xoá</option>
                </select>
                <table id="user-list-table" class="table table-striped table-bordered mt-4" role="grid"
                       aria-describedby="user-list-page-info">
                  <thead>
                  <tr class="text-center">
                    <th>STT</th>
                    <th>Tên thể loại</th>
                    <th>Hành động</th>
                  </tr>
                  </thead>
                  <tbody>
                  <%--                  Hide--%>
                  <input type="hidden" id="id" value="">
                  <input type="hidden" id="state" value="">
                  <c:if test="${state == 'false'}">
                    <c:forEach items="${categories}" var="categories" varStatus="STT" >
                      <tr class="text-center">
                        <td>${STT.index + 1 + totalItemInPage*(tag-1)}</td>
                        <td>${categories.name}</td>
                        <td>
                          <div class="d-flex align-items-center list-user-action justify-content-around">
                            <a href="category/edit?id=${categories.id}"  class="bg-primary p-3"><i class="fa-solid fa-pencil" style="transform: translate(-50%, -50%); color: white"></i></a>
                            <a href="" onclick="ClickIcon(event, 'delete-soft' )" data-toggle="modal" data-target="#deleteModal" class="bg-primary p-3"><i id="${categories.id}" class="fa-solid fa-trash" style="padding: 10px; transform: translate(-50%, -50%); color: white"></i></a>
                          </div>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:if>
                  <c:if test="${state == 'true'}">
                    <c:forEach items="${categories}" var="categories" varStatus="STT" >
                      <tr>
                        <td>${STT.index + 1 + totalItemInPage*(tag-1)}</td>
                        <td>${categories.name}</td>
                        <td>
                          <div class="d-flex align-items-center list-user-action justify-content-around">
                            <a href="" onclick="ClickIcon(event, 'delete')" data-toggle="modal" data-target="#deleteModal" class="bg-primary p-3"><i id="${categories.id}" class="fa-solid fa-trash" style="padding: 10px; transform: translate(-50%, -50%); color: white"></i></a>
                            <a href="" onclick="ClickIcon(event, 'restore')" data-toggle="modal" data-target="#deleteModal"  class="bg-primary p-3"><i id="${categories.id}" class="fa-solid fa-window-restore" style="transform: translate(-50%, -50%); color: white"></i></a>
                          </div>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:if>
                  </tbody>
                </table>
              </div>
              <div class="col-sm-12 col-md-4 mx-auto">
                <div class="dataTables_paginate paging_simple_numbers">
                  <ul class="pagination justify-content-center">
                    <li class="paginate_button page-item ${tag == 1 ? "disabled" : ""}">
                      <a href="${pageContext.request.contextPath}/admin/product/category?state=${state}&index=${tag - 1}"
                         class="page-link">Previous
                      </a>
                    </li>
                    <c:forEach begin="1" end="${endP}" var="i">
                      <li class="paginate_button page-item ${i == tag ? "active" : ""}">
                        <a href="${pageContext.request.contextPath}/admin/product/category?state=${state}&index=${i}"
                           class="page-link">${i}</a>
                      </li>
                    </c:forEach>
                    <li class="paginate_button page-item ${tag == endP ? "disabled" : ""}">
                      <a href="${pageContext.request.contextPath}/admin/product/category?state=${state}&index=${tag + 1}"
                         class="page-link">Next</a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="iq-card-header-toolbar d-flex align-items-center flex-row-reverse">
        <a href="category/add" class="btn btn-primary">Thêm mới</a>
      </div>
      <%--      MODAL DELETE--%>
      <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="deleteModalLabel"></h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body" id="deleteModalBody">
              Bạn có thực sự muốn xoá không?
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Huỷ</button>
              <button type="button" onclick="Action()" class="btn btn-primary" data-dismiss="modal">Chắc chắn</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
  function ClickIcon(e, state) {
    e.preventDefault();
    const id = e.target.id;
    console.log(id)
    document.getElementById('id').value = id;
    document.getElementById('state').value = state;

    if (state == 'delete-soft') {
      document.getElementById('deleteModalLabel').innerText = 'Xoá loại sản phẩm';
      document.getElementById('deleteModalBody').innerText = 'Bạn có thực sự muốn xoá không?'
    } else if (state == 'delete'){
      document.getElementById('deleteModalLabel').innerText = 'Xoá vĩnh viễn loại sản phẩm';
      document.getElementById('deleteModalBody').innerText = 'Bạn có thực sự muốn xoá không?'
    } else {
      document.getElementById('deleteModalLabel').innerText = 'Hoàn tác loại sản phẩm';
      document.getElementById('deleteModalBody').innerText = 'Bạn có thực sự muốn hoàn tác không?'
    }
  }
  function Action() {
    const state = document.getElementById('state').value;
    const id = document.getElementById('id').value;
    if (state == 'delete-soft') {
      window.location.href = 'category/delete-soft?id='+id.toString();
    } else if (state == 'delete'){
      window.location.href = 'category/delete?id='+id.toString();
    } else {
      window.location.href = 'category/restore?id='+id.toString();
    }
  }

  $('#selectCate').change(function () {
    const state = $("#selectCate option:selected").val();
    window.location.href = "${urlList}?state=" + state;
  });
    function Search() {
    const key = $('#input-search').val();
    window.location.href = "${urlProc}" + key;
  }
</script>
</body>
</html>

