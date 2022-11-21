<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/common/taglib.jsp"%>
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
                                <h4 class="card-title">Thêm mới đơn vị vận chuyển</h4>
                            </div>
                        </div>
                        <div class="iq-card-body">
                            <form method="POST" action="add">
                                <div class="form-group">
                                    <label>Tên đơn vị vận chuyển:</label>
                                    <input type="text" class="form-control" id="name">
                                </div>
                                <div class="form-group">
                                    <label>Mô tả:</label>
                                    <textarea class="form-control" rows="4" id="description"></textarea>
                                </div>
                                <div class="form-group">
                                    <label>Giá:</label>
                                    <input type="number" min=0 class="form-control" id="price">
                                </div>
                                <button type="submit" class="btn btn-primary">Xác nhận</button>
                                <button type="button" id="reset" class="btn btn-danger">Dọn dẹp</button>
                                <button type="button" id="back" class="btn btn-secondary">Huỷ</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    document.getElementById('reset').addEventListener('click', function () {
        document.getElementById('name').value = "";
        document.getElementById('description').value = "";
        document.getElementById('price').value = "";

    })
    document.getElementById('back').addEventListener('click', function () {
        history.back()
    })
</script>
</body>
</html>
