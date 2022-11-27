
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglib.jsp" %>
<section class="sign-in-page">
    <div class="container p-0">
        <div class="row no-gutters height-self-center">
            <div class="col-sm-12 align-self-center page-content rounded">
                <div class="row m-0">
                    <div class="col-sm-12 sign-in-page-data">
                        <div class="sign-in-from bg-primary rounded">
                            <h3 class="mb-0 text-center text-white">Đăng nhập</h3>
                            <form action="login" method="post" class="mt-4 form-text">
                                <div class="form-group">
                                    <label>Địa chỉ email</label>
                                    <input type="email" class="form-control mb-0" name="username"
                                           placeholder="Nhập email">
                                </div>
                                <div class="form-group">
                                    <label>Password</label>
                                    <input type="password" class="form-control mb-0" name="password"
                                           placeholder="Nhập password">
                                </div>
                                <div class="sign-info text-center">
                                    <button type="submit" class="btn btn-white d-block w-100 mb-2">Đăng nhập</button>
                                    <span class="text-dark dark-color d-inline-block line-height-2">Bạn chưa có tài
                                        khoản ? <a href="#" class="text-white">Đăng ký</a></span>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>