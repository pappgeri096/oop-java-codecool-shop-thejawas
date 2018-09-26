package com.codecool.shop.controller;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = {"/search"})
public class SearchController extends HttpServlet {
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private ProductDao productDataManager = IMPLEMENTATION_FACTORY.getProductDataManagerInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Product> productList = productDataManager.getBy(req.getParameter("keyWord"));
        if (productList.size() == 0) productList = productDataManager.getAll();

        StringBuilder html = new StringBuilder();

        for (Product product : productList) {
            html.append("<div class='item col-xs-4 col-lg-4'>");
            html.append("<div class='thumbnail'>");
            html.append("<img class='group list-group-image' src='/static/img/product_").append(product.getId()).append(".jpg'>");
            html.append("<div class='caption'>");
            html.append("<h4 class='group inner list-group-item-heading'>").append(product.getName()).append("</h4>");
            html.append("<p class='group inner list-group-item-text'>").append(product.getDescription()).append("</p>");
            html.append("<div class='row'> <div class='col-xs-12 col-md-6'>");
            html.append("<p class='lead'>").append(product.getPrice()).append("</p></div>");
            html.append("<div class='col-xs-12 col-md-6'>\n")
                    .append("<a class=\"btn btn-success\" href='/index?product=1'>Add to cart</a>\n")
                    .append("</div></div></div></div></div>\n");

        }

        resp.getWriter().write(html.toString());

    }
}
