package com.mdk.controllers.vendor;

import com.mdk.models.ImageStore;
import com.mdk.models.Product;
import com.mdk.models.Store;
import com.mdk.models.User;
import com.mdk.services.IImageStoreService;
import com.mdk.services.IProductService;
import com.mdk.services.IStoreService;
import com.mdk.services.impl.ImageStoreService;
import com.mdk.services.impl.ProductService;
import com.mdk.services.impl.StoreService;
import com.mdk.utils.DeleteImageUtil;
import com.mdk.utils.MessageUtil;
import com.mdk.utils.SessionUtil;
import com.mdk.utils.UploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mdk.utils.AppConstant.UPLOAD_STORE_DIRECTORY;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
@WebServlet(urlPatterns = {"/vendor/store", "/vendor/store/create", "/vendor/store/edit", "/vendor/home"})
public class StoreVendorController extends HttpServlet {
    IStoreService storeService = new StoreService();
    IImageStoreService imageStoreService = new ImageStoreService();
    IProductService productService = new ProductService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        if (url.contains("create")) {
            User user = (User) SessionUtil.getInstance().getValue(req, "USERMODEL");
            req.setAttribute("ownerId", user.getId());
            req.getRequestDispatcher("/views/vendor/store.jsp").forward(req, resp);
        } else if (url.contains("edit")) {
            req.getRequestDispatcher("/views/vendor/store.jsp").forward(req, resp);
        } else if (url.contains("home")) {
            checkStoreExist(req, resp);
            findAllProduct(req, resp);
            req.getRequestDispatcher("/views/vendor/home.jsp").forward(req, resp);
        } else {
            checkStoreExist(req, resp);
            MessageUtil.showMessage(req, resp);
            req.getRequestDispatcher("/views/vendor/store.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        if (url.contains("create")) {
            insert(req, resp);
            resp.sendRedirect(req.getContextPath() + "/vendor/store?message=insert_success");
        } else if (url.contains("edit")) {
            update(req, resp);
            resp.sendRedirect(req.getContextPath() + "/vendor/store?message=update_success");
        }
    }

    protected  void checkStoreExist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Store store = (Store) SessionUtil.getInstance().getValue(req, "STORE");
        int count = 0;
        if (store != null) {
            count = 1;
            List<String> images = new ArrayList<>();
            for (ImageStore image : store.getImages()) {
                images.add(image.getName());
            }
            req.setAttribute("store", store);
            req.setAttribute("images", images);
        }
        req.setAttribute("count", count);
    }
    protected void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        Store store = new Store();
        List<ImageStore> images = new ArrayList<>();
        store.setName(req.getParameter("name"));
        store.setBio(req.getParameter("bio"));
        store.setOwnerID(Integer.valueOf(req.getParameter("ownerId")));
        // xử lý ảnh
        Collection<Part> parts = req.getParts();
        for(Part filePart : parts) {
            if(filePart.getHeader("content-disposition").contains("filename=")){
                String fileName = "" + System.currentTimeMillis();
                String realPath = UPLOAD_STORE_DIRECTORY;
                if(filePart.getName().equals("avatar")){
                    store.setAvatar(UploadUtil.processUpload(filePart.getName(), req, realPath, fileName));
                } else {
                    ImageStore image = new ImageStore();
                    image.setName(UploadUtil.processUpload(filePart.getName(), req, realPath, fileName));
                    images.add(image);
                }
            }
        }
        store.setImages(images);
        try {
            storeService.insert(store);
            SessionUtil.getInstance().putValue(req, "STORE", store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void update (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        //Store moi
        Store store = new Store();
        List<ImageStore> images = new ArrayList<>();

        store.setName(req.getParameter("name"));
        store.setBio(req.getParameter("bio"));
        store.setOwnerID(Integer.valueOf(req.getParameter("ownerId")));

        // Store cũ
        Store oldStore = storeService.findByUserId(1);
        List<ImageStore> oldImages = imageStoreService.findByStoreId(oldStore.getId());
        int flag = 0;
        // Xử lý ảnh
        Collection<Part> parts = req.getParts();
        for(Part filePart : parts) {
            if (filePart.getHeader("content-disposition").contains("filename=")) {
                String fileName = "" + System.currentTimeMillis();
                String realPath = UPLOAD_STORE_DIRECTORY;
                if(filePart.getName().equals("avatar")) {
                    if (filePart.getSize() == 0) {
                        store.setAvatar(oldStore.getAvatar());
                    } else {
                        if (oldStore.getAvatar() != null) {
                            // xoa anh cu
                            String fileNameAvatar = oldStore.getAvatar();
                            String storeFolder = UPLOAD_STORE_DIRECTORY;
                            DeleteImageUtil.processDelete(storeFolder, fileNameAvatar);
                        }
                        store.setAvatar(UploadUtil.processUpload(filePart.getName(), req, realPath, fileName));
                    }
                } else {
                    ImageStore image = new ImageStore();
                    if(filePart.getSize() == 0) {
                        image.setName(oldImages.get(flag).getName());
                        images.add(image);
                    } else {
                        if(oldImages.get(flag) != null) {
                            // xoa anh cu
                            String fileNameImg = oldImages.get(flag).getName();
                            File file = new File(UPLOAD_STORE_DIRECTORY + "\\" +fileNameImg);
                            if (file.delete()) {
                                System.out.println("Đã xóa");
                            } else {
                                System.out.println(UPLOAD_STORE_DIRECTORY + "\\" +fileNameImg);
                            }
                        }
                        image.setName(UploadUtil.processUpload(filePart.getName(), req, realPath, fileName));
                        images.add(image);
                    }
                    flag++;
                }
            }
        }
        store.setImages(images);
        storeService.update(store);
    }
    protected void findAllProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        Store store = (Store) SessionUtil.getInstance().getValue(req,"STORE");
        List<Product> products = productService.findAllByStoreId(store.getId());
        req.setAttribute("products", products);
    }
}
