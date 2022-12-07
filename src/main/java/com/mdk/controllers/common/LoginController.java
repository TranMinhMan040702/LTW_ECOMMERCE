package com.mdk.controllers.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mdk.models.Store;
import com.mdk.models.User;
import com.mdk.models.UserGoogle;
import com.mdk.services.IStoreService;
import com.mdk.services.IUserService;
import com.mdk.services.impl.StoreService;
import com.mdk.services.impl.UserService;
import com.mdk.utils.AppConstant;
import com.mdk.utils.MessageUtil;
import com.mdk.utils.SessionUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mdk.utils.AppConstant.*;

@WebServlet(urlPatterns = {"/login", "/logout", "/LoginGoogleHandler"})
public class LoginController extends HttpServlet {
    IUserService userService = new UserService();
    IStoreService storeService = new StoreService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        if (url.contains("login")) {
            MessageUtil.showMessage(req, resp);
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        } else if (url.contains("LoginGoogleHandler")) {
            ReqLogin(req, resp, toUser(req, resp));
        } else if (url.contains("logout")) {
            SessionUtil.getInstance().removeValue(req, USER_MODEL);
            SessionUtil.getInstance().removeValue(req, CART);
            SessionUtil.getInstance().removeValue(req, CART_HEADER);
            SessionUtil.getInstance().removeValue(req, COUNT_CART_HEADER);
            SessionUtil.getInstance().removeValue(req, CART_USER);
            SessionUtil.getInstance().removeValue(req, STORE_MODEL);
            resp.sendRedirect(req.getContextPath() + "/home");
        }
        else {
            req.getRequestDispatcher("/views/home.jsp").forward(req, resp);;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        if (url.contains("login")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            User user = new User();
            user.setEmail(username);
            user.setPassword(password);
            ReqLogin(req, resp, user);
        }
    }

    public static String getToken(String code) throws ClientProtocolException, IOException {
        // call api to get token
        String response = Request.Post(AppConstant.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", AppConstant.GOOGLE_CLIENT_ID)
                        .add("client_secret", AppConstant.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", AppConstant.GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", AppConstant.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }
    public static UserGoogle getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = AppConstant.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        System.out.println(response);
        UserGoogle googlePojo = new Gson().fromJson(response, UserGoogle.class);
        return googlePojo;
    }
    protected void ReqLogin(HttpServletRequest req, HttpServletResponse resp, User userLogin) throws ServletException,
            IOException {
        User user = userService.findOneByUsernameAndPassword(userLogin.getEmail(), userLogin.getPassword());
        if (user != null) {
            SessionUtil.getInstance().putValue(req, USER_MODEL, user);
            Store store = storeService.findByUserId(user.getId());
            SessionUtil.getInstance().putValue(req, STORE_MODEL, store);
            if (user.getRole().equals(ADMIN)) {
                resp.sendRedirect(req.getContextPath() + "/admin/user/all");
            } else if (user.getRole().equals(USER)) {
                resp.sendRedirect(req.getContextPath() + "/web");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login?message=login_error");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/signup?firstname=" + userLogin.getFirstname() + "&lastname=" + userLogin.getLastname() + "&email=" + userLogin.getEmail());
        }
    }
    protected User toUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();
        String code = req.getParameter("code");
        String accessToken = getToken(code);
        UserGoogle userGoogle = getUserInfo(accessToken);
        user.setEmail(userGoogle.getEmail());
        user.setFirstname(userGoogle.getGiven_name());
        user.setLastname(userGoogle.getFamily_name());
        user.setPassword(userGoogle.getId());
        return user;
    }
}
